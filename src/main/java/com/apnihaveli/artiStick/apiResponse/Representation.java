package com.apnihaveli.artiStick.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Representation<T> {
    private long code;

    private T data;

    public Representation() {
        //Jackson Deserialisation
    }

    public Representation(long code, T data) {
        this.code = code;
        this.data = data;
    }

    @JsonProperty
    public long getCode() {
        return code;
    }

    @JsonProperty
    public T getData() {
        return data;
    }
}
