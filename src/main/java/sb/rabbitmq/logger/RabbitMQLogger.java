package sb.rabbitmq.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import sb.rabbitmq.logger.utils.MDCConstant;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.UUID;

public class RabbitMQLogger {
    private final Logger log;

    public RabbitMQLogger(String loggerName) {
        this.log = LoggerFactory.getLogger(loggerName);
    }
    public RabbitMQLogger() {
        this.log = LoggerFactory.getLogger(RabbitMQLogger.class);
    }

    public void info(Object message, Step step, String method) {
        MDC.put(MDCConstant.STEP.name().toLowerCase(Locale.ROOT), step.name());
        MDC.put(MDCConstant.METHOD.name().toLowerCase(Locale.ROOT), method);
        log.info("{}", message);
    }
    public void debug(Object message, Step step, String method) {
        MDC.put(MDCConstant.STEP.name().toLowerCase(Locale.ROOT), step.name());
        MDC.put(MDCConstant.METHOD.name().toLowerCase(Locale.ROOT), method);
        log.debug("{}", message);
    }
    public void error(Object message, String method) {
        MDC.put(MDCConstant.STEP.name().toLowerCase(Locale.ROOT), Step.ERROR.name());
        MDC.put(MDCConstant.METHOD.name().toLowerCase(Locale.ROOT), method);
        log.error("{}", message);
    }
    public void trace(Object message, Step step, String method) {
        MDC.put(MDCConstant.STEP.name().toLowerCase(Locale.ROOT), step.name());
        MDC.put(MDCConstant.METHOD.name().toLowerCase(Locale.ROOT), method);
        log.trace("{}", message);
    }

    public void setMDC(String operation){
        MDC.put(MDCConstant.HOST.name().toLowerCase(Locale.ROOT), getHost());
        MDC.put(MDCConstant.OPERATION.name().toLowerCase(Locale.ROOT), operation);
        MDC.put(MDCConstant.CORRELATION_ID.name().toLowerCase(Locale.ROOT), UUID.randomUUID().toString());
    }

    public enum Step{
        START, END, PROGRESS, ERROR
    }

    private String getHost(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
