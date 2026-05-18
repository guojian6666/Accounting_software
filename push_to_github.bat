@echo off
echo 正在初始化 Git 仓库...
git init 2>nul || (echo Git 未找到，请关闭当前终端后重新打开；或确保 Git 已正确安装； & pause; exit /b 1)

git config user.name "guojian6666"
git config user.email "user@example.com"

echo.
echo 正在添加文件...
git add .

echo.
echo 正在提交更改...
git commit -m "Initial commit: 记账助手 Android App"

echo.
echo 正在关联远程仓库...
git remote add origin https://github.com/guojian6666/Accounting_software.git 2>nul || git remote set-url origin https://github.com/guojian6666/Accounting_software.git

echo.
echo 正在推送到 GitHub...
echo 用户名: guojian6666
echo 密码: (请输入您的 Personal Access Token，不是真实密码！)
git branch -M main
git push -u origin main

echo.
echo 如果一切顺利，代码已成功推送到 GitHub！
pause
