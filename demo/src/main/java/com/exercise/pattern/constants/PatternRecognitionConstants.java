package com.exercise.pattern.constants;

import org.apache.logging.log4j.util.Strings;

public class PatternRecognitionConstants {
    //post point
    public static final String NULL_X = "x must not be null.";
    public static final String NULL_Y = "y must not be null.";
    public static final String ADD_POINT_OK = "Point has been correctly inserted in the plane.";
    public static final String ADD_POINT_KO = "This point is already present in the plane.";

    //get space
    public static final String GET_SPACE_OK = "There is/are %s points in the plane.";
    public static final String GET_SPACE_KO = "There are no points in the plane.";

    //get lines
    public static final String GET_LINES_LOW = "N must be greater than 1.";
    public static final String GET_LINES_1 = String.join(Strings.EMPTY, GET_LINES_LOW, " Mathematically infinite lines pass through one point if at least one exists in the plane.");
    public static final String GET_LINES_OK = "There is/are %s line segments which pass through at least %s points in the plane.";

    //delete space
    public static final String CLEAR_PLANE_OK = "Plane has been successfully cleared. %s points have been removed.";
    public static final String CLEAR_PLANE_KO = "Plane is already empty.";

    //other
    public static final String INDEX_N = "Line segment n. %s:  %s";
    public static final String EQUATION = "y = %sx %s %s";
    public static final String EQUATION_HORIZONTAL = "x = %s";
    public static final String EQUATION_VERTICAL = "y = %s";
    public static final String PLUS_SIGN = "+";
    public static final String MINUS_SIGN = "-";
}