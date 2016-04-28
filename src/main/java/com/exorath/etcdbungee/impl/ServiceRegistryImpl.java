package com.exorath.etcdbungee.impl;

import com.exorath.etcdbungee.MCService;
import com.exorath.etcdbungee.api.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toon Sevrin on 4/28/2016.
 */
public class ServiceRegistryImpl implements ServiceRegistry {
    private List<MCService> services = new ArrayList<>();

    @Override
    public List<MCService> getServices() {
        return services;
    }

    @Override
    public void addService(MCService service){
        services.add(service);
    }
    @Override
    public void clearServices(){
        services.clear();
    }
}
