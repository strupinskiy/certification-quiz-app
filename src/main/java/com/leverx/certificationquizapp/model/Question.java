package com.leverx.certificationquizapp.model;

import java.util.List;
import java.util.Set;

public record Question(String text, List<String> options, Set<Integer> correctAnswers) {
    public boolean isMultipleChoice() {
        return correctAnswers.size() > 1;
    }
}