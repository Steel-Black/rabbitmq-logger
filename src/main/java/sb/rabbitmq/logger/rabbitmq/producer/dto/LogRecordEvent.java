package sb.rabbitmq.logger.rabbitmq.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import sb.rabbitmq.logger.entity.LogRecord;

import java.util.Collection;
@Data
@AllArgsConstructor
public class LogRecordEvent {
    private String table;
    private Collection<LogRecord> records;
}
