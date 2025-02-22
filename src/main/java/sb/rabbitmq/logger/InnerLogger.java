package sb.rabbitmq.logger;

public final class InnerLogger {
    private InnerLogger() {}
    public static RabbitMQLogger RABBITMQ_LOGGER = new RabbitMQLogger();
}
