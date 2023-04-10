以下内容是 Windows 下的“清除脚本”的内容。`REPOSITORY_PATH` 变量的值根据具体情况的不同可以有调整。

```sh
@echo off

set REPOSITORY_PATH=C:%HOMEPATH%\.m2\repository
rem 正在搜索...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    echo %%i
    del /s /q "%%i"
)
rem 搜索完毕
pause
```

