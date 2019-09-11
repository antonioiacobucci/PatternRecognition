package com.exercise.pattern.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
public class DeleteSpaceResponse implements Serializable {
    private static final long serialVersionUID = 3536203799551377825L;

    private String status;
    private String message;
}