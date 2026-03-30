@echo off
echo ==============================================
echo Starting the rest of Fitness Microservices 
echo (Assuming Eureka is already running)
echo ==============================================

echo Starting Config Server...
start "Config Server" cmd /k "cd configserver && mvnw.cmd spring-boot:run"

echo Waiting for Config Server to initialize...
timeout /t 15 /nobreak

echo Starting API Gateway...
start "API Gateway" cmd /k "cd gateway && mvnw.cmd spring-boot:run"

echo Starting User Service...
start "User Service" cmd /k "cd UserService && mvnw.cmd spring-boot:run"

echo Starting Activity Service...
start "Activity Service" cmd /k "cd activityservice && mvnw.cmd spring-boot:run"

echo Starting AI Service...
start "AI Service" cmd /k "cd aiservices && mvnw.cmd spring-boot:run"

echo ==============================================
echo Rest of services launched.
echo ==============================================
