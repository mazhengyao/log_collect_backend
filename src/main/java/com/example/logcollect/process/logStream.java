package com.example.logcollect.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

// 利用kafka stream 将几个topic的数据 处理输出到一个topic中
public class logStream {
    // kafka slf4j日志打印
    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = loggerContext.getLogger("root");
        root.setLevel(Level.INFO);
    }

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"LogStream");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");

        // SerDes表示串行器和解串器。每个Kafka流都必须为记录的数据类型提供SerDes，并在必要时记录值以实现数据。
        // 使用Serdes类创建序列化/反序列化所需的Serde实例 Serdes类为以下类型提供默认的实现：String、Byte array、Long、Integer和Double。
        Serde<String> stringSerde = Serdes.String();
        StreamsBuilder builder = new StreamsBuilder();

        // 集合
        Collection<String> coll = new HashSet<>();
        // stream订阅的topic
        coll.add("debuglogs");
        coll.add("infologs");
        coll.add("warnlogs");
        coll.add("errorlogs");
        coll.add("fatallogs");

        // stream内置consumed
        KStream<String, String> simpleFirstStream = builder.stream(coll, Consumed.with(stringSerde, stringSerde));
        // 使用KStream.mapValues 截取
        KStream<String, String> kStream =  simpleFirstStream.mapValues(line -> ((JSONObject) JSON.parse(line)).getString("message").toLowerCase().split(" ")[0]);
        // stream内置produced，把转换结果输出到另一个topic
        kStream.to("loglevel", Produced.with(stringSerde, stringSerde));

        // 创建和启动KStream
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
        kafkaStreams.start();
    }
}
