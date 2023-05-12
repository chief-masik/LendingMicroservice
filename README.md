# Credit service

Данный проект - выпускной проект для МТС финтех академии по написанию CRUD-service на тему «Оформление кредита» и дополнений к нему.

### В проекте было реализовано 4 микросервиса:

* lending-microservice - сервис, отвечающий за обработку кредитных заявок.
* eureka-server - сервис, отвечающий за регистрацию и обнаружение микросервисов.(https://github.com/chief-masik/eureka-server-bank-service)
* api-gateway - сервис, являющийся единой точкой входа для всех пользователей.(https://github.com/chief-masik/api-gateway-bank-service)
* front-microservice - front-end service с интегрированным Spring Security.(https://github.com/chief-masik/front-microservice)

### Для реализации проекта использовались такие технологии как:

* Spring (Boot, Security, Cloud, Web, Data)
* PostgreSQL
* H2
* Liquibase
* Swagger
* ShedLock
* Hibernate Validator
* Lombok
* Thymeleaf
* И другие

### Запуск сервиса

1. Необходимо установить и запустить PostgreSQL (рекомендую использовать GUI pg admin 4). После следует создать две базы данных с именами: account_db и loan_db.
2. Открыть каждый из микросервисов.
3. В микросервисе lending-microservice в файле application.yaml необходимо заполнить поля username и password (владелец loan_db в PostgreSQL), а также сверить порт с указанным на сервере (PostgreSQL 14/15->Properties->Port)
4. Тоже самое для front-microservice с account_db.
5. Поочередно запустить сервисы: eureka-server -> lending-microservice -> front-microservice -> api-gateway
6. По адресу http://localhost:8761 можно увидеть видимые сервером сервисы (должен видеть lending-service, front-service и api-gateway) 
7. Взаимодействие с front-microservice и lending-microservice происходит на порту 8765. Адрес стартовой страницы сайта: http://localhost:8765/account/start-page. Пример адреса запроса к lending-microservice: http://localhost:8765/loan-service/getTariffs.
8. Для просмотра документации к rest-контроллеру lending-microservice с помощью Swagger UI необходимо перейти по адресу http://localhost:00000/swagger-ui/index.html, где вместо '00000' нужно подставить порт запустившегося сервиса. В application.yaml установлено "port:0", т.е. при каждом новом запуске сервиса будет генерироваться новый порт. Актуальный порт можно посмотреть в логах запуска сервиса, либо указать фиксированный порт в application.yaml и использовать его.
