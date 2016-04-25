package com.exorath.etcdbungee;

import com.github.kevinsawicki.http.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by twan1 on 4/25/2016.
 */
public class ServiceRequestHandlerImpl implements ServiceRequestHandler {

    @Override
    public void run() {
        try {
            HttpRequest request = HttpRequest.get(new URL(getProtocol(), getHost(), getPort(), "/v2/keys/?recursive=true"));
            EtcdBungee.getInstance().getServiceResponseHandler().onResponse(request.body());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getIntervalInMilliseconds() {
        return EtcdBungee.getInstance().getConfiguration().getInt("interval", ServiceRequestHandler.DEFAULT_INTERVAL_IN_MILLIS);
    }

    public String getProtocol() {
        return EtcdBungee.getInstance().getConfiguration().getString("protocol", "http");
    }

    public String getHost() {
        return EtcdBungee.getInstance().getConfiguration().getString("host", "localhost");
    }

    public int getPort() {
        return EtcdBungee.getInstance().getConfiguration().getInt("port", 2379);
    }
}
