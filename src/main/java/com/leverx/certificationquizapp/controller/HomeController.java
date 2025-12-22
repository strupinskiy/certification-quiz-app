package com.leverx.certificationquizapp.controller;

import com.leverx.certificationquizapp.model.Question;
import com.leverx.certificationquizapp.model.QuizResult;
import com.leverx.certificationquizapp.strategy.QuizStrategy;
import com.leverx.certificationquizapp.strategy.impl.ExamQuizStrategy;
import com.leverx.certificationquizapp.strategy.impl.TrainQuizStrategy;
import com.leverx.certificationquizapp.util.ResultsLoader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.leverx.certificationquizapp.util.QuestionLoader.loadQuestionsFromFile;
import static com.leverx.certificationquizapp.util.QuestionLoader.loadQuestionsFromResource;

public class HomeController {

    @FXML
    private Label statusLabel;
    @FXML
    private Label resultsLabel;
    @FXML
    private CheckBox randomOrderCheckBox;
    @FXML
    private VBox quizModesVBox;
    @FXML
    private TableView<QuizResult> resultsTable;
    @FXML
    private TableColumn<QuizResult, String> nameCol;
    @FXML
    private TableColumn<QuizResult, String> modeCol;
    @FXML
    private TableColumn<QuizResult, Integer> totalCol;
    @FXML
    private TableColumn<QuizResult, Integer> errorsCol;
    @FXML
    private TableColumn<QuizResult, Double> percentCol;
    @FXML
    private TableColumn<QuizResult, String> dateCol;

    List<Question> questions;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().name()));
        modeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().mode()));
        totalCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().total()));
        errorsCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().errors()));
        percentCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().percentage()));
        dateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().date()));
        loadTableData();
    }

    private void loadTableData() {
        try {
            List<QuizResult> results = ResultsLoader.getResultList();

            resultsTable.getItems().setAll(results.stream()
                    .limit(10)
                    .toList());
        } catch (Exception e) {
            resultsTable.setVisible(false);
            resultsLabel.setText("Error while records loading");
        }
    }


    @FXML
    private void onLoadInternal() {
        try {
            questions = loadQuestionsFromResource();
            statusLabel.setText("Questions loaded from DB");
            quizModesVBox.setVisible(true);
        } catch (Exception e) {
            statusLabel.setText("Error: file not found");
        }
    }

    @FXML
    private void onSelectExternal() {
        try {
            questions = loadQuestionsFromFile(statusLabel.getScene().getWindow());
            statusLabel.setText("Questions loaded from external source");
            quizModesVBox.setVisible(true);
        } catch (Exception e) {
            statusLabel.setText("Error during file loading");
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