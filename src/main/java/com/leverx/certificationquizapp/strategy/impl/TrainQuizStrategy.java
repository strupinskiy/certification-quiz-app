package com.leverx.certificationquizapp.strategy.impl;

import com.leverx.certificationquizapp.strategy.QuizStrategy;

public class TrainQuizStrategy implements QuizStrategy {

    @Override
    public boolean shouldShowIntermediaryFeedback() {
        return true;
    }

    @Override
    public boolean hasNext(int currentIndex, int totalCount) {
        return currentIndex < totalCount - 1;
    }

    @Override
    public String getProgressStatus(int currentIndex, int totalCount) {
        return "Train mode: questions " + (currentIndex + 1) + " from " + totalCount;
    }
}
