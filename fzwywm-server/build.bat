@echo off
cd /d "%~dp0"

if not exist "mvnw.cmd" (
    echo [ERROR] mvnw.cmd was not found.
    pause
    exit /b 1
)

call "%~dp0mvnw.cmd" -s settings.xml clean package -DskipTests
if errorlevel 1 (
    echo.
    echo [ERROR] Build failed. See the log above.
    pause
    exit /b 1
)

echo.
echo [OK] Built target\fzwywm-server-1.0.0.jar
pause
