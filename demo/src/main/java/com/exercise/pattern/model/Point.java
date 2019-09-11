package com.exercise.pattern.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class Point {
    private BigInteger x;
    private BigInteger y;

    @Override
    public boolean equals(Object point) {
        boolean isEqual = false;
        if (point instanceof Point) {
            Point p = (Point) point;
            isEqual = Objects.equals(x, p.x) && Objects.equals(y, p.y);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
