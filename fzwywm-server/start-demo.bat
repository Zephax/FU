@echo off
cd /d "%~dp0"

if exist "%~dp0config.local.bat" call "%~dp0config.local.bat"
if not defined FZWYWM_PORT set "FZWYWM_PORT=8080"
set "PORT=%FZWYWM_PORT%"
if not defined FZWYWM_DB_USER set "FZWYWM_DB_USER=fzwywm"
if not defined FZWYWM_DB_PASSWORD set "FZWYWM_DB_PASSWORD=Fzwywm@2026"

set "JAR=target\fzwywm-server-1.0.0.jar"
if not exist "%JAR%" (
    echo [ERROR] %JAR% was not found.
    echo Please run build.bat first.
    pause
    exit /b 1
)

if not defined FZWYWM_DB_URL call :ensure_local_db
if errorlevel 1 exit /b 1

echo Starting the website. The browser will open http://localhost:%PORT%/
echo Press Ctrl+C to stop the server.
echo.
start "" /b powershell -NoProfile -WindowStyle Hidden -Command "Start-Sleep -Seconds 8; Start-Process 'http://localhost:%PORT%/'"

java -jar "%JAR%"
pause
exit /b 0

:ensure_local_db
set "MYSQL=D:\MySQL\MySQL Server 8.0\bin\mysql.exe"
if not exist "%MYSQL%" (
    for /f "delims=" %%i in ('where mysql 2^>nul') do set "MYSQL=%%i"
)
if not exist "%MYSQL%" (
    echo [ERROR] mysql.exe was not found. Please check MySQL 8.0.
    exit /b 1
)

sc query MySQL80 | findstr RUNNING >nul
if errorlevel 1 (
    echo MySQL80 is not running. Trying to start it...
    net start MySQL80 >nul 2>&1
)
sc query MySQL80 | findstr RUNNING >nul
if errorlevel 1 (
    echo [ERROR] MySQL80 is not running.
    echo Start the MySQL80 service as administrator, then run this file again.
    exit /b 1
)

"%MYSQL%" -u "%FZWYWM_DB_USER%" -p"%FZWYWM_DB_PASSWORD%" -e "USE fzwywm;" >nul 2>&1
if errorlevel 1 (
    echo First run: the database is not ready.
    echo.
    call "%~dp0init-db.bat" --no-pause
    if errorlevel 1 exit /b 1
)
exit /b 0