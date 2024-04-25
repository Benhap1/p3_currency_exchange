# Сервис Обмен валюты
Приложение для практического кейса "Обмен Валюты" обучающей платформы [Skillbox](https://skillbox.ru)

### Изменения в коде
http://localhost:8080/api/currency/all - в ответе возвращаются все хранящиеся в базе данных валюты и их стоимость. 
1. Добавлен файл миграции add_iso_char_code_to_currency.xml для поля буквенного кода валюты
2. Добавлен файл миграции remove_inserted_currency_rows.xml, удаляющий старые записи, так как
   метод класса CurrencyParserService парсит XML-файл целиком.
3. В классе CurrencyParserService создан метод init(), который сразу подгружает данные банка в базу,
   а parseAndSaveCurrencyData() сработает по условию через 1 час.
4. Созданы маппинг-классы Valute, ValCurs.

## Используемые технологии

- Spring Boot 2.7
- Maven 3
- Lombok
- Mapstruct
- Liquibase
- PostgreSQL

## Требования

### JDK 17

Проект использует синтаксис Java 17. Для локального запуска вам потребуется
установленный JDK 17.

### Docker
Для запуска проекта вам потребуется установленный и запущенный Docker.
Для запуска БД(PostgreSQL) требется запустить соответствующий сервис в Docker.

### Подключение к интернету

Подключение к интернету для получения курсов валют

## Полезные команды

### Запуск контейнера с базой данных

```bash
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=postgres -d postgres
```

Пользователь для подключения к контейнеру `postgres`.

### IntelliJ IDEA

Запустите main метод класса Application

### Запросы API

Создание новой записи о валюте

```bash
curl --request POST \
  --url http://localhost:8080/api/currency/create \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "Доллар Готэм-Сити",
  "nominal": 3,
  "value": 32.2,
  "isoNumCode": 1337
}'
```

Получение Валюты по id

```bash
curl --request GET \
  --url http://localhost:8080/api/currency/1333
```

Конвертация валюты по числовому коду

```bash
curl --request GET \
--url http://localhost:8080/api/currency/convert?value=100&numCode=840
```

