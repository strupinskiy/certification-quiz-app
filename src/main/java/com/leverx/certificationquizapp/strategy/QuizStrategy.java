package com.leverx.certificationquizapp.strategy;

import com.leverx.certificationquizapp.controller.QuizController;

public interface QuizStrategy {
    boolean shouldShowIntermediaryFeedback();

    boolean hasNext(int currentIndex, int totalCount);

    String getProgressStatus(int currentIndex, int totalCount);
}