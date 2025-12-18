package com.mateus.ana.modules.web;

import com.mateus.ana.modules.CoreModule;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class WebSearchModule implements CoreModule {

    @Override
    public boolean canHandle(String input) {
        if (input == null || input.isEmpty()) return false;

        String text = normalize(input);

        return text.matches(".*\\b(procure|procurar|procura|buscar|busque|pesquisar|pesquise)\\b.*");
    }

    private String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    @Override
    public String execute(String input) {
        try {
            String query = input.replace("pesquisar", "").trim();
            String url = "https://google.com/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);

            Desktop.getDesktop().browse(new URI(url));

            return "Pesquisando por " + query;

        } catch (URISyntaxException e) {
            return "URL invalida";
        } catch (IOException e) {
            return "Erro ao abrir o navegador";

        }
    }
}
