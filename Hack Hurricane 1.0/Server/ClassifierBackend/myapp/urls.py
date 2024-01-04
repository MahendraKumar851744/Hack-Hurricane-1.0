from django.urls import path
from myapp.views import process_text

urlpatterns = [
    path('api/process_text/', process_text, name='process_text'),
]
