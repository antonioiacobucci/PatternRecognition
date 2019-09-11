package com.exercise.pattern.model.response;

import com.exercise.pattern.model.Line;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetLinesResponse implements Serializable {
    private static final long serialVersionUID = -5777534248933141656L;

    private String status;
    private String message;
    private BigInteger linesFound;
    private List<Line> lines;
}