package com.exorath.etcdbungee;

/**
 * Created by Toon Sevrin on 4/25/2016.
 */
public interface ServiceResponseHandler {
    void onResponse(String body);

    static ServiceResponseHandler create(){
        return new ServiceResponseHandlerImpl();
    }
}
