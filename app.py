import os
import torch
from fastapi import FastAPI
from pydantic import BaseModel
from transformers import AutoTokenizer, AutoModelForSequenceClassification, logging

logging.set_verbosity_error()

MODEL_TOKENIZER = "ruSpamModels/ruSpam_big"
MODEL_WEIGHTS = "ruSpamModels/ruSpam_big"
HF_TOKEN = os.environ.get("HUGGINGFACE_TOKEN") or os.environ.get("HF_TOKEN") or None


def load_model():
    try:
        tokenizer = AutoTokenizer.from_pretrained(
            MODEL_TOKENIZER,
            token=HF_TOKEN,
        )
        model = AutoModelForSequenceClassification.from_pretrained(
            MODEL_WEIGHTS,
            num_labels=1,
            token=HF_TOKEN,
        ).to(torch.device("cuda" if torch.cuda.is_available() else "cpu")).eval()
        return tokenizer, model, None
    except Exception as e:
        return None, None, str(e)


tokenizer, model, load_error = load_model()

app = FastAPI()

class PredictRequest(BaseModel):
    text: str

@app.post("/predict")
def predict(req: PredictRequest):
    if tokenizer is None or model is None:
        return {
            "spam": False,
            "score": 0.0,
            "error": "Model unavailable. Check HUGGINGFACE_TOKEN and access rights.",
            "detail": load_error
        }

    encoding = tokenizer(
        req.text,
        padding="max_length",
        truncation=True,
        max_length=512,
        return_tensors="pt"
    )
    input_ids = encoding["input_ids"].to(model.device)
    attention_mask = encoding["attention_mask"].to(model.device)

    with torch.no_grad():
        logits = model(input_ids, attention_mask=attention_mask).logits
        score = torch.sigmoid(logits).cpu().numpy()[0][0]

    return {
        "spam": bool(score >= 0.5),
        "score": float(score)
    }
