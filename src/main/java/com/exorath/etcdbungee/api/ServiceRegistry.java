package com.exorath.etcdbungee.api;


import com.exorath.etcdbungee.MCService;
import com.exorath.etcdbungee.impl.ServiceRegistryImpl;

import java.util.Collection;

/**
 * Created by Toon Sevrin on 4/28/2016.
 */
public interface ServiceRegistry {
    Collection<MCService> getServices();
    void addService(MCService service);
    void clearServices();

    static ServiceRegistry create() {
        return new ServiceRegistryImpl();
    }

}
