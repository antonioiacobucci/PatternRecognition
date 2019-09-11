package com.exercise.pattern.utils;

import com.exercise.pattern.constants.PatternRecognitionConstants;
import com.exercise.pattern.model.Fraction;
import com.exercise.pattern.model.Line;
import com.exercise.pattern.model.Point;
import com.exercise.pattern.model.request.PostPointRequest;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class PatternRecognitionUtils {

    public static void checkRequest(PostPointRequest request) {
        Assert.notNull(request.getX(), PatternRecognitionConstants.NULL_X);
        Assert.notNull(request.getY(), PatternRecognitionConstants.NULL_Y);
    }

    public static List<Pair<Point, Point>> createPairList(Set<Point> pointSet) {
        List<Point> pointList = new ArrayList<>(pointSet);
        List<Pair<Point, Point>> pairList = new ArrayList<>();
        for (int p1 = 0; p1 < pointList.size() - 1; p1++) { // size-1: last item would not be matched anyway
            for (int p2 = p1 + 1; p2 < pointList.size(); p2++)
                pairList.add(new Pair<>(pointList.get(p1), pointList.get(p2)));
        }
        log.info(String.format("%s pairs created", String.valueOf(pairList.size())));
        return pairList;
    }

    public static Line initializeLine(Pair<Point, Point> pair, String description, Fraction m, Fraction q) {
        Line line = new Line();
        line.setM(m);
        line.setQ(q);
        line.setDescription(description);
        line.setLinePoints(initializeLinePoints(pair));
        return line;
    }

    private static Set<Point> initializeLinePoints(Pair<Point, Point> pair) {
        Set<Point> linePoints = new HashSet<>();
        linePoints.add(pair.getKey());
        linePoints.add(pair.getValue());
        return linePoints;
    }

    public static boolean isValidLine(Line line, BigInteger numberRequest) {
        boolean valid = false;
        if (line.getLinePoints().size() >= numberRequest.intValue()) {
            log.info("Valid line found");
            valid = true;
        }
        return valid;
    }

    public static Fraction calculateM(Pair<Point, Point> pair) {
        Point p1 = pair.getKey();
        Point p2 = pair.getValue();
        BigInteger diffY = p2.getY().subtract(p1.getY());
        BigInteger diffX = p2.getX().subtract(p1.getX());
        return new Fraction(diffY, diffX); // m=(y2-y1)/(x2-x1). Note that x2-x1 cannot be 0(it would have been an horizontal line)
    }

    public static Fraction calculateQ(Pair<Point, Point> pair, Fraction m) {
        Point p1 = pair.getKey();
        BigInteger tempY = p1.getY().multiply(m.getDenominator());
        BigInteger tempMX = m.getNumerator().multiply(p1.getX());
        BigInteger numerator = tempY.subtract(tempMX);
        return new Fraction(numerator, m.getDenominator()); // q = y1-(m*x1)
    }
}