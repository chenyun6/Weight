#!/bin/bash

# Weight服务重启脚本
# 使用方法: ./restart.sh [dev|prod]

# 获取运行环境（dev或prod，默认为dev）
PROFILE=${1:-dev}

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "=========================================="
echo "重启Weight服务"
echo "运行环境: $PROFILE"
echo "=========================================="

# 先停止服务
if [ -f "./stop.sh" ]; then
    ./stop.sh
    sleep 2
else
    echo "警告: 未找到stop.sh脚本"
fi

# 再启动服务
if [ -f "./start.sh" ]; then
    ./start.sh "$PROFILE"
else
    echo "错误: 未找到start.sh脚本"
    exit 1
fi
