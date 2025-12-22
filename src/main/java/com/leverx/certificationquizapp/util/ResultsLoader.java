package com.leverx.certificationquizapp.util;

import com.leverx.certificationquizapp.model.QuizResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class ResultsLoader {

    static final String RESULTS_FILE_PATH = "src/main/resources/com/leverx/certificationquizapp/data/results.txt";

    public static List<QuizResult> getResultList() throws IOException {
        File file = new File(RESULTS_FILE_PATH);
        if (!file.exists()) throw new NoSuchFileException(RESULTS_FILE_PATH, "No such file", "");

        return Files.readAllLines(file.toPath())
                .stream()
                .map(QuizResult::fromString)
                .collect(Collectors.toList())
                .reversed();
    }

    public static Path saveResultToFile(QuizResult result) throws IOException {
        return Files.writeString(Paths.get(RESULTS_FILE_PATH), result.toString() + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static String loadLastNameFromResults() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(RESULTS_FILE_PATH));
            if (!lines.isEmpty()) {
                return lines.getLast().split("\\|")[0];
            }
        } catch (IOException ignored) {}
        return "Player";
    }
}
