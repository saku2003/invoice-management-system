package org.example;

import org.example.cli.CliApp;

/**
 * Main entry point for the Invoice Management System CLI Application
 */
public class App {
   public static void main(String[] args) {
        CliApp cliApp = new CliApp();
        cliApp.run();
    }
}

