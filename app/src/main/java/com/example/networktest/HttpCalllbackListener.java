package com.example.networktest;

public interface HttpCalllbackListener {
    void onFinsh(String reponse);
    void onError(Exception e);
}
