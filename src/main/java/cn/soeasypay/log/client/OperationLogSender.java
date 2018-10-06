package cn.soeasypay.log.client;

import cn.soeasypay.log.constants.SystemEnum;
import cn.soeasypay.log.constants.TypeEnum;
import cn.soeasypay.log.domain.OperationLog;
import cn.soeasypay.log.domain.ReqLog;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import okhttp3.*;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author baixiaobin
 * 日志业务操作
 */
public class OperationLogSender {

    /**
     * 请求成功返回的状态码
     */
    private static final Integer OK = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogSender.class);

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");

    private static String sendUrl;


    /**
     * 线程池
     */
    private static ExecutorService pools;

    private static LinkedBlockingQueue queue;

    private static OkHttpClient operationLogClient;

    private static int corePoolSize = 2;

    private static int maximumPoolSize = 4;

    private static int maxQueueNum = 20000;

    private static int connectTimeout = 3;
    private static int readTimeout = 3;
    private static int writeTimeout = 3;

    static {

        // Create a factory that produces daemon threads with a naming pattern and
        // a priority
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("OperationLogSender-thread-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build();

        queue = new LinkedBlockingQueue(maxQueueNum);
        pools = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                1000L, TimeUnit.MILLISECONDS,
                queue, factory, new ThreadPoolExecutor.DiscardOldestPolicy());

    }

    /**
     * 初始化 operationLogClient
     *
     * @param reqUrl          日志请求路径
     * @param maxQueueNum     最大队列
     * @param corePoolSize    核心线程
     * @param maximumPoolSize 最大线程
     */
    public  static void init(
            String reqUrl, int maxQueueNum, int corePoolSize, int maximumPoolSize) {
        if (corePoolSize > OperationLogSender.corePoolSize) {
            OperationLogSender.corePoolSize = corePoolSize;
        }
        if (maximumPoolSize > OperationLogSender.maximumPoolSize) {
            OperationLogSender.maximumPoolSize = corePoolSize;
        }
        if (maxQueueNum > OperationLogSender.maximumPoolSize) {
            OperationLogSender.maximumPoolSize = maximumPoolSize;
        }
        OperationLogSender.sendUrl = reqUrl;
        if (operationLogClient == null) {
            OperationLogClient client = new OperationLogClient(connectTimeout, readTimeout, writeTimeout);
            operationLogClient = client.getInstance();
        }
    }

    /**
     * 发送消息
     *
     * @param log 要发送操作日志
     */
    public static void send(final OperationLog log) {
        pools.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseWapper response = safeSend(log, sendUrl);
                    if (response.status == OK) {
                        LOGGER.info("response success");
                    } else {
                        LOGGER.error("response code:{},msg:{}", response.status, response.message);
                    }
                } catch (Exception e) {
                    LOGGER.error("send failed!");
                    LOGGER.error(e.getMessage(), e);
                }

            }
        });

    }

    /**
     * 发送消息
     *
     * @param log 要发送操作日志
     * @param url operation的publish接口的rest地址，由于各个业务的地址可能不一样，所以url由业务传过来。
     */
    public static void send(final OperationLog log, final String url) {
        pools.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseWapper response = safeSend(log, url);
                    if (response.status == OK) {
                        LOGGER.info("response success");
                    } else {
                        LOGGER.error("response code:{},msg:{}", response.getData(), response.getMessage());
                    }
                } catch (Exception e) {
                    LOGGER.error("send failed!");
                    LOGGER.error(e.getMessage(), e);
                }

            }
        });


    }

    /***
     * @param log 日志
     * @param url
     * @return
     * @throws IOException
     */
    private static ResponseWapper safeSend(final OperationLog log, final String url) throws IOException {


        checkParams(log, url);
        checkSystem(log.getServerId());
        checkType(log.getType());
        List<OperationLog> logs = Lists.newArrayList(log);
        ReqLog reqLog = new ReqLog(log.getType(), log.getVersion(), JSON.toJSONString(logs));

        String content = JSON.toJSONString(reqLog);
        LOGGER.debug("等待发送日志的队列大小:{}", queue.size());
        RequestBody data = RequestBody.create(MEDIA_JSON, content);

        if (null == operationLogClient) {
            LOGGER.error("operationLogClient is not inited");
            throw new IllegalStateException("operationLogClient is not inited");
        }

        LOGGER.debug("开始发送日志数据 reqUrl:{},data:{}", url, content);

        Response response = operationLogClient.newCall(new Request.Builder().url(url).post(data).build()).execute();

        if (!response.isSuccessful()) {
            LOGGER.debug(response.toString());
            LOGGER.error("send failed by http, please check the network");
            throw new IllegalStateException("send failed by http, please check the network");
        }
        String jsonBiz = response.body().string();

        ResponseWapper bizData = null;

        try {
            bizData = JSON.parseObject(jsonBiz, ResponseWapper.class);
        } catch (Exception e) {
            LOGGER.error("parse the bizData{} failed, http response data invalid!", jsonBiz);
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException("parse the bizData failed, http response data is invalid");
        }
        LOGGER.debug("日志上报成功");
        return bizData;

    }

    private static void checkParams(OperationLog log, String url) {
        Preconditions.checkArgument(log != null, "log not be null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "http_send_url not be null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(log.getServerId()), "serverId is not null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(log.getMethod()), "method is not null");
    }

    private static void checkType(String type) {
        TypeEnum[] typeEnums = TypeEnum.values();
        boolean typeFlag = false;
        for (TypeEnum typeEnum : typeEnums) {
            if (typeEnum.getType().equals(type)) {
                typeFlag = true;
            }
        }
        Preconditions.checkArgument(typeFlag, "type is not not support");
    }

    private static void checkSystem(String serviceId) {
        SystemEnum[] systemEnums = SystemEnum.values();
        boolean systemFlag = false;
        for (SystemEnum systemEnum : systemEnums) {
            if (systemEnum.getName().equals(serviceId)) {
                systemFlag = true;
            }
        }

        Preconditions.checkArgument(systemFlag, "system  is not support");

    }

    public static class ResponseWapper {
        private int status;
        private Object data;
        private String message;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
