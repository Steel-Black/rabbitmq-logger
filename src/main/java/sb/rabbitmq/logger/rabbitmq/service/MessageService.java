package sb.rabbitmq.logger.rabbitmq.service;

import java.util.Collection;

public interface MessageService<L> {
    void sendMessages(Collection<L> messages, String table);
}
