package sb.rabbitmq.logger.rabbitmq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sb.rabbitmq.logger.InnerLogger;
import sb.rabbitmq.logger.RabbitMQLogger;
import sb.rabbitmq.logger.entity.LogRecord;
import sb.rabbitmq.logger.rabbitmq.producer.MessageProducer;
import sb.rabbitmq.logger.rabbitmq.producer.dto.LogRecordEvent;
import sb.rabbitmq.logger.rabbitmq.service.LogRecordService;

import java.util.Collection;
@Service
public final class MessageServiceImpl implements LogRecordService {
    @Autowired
    private final MessageProducer messageProducer;

    public MessageServiceImpl(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }
    private final RabbitMQLogger logger = InnerLogger.RABBITMQ_LOGGER;
    @Override
    public void sendMessages(Collection<LogRecord> messages, String table) {
        String method = this.getClass().getSimpleName().concat(".sendMessages");
        logger.trace(String.format("%s, %s", messages, table), RabbitMQLogger.Step.START, method);
        LogRecordEvent logRecordEvent = buildLogRecordEvent(messages, table);
        messageProducer.convertAndSend(logRecordEvent);
        messages.clear();
        logger.trace("Message queue is cleared", RabbitMQLogger.Step.END, method);
    }

    private LogRecordEvent buildLogRecordEvent(Collection<LogRecord> messages, String table) {
        return new LogRecordEvent(table, messages);
    }
}
