package sb.rabbitmq.logger.entity;

import lombok.Builder;

import java.sql.Timestamp;
@Builder
public record LogRecord(String message, String step,String logger,String host,String correlationId,String level,Timestamp timestamp){
}
