package com.mateus.ana.modules.system;

import com.mateus.ana.modules.CoreModule;

import static com.mateus.ana.core.Normalize.normalize;

public class OpenAplicationModule implements CoreModule {

    @Override
    public boolean canHandle(String input) {
        if (input == null || input.isEmpty()) return false;

        String text = normalize(input);

        return text.matches(".*\\b(abrir|abra|open)\\b.*");
    }

    @Override
    public String execute(String input) {
        return "Abrindo menu";
    }

}
