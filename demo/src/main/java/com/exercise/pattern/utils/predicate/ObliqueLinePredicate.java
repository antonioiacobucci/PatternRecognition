package com.exercise.pattern.utils.predicate;

import com.exercise.pattern.model.Fraction;
import com.exercise.pattern.model.Line;
import org.springframework.cglib.core.Predicate;

public class ObliqueLinePredicate implements Predicate {

    private Fraction m;
    private Fraction q;

    public ObliqueLinePredicate(Fraction m, Fraction q) {
        this.m = m;
        this.q = q;
    }

    @Override
    public boolean evaluate(Object o) {
        Line l = (Line) o;
        return m.equals(l.getM()) && q.equals(l.getQ());
    }
}