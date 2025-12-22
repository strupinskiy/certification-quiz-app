package com.leverx.certificationquizapp.strategy;

public interface QuizStrategy {
    boolean shouldShowIntermediaryFeedback();

    boolean hasNext(int currentIndex, int totalCount);

    String getProgressStatus(int currentIndex, int totalCount);

    String getModeName();
}