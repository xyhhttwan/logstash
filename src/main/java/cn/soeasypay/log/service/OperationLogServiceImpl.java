package cn.soeasypay.log.service;


import cn.soeasypay.log.client.OperationLogSender;
import cn.soeasypay.log.domain.OperationLog;
import cn.soeasypay.log.factory.QueueFactory;

/**
 * @author baixb
 * @ 生产 消费
 */
public class OperationLogServiceImpl implements OperationLogService {


    @Override
    public void addJob(OperationLog log) {
        QueueFactory.offer(log);
    }

    @Override
    public void consumeJob() {
        OperationLog log = QueueFactory.take();
        if (log != null) {
            OperationLogSender.send(log);
        }
    }
}
