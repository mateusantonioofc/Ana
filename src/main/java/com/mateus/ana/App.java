package com.mateus.ana;

import com.mateus.ana.core.Core;
import com.mateus.ana.listening.SpeechListener;
import com.mateus.ana.listening.SpeechModule;

public class App {

    public static void main(String[] args) {

        Core modules = new Core();
        SpeechModule speech = new SpeechModule();

        speech.setListener(new SpeechListener() {

            @Override
            public void onFinalResult(String text) {
                System.out.println(text);

                modules.handle(text);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        speech.start();
    }
}
