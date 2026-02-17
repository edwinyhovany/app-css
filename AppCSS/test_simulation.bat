@echo off
chcp 65001 >nul
echo ============================================
echo 🧪 CCS - SCRIPTS DE PRUEBA
echo ============================================
echo.

set BASE_URL=http://localhost:8080/api/v1

:menu
echo.
echo SELECCIONA UN CASO DE PRUEBA:
echo.
echo 1. 🚨 Test Emergencia ^<2s
echo 2. 📊 Test Throughput 500 señales/s (10 segundos)
echo 3. 📊 Test Throughput 500 señales/s (2 MINUTOS = 60,000 señales^)
echo 4. 📋 Crear Regla de Velocidad
echo 5. 🚛 Escenario Completo: Camión Refrigerado
echo 6. 📡 Enviar Señal Normal
echo 7. 🚨 Botón de Pánico
echo 8. 📊 Ver Estadísticas de RabbitMQ
echo 9. ❌ Salir
echo.
set /p choice="Ingresa opción (1-9): "

if "%choice%"=="1" goto test_emergency
if "%choice%"=="2" goto test_throughput_short
if "%choice%"=="3" goto test_throughput_full
if "%choice%"=="4" goto create_rule
if "%choice%"=="5" goto scenario_truck
if "%choice%"=="6" goto send_signal
if "%choice%"=="7" goto panic_button
if "%choice%"=="8" goto rabbitmq_stats
if "%choice%"=="9" goto end

echo Opción inválida
goto menu

:test_emergency
echo.
echo ============================================
echo 🚨 TEST: EMERGENCIA ^<2 SEGUNDOS
echo ============================================
echo.
echo Presiona Ctrl+C para cancelar en los próximos 3 segundos...
timeout /t 3 >nul
echo.
echo Enviando emergencia para VEH001...
echo.

curl -X POST "%BASE_URL%/signals/panic?vehicleId=VEH001" ^
  -H "Content-Type: application/json"

echo.
echo.
echo ✅ Emergencia enviada
echo 👁️ Revisa los logs de la aplicación para ver:
echo    - Tiempo de procesamiento ^(debe ser ^<2000ms^)
echo    - Acciones ejecutadas: CALL_AUTHORITIES + SEND_SMS + NOTIFY_OWNER
echo.
pause
goto menu

:test_throughput_short
echo.
echo ============================================
echo 📊 TEST: THROUGHPUT 500 SEÑALES/S (10s)
echo ============================================
echo.
echo Esto enviará 5,000 señales en 10 segundos
echo.
pause

curl -X POST "%BASE_URL%/testing/throughput?signalsPerSecond=500&durationSeconds=10" ^
  -H "Content-Type: application/json"

echo.
echo.
echo ✅ Test completado
pause
goto menu

:test_throughput_full
echo.
echo ============================================
echo 📊 TEST: THROUGHPUT 500 SEÑALES/S (2 MINUTOS)
echo ============================================
echo.
echo ⚠️ ADVERTENCIA: Esto enviará 60,000 señales
echo    Tiempo estimado: 2 minutos
echo.
echo Presiona Ctrl+C para cancelar en los próximos 5 segundos...
timeout /t 5 >nul
echo.
echo Iniciando test de carga pesada...
echo.

curl -X POST "%BASE_URL%/testing/throughput?signalsPerSecond=500&durationSeconds=120" ^
  -H "Content-Type: application/json"

echo.
echo.
echo ✅ Test de 2 minutos completado
echo 📊 Verifica las métricas en Prometheus: http://localhost:8080/actuator/prometheus
pause
goto menu

:create_rule
echo.
echo ============================================
echo 📋 CREAR REGLA: Velocidad Máxima 100 km/h
echo ============================================
echo.

curl -X POST "%BASE_URL%/testing/rules" ^
  -H "Content-Type: application/json" ^
  -d "{\"vehicleId\":\"VEH002\",\"name\":\"Velocidad Máxima Bus\",\"conditionType\":\"THRESHOLD\",\"conditionParameter\":\"speed\",\"conditionOperator\":\">\",\"conditionValue\":\"100\",\"actions\":[\"SEND_SMS\",\"SEND_EMAIL\"],\"active\":true}"

echo.
echo.
echo ✅ Regla creada para VEH002
pause
goto menu

:scenario_truck
echo.
echo ============================================
echo 🚛 ESCENARIO: CAMIÓN REFRIGERADO
echo ============================================
echo.
echo Simulará un viaje completo con:
echo   1. Operación normal
echo   2. Exceso de velocidad (trigger regla)
echo   3. Temperatura fuera de rango (trigger regla)
echo   4. Emergencia (botón de pánico)
echo.
pause

curl -X POST "%BASE_URL%/testing/scenario/refrigerated-truck/VEH001" ^
  -H "Content-Type: application/json"

echo.
echo.
echo ✅ Escenario completado
echo 👁️ Revisa logs para ver las 3 reglas disparadas
pause
goto menu

:send_signal
echo.
echo ============================================
echo 📡 ENVIAR SEÑAL NORMAL
echo ============================================
echo.

curl -X POST "%BASE_URL%/signals" ^
  -H "Content-Type: application/json" ^
  -d "{\"vehicleId\":\"VEH001\",\"signalType\":\"NORMAL\",\"timestamp\":\"2026-02-13T20:00:00\",\"latitude\":4.6097,\"longitude\":-74.0817,\"speed\":60,\"temperature\":3,\"emergency\":false}"

echo.
echo.
echo ✅ Señal enviada
pause
goto menu

:panic_button
echo.
echo ============================================
echo 🚨 BOTÓN DE PÁNICO
echo ============================================
echo.
set /p vehicle="Ingresa ID del vehículo (ej: VEH001): "

curl -X POST "%BASE_URL%/signals/panic?vehicleId=%vehicle%" ^
  -H "Content-Type: application/json"

echo.
echo.
echo ✅ Emergencia disparada
pause
goto menu

:rabbitmq_stats
echo.
echo ============================================
echo 📊 ESTADÍSTICAS RABBITMQ
echo ============================================
echo.
echo Abriendo RabbitMQ Management UI...
start http://localhost:15672
echo.
echo Usuario: guest
echo Password: guest
echo.
echo Revisa:
echo   - Queues: ccs.emergency.queue, ccs.signals.queue
echo   - Message rates
echo   - Consumers
echo.
pause
goto menu

:end
echo.
echo ¡Hasta luego!
exit