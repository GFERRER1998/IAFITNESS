@echo off
set KAFKA_HEAP_OPTS=-Xmx1G -Xms1G
echo Iniciando Kafka en modo KRaft (bypass wmic)...
C:\kafka213\kafka_2.13-4.2.0\bin\windows\kafka-server-start.bat C:\kafka213\kafka_2.13-4.2.0\config\server.properties
pause
