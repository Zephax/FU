@echo off
setlocal

set "MYSQL=D:\MySQL\MySQL Server 8.0\bin\mysql.exe"
if not exist "%MYSQL%" (
    for /f "delims=" %%i in ('where mysql 2^>nul') do set "MYSQL=%%i"
)
if not exist "%MYSQL%" (
    echo [ERROR] mysql.exe was not found. Please check MySQL 8.0.
    pause
    exit /b 1
)

echo Creating database fzwywm and user fzwywm.
echo Please enter the MySQL root password when prompted.
echo.
"%MYSQL%" -u root -p --default-character-set=utf8mb4 < "%~dp0init-db.sql"

if errorlevel 1 (
    echo.
    echo [ERROR] Database initialization failed. Check the root password.
    pause
    exit /b 1
)

echo.
echo [OK] Database is ready. You can start the app now.
if /i not "%~1"=="--no-pause" pause
