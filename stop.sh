#!/bin/bash

# Weight服务停止脚本

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# 查找jar文件所在目录
JAR_FILE=weight.jar

if [ -z "$JAR_FILE" ]; then
    echo "错误: 未找到Weight-start jar文件"
    exit 1
fi

JAR_DIR="$(cd "$(dirname "$JAR_FILE")" && pwd)"
PID_FILE="$JAR_DIR/weight.pid"

if [ ! -f "$PID_FILE" ]; then
    echo "PID文件不存在，服务可能未运行"
    exit 1
fi

PID=$(cat "$PID_FILE")

if [ -z "$PID" ]; then
    echo "PID文件为空，清理PID文件"
    rm -f "$PID_FILE"
    exit 1
fi

if ! ps -p "$PID" > /dev/null 2>&1; then
    echo "进程 $PID 不存在，清理PID文件"
    rm -f "$PID_FILE"
    exit 1
fi

echo "正在停止服务，PID: $PID"

# 优雅停止
kill "$PID"

# 等待进程结束，最多等待30秒
for i in {1..30}; do
    if ! ps -p "$PID" > /dev/null 2>&1; then
        echo "服务已停止"
        rm -f "$PID_FILE"
        exit 0
    fi
    sleep 1
done

# 如果30秒后还在运行，强制杀死
if ps -p "$PID" > /dev/null 2>&1; then
    echo "服务未在30秒内停止，强制杀死进程"
    kill -9 "$PID"
    sleep 1
    rm -f "$PID_FILE"
    echo "服务已强制停止"
else
    rm -f "$PID_FILE"
    echo "服务已停止"
fi
