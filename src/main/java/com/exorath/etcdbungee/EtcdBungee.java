package com.exorath.etcdbungee;

import com.exorath.etcdbungee.api.ServiceRegistry;
import com.exorath.etcdbungee.api.ServiceRequestHandler;
import com.exorath.etcdbungee.api.ServiceResponseHandler;
import net.md_5.bungee.api.ProxyServer;
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
    private ServiceRegistry registry;

    @Override
    public void onEnable() {

        instance = this;
        loadConfig();
        serviceResponseHandler = ServiceResponseHandler.create();
        serviceRequestHandler = ServiceRequestHandler.create();
        registry = ServiceRegistry.create();
        ProxyServer.getInstance().getPluginManager().registerListener(this,new JoinHandler());
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

    public ServiceRegistry getRegistry() {
        return registry;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static EtcdBungee getInstance() {
        return instance;
    }
}
