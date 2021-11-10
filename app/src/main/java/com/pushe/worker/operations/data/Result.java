package com.pushe.worker.operations.data;

import org.jetbrains.annotations.NotNull;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    @NotNull
    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success<?> success = (Result.Success<?>) this;
            return success.getData().toString();
        } else if (this instanceof Result.Error) {
            Result.Error<Exception> error = (Result.Error<Exception>) this;
            return error.getError().toString();
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends Result<T> {
        final private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error<Exception> extends Result<Exception> {
        final private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}