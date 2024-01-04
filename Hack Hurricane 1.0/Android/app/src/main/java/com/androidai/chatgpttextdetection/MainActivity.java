/*
Developed as a part of hackathon solely - 'Hack Hurricane 1.0'
Author : Mahendra Kumar
Gmail: androiprogrammers@gmail.com
Linkedin: https://www.linkedin.com/in/mahendra-kumar-b5881324b/
*/

package com.androidai.chatgpttextdetection;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class MainActivity extends AppCompatActivity {
    ImageView mImageView;
    LinearLayout btn_image;
    TextView txt_image;
    CardView cv1,cv2;
    public static int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_image = findViewById(R.id.btn_image);
        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv1.setOnClickListener(view -> {
            Intent i = new Intent(this,Act_OCR.class);
            startActivity(i);
        });
        cv2.setOnClickListener(view -> {
            Intent i = new Intent(this,Act_Text.class);
            startActivity(i);
        });

        btn_image.setOnClickListener(view -> chooseImage());
    }

    private void chooseImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        mImageView.setImageBitmap(bitmap);

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);

        task.addOnSuccessListener(firebaseVisionText -> {
            String text = firebaseVisionText.getText();
            txt_image.setText(text);
        });
        task.addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}

