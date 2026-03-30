@echo off
set KEYCLOAK_ADMIN=admin
set KEYCLOAK_ADMIN_PASSWORD=admin
echo Iniciando Keycloak 26.1.0 en puerto 7080...
echo Usuario: admin
echo Password: admin
C:\keycloak\bin\kc.bat start-dev --http-port 7080
pause
