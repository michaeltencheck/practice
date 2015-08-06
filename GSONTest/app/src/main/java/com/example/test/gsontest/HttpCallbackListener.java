package com.example.test.gsontest;

/**
 * Created by test on 8/6/2015.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
