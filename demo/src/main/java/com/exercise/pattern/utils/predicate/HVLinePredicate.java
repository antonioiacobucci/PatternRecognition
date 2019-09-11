package com.exercise.pattern.utils.predicate;

import com.exercise.pattern.model.Line;
import com.exercise.pattern.model.LineType;
import com.exercise.pattern.model.Point;
import org.springframework.cglib.core.Predicate;

import java.math.BigInteger;
import java.util.ArrayList;

import static java.util.Objects.isNull;

public class HVLinePredicate implements Predicate {

    private BigInteger x;
    private BigInteger y;

    public HVLinePredicate(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean evaluate(Object o) {
        Line l = (Line) o;
        boolean found = false;
        Point pointInLine = new ArrayList<>(l.getLinePoints()).get(0);
        if (isNull(y) && l.getType().equals(LineType.HORIZONTAL) && x.equals(pointInLine.getX())) { //horizontal
            found = true;
        } else if (isNull(x) && l.getType().equals(LineType.VERTICAL) && y.equals(pointInLine.getY())) { // vertical
            found = true;
        }
        return found;
    }
}