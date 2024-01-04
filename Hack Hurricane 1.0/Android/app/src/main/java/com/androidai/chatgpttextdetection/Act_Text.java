package com.androidai.chatgpttextdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidai.chatgpttextdetection.databinding.ActivityActResultsBinding;
import com.androidai.chatgpttextdetection.databinding.ActivityActTextBinding;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Act_Text extends AppCompatActivity {
    ActivityActTextBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        binding.submit.setOnClickListener(view -> {
            if(binding.text.getText().toString().isEmpty()){
                Toast.makeText(this, "Please Input something...", Toast.LENGTH_SHORT).show();
            }else{
                binding.progressCircular.setVisibility(View.VISIBLE);
                String url = "https://zerogpt.p.rapidapi.com/api/v1/detectText";
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", "7b9cfe23bdmshd64c67f6423dcbep1a37c3jsn982c4280e095");
                headers.put("X-RapidAPI-Host", "zerogpt.p.rapidapi.com");
                Map<String, String> postData = new HashMap<>();
                postData.put("input_text", binding.text.getText().toString());
                makePostRequest(url, headers, postData);
            }
        });
    }
    private void makePostRequest(String url, final Map<String, String> headers, final Map<String, String> postData) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        binding.progressCircular.setVisibility(View.GONE);
                        Log.d("debug",response.toString());
                        Gson gson = new Gson();
                        DetectionResponse detectionResponse = gson.fromJson(response, DetectionResponse.class);

                        if (detectionResponse.isSuccess()) {
                            DetectionResponse.Data data = detectionResponse.getData();
                            double isHumanWritten = data.getIsHumanWritten();
                            double isGptGenerated = data.getIsGptGenerated();
                            String feedbackMessage = data.getFeedbackMessage();
                            List<String> gptGeneratedSentences = data.getGptGeneratedSentences();
                            int wordsCount = data.getWordsCount();

                            SharedPreferences sp = getSharedPreferences("BASE_APP", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("isHumanWritten", String.valueOf(isHumanWritten));
                            editor.putString("isGptGenerated", String.valueOf(isGptGenerated));
                            editor.putString("feedbackMessage", String.valueOf(feedbackMessage));
                            editor.putString("wordsCount", String.valueOf(wordsCount));
                            editor.apply();

                            Intent i = new Intent(Act_Text.this, Act_Results.class);
                            startActivity(i);

                        } else {
                            String errorMessage = detectionResponse.getMessage();
                            Toast.makeText(Act_Text.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.progressCircular.setVisibility(View.GONE);

                        Log.d("debug",error.getMessage());
                        Toast.makeText(Act_Text.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return postData;
            }
        };

        requestQueue.add(stringRequest);
    }

}