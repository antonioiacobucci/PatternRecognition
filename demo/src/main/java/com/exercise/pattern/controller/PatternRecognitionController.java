package com.exercise.pattern.controller;

import com.exercise.pattern.constants.PatternRecognitionConstants;
import com.exercise.pattern.model.Fraction;
import com.exercise.pattern.model.Line;
import com.exercise.pattern.model.Point;
import com.exercise.pattern.model.request.PostPointRequest;
import com.exercise.pattern.model.response.*;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

import static com.exercise.pattern.utils.PatternRecognitionUtils.*;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@SpringBootApplication
public class PatternRecognitionController {

    private static Set<Point> pointSet = new HashSet<>();

    @PostMapping(path = "/point", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    public PostPointResponse postPoint(@RequestBody @Validated final PostPointRequest request) {
        checkRequest(request);
        final Point pointToInsert = new Point(request.getX(), request.getY());
        PostPointResponse response;
        if (pointSet.add(pointToInsert)) {
            response = new PostPointResponse(Status.OK.getValue(), PatternRecognitionConstants.ADD_POINT_OK);
        } else {
            response = new PostPointResponse(Status.KO.getValue(), PatternRecognitionConstants.ADD_POINT_KO);
        }
        return response;
    }


    @GetMapping(path = "/space", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    public GetSpaceResponse getSpace() {
        GetSpaceResponse response;
        if (pointSet.isEmpty()) {
            response = new GetSpaceResponse(Status.KO.getValue(), PatternRecognitionConstants.GET_SPACE_KO, Collections.emptySet());
        } else {
            String message = String.format(PatternRecognitionConstants.GET_SPACE_OK, String.valueOf(pointSet.size()));
            response = new GetSpaceResponse(Status.OK.getValue(), message, pointSet);
        }
        return response;
    }


    @DeleteMapping(path = "/space", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    public DeleteSpaceResponse deleteSpace() {
        DeleteSpaceResponse response;
        if (pointSet.isEmpty()) {
            response = new DeleteSpaceResponse(Status.KO.getValue(), PatternRecognitionConstants.CLEAR_PLANE_KO);
        } else {
            String message = String.format(PatternRecognitionConstants.CLEAR_PLANE_OK, String.valueOf(pointSet.size()));
            pointSet.clear();
            response = new DeleteSpaceResponse(Status.OK.getValue(), message);
        }
        return response;
    }


    @GetMapping(path = "/lines/{numberRequest}", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    public GetLinesResponse getLines(@PathVariable(name = "numberRequest") final BigInteger numberRequest) {
        GetLinesResponse response;
        if (numberRequest.compareTo(BigInteger.ONE) < 0) { // n<1
            response = new GetLinesResponse(Status.KO.getValue(), PatternRecognitionConstants.GET_LINES_LOW, null, Collections.emptyList());
        } else if (numberRequest.equals(BigInteger.ONE)) { // n=1
            response = new GetLinesResponse(Status.KO.getValue(), PatternRecognitionConstants.GET_LINES_1, null, Collections.emptyList());
        } else if (numberRequest.compareTo(BigInteger.valueOf(pointSet.size())) > 0) { // n>p
            String message = String.format(PatternRecognitionConstants.GET_LINES_OK, String.valueOf(BigInteger.ZERO), String.valueOf(numberRequest));
            response = new GetLinesResponse(Status.OK.getValue(), message, BigInteger.ZERO, Collections.emptyList());
        } else { // 1<n<=p
            List<Pair<Point, Point>> pairList = createPairList(pointSet);
            response = retrieveLines(new ArrayList<>(pointSet), pairList, numberRequest);
        }
        return response;
    }


    private GetLinesResponse retrieveLines(List<Point> points, List<Pair<Point, Point>> pairList, BigInteger numberRequest) {
        GetLinesResponse response = new GetLinesResponse();
        List<Line> lines = calculateLines(points, pairList, numberRequest);
        response.setLines(lines);
        response.setMessage(String.format(PatternRecognitionConstants.GET_LINES_OK, String.valueOf(lines.size()), String.valueOf(numberRequest)));
        response.setLinesFound(BigInteger.valueOf(lines.size()));
        response.setStatus(Status.OK.getValue());
        return response;
    }

    private List<Line> calculateLines(List<Point> points, List<Pair<Point, Point>> pairList, BigInteger numberRequest) {
        List<Line> linesResponse = new ArrayList<>();
        BigInteger id = BigInteger.ONE;
        for (Pair<Point, Point> pair : pairList) {
            List<Point> pointsLeft = createPointsLeftList(points, pair);
            Line line = createLine(pair, pointsLeft, numberRequest);
            if (!isNull(line) && !linesResponse.contains(line)) {
                line.setId(id);
                line.setDescription(String.format(PatternRecognitionConstants.INDEX_N, id, line.getDescription()));
                linesResponse.add(line);
                id = id.add(BigInteger.ONE);
            }
        }
        return linesResponse;
    }

    private List<Point> createPointsLeftList(List<Point> points, Pair<Point, Point> pair) {
        List<Point> tempPoints = new ArrayList<>(points);
        tempPoints.remove(pair.getKey());
        tempPoints.remove(pair.getValue());
        return tempPoints;
    }

    private Line createLine(Pair<Point, Point> pair, List<Point> pointsLeft, BigInteger numberRequest) {
        Line line;
        if (pair.getKey().getX().equals(pair.getValue().getX())) { // horizontal line: p1.x = p2.x
            log.debug("Checking horizontal line");
            line = checkHorizontalLine(pointsLeft, pair, numberRequest);
        } else if (pair.getKey().getY().equals(pair.getValue().getY())) { //vertical line: p1.y = p2.y
            log.debug("Checking vertical line");
            line = checkVerticalLine(pointsLeft, pair, numberRequest);
        } else { //oblique line
            log.debug("Checking oblique line");
            line = checkObliqueLine(pointsLeft, pair, numberRequest);
        }
        return line;
    }

    private Line checkHorizontalLine(List<Point> pointsLeft, Pair<Point, Point> pair, BigInteger numberRequest) {
        BigInteger x = pair.getKey().getX();
        String description = String.format(PatternRecognitionConstants.EQUATION_HORIZONTAL, x);
        Line line = initializeLine(pair, description, null, null);
        for (Point point : pointsLeft) {
            Set<Point> linePoints = line.getLinePoints();
            if (x.equals(point.getX())) { // pointX = x && y(min) <= pointY <= y(max)
                linePoints.add(point);
            }
            line.setLinePoints(linePoints);
        }
        return (isValidLine(line, numberRequest)) ? line : null;
    }

    private Line checkVerticalLine(List<Point> pointsLeft, Pair<Point, Point> pair, BigInteger numberRequest) {
        BigInteger y = pair.getKey().getY();
        String description = String.format(PatternRecognitionConstants.EQUATION_VERTICAL, y);
        Line line = initializeLine(pair, description, null, null);
        for (Point point : pointsLeft) {
            Set<Point> linePoints = line.getLinePoints();
            if (y.equals(point.getY())) { // pointY = y && x(min) <= pointX <= x(max)
                linePoints.add(point);
            }
            line.setLinePoints(linePoints);
        }
        return (isValidLine(line, numberRequest)) ? line : null;
    }

    private Line checkObliqueLine(List<Point> pointsLeft, Pair<Point, Point> pair, BigInteger numberRequest) {
        Fraction m = calculateM(pair);
        log.debug(String.format("m = %s", m.toString()));
        Fraction q = calculateQ(pair, m);
        log.debug(String.format("q = %s", q.toString()));
        String description = String.format(PatternRecognitionConstants.EQUATION, m.toString(), extractSign(q), q.abs().toString());
        Line line = initializeLine(pair, description, m, q);
        for (Point point : pointsLeft) {
            Set<Point> linePoints = line.getLinePoints();
            if (pointInLine(point, m, q)) { //check if y=mx+q
                linePoints.add(point);
            }
            line.setLinePoints(linePoints);
        }
        return (isValidLine(line, numberRequest)) ? line : null;
    }

    private String extractSign(Fraction q) {
        String sign = PatternRecognitionConstants.PLUS_SIGN;
        if (q.getNumerator().compareTo(BigInteger.ZERO) < 0) {
            sign = PatternRecognitionConstants.MINUS_SIGN;
        }
        return sign;
    }

    private boolean pointInLine(Point point, Fraction m, Fraction q) {
        Fraction y = new Fraction(point.getY(), BigInteger.ONE);
        Fraction tempMX = new Fraction(m.getNumerator().multiply(point.getX()), m.getDenominator());
        BigInteger resultNum = (tempMX.getNumerator().multiply(q.getDenominator())).add(q.getNumerator().multiply(tempMX.getDenominator())); //n1*d2 + n2*d1
        BigInteger resultDen = tempMX.getDenominator().multiply(q.getDenominator()); //d1*d2
        Fraction result = new Fraction(resultNum, resultDen);
        return y.equals(result);
    }
}