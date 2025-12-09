package com.cheffai.suggestor;

import com.cheffai.llm.LLMClient;

public class SuggestorFactory {
    public static SuggestorBase create(String type, LLMClient client) {
        return switch (type.toLowerCase()) {
            case "saudavel", "healthy" -> new SuggestorSaudavel(client);
            case "rapido", "quick" -> new SuggestorRapido(client);
            default -> new SuggestorRapido(client);
        };
    }
}