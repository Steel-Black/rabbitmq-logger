package sb.rabbitmq.logger.rabbitmq.service;

import sb.rabbitmq.logger.entity.LogRecord;

import java.util.Collection;

public interface LogRecordService extends MessageService<LogRecord> {
    @Override
    void sendMessages(Collection<LogRecord> messages, String table);
}
