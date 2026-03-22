@echo off
setlocal
set SCRIPT_DIR=%~dp0
mvn -gs "%SCRIPT_DIR%.mvn\global-settings.xml" -s "%SCRIPT_DIR%.mvn\settings.xml" "-Dmaven.repo.local=%SCRIPT_DIR%.mvn\repository" %*