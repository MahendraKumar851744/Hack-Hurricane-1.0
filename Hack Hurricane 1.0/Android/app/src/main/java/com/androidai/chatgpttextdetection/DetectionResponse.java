package com.androidai.chatgpttextdetection;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetectionResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private Data data;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static class Data {
        @SerializedName("is_human_written")
        private double isHumanWritten;

        @SerializedName("is_gpt_generated")
        private double isGptGenerated;

        @SerializedName("feedback_message")
        private String feedbackMessage;

        @SerializedName("gpt_generated_sentences")
        private List<String> gptGeneratedSentences;

        @SerializedName("words_count")
        private int wordsCount;

        public double getIsHumanWritten() {
            return isHumanWritten;
        }

        public double getIsGptGenerated() {
            return isGptGenerated;
        }

        public String getFeedbackMessage() {
            return feedbackMessage;
        }

        public List<String> getGptGeneratedSentences() {
            return gptGeneratedSentences;
        }

        public int getWordsCount() {
            return wordsCount;
        }
    }
}
