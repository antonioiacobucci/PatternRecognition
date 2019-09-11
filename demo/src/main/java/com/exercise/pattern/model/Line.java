package com.exercise.pattern.model;

import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

@Data
public class Line {
    private BigInteger id;
    private String description;
    private LineType type;
    private Fraction m;
    private Fraction q;
    private Set<Point> linePoints;

    @Override
    public boolean equals(Object line) {
        boolean isEqual = false;
        if (line instanceof Line) {
            Line l = (Line) line;
            isEqual = this.linePoints.equals(l.getLinePoints());
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
