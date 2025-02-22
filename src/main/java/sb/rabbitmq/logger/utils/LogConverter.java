package sb.rabbitmq.logger.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.event.KeyValuePair;
import sb.rabbitmq.logger.InnerLogger;
import sb.rabbitmq.logger.RabbitMQLogger;
import sb.rabbitmq.logger.entity.LogRecord;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class LogConverter {
    private LogConverter() {}
    private static final RabbitMQLogger logger = InnerLogger.RABBITMQ_LOGGER;
    public static LogRecord convertLog(ILoggingEvent event) {
        String method = LogConverter.class.getName().concat("convertLog");
        logger.trace(event, RabbitMQLogger.Step.START, method);
        LogRecord logRecord = LogRecord.builder()
                .level(event.getLevel().toString())
                .message(event.getMessage())
                .logger(event.getLoggerName())
                .timestamp(new Timestamp(event.getTimeStamp()))
                .correlationId(getValue(event, MDCConstant.CORRELATION_ID.name()))
                .step(getValue(event, MDCConstant.STEP.name()))
                .host(getValue(event, MDCConstant.HOST.name()))
                .build();
        logger.trace(logRecord, RabbitMQLogger.Step.END, method);
        return logRecord;
    }

    public static String getValue(ILoggingEvent event, String key) {
        Map<String,String> props = event.getMDCPropertyMap();
        String value = props.get(key.toLowerCase(Locale.ROOT));
        return value == null ? "" : value;
    }
}
