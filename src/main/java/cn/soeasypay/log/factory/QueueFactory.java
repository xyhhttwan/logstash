package cn.soeasypay.log.factory;


import cn.soeasypay.log.client.OperationLogSender;
import cn.soeasypay.log.domain.OperationLog;
import cn.soeasypay.log.service.OperationLogService;
import cn.soeasypay.log.service.OperationLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 队列工厂
 *
 * @author baixb
 */
public class QueueFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueFactory.class);

    private static ThreadPoolExecutor cachedThreadPool;


    public static int MAX_QUEUE_NUM = 10000;

    private static LinkedBlockingQueue<OperationLog> linkedBlockingQueue;


    public static void put(OperationLog log) {

        if (null == linkedBlockingQueue) {
            LOGGER.error("linkedBlockingQueue is  null ");
            return;
        }
        try {
            linkedBlockingQueue.put(log);
        } catch (Exception ignore) {
            //
        }
    }

    public static void offer(OperationLog log, long timeout, TimeUnit unit) {

        if (null == log) {
            return;
        }
        if (null == linkedBlockingQueue) {
            LOGGER.error("linkedBlockingQueue is  null ");
            return;
        }
        try {
            linkedBlockingQueue.offer(log, timeout, unit);
        } catch (Exception ignore) {
            //
        }
    }

    public static void offer(final OperationLog log) {

        if (null == linkedBlockingQueue) {
            LOGGER.error("linkedBlockingQueue is  null ");
            return;
        }
        try {
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        linkedBlockingQueue.offer(log, 1, TimeUnit.SECONDS);
                    } catch (InterruptedException ignore) {
                        //
                    }

                }
            });
        } catch (Exception ignore) {
            //
        }
    }

    public static OperationLog take() {

        if (null == linkedBlockingQueue) {
            LOGGER.error("linkedBlockingQueue is  null ");
            return null;
        }
        try {
            return linkedBlockingQueue.take();
        } catch (Exception ignore) {
            //
        }

        return null;
    }

    /**
     * @param reqUrl                 日志发送的完整路径
     * @param maxQueueNum            存储待发送队列队列最大值
     * @param corePoolSize           存储待发送队列核心线程数量
     * @param maximumPoolSize        存储待发送队列最大线程
     * @param sendLogMaxQueueNum     发送日志等待队列大小
     * @param sendLogCorePoolSize    发送日志 核心线程
     * @param sendLogMaximumPoolSize 发送日志最大线程
     */
    public static  void init(String reqUrl, int maxQueueNum, int corePoolSize,
                                         int maximumPoolSize, int sendLogMaxQueueNum,
                                         int sendLogCorePoolSize, int sendLogMaximumPoolSize) {
        if (maxQueueNum <= 0) {
            maxQueueNum = MAX_QUEUE_NUM;
        }
        if (corePoolSize <= 1) {
            corePoolSize = 2;
        }
        if (maximumPoolSize <= 2) {
            maximumPoolSize = 3;
        }
        if (null == linkedBlockingQueue) {
            linkedBlockingQueue = new LinkedBlockingQueue<>(maxQueueNum);

        }

        if (null == cachedThreadPool) {
            cachedThreadPool =
                    new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0,
                            TimeUnit.SECONDS, new LinkedBlockingQueue<>(maxQueueNum),
                            new ThreadPoolExecutor.DiscardOldestPolicy());
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    OperationLogService operationLogService = new OperationLogServiceImpl();
                    while (true) {
                        operationLogService.consumeJob();
                    }
                }
            });
        }

        //初始化日志发送服务
        OperationLogSender.init(reqUrl, sendLogMaxQueueNum, sendLogCorePoolSize, sendLogMaximumPoolSize);

        LOGGER.info("日志服务初始化完成 开始消费");

    }
}
