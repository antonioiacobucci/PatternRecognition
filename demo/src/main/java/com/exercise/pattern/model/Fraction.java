package com.exercise.pattern.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Fraction {

    public static final String DELIMITER = "/";

    private BigInteger numerator;
    private BigInteger denominator;

    public Fraction(BigInteger numr, BigInteger denr) {
        if (BigInteger.ZERO.equals(numr)) {
            numerator = numr;
            denominator = BigInteger.ONE;
        } else if (denr.compareTo(BigInteger.ZERO) < 0) {
            numerator = numr.negate();
            denominator = denr.negate();
        } else {
            numerator = numr;
            denominator = denr;
        }
        reduce();
    }

    private void reduce() {
        boolean negated = false;
        if (numerator.compareTo(BigInteger.ZERO) < 0) {
            numerator = numerator.negate();
            negated = true;
        }
        BigInteger gcd = calculateGCD(numerator, denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
        if (negated) {
            numerator = numerator.negate();
        }
    }

    private BigInteger calculateGCD(BigInteger numerator, BigInteger denominator) { //greatest common divisor
        if (BigInteger.ZERO.equals(numerator.mod(denominator))) {
            return denominator;
        }
        return calculateGCD(denominator, numerator.mod(denominator));
    }

    public Fraction add(Fraction toAdd) {
        BigInteger resultNum = (this.getNumerator().multiply(toAdd.getDenominator())).add(toAdd.getNumerator().multiply(this.getDenominator())); //n1*d2 + n2*d1
        BigInteger resultDen = this.getDenominator().multiply(toAdd.getDenominator()); //d1*d2
        return new Fraction(resultNum, resultDen);
    }

    public Fraction add(BigInteger toAdd) {
        return add(new Fraction(toAdd, BigInteger.ONE));
    }

    public Fraction multiply(Fraction toMultiply) {
        return new Fraction(this.numerator.multiply(toMultiply.getNumerator()), this.denominator.multiply(toMultiply.getDenominator()));
    }

    public Fraction multiply(BigInteger toMultiply) {
        return multiply(new Fraction(toMultiply, BigInteger.ONE));
    }

    @Override
    public String toString() {
        String fraction = String.valueOf(this.numerator);
        if (!BigInteger.ONE.equals(this.denominator)) {
            fraction = String.join(DELIMITER, fraction, String.valueOf(this.denominator));
        }
        return fraction;
    }

    @Override
    public boolean equals(Object fraction) {
        boolean isEqual = false;
        if (fraction instanceof Fraction) {
            Fraction f = (Fraction) fraction;
            isEqual = this.numerator.equals(f.getNumerator()) && this.denominator.equals(f.getDenominator());
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public Fraction abs() {
        BigInteger abs = this.numerator.abs();
        return new Fraction(abs, this.denominator);
    }

    public Fraction negate() {
        return new Fraction(this.numerator.negate(), this.denominator);
    }
}