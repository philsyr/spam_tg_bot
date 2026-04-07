# Environment Setup

## Getting Started

### 1. Telegram Bot Token

1. Create a bot with [@BotFather](https://t.me/botfather) in Telegram
2. Get your token
3. Set it as environment variable:

```bash
export BOT_TOKEN="YOUR_TOKEN_HERE"
```

### 2. Hugging Face Token

1. Register on https://huggingface.co
2. Go to https://huggingface.co/ruSpamModels/ruSpam_big and click "Access repository"
3. Get token from https://huggingface.co/settings/tokens
4. Set it:

```bash
export HUGGINGFACE_TOKEN="hf_YOUR_TOKEN_HERE"
```

### 3. Run Both Services

**Service 1: Python API (one terminal)**
```bash
cd /path/to/spam_tg_bot
source venv/bin/activate
export HUGGINGFACE_TOKEN="hf_YOUR_TOKEN_HERE"
uvicorn app:app --host 127.0.0.1 --port 8001
```

**Service 2: Java Bot (another terminal)**
```bash
cd /path/to/spam_tg_bot
export BOT_TOKEN="YOUR_TOKEN_HERE"
mvn spring-boot:run
```

## Never Commit

- Telegram bot token
- Hugging Face token
- API keys or credentials
- Private configuration files

Use `.env` files locally (not in git) or environment variables.
