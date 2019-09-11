package com.exercise.pattern.utils;

import com.exercise.pattern.constants.PatternRecognitionConstants;
import com.exercise.pattern.model.Fraction;
import com.exercise.pattern.model.Line;
import com.exercise.pattern.model.Point;
import com.exercise.pattern.model.request.PostPointRequest;
import com.exercise.pattern.utils.predicate.HVLinePredicate;
import com.exercise.pattern.utils.predicate.ObliqueLinePredicate;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.*;

import static java.util.Objects.isNull;

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
        if (!isNull(line) && line.getLinePoints().size() >= numberRequest.intValue()) {
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
        return m.multiply(p1.getX()).negate().add(p1.getY()); // q = y1-(m*x1)
    }

    public static Line checkPresent(List<Line> tempLines, Line newLine, BigInteger x, BigInteger y) {
        Collection presentList = CollectionUtils.filter(tempLines, new HVLinePredicate(x, y));
        return presentList.isEmpty() ? newLine : null;
    }

    public static Line checkPresentOblique(List<Line> tempLines, Line newLine, Fraction m, Fraction q) {
        Collection presentList = CollectionUtils.filter(tempLines, new ObliqueLinePredicate(m, q));
        return presentList.isEmpty() ? newLine : null;
    }
}