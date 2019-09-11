package com.exercise.pattern.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
public class PostPointResponse implements Serializable {
    private static final long serialVersionUID = 3926242570007497876L;

    private String status;
    private String message;
}
