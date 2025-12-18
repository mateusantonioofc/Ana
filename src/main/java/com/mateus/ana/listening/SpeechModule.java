package com.mateus.ana.listening;

import com.mateus.ana.config.Config;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.text.Normalizer;

public class SpeechModule {

    private SpeechListener listener;

    private Model model;
    private Recognizer recognizer;
    private TargetDataLine microphone;
    private boolean running = false;

    private long listenUntil = 0;

    public void setListener(SpeechListener listener) {
        this.listener = listener;
    }

    public void start() {
        try {
            model = new Model(Config.MODEL_PATH);
            recognizer = new Recognizer(model, Config.SAMPLE_RATE);

            AudioFormat format = new AudioFormat(
                    Config.SAMPLE_RATE,
                    16,
                    1,
                    true,
                    false
            );

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            System.out.println("[Speech] Escutando...");

            running = true;
            new Thread(this::listenLoop, "Speech-Recognition-Thread").start();

        } catch (Exception e) {
            if (listener != null) listener.onError(e);
        }
    }

    private void listenLoop() {
        byte[] buffer = new byte[4096];

        try {
            while (running) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);

                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    String text = extractText(recognizer.getResult());
                    processText(text);
                }
            }
        } catch (Exception e) {
            if (listener != null) listener.onError(e);
        }
    }

    private void processText(String text) {
        if (text == null || text.isEmpty()) return;

        String normalized = normalize(text);
        long now = System.currentTimeMillis();

        if (!Config.WAKE_WORD_ENABLED) {
            if (listener != null) listener.onFinalResult(normalized);
            return;
        }

        if (now < listenUntil) {
            listenUntil = now + Config.LISTEN_WINDOW_MS;
            if (listener != null) listener.onFinalResult(normalized);
            return;
        }

        for (String wakeWord : Config.WAKE_WORDS) {
            String wake = normalize(wakeWord);

            if (normalized.matches(".*\\b" + wake + "\\b.*")) {
                listenUntil = now + Config.LISTEN_WINDOW_MS;

                String command = normalized
                        .replaceAll("\\b" + wake + "\\b", "")
                        .replaceAll("\\s+", " ")
                        .trim();

                if (!command.isEmpty() && listener != null) {
                    listener.onFinalResult(command);
                }
                return;
            }
        }
    }

    public void stop() {
        running = false;

        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
        if (recognizer != null) recognizer.close();
        if (model != null) model.close();
    }

    private String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String extractText(String json) {
        return json.replaceAll(".*\"text\"\\s*:\\s*\"([^\"]*)\".*", "$1");
    }
}
