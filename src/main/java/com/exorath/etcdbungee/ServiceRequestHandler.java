package com.exorath.etcdbungee;

import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Toon Sevrin on 4/25/2016.
 */
public interface ServiceRequestHandler extends Runnable {
    int DEFAULT_INTERVAL_IN_MILLIS = 1000;

    int getIntervalInMilliseconds();

    void run();

    static ServiceRequestHandler create() {
        ServiceRequestHandler serviceRequestHandler = new ServiceRequestHandlerImpl();

        ProxyServer.getInstance().getScheduler().schedule(EtcdBungee.getInstance(), serviceRequestHandler,
                0, serviceRequestHandler.getIntervalInMilliseconds(), TimeUnit.MILLISECONDS);

        return serviceRequestHandler;
    }
}
