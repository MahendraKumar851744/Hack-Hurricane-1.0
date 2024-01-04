/*
Developed as a part of hackathon solely - 'Hack Hurricane 1.0'
Author : Mahendra Kumar
Gmail: androiprogrammers@gmail.com
Linkedin: https://www.linkedin.com/in/mahendra-kumar-b5881324b/
*/

package com.androidai.chatgpttextdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.androidai.chatgpttextdetection.databinding.ActivityActResultsBinding;

public class Act_Results extends AppCompatActivity {

    ActivityActResultsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sp = getSharedPreferences("BASE_APP",MODE_PRIVATE);
        int isHumanWritten = (int)Double.parseDouble(sp.getString("isHumanWritten","0"));
        int isGptGenerated = (int)Double.parseDouble(sp.getString("isGptGenerated","0"));
        String wordsCount = sp.getString("wordsCount","0");
        binding.wordcount.setText(wordsCount);
        binding.human.setText(isHumanWritten);
        binding.gpt.setText(isGptGenerated);
        binding.progressBar1.setProgress(isHumanWritten, true);
        binding.progressBar2.setProgress(isGptGenerated, true);
        binding.progressBar1.setMax(100);
        binding.progressBar2.setMax(100);
        if(isGptGenerated>isHumanWritten){
            binding.feedback.setText("Feedback: The Content is AI Generated");
        }else{
            binding.feedback.setText("Feedback: The Content is Human Written");
        }
    }
}
/*
{
        "success": true,
        "data": {
        "is_human_written": 9.3,
        "is_gpt_generated": 90.7,
        "feedback_message": "",
        "gpt_generated_sentences": [
        "C++ is a high-level, general-purpose programming language that was developed by Bjarne Stroustrup in 1983 as an extension of the C programming language.",
        "It is an object-oriented language that allows developers to write efficient and portable code that can run on a wide range of platforms, from embedded systems to supercomputers.",
        "In this article, we will discuss some of the key features and benefits of C++, as well as its various applications and use cases."
        ],
        "words_count": 75
        },
        "code": 200,
        "message": "Detection complete"
        }
 */