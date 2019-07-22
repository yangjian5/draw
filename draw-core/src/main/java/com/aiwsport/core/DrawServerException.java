package com.aiwsport.core;

/**
 * xin.pang
 */
public class DrawServerException extends RuntimeException {
    private DrawServerExceptionFactor factor;

    public DrawServerException(DrawServerExceptionFactor factor) {
        this(factor, factor.getErrorMsg());
    }

    public DrawServerException(DrawServerExceptionFactor factor, String message) {
        super((message == null ? factor.getErrorMsg() : message));
        this.factor = factor;
    }

    public DrawServerException(Exception e) {
        this(DrawServerExceptionFactor.DEFAULT, e.getMessage());
    }

    public DrawServerExceptionFactor getFactor() {
        return factor;
    }
}
