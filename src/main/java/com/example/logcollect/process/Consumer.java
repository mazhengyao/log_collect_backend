package com.example.logcollect.process;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Properties;

// 将stream处理好的数据写入redis
public class Consumer {
    // kafka slf4j日志打印
    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = loggerContext.getLogger("root");
        root.setLevel(Level.INFO);
    }
    public static void main(String[] args) {
        // 定义一些属性
        Properties p = new Properties();
        // 引导服务器配置
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 反序列化程序类配置
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "level");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(p);
        // 订阅消息
        kafkaConsumer.subscribe(Collections.singletonList("loglevel"));
        // redis连接
        Jedis jedis = new Jedis("localhost");

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                // 存储数据到hash

                jedis.hincrBy("logDemo", record.value(), 1);
                System.out.println(jedis.hget("logDemo",record.value()));
            }
        }
    }
}