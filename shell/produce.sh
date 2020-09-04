#!/usr/bin/env bash
# 日志级别：DEBUG INFO WARN ERROR FATAL
level=(DEBUG INFO WARN ERROR FATAL)
# 操作
operation=(create retrieve update delete)
# 乐器类
music=(Guitar Drum Bass Piano Violin Accordion Trombone Triangle Whistle Harmonica)
i=1
for i in $(seq 1 100)
    do
        serial=`openssl rand -base64 8 | cksum | cut -c1-8`
        # 随机下标
        index1=`expr $serial % 5`
        index2=`expr $serial % 4`
        index3=`expr $serial % 10`
        # 当前时间
        ctime=`date +'%Y-%m-%d %H:%M:%S'`
        # 转换格式
        cdate=`date -d "$ctime"`
        # 追加日志
        echo "${level[$index1]} $ctime org.example.${music[$index3]}:${operation[$index2]}() $serial" >> logs/ops.log
        sleep 1
    done