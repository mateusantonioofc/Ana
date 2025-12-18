package com.mateus.ana.listening;

public interface SpeechListener {
    void onFinalResult(String text);
    void onError(Exception e);
}