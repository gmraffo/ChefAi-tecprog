package com.cheffai;

import com.cheffai.cli.CLI;
import com.cheffai.llm.LLMClient;

/**
 * Entrypoint.
 */
public class Main {
    public static void main(String[] args) {
        try {
            LLMClient llmClient = new LLMClient();
            CLI cli = new CLI(llmClient);
            cli.run();
        } catch (Exception e) {
            System.err.println("Fatal error initializing ChefAI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}