## Домашнее задание №16

Приложение каталог книг в библиотеке.

К приложению подключен Spring Boot Actuator, включены метрики, healthchecks и logfile.

Добавлен индикатор, проверяющий количество обращений к ресурсам по url /api/v1/*\
Если за прошедшую минуту было меньше 10 обращений, то возвращает статус DOWN.
Состояние индикатора доступно по адресу
http://localhost:8080/actuator/health/requestCountingIndicator

В каталоге [hw16-ui](../hw16-ui) фронтенд приложения на React.

#### Запустить приложение
```
mvn package spring-boot:repackage
$JAVA_HOME/bin/java -jar target/hw16-actuator-1.0.jar
```
Приложение будет доступно по адресу http://localhost:8080

#### Отдельно запустить фронтенд
В каталоге [hw16-ui](../hw16-ui) выполнить
```
npm start
```
По адресу http://localhost:3000 будет доступен фронтенд.
