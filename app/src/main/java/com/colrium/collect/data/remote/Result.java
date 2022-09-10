package com.colrium.collect.data.remote;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.colrium.collect.data.remote.api.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    private Result.Success<T> success;
    private Result.Error error;
    // hide the private constructor to limit subclass types (Success, Error)
    public Result() {
    }
    public Result(Result.Success<T> success) {
        this.success = success;
    }
    public Result(Result.Error error) {
        this.error = error;
    }

    public Result.Success<T> getSuccess() {
        return success;
    }

    public void setData(Result.Success<T> success) {
        this.success = success;
    }

    public Result.Error getError() {
        return error;
    }

    public void setError(Result.Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class


    public final static class Error extends Result {
        private int statusCode;
        private String message;
        private Object data;

        public Error() {
        }
        private Error(String message) {
            this.message = message;
        }

        public int statusCode() {
            return statusCode;
        }

        public String message() {
            return message;
        }
        public Object data() {
            return data;
        }
        @Override
        public String toString() {
            return "Status Code:"+statusCode+" \n Message: "+message+" \n Data: "+data;
        }
        public static Error parse(Response<?> response) {
            Converter<ResponseBody, Result.Error> converter =
                    ApiClient.retrofit()
                            .responseBodyConverter(Result.Error.class, new Annotation[0]);

            Result.Error error;

            try {
                error = converter.convert(response.errorBody());
            } catch (IOException e) {
                return new Result.Error();
            }


            return error;
        }
    }
}