package cn.soeasypay.log.service;


import cn.soeasypay.log.domain.OperationLog;

public interface OperationLogService {


    /**
     *
     * @param log
     */
    void addJob(OperationLog log);

    void consumeJob();
}
