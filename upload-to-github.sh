#!/bin/bash

# Weighty 项目上传到 GitHub 脚本

cd "$(dirname "$0")"

echo "=========================================="
echo "Weighty 项目上传到 GitHub"
echo "=========================================="
echo ""

# 1. 初始化 Git 仓库（如果还没有）
if [ ! -d .git ]; then
    echo "1. 初始化 Git 仓库..."
    git init
    echo "✓ Git 仓库初始化完成"
else
    echo "1. Git 仓库已存在"
fi

# 2. 添加所有文件
echo ""
echo "2. 添加文件到 Git..."
git add .
echo "✓ 文件已添加"

# 3. 创建提交
echo ""
echo "3. 创建提交..."
git commit -m "Initial commit: Weighty - Weight management system with DDD architecture" || {
    echo "⚠ 提交失败，可能没有新文件需要提交"
}
echo "✓ 提交完成"

# 4. 显示当前状态
echo ""
echo "4. 当前 Git 状态："
git log --oneline -1 2>/dev/null || echo "  暂无提交记录"
echo ""

# 5. 检查远程仓库
echo "5. 远程仓库配置："
git remote -v
echo ""

echo "=========================================="
echo "下一步操作："
echo "=========================================="
echo ""
echo "1. 在 GitHub 创建新仓库（名称建议：weighty 或 weighty-app）"
echo ""
echo "2. 添加远程仓库并推送："
echo "   git remote add origin https://github.com/YOUR_USERNAME/REPO_NAME.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "或者使用 SSH："
echo "   git remote add origin git@github.com:YOUR_USERNAME/REPO_NAME.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
