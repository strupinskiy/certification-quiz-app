package com.leverx.certificationquizapp.controller;

import com.leverx.certificationquizapp.model.Question;
import com.leverx.certificationquizapp.strategy.QuizStrategy;
import com.leverx.certificationquizapp.strategy.impl.ExamQuizStrategy;
import com.leverx.certificationquizapp.strategy.impl.TrainQuizStrategy;
import com.leverx.certificationquizapp.util.QuestionLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeController {

    @FXML
    private Label statusLabel;

    @FXML
    private CheckBox randomOrderCheckBox;

    @FXML
    private VBox quizModesVBox;

    private final QuestionLoader loader = new QuestionLoader();

    final String FILE_PATH = "/com/leverx/certificationquizapp/data/quiz.txt";

    List<Question> questions;

    @FXML
    private void onLoadInternal() {
        try {
            questions = loader.loadQuestionsFromResource(FILE_PATH);
            statusLabel.setText("Questions loaded from DB");
            quizModesVBox.setVisible(true);
        } catch (Exception e) {
            statusLabel.setText("Error: file not found");
        }
    }

    @FXML
    private void onSelectExternal() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(statusLabel.getScene().getWindow());

        if (selectedFile != null) {
            try {
                questions = loader.loadQuestionsFromFile(selectedFile);
                statusLabel.setText("Questions loaded from external source");
                quizModesVBox.setVisible(true);
            } catch (Exception e) {
                statusLabel.setText("Error during file loading");
            }
        }
    }

    private void startQuiz(QuizStrategy strategy) {
        if (questions == null || questions.isEmpty()) return;

        List<Question> questionsToUse = new ArrayList<>(questions);

        if (randomOrderCheckBox.isSelected()) {
            Collections.shuffle(questionsToUse);
        }

        try {
            switchToQuizScene(questionsToUse, strategy);
        } catch (Exception e) {
            statusLabel.setText("Error while quiz start");
        }
    }

    @FXML
    private void onSelectExamMode() {
        startQuiz(new ExamQuizStrategy());
    }

    @FXML
    private void onSelectTrainMode() {
        startQuiz(new TrainQuizStrategy());
    }

    private void switchToQuizScene(List<Question> questions, QuizStrategy strategy) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        QuizController quizController = fxmlLoader.getController();
        quizController.initData(questions, strategy);

        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.setScene(scene);
    }
}