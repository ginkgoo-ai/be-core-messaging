package com.ginkgooai.service;

import com.ginkgooai.client.ai.AiClient;
import com.ginkgooai.core.common.bean.ActivityType;
import com.ginkgooai.core.common.utils.ActivityLogger;
import com.ginkgooai.core.common.utils.ContextUtils;
import com.sun.mail.imap.IMAPMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.event.MessageCountAdapter;
import jakarta.mail.event.MessageCountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ImapMailListenerService {
    // 配置参数
    @Value("${mail.imap.host}") private String host;
    @Value("${mail.imap.port}") private int port;
    @Value("${mail.imap.username}") private String username;
    @Value("${mail.imap.password}") private String password;

    private volatile Store store;
    private volatile Folder inbox;
    @Resource
    private AiClient aiClient;
    @Autowired
    private ActivityLogger activityLogger;
    private ScheduledFuture<?> heartbeatTask;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final AtomicInteger retryCounter = new AtomicInteger(0);
    private static final int MAX_RETRIES = 5;

    @PostConstruct
    public void startListening() {
        initAsync();
    }

    private void initAsync() {
        establishConnection();
    }

    /**
     * Establishes a connection to the IMAP server and sets up the necessary components.
     * This method attempts to create a session, connect to the IMAP store, open the inbox folder,
     * set up a message listener, and start a heartbeat task. If the connection fails, it schedules
     * a retry with an exponential backoff strategy.
     */
    private void establishConnection() {
        try {
            // Create a new session using the configured properties
            Session session = Session.getInstance(createSessionProperties());
            // Get the IMAP store from the session
            store = session.getStore("imaps");
            // Connect to the IMAP server using the provided username and password
            store.connect(username, password);
            // Get the INBOX folder from the store
            inbox = store.getFolder("INBOX");
            // Open the INBOX folder in read-write mode
            inbox.open(Folder.READ_WRITE);

            // Set up the message listener to handle new messages
            setupMessageListener();
            // Start the heartbeat task to monitor the connection
            startHeartbeat();
            // Log a success message indicating the connection was established
            log.info("IMAP connection successful {}:{}", host, port);

        } catch (Exception e) {
            // Log an error message if the connection fails
            log.error("Failed to establish connection", e);
            // Schedule a retry with exponential backoff
            scheduleRetryWithBackoff();
        }
    }


    private void setupMessageListener() throws MessagingException {
        inbox.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                processMessages(e.getMessages());
            }
        });
    }

    /**
     * Starts a heartbeat task to periodically check the IMAP connection status.
     * If the connection is inactive or an exception occurs during the check,
     * it attempts to reconnect using the reconnectIfNeeded method.
     */
    private void startHeartbeat() {
        // Schedule a fixed-delay task to run every 30 seconds, starting after an initial delay of 30 seconds
        heartbeatTask = scheduler.scheduleWithFixedDelay(() -> {
            try {
                // Check if the connection is still active
                if (!isConnectionActive()) {
                    // If the connection is not active, attempt to reconnect
                    reconnectIfNeeded();
                }
            } catch (Exception ex) {
                // Log a warning if an exception occurs during the heartbeat check
                log.warn("Heartbeat check exception", ex);
                // Attempt to reconnect if an exception occurs
                reconnectIfNeeded();
            }
        }, 30, 30, TimeUnit.SECONDS);
    }


    private void processMessages(Message[] messages) {
        try {
            for (Message msg : messages) {
                if (!msg.isSet(Flags.Flag.SEEN)) {
                    handleNewMessage(msg);
                }
            }
        } catch (MessagingException e) {
            log.error("email handle error", e);
        }
    }

    /**
     * Handles a new, unseen message.
     *
     * @param msg The Message object representing the new email.
     */

    private void handleNewMessage(Message msg) throws MessagingException {
        log.info("new message achieve - subject: {}", msg.getSubject());
        IMAPMessage imapMessage = (IMAPMessage)msg;
        activityLogger.log(
                "project.getWorkspaceId()",
                "project.getId()",
                null,
                ActivityType.PROJECT_CREATED,
                null,
                null,
                ContextUtils.getUserId());
        msg.setFlag(Flags.Flag.SEEN, true);

    }

    private boolean isConnectionActive() throws MessagingException {
        return inbox != null && inbox.isOpen() && store.isConnected();
    }

    private synchronized void reconnectIfNeeded() {
        closeResources();
        scheduler.schedule(this::establishConnection, 5, TimeUnit.SECONDS);
    }

    private void scheduleRetryWithBackoff() {
        int attempt = retryCounter.incrementAndGet();
        if (attempt > MAX_RETRIES) {
            log.error("max retry times: {}", MAX_RETRIES);
            return;
        }

        long delay = (long) Math.pow(2, attempt) * 1000;
        log.warn("{} times retry，{}ms after invoke...", attempt, delay);
        
        scheduler.schedule(() -> {
            establishConnection();
            retryCounter.set(0); // 成功连接后重置计数器
        }, delay, TimeUnit.MILLISECONDS);
    }

    // 关闭资源
    private void closeResources() {
        try {
            if (inbox != null && inbox.isOpen()) inbox.close(false);
            if (store != null && store.isConnected()) store.close();
        } catch (MessagingException e) {
            log.warn("resource close error", e);
        }
    }

    // 取消现有心跳任务
    private void cancelExistingHeartbeat() {
        if (heartbeatTask != null && !heartbeatTask.isDone()) {
            heartbeatTask.cancel(true);
        }
    }

    // 创建会话配置
    private Properties createSessionProperties() {
        Properties props = new Properties();
        props.put("mail.imap.ssl.trust", "*");
        props.put("mail.imap.starttls.enable", "true");
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", port);
        return props;
    }

    @PreDestroy
    public void shutdown() {
        cancelExistingHeartbeat();
        scheduler.shutdown();
        closeResources();
    }
}