@echo off
setlocal EnableExtensions
cd /d "%~dp0"

net session >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Administrator rights are required.
    echo Right-click open-firewall.bat and choose "Run as administrator".
    pause
    exit /b 1
)

if exist "%~dp0config.local.bat" call "%~dp0config.local.bat"
if not defined FZWYWM_PORT set "FZWYWM_PORT=8080"
set "PORT=%FZWYWM_PORT%"

echo Adding inbound TCP rule "FZwywm %PORT%" for private and public networks...
netsh advfirewall firewall delete rule name="FZwywm %PORT%" >nul 2>&1
netsh advfirewall firewall add rule name="FZwywm %PORT%" dir=in action=allow protocol=TCP localport=%PORT% profile=private,public
if errorlevel 1 (
    echo [ERROR] Failed to add the firewall rule.
    pause
    exit /b 1
)

echo.
echo [OK] Firewall rule added. Only use this on trusted networks.
pause