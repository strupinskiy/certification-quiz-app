package com.leverx.certificationquizapp.model;

import java.util.Locale;

public record QuizResult(String name, int errors, double percentage, String date) {
    public static QuizResult fromString(String line) {
        String[] parts = line.split("\\|");
        return new QuizResult(parts[0], Integer.parseInt(parts[1]),
                Double.parseDouble(parts[2]), parts[3]);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s|%d|%.1f|%s", name, errors, percentage, date);
    }
}