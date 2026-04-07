# Anti-Spam Telegram Bot

Телеграм-бот для обнаружения спама в русскоязычных текстах с использованием нейросетевой модели ruSpam_big.

## 📋 Описание

Бот анализирует входящие сообщения и классифицирует их как спам или legitimate текст. Использует модель машинного обучения ruSpam_big, обученную на ~5 миллионах русскоязычных примеров.

## 🛠️ Технологический стек

- **Backend (Java)**: Spring Boot 3.4.3, Telegram Bots API 6.5.0, WebFlux
- **ML (Python)**: FastAPI, PyTorch, Transformers (Hugging Face)
- **Model**: ruSpam_big (RuBERT-based, binary classification)
- **Build**: Maven (Java), pip (Python)

## 📦 Архитектура

```
┌─────────────────────────────┐
│   Telegram User             │
└────────────┬────────────────┘
             │
             ▼
┌─────────────────────────────┐
│  Java Spring Boot Bot       │ :8080
│  (Receives messages)        │
└────────────┬────────────────┘
             │
    HTTP POST /predict
             │
             ▼
┌─────────────────────────────┐
│  Python FastAPI             │ :8001
│  (ruSpam_big model)         │
└─────────────────────────────┘
```

## 🚀 Быстрый старт

### Требования

- Java 17+
- Python 3.10+
- Maven 3.6+

### Установка

1. Клонируй репозиторий:
```bash
git clone <repo-url>
cd spam_tg_bot
```

2. Создай виртуальное окружение Python:
```bash
python3 -m venv venv
source venv/bin/activate  # macOS/Linux
# или
venv\Scripts\activate  # Windows
```

3. Установи Python-зависимости:
```bash
pip install -r requirements.txt
```

### 🔑 Hugging Face токен

Модель ruSpam_big требует доступа. Получи токен:

1. Зарегистрируйся на https://huggingface.co
2. Перейди на https://huggingface.co/ruSpamModels/ruSpam_big и нажми "Access repository"
3. Получи токен на https://huggingface.co/settings/tokens (выбери `read`)

### ▶️ Запуск

**Терминал 1: Python API (модель)**

```bash
cd /path/to/spam_tg_bot
source venv/bin/activate
export HUGGINGFACE_TOKEN="<твой_токен>"
uvicorn app:app --host 127.0.0.1 --port 8001
```

Ожидаемый вывод:
```
INFO:     Uvicorn running on http://127.0.0.1:8001 (Press CTRL+C to quit)
```

**Терминал 2: Java бот**

```bash
cd /path/to/spam_tg_bot
mvn spring-boot:run
```

Ожидаемый вывод:
```
Started AntiSpamTelegramBotApplication
Netty started on port 8080
```

### 🧪 Проверка

Отправь боту (`@anti_spam_telegram_bot`) сообщение:
- Обычное: "привет" → `Hello, Фил! You said: привет`
- Спам: "Выиграй 1000000 рублей!!!" → `Обнаружен спам! Сообщение: ...`

## ⚙️ Конфигурация

Параметры в `src/main/resources/application.properties`:

```properties
spring.application.name=anti-spam-telegram-bot
bot.name=anti_spam_telegram_bot
bot.token=<твой_токен_telegram_бота>
model.service.url=http://127.0.0.1:8001
```

Переменные окружения для Python:
- `HUGGINGFACE_TOKEN` - токен Hugging Face
- `HF_TOKEN` - альтернативное имя переменной

## 📝 API

### POST /predict

Классифицирует текст на спам.

**Request:**
```json
{
  "text": "Кликни здесь и выиграй 1000000 рублей!!!"
}
```

**Response:**
```json
{
  "spam": true,
  "score": 0.8622689
}
```

## 🔒 Безопасность

Никогда не коммитьте:
- Telegram bot token (`bot.token`)
- Hugging Face token (`HUGGINGFACE_TOKEN`)

Используй `.gitignore` и переменные окружения.

## 📖 Документация модели

https://huggingface.co/ruSpamModels/ruSpam_big

## 📄 Лицензия

CC-BY-NC-ND-4.0 (модель ruSpam_big)
