#!/bin/bash

# Weight服务启动脚本
# 使用方法: ./start.sh [dev|prod]

# 获取脚本所在目录（jar文件所在目录）
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# 查找jar文件
JAR_FILE=weight.jar

if [ -z "$JAR_FILE" ]; then
    echo "错误: 未找到Weight-start jar文件"
    echo "请确保jar文件在当前目录或子目录中"
    exit 1
fi

echo "找到jar文件: $JAR_FILE"

# 获取jar文件所在目录
JAR_DIR="$(cd "$(dirname "$JAR_FILE")" && pwd)"
JAR_NAME="$(basename "$JAR_FILE")"

# 设置日志目录为jar所在目录下的logs文件夹
LOG_PATH="$JAR_DIR/logs"

# 创建日志目录
mkdir -p "$LOG_PATH"

# 设置环境变量
export LOG_PATH="$LOG_PATH"

# 获取运行环境（dev或prod，默认为dev）
PROFILE=${1:-dev}

# JVM参数
JVM_OPTS="-Xms512m -Xmx1024m"
JVM_OPTS="$JVM_OPTS -XX:+UseG1GC"
JVM_OPTS="$JVM_OPTS -XX:MaxGCPauseMillis=200"
JVM_OPTS="$JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JVM_OPTS="$JVM_OPTS -XX:HeapDumpPath=$LOG_PATH/heapdump.hprof"
JVM_OPTS="$JVM_OPTS -Dspring.profiles.active=$PROFILE"
JVM_OPTS="$JVM_OPTS -DLOG_PATH=$LOG_PATH"

# PID文件路径
PID_FILE="$JAR_DIR/weight.pid"

# 检查是否已经运行
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "服务已经在运行中，PID: $PID"
        echo "如需重启，请先执行: ./stop.sh"
        exit 1
    else
        echo "清理无效的PID文件"
        rm -f "$PID_FILE"
    fi
fi

# 启动服务
echo "=========================================="
echo "启动Weight服务"
echo "JAR文件: $JAR_FILE"
echo "日志目录: $LOG_PATH"
echo "运行环境: $PROFILE"
echo "JVM参数: $JVM_OPTS"
echo "=========================================="

cd "$JAR_DIR"
nohup java $JVM_OPTS -jar "$JAR_NAME" > "$LOG_PATH/console.log" 2>&1 &

# 保存PID
echo $! > "$PID_FILE"

# 等待一下，检查进程是否启动成功
sleep 2
if ps -p $(cat "$PID_FILE") > /dev/null 2>&1; then
    echo "服务启动成功！"
    echo "PID: $(cat "$PID_FILE")"
    echo "日志目录: $LOG_PATH"
    echo "查看日志: tail -f $LOG_PATH/weight.log"
    echo "查看控制台日志: tail -f $LOG_PATH/console.log"
else
    echo "服务启动失败，请查看日志: $LOG_PATH/console.log"
    rm -f "$PID_FILE"
    exit 1
fi
