package sb.rabbitmq.logger.rabbitmq.producer;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import sb.rabbitmq.logger.InnerLogger;
import sb.rabbitmq.logger.RabbitMQLogger;
import sb.rabbitmq.logger.rabbitmq.producer.dto.LogRecordEvent;
@AllArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    private final RabbitMQLogger logger = InnerLogger.RABBITMQ_LOGGER;

    public void convertAndSend(LogRecordEvent logRecordEvent) {
        String method = this.getClass().getSimpleName().concat(".convertAndSend");
        String outMessage = "Message has been delivered";
        logger.trace(logRecordEvent, RabbitMQLogger.Step.START, method);
        try {
            rabbitTemplate.convertAndSend(logRecordEvent);
        } catch (AmqpException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), method);
            outMessage= e.getMessage();
        }
        logger.trace(outMessage, RabbitMQLogger.Step.END, method);
    }
}