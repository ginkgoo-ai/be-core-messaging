package com.ginkgooai.config.mq;

import com.ginkgooai.core.common.queue.QueueInterface;
import com.ginkgooai.core.common.queue.QueueMessage;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedissonMQ implements QueueInterface {

    private final RedissonClient redissonClient;

    @Override
    public <T extends QueueMessage> void send(String queueName, T message) {
        RQueue<T> queue = redissonClient.getQueue(queueName);
        message.setMsgId(UUID.randomUUID().toString());
        message.setTimestamp(System.currentTimeMillis());
        queue.offer(message);
    }

    @Override
    public void subscribe(String queueName, MessageListener listener) {
    }

    @Override
    public void shutdown() {
        redissonClient.shutdown();
    }

    @Override
    public <T extends QueueMessage> List<T> getMessages(String queueName, int batchSize, Class<T> clazz) {
        RQueue<T> queue = redissonClient.getQueue(queueName);
        return queue.poll(batchSize);
    }

}
