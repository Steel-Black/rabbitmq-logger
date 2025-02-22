package sb.rabbitmq.logger.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Setter;
import sb.rabbitmq.logger.InnerLogger;
import sb.rabbitmq.logger.RabbitMQLogger;
import sb.rabbitmq.logger.entity.LogRecord;
import sb.rabbitmq.logger.rabbitmq.service.LogRecordService;
import sb.rabbitmq.logger.rabbitmq.service.MessageService;
import sb.rabbitmq.logger.utils.LogConverter;
import sb.rabbitmq.logger.rabbitmq.utils.SpringUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static sb.rabbitmq.logger.RabbitMQLogger.Step.*;

@Setter
public class QueueAppender extends AppenderBase<ILoggingEvent> {
    private long DEFAULT_DELAY = 10 * TimeUnit.SECOND;

    private boolean isActive = false;
    private int batchSize;
    private String table;
    private long delay;

    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();
    MessageService<LogRecord> messageService;
    private RabbitMQLogger logger;
    private Timer timer;
    TimerTask timerTask;

    public QueueAppender() {
        this.logger = InnerLogger.RABBITMQ_LOGGER;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendLogs();
            }
        };
        timer.schedule(timerTask, DEFAULT_DELAY * TimeUnit.SECOND);
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        logger.setMDC("AppendLog");
        String method = this.getClass().getName().concat("append");
        logger.trace(iLoggingEvent, START, method);
        if (!isActive) {
            logger.trace("Appender deactivated", END, method);
            return;
        }
        LogRecord logRecord = LogConverter.convertLog(iLoggingEvent);

        queue.add(logRecord);
        if (queue.size() >= batchSize) {
            sendLogs();
        }
        logger.trace(logRecord, END, method);
    }

    public void sendLogs() {
        messageService = SpringUtils.getBean(LogRecordService.class);
        messageService.sendMessages(queue, table);
    }

    public void setDelay(long delay) {
        this.delay = delay;
        timer.schedule(timerTask, delay);
    }

    static class TimeUnit {
        static long SECOND = 1000;
        static long MINUTE = 60 * SECOND;
        static long HOUR = 60 * MINUTE;
        static long DAY = 24 * HOUR;
    }
}
