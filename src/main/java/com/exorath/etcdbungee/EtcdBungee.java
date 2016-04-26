package com.exorath.etcdbungee;

import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Toon Sevrin on 4/24/2016.
 */
public class EtcdBungee extends Plugin {
    private static EtcdBungee instance;
    private Configuration configuration;
    private ServiceRequestHandler serviceRequestHandler;
    private ServiceResponseHandler serviceResponseHandler;

    @Override
    public void onEnable() {
        ListenerInfo listener = getProxy().getConfigurationAdapter().getListeners().iterator().next();
        getProxy().setConfigurationAdapter(new ConfigAdapter());
        getProxy().getConfigurationAdapter().getListeners().add(listener);

        instance = this;
        loadConfig();
        serviceResponseHandler = ServiceResponseHandler.create();
        serviceRequestHandler = ServiceRequestHandler.create();
    }

    private void loadConfig() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigFile());
            loadConfigDefaults();
        } catch (IOException e) {e.printStackTrace();}
    }

    private void loadConfigDefaults() throws IOException {
        if (configuration.getKeys().size() == 0) {
            configuration.set("interval", ServiceRequestHandler.DEFAULT_INTERVAL_IN_MILLIS);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, getConfigFile());
        }
    }

    private File getConfigFile() throws IOException {
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), "config.yml");
        file.createNewFile();
        return file;
    }

    public ServiceRequestHandler getServiceRequestHandler() {
        return serviceRequestHandler;
    }

    public ServiceResponseHandler getServiceResponseHandler() {
        return serviceResponseHandler;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static EtcdBungee getInstance() {
        return instance;
    }
}
