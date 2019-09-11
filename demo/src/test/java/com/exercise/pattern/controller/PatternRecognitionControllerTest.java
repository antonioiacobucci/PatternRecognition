package com.exercise.pattern.controller;

import com.exercise.pattern.model.Fraction;
import com.exercise.pattern.model.Point;
import com.exercise.pattern.model.request.PostPointRequest;
import com.exercise.pattern.model.response.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class PatternRecognitionControllerTest {

    @Mock
    private static Set<Point> pointSet;

    @InjectMocks
    private PatternRecognitionController controller;

    @Before
    public void createBarTest() {
        pointSet = new HashSet<>();
        ReflectionTestUtils.setField(controller, "pointSet", pointSet);
    }

    @Test
    public void postPointTest() {
        PostPointRequest request = new PostPointRequest();
        request.setX(BigInteger.ONE);
        request.setY(BigInteger.ZERO);
        PostPointResponse response = controller.postPoint(request);
        Assert.assertEquals(Status.OK, response.getStatus());

        PostPointResponse response2 = controller.postPoint(request);
        Assert.assertEquals(Status.KO, response2.getStatus());
        Assert.assertEquals(1, pointSet.size());
    }

    @Test
    public void getSpaceTest() {
        pointSet.add(new Point(new BigInteger("1"), new BigInteger("3")));
        pointSet.add(new Point(new BigInteger("-1"), new BigInteger("2")));
        pointSet.add(new Point(new BigInteger("4"), new BigInteger("2")));
        GetSpaceResponse space = controller.getSpace();
        Assert.assertEquals(3, space.getPointSet().size());
    }

    @Test
    public void deleteSpaceTest() {
        DeleteSpaceResponse response = controller.deleteSpace();
        Assert.assertEquals(Status.KO, response.getStatus());

        pointSet.add(new Point(new BigInteger("1"), new BigInteger("3")));
        pointSet.add(new Point(new BigInteger("-1"), new BigInteger("2")));
        DeleteSpaceResponse response2 = controller.deleteSpace();
        Assert.assertEquals(Status.OK, response2.getStatus());
    }

    @Test
    public void getLinesHorizontalTest() {
        pointSet.add(new Point(new BigInteger("1"), new BigInteger("3")));
        pointSet.add(new Point(new BigInteger("1"), new BigInteger("-2")));
        pointSet.add(new Point(new BigInteger("1"), new BigInteger("4")));
        GetLinesResponse response = controller.getLines(new BigInteger("3"));
        Assert.assertEquals(1, response.getLinesFound().intValue());
        Assert.assertEquals(3, response.getLines().get(0).getLinePoints().size());
    }

    @Test
    public void getLinesVerticalTest() {
        pointSet.add(new Point(new BigInteger("3"), new BigInteger("3")));
        pointSet.add(new Point(new BigInteger("2"), new BigInteger("3")));
        pointSet.add(new Point(new BigInteger("-1"), new BigInteger("3")));
        GetLinesResponse response = controller.getLines(new BigInteger("3"));
        Assert.assertEquals(1, response.getLinesFound().intValue());
        Assert.assertEquals(3, response.getLines().get(0).getLinePoints().size());
    }

    @Test
    public void getLinesObliqueTest() {
        pointSet.add(new Point(new BigInteger("11"), new BigInteger("8")));
        pointSet.add(new Point(new BigInteger("5"), new BigInteger("3")));
        pointSet.add(new Point(new BigInteger("-1"), new BigInteger("-2")));
        GetLinesResponse response = controller.getLines(new BigInteger("3"));
        Assert.assertEquals(1, response.getLinesFound().intValue());
        Assert.assertEquals(3, response.getLines().get(0).getLinePoints().size());
        Assert.assertEquals(new Fraction(new BigInteger("5"), new BigInteger("6")), response.getLines().get(0).getM());
        Assert.assertEquals(new Fraction(new BigInteger("-7"), new BigInteger("6")), response.getLines().get(0).getQ());
    }

    @Test
    public void getLinesTest() {
        pointSet.add(new Point(new BigInteger("-2"), new BigInteger("-2")));
        pointSet.add(new Point(new BigInteger("0"), new BigInteger("0")));
        pointSet.add(new Point(new BigInteger("2"), new BigInteger("2")));
        pointSet.add(new Point(new BigInteger("-2"), new BigInteger("2")));
        pointSet.add(new Point(new BigInteger("2"), new BigInteger("-2")));
        pointSet.add(new Point(new BigInteger("4"), new BigInteger("4")));
        GetLinesResponse response = controller.getLines(new BigInteger("4"));
        Assert.assertEquals(1, response.getLinesFound().intValue());

        GetLinesResponse response2 = controller.getLines(new BigInteger("3"));
        Assert.assertEquals(2, response2.getLinesFound().intValue());

        GetLinesResponse response3 = controller.getLines(new BigInteger("2"));
        Assert.assertEquals(8, response3.getLinesFound().intValue());
    }
}