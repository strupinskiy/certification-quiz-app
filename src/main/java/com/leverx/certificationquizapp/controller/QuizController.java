package com.leverx.certificationquizapp.controller;

import com.leverx.certificationquizapp.logic.QuizEngine;
import com.leverx.certificationquizapp.model.Question;
import com.leverx.certificationquizapp.model.QuizResult;
import com.leverx.certificationquizapp.strategy.QuizStrategy;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.leverx.certificationquizapp.util.ResultsLoader.loadLastNameFromResults;
import static com.leverx.certificationquizapp.util.ResultsLoader.saveResultToFile;

public class QuizController {

    @FXML
    private Label questionLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox optionsContainer;
    @FXML
    private Label statusLabel;
    @FXML
    private Button prevBtn;
    @FXML
    private Button nextBtn;

    private QuizEngine engine;

    private QuizStrategy strategy;

    private Boolean isShowingErrors;

    private Boolean isGameOver;

    void initData(List<Question> questions, QuizStrategy strategy) {
        this.strategy = strategy;
        this.engine = new QuizEngine(questions);
        isGameOver = false;
        isShowingErrors = false;
        render();
    }

    @FXML
    private void onNextClick() {
        saveAnswers();

        if (strategy.shouldShowIntermediaryFeedback() && !isShowingErrors) {
            highlightErrors();
            isShowingErrors = true;
            nextBtn.setText("Ok, got it.");
            return;
        }

        isShowingErrors = false;
        if (strategy.hasNext(engine.getCurrentIndex(), engine.getTotal())) {
            engine.next();
            render();
        } else {
            showResults();
        }

        if (isGameOver)
            highlightErrors();
    }

    @FXML
    private void onPrevClick() {
        saveAnswers();
        engine.prev();
        render();
        if (isGameOver) {
            highlightErrors();
        }
    }

    private void render() {
        Question q = engine.getCurrentQuestion();
        questionLabel.setText(q.text());

        if (scrollPane != null) {
            scrollPane.setVvalue(0.0);
        }

        FadeTransition ft = new FadeTransition(Duration.millis(300), scrollPane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        statusLabel.setText(
                strategy.getProgressStatus(engine.getCurrentIndex(), engine.getTotal())
        );

        optionsContainer.getChildren().clear();
        Set<Integer> saved = engine.getCurrentSavedAnswers();

        if (q.isMultipleChoice()) {
            for (int i = 0; i < q.options().size(); i++) {
                CheckBox cb = new CheckBox(q.options().get(i));
                if (saved.contains(i)) cb.setSelected(true);
                optionsContainer.getChildren().add(cb);
            }
        } else {
            ToggleGroup group = new ToggleGroup();
            for (int i = 0; i < q.options().size(); i++) {
                RadioButton rb = new RadioButton(q.options().get(i));
                rb.setToggleGroup(group);
                if (saved.contains(i)) rb.setSelected(true);
                optionsContainer.getChildren().add(rb);
            }
        }

        prevBtn.setDisable(engine.getProgress() == 1);
        nextBtn.setText(engine.getProgress() == engine.getTotal() ? "Complete" : "Next");
    }

    private void saveAnswers() {
        Set<Integer> selected = new HashSet<>();
        for (int i = 0; i < optionsContainer.getChildren().size(); i++) {
            var node = optionsContainer.getChildren().get(i);
            if (node instanceof CheckBox cb && cb.isSelected()) selected.add(i);
            if (node instanceof RadioButton rb && rb.isSelected()) selected.add(i);
        }
        engine.saveAnswer(selected);
    }

    private void showResults() {
        isGameOver = true;
        int total = engine.getTotal();
        int score = engine.calculateScore();
        int errors = total - score;
        double percentage = (double) score / total * 100;

        TextInputDialog dialog = new TextInputDialog(loadLastNameFromResults());
        dialog.setTitle("Results!");
        dialog.setHeaderText(String.format("Your result: %d/%d (%.1f%%)", score, total, percentage));
        dialog.setContentText("Enter your name:");

        dialog.showAndWait().ifPresent(name -> {
            saveResult(new QuizResult(name, errors, percentage, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            onHomeClick();
        });
    }

    @FXML
    private void onHomeClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Want to exit?");
        alert.setContentText("Current progress will be lost.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) statusLabel.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    showError("Error: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
        Platform.exit();
        System.exit(0);
    }

    private void highlightErrors() {
        Question q = engine.getCurrentQuestion();
        Set<Integer> correctAnswers = q.correctAnswers();

        for (int i = 0; i < optionsContainer.getChildren().size(); i++) {
            var node = optionsContainer.getChildren().get(i);

            if (correctAnswers.contains(i)) {
                node.getStyleClass().add("answer-right");
            }
        }
    }

    private void saveResult(QuizResult result) {
        try {
            saveResultToFile(result);
        } catch (IOException e) {
            showError("Error while results saving");
        }
    }
}