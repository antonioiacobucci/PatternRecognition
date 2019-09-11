package com.exercise.pattern.model.response;

import com.exercise.pattern.model.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class GetSpaceResponse implements Serializable {
    private static final long serialVersionUID = 3006981677632344734L;

    private String status;
    private String message;
    private Set<Point> pointSet;
}