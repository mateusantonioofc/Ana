package com.mateus.ana.config;

public class Config {

    public static final String MODEL_PATH = "models/pt/";
    public static final float SAMPLE_RATE = 16000f;
    public static final String[] WAKE_WORDS = {
            "assistente",
            "ana",
            "hey ana",
            "teste"
    };

    public static final boolean WAKE_WORD_ENABLED = true;

    public static final long LISTEN_WINDOW_MS = 5000;
    public static final long COMMAND_TIMEOUT_MS = 1200;

}
