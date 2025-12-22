package com.leverx.certificationquizapp.strategy.impl;

import com.leverx.certificationquizapp.strategy.QuizStrategy;

public class ExamQuizStrategy implements QuizStrategy {

    @Override
    public boolean shouldShowIntermediaryFeedback() {
        return false;
    }

    @Override
    public boolean hasNext(int currentIndex, int totalCount) {
        return currentIndex < totalCount - 1;
    }

    @Override
    public String getProgressStatus(int currentIndex, int totalCount) {
        return "Exam: questions " + (currentIndex + 1) + " from " + totalCount;
    }

    @Override
    public String getModeName() {
        return "Exam";
    }
}
