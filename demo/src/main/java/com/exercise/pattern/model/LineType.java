package com.exercise.pattern.model;

public enum LineType {
    HORIZONTAL("H"),
    VERTICAL("V"),
    OBLIQUE("O");

    private final String value;

    LineType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}