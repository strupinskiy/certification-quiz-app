package com.leverx.certificationquizapp.util;

import com.leverx.certificationquizapp.model.Question;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class QuestionLoader {

    private static final String QUESTION_PREFIX = "Q:";
    private static final String ANSWER_PREFIX = "A:";
    private static final String OPTION_PREFIX = "-";

    final String QUESTIONS_FILE_PATH = "/com/leverx/certificationquizapp/data/quiz.txt";

    public List<Question> loadQuestionsFromFile(Window window) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile == null) {
            throw new IOException();
        }
        List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
        return parseLines(lines);
    }

    public List<Question> loadQuestionsFromResource() throws Exception {
        try (InputStream is = getClass().getResourceAsStream(QUESTIONS_FILE_PATH)) {
            if (is == null) throw new RuntimeException("File not found: " + QUESTIONS_FILE_PATH);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return parseLines(reader.lines().collect(Collectors.toList()));
            }
        }
    }

    public List<Question> parseLines(List<String> lines) {
        List<Question> questions = new ArrayList<>();
        QuestionBuilder builder = new QuestionBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                flushBuilder(questions, builder);
            } else if (line.startsWith(QUESTION_PREFIX)) {
                flushBuilder(questions, builder);
                builder.text = parseQuestionText(line);
            } else if (line.startsWith(ANSWER_PREFIX)) {
                builder.correctAnswers.addAll(parseCorrectAnswers(line));
            } else if (line.startsWith(OPTION_PREFIX)) {
                builder.options.add(line.substring(1).trim());
            }
        }
        flushBuilder(questions, builder);
        return questions;
    }

    private static void flushBuilder(List<Question> questions, QuestionBuilder builder) {
        if (!builder.isEmpty()) {
            questions.add(builder.build());
            builder.text = null;
            builder.options.clear();
            builder.correctAnswers.clear();
        }
    }

    private static String parseQuestionText(String line) {
        String text = line.substring(QUESTION_PREFIX.length()).trim();
        return text.replaceFirst("^\\s*\\d+\\.\\s*", ""); // Удаляем "1. ", "2. "
    }

    private static Set<Integer> parseCorrectAnswers(String line) {
        String answersStr = line.substring(ANSWER_PREFIX.length()).trim();
        return Arrays.stream(answersStr.split(","))
                .map(String::trim)
                .filter(s -> s.matches("\\d+")) // Проверяем, что это число
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private static class QuestionBuilder {
        private String text;
        private final List<String> options = new ArrayList<>();
        private final Set<Integer> correctAnswers = new HashSet<>();

        public boolean isEmpty() {
            return text == null || text.isBlank();
        }

        public Question build() {
            return new Question(
                    text,
                    new ArrayList<>(options),
                    new HashSet<>(correctAnswers)
            );
        }
    }
}