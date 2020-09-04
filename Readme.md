# log collect system
#### stack:filebeat->kafka->kafka stream->redis->spring boot->vue->echarts
##### shell/produce.sh:
    production of fake logs
##### src/main/java/com/example/logcollect/config/CrosConfig.java:
    processing cross domain requests
##### src/main/java/com/example/logcollect/controller/LogHandler.java:
    get the date from the redis to the front-end
##### src/main/java/com/example/logcollect/process/logSrream.java:
    using Kafka stream to output data processing of several topics to the one topic
##### src/main/java/com/example/logcollect/process/Comsumer.java:
    process the topic and store it in redis