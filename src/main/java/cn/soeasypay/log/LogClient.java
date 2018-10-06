package cn.soeasypay.log;

import cn.soeasypay.log.factory.QueueFactory;
import cn.soeasypay.log.service.OperationLogService;
import cn.soeasypay.log.service.OperationLogServiceImpl;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LogClient {

    private String reqUrl;
    private int maxQueueNum;
    private int corePoolSize;
    private int maximumPoolSize;
    private int sendLogMaxQueueNum;
    private int sendLogCorePoolSize;
    private int sendLogMaximumPoolSize;

    private static AtomicInteger count = new AtomicInteger(0);


    private static OperationLogService operationLogService;

    private LogClient() {

    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String reqUrl;
        private int maxQueueNum;
        private int corePoolSize;
        private int maximumPoolSize;
        private int sendLogMaxQueueNum;
        private int sendLogCorePoolSize;
        private int sendLogMaximumPoolSize;

        private Builder() {

        }

        public Builder setReqUrl(String reqUrl) {
            this.reqUrl = reqUrl;
            return this;
        }

        public Builder setMaxQueueNum(int maxQueueNum) {
            this.maxQueueNum = maxQueueNum;
            return this;
        }

        public Builder setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public Builder setSendLogMaxQueueNum(int sendLogMaxQueueNum) {
            this.sendLogMaxQueueNum = sendLogMaxQueueNum;
            return this;
        }

        public Builder setSendLogCorePoolSize(int sendLogCorePoolSize) {
            this.sendLogCorePoolSize = sendLogCorePoolSize;
            return this;
        }

        public Builder setSendLogMaximumPoolSize(int sendLogMaximumPoolSize) {
            this.sendLogMaximumPoolSize = sendLogMaximumPoolSize;
            return this;
        }

        public LogClient build() {
            return new LogClient(this);
        }
    }

    private LogClient(Builder builder) {
        this.reqUrl = builder.reqUrl;
        this.maxQueueNum = builder.maxQueueNum;
        this.corePoolSize = builder.corePoolSize;
        this.maximumPoolSize = builder.maximumPoolSize;
        this.sendLogMaxQueueNum = builder.sendLogMaxQueueNum;
        this.sendLogCorePoolSize = builder.sendLogCorePoolSize;
        this.sendLogMaximumPoolSize = builder.sendLogMaximumPoolSize;
    }


    private OperationLogService init() {
        if (null == operationLogService) {

            if (count.incrementAndGet() == 1) {
                QueueFactory.init(reqUrl, maxQueueNum, corePoolSize,
                        maximumPoolSize, sendLogMaxQueueNum, sendLogCorePoolSize,
                        sendLogMaximumPoolSize);
                operationLogService = new OperationLogServiceImpl();
            }


        }
        return operationLogService;

    }

    public OperationLogService instance() {
        return init();
    }

}
