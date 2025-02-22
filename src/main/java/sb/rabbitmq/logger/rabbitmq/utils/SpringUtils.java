package sb.rabbitmq.logger.rabbitmq.utils;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sb.rabbitmq.logger.InnerLogger;
import sb.rabbitmq.logger.RabbitMQLogger;
import sb.rabbitmq.logger.rabbitmq.config.RabbitConfig;

public final class SpringUtils {
    private SpringUtils() {}
    private static final RabbitMQLogger rabbitmqLogger = InnerLogger.RABBITMQ_LOGGER;
    private static final AnnotationConfigApplicationContext ctx;
    static {
        rabbitmqLogger.trace("Initialization AnnotationConfigApplicationContext", RabbitMQLogger.Step.START, "SpringUtils.staticInit");
        ctx = new AnnotationConfigApplicationContext(RabbitConfig.class);
        rabbitmqLogger.trace(ctx, RabbitMQLogger.Step.END, "SpringUtils.staticInit");
    }
    public static <T> T getBean(Class<T> clazz) {
        rabbitmqLogger.trace(String.format("get bean of class: %s", clazz), RabbitMQLogger.Step.START, "SpringUtils.getBean");
        return ctx.getBean(clazz);
    }
}
