package com.leverx.certificationquizapp.logic;

import com.leverx.certificationquizapp.model.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizEngine {
    private final List<Question> questions;
    private final List<Set<Integer>> userAnswers;
    private int currentIndex = 0;

    public QuizEngine(List<Question> questions) {
        this.questions = questions;
        this.userAnswers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add(new HashSet<>());
        }
    }

    public Question getCurrentQuestion() {
        return questions.get(currentIndex);
    }

    public void saveAnswer(Set<Integer> answers) {
        userAnswers.set(currentIndex, answers);
    }

    public Set<Integer> getCurrentSavedAnswers() {
        return userAnswers.get(currentIndex);
    }

    public boolean next() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public boolean prev() {
        if (currentIndex > 0) {
            currentIndex--;
            return true;
        }
        return false;
    }

    public int getProgress() { return currentIndex + 1; }
    public int getTotal() { return questions.size(); }
    
    public int calculateScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).correctAnswers().equals(userAnswers.get(i))) {
                score++;
            }
        }
        return score;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}