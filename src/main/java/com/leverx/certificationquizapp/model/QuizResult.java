package com.leverx.certificationquizapp.model;

import java.util.Locale;

public record QuizResult(String name, String mode, int total, int errors, double percentage, String date) {
    public static QuizResult fromString(String line) {
        String[] parts = line.split("\\|");
        return new QuizResult(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                Double.parseDouble(parts[4]), parts[5]);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s|%s|%d|%d|%.1f|%s", name, mode, total, errors, percentage, date);
    }
}