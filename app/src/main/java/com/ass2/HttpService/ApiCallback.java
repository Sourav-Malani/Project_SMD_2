package com.ass2.HttpService;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
