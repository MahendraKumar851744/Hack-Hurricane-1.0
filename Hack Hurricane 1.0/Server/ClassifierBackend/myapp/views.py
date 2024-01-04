from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from transformers import pipeline
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from transformers import logging
import os

@csrf_exempt
def process_text(request):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            input_text = data.get('text', '')  # Use get to handle missing 'text' key
            result = classifyText(input_text)
            if result[0]>result[2]:
                feedback_message = 'The text is human written.'
            else:
                feedback_message = 'The Content is AI Generated.'    
            response_data = {
                "success": True,
                "data": {
                    "is_human_written": round(result[0], 2),
                    "is_gpt_generated": round(result[2], 2),
                    "feedback_message": feedback_message,
                    "words_count": len(input_text.split())
                },
                "code": 200,
                "message": "Detection complete"
            }
            return JsonResponse(response_data)
        except json.JSONDecodeError:
            return JsonResponse({'error': 'Invalid JSON format'}, status=400)
    else:
        return JsonResponse({'error': 'Invalid request method'}, status=400)
    
def classifyText(text):
    logging.set_verbosity_error()
    absolute_path = os.path.dirname(__file__)
    relative_path = "./roberta-base-openai-detector/"
    full_path = os.path.join(absolute_path, relative_path)
    tokenizer = AutoTokenizer.from_pretrained(full_path, local_files_only=True)
    model = AutoModelForSequenceClassification.from_pretrained(full_path, local_files_only=True)
    classifier = pipeline("sentiment-analysis", model=model, tokenizer=tokenizer)

    if text:
        res = classifier(text, truncation=True, max_length=510)
        label = res[0]['label']
        score = res[0]['score']

        if label == 'Real':
            real_score = score * 100
            fake_score = 100 - real_score
            real_score_label = f"{real_score:.1f}%"
            fake_score_label = f"{fake_score:.1f}%"
        else:
            fake_score = score * 100
            real_score = 100 - fake_score
            fake_score_label = f"{fake_score:.1f}%"
            real_score_label = f"{real_score:.1f}%"

        return [real_score, real_score_label, fake_score, fake_score_label, '']
    else:
        return [50.0, "", 50.0, "", '']
