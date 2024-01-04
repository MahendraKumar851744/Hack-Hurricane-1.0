/*
Developed as a part of hackathon solely - 'Hack Hurricane 1.0'
Author : Mahendra Kumar
Gmail: androiprogrammers@gmail.com
Linkedin: https://www.linkedin.com/in/mahendra-kumar-b5881324b/
*/

package com.androidai.chatgpttextdetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Act_OCR extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    List<Bitmap> capturedImages;
    RecyclerView rv_products;
    Ad_Images ad_products;
    ImageView back;
    ProgressBar progress_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ocr);
        capturedImages = new ArrayList<>();
        rv_products = findViewById(R.id.rv_products);
        progress_circular = findViewById(R.id.progress_circular);
        back = findViewById(R.id.back);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        rv_products.setLayoutManager(linearLayoutManager);
        ad_products = new Ad_Images(Act_OCR.this, (ArrayList<Bitmap>) capturedImages);
        rv_products.setAdapter(ad_products);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImages.add(imageBitmap);
            ad_products = new Ad_Images(Act_OCR.this, (ArrayList<Bitmap>) capturedImages);
            rv_products.setAdapter(ad_products);
        }
    }

    public void onSubmitButtonClick(View view) {
        progress_circular.setVisibility(View.VISIBLE);
        final StringBuilder buffer = new StringBuilder();
        final int totalImages = capturedImages.size();
        AtomicInteger completedTasks = new AtomicInteger(0);

        TextRecognitionCallback callback = new TextRecognitionCallback() {
            @Override
            public void onTextRecognized(String text) {
                buffer.append(text);
                int count = completedTasks.incrementAndGet();
                if (count == totalImages) {
                    handleTextRecognitionCompleted(buffer.toString());
                }
            }
        };

        for (Bitmap bitmap : capturedImages) {
            mlScan(bitmap, callback);
        }
    }

    private void handleTextRecognitionCompleted(String result) {

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();


        String url = "https://zerogpt.p.rapidapi.com/api/v1/detectText";

        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Key", "7b9cfe23bdmshd64c67f6423dcbep1a37c3jsn982c4280e095");
        headers.put("X-RapidAPI-Host", "zerogpt.p.rapidapi.com");

        Map<String, String> postData = new HashMap<>();
        postData.put("input_text", result);

        makePostRequest(url, headers, postData);
    }


    private void mlScan(Bitmap bitmap, TextRecognitionCallback callback) {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);

        task.addOnSuccessListener(firebaseVisionText -> {
            String text = firebaseVisionText.getText();
            callback.onTextRecognized(text);
        });

        task.addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            callback.onTextRecognized("");
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
                        progress_circular.setVisibility(View.GONE);
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

                            Intent i = new Intent(Act_OCR.this, Act_Results.class);
                            startActivity(i);

                        } else {
                            String errorMessage = detectionResponse.getMessage();
                            Toast.makeText(Act_OCR.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_circular.setVisibility(View.GONE);
                        Log.d("debug",error.getMessage());
                        Toast.makeText(Act_OCR.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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

interface TextRecognitionCallback {
    void onTextRecognized(String text);
}
