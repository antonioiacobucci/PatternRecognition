package com.exercise.pattern.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
public class PostPointRequest implements Serializable {
    private static final long serialVersionUID = -1429274578236700954L;

    private BigInteger x;
    private BigInteger y;
}