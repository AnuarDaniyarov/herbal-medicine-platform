# Medicine Platform — backend MVP

Backend дипломного проекта на Spring Boot 4, Java 21 и PostgreSQL. В текущем MVP реализованы чтение каталога растений и соединений, связи между ними, происхождение данных, экспериментальные измерения, OpenAPI и сохраняемый API-прототип прогноза.

## Требования

- JDK 21
- PostgreSQL 15 или новее
- Maven Wrapper (`mvnw.cmd` уже включён)

## Запуск

Создайте базу `medicine_platform` в локальном PostgreSQL. Для отдельной локальной БД можно использовать `docker compose up -d db`, если Docker установлен. Приложение использует такие переменные окружения:

| Переменная | Значение по умолчанию |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/medicine_platform` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | задаётся для локальной PostgreSQL |
| `SERVER_PORT` | `8081` |

Передавайте пароль через переменную окружения для своей установки PostgreSQL; не сохраняйте реальные пароли в репозитории. Пример для PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/medicine_platform'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = 'your-local-password'
```

Запуск из PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Или запустите `MedicinePlatformApplication` из IntelliJ IDEA с JDK 21. Flyway применяет SQL-миграции из `src/main/resources/db/migration`; Hibernate проверяет схему (`ddl-auto: validate`) и не изменяет её автоматически. Для старой локальной базы включён Flyway baseline версии 0: существующая таблица прежнего MVP `plant_compounds` сохраняется, но текущий нормализованный API её пока не импортирует.

## API и документация

Swagger UI: `http://localhost:8081/swagger-ui.html`  
OpenAPI JSON: `http://localhost:8081/v3/api-docs`  
Health check: `http://localhost:8081/actuator/health`

Все каталожные списки имеют пагинацию (`page`, `size`, максимальный `size` — 100) и возвращают `content`, `page`, `size`, `totalElements`, `totalPages`, `first`, `last`.

| Метод | Путь | Назначение |
| --- | --- | --- |
| `GET` | `/api/plants?query=...&page=0&size=20` | Поиск растений |
| `GET` | `/api/plants/{id}` | Карточка растения |
| `GET` | `/api/plants/{id}/compounds?page=0&size=20` | Соединения растения |
| `GET` | `/api/compounds?query=...&plantId=1&structureStatus=UNVERIFIED` | Поиск и фильтрация соединений |
| `GET` | `/api/compounds/{id}` | Карточка соединения и происхождение |
| `GET` | `/api/compounds/{id}/measurements` | Экспериментальные измерения |
| `GET` | `/api/assays` | Список тестов/активностей |
| `GET` | `/api/sources/{id}` | Источник данных |
| `POST` | `/api/predictions` | Сохранить результат-заглушку запроса прогноза (`201`) |
| `GET` | `/api/predictions/{id}` | Получить сохранённый результат |

Для неизвестного ID возвращается `404`. Ошибки API имеют общий JSON-формат: `timestamp`, `status`, `code`, `message`, `path`, `fieldErrors`.

### Пример запроса прототипа прогноза

```powershell
$payload = @{ smiles = 'CCO'; activityCode = 'demo' } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/predictions' `
  -ContentType 'application/json' -Body $payload
```

Этот endpoint пока **не выполняет химический или биологический прогноз**. Он записывает запрос со статусом `STUB`; вероятности и предсказанной активности нет. В ответе есть предупреждение. `SMILES` сейчас проверяется только на наличие и допустимую длину строки, полноценная химическая проверка не подключена.

Каталожные данные пока загружаются через миграции/подготовку БД: публичные операции изменения каталога не добавлены. Авторизация и Angular в текущий backend-only этап не входят. Сравнение соединений и выгрузка отчётов остаются следующими задачами плана.

## Тесты

```powershell
.\mvnw.cmd test
```

Интеграционные тесты используют изолированную H2-базу и запускают Flyway-миграции; PostgreSQL для выполнения этого набора тестов не требуется.
