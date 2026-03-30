@echo off
echo ==============================================
echo Starting All Fitness Microservices (Java)
echo ==============================================

echo [1/6] Starting Config Server...
start "Config Server" cmd /k "cd configserver && if exist mvnw.cmd (mvnw.cmd spring-boot:run) else (mvn spring-boot:run)"
echo Waiting for Config Server to initialize...
timeout /t 15 /nobreak

echo [2/6] Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka && if exist mvnw.cmd (mvnw.cmd spring-boot:run) else (mvn spring-boot:run)"
echo Waiting for Eureka Server to initialize...
timeout /t 20 /nobreak

echo [3/6] Starting API Gateway...
start "API Gateway" cmd /k "cd gateway && if exist mvnw.cmd (mvnw.cmd spring-boot:run) else (mvn spring-boot:run)"
timeout /t 5 /nobreak

echo [4/6] Starting User Service...
start "User Service" cmd /k "cd UserService && if exist mvnw.cmd (mvnw.cmd spring-boot:run) else (mvn spring-boot:run)"

echo [5/6] Starting Activity Service...
start "Activity Service" cmd /k "cd activityservice && if exist mvnw.cmd (mvnw.cmd spring-boot:run) else (mvn spring-boot:run)"

echo [6/6] Starting AI Service...
start "AI Service" cmd /k "cd aiservices && if exist mvnw.cmd (mvnw.cmd spring-boot:run) else (mvn spring-boot:run)"

echo ==============================================
echo All services are launching!
echo Please check the newly opened console windows for their logs.
echo ==============================================
pause
