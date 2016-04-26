package com.exorath.etcdbungee;

import net.md_5.bungee.api.ProxyConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.*;

/**
 * Created by Toon Sevrin on 4/26/2016.
 */
public class ConfigAdapter implements ConfigurationAdapter{
    private ProxyConfig config = ProxyServer.getInstance().getConfig();
    private Set<ListenerInfo> listeners = new HashSet<>();
    private Map<String, ServerInfo> servers = new HashMap<>();


    @Override
    public Map<String, ServerInfo> getServers() {return servers;}

    @Override
    public Collection<ListenerInfo> getListeners() {return listeners;}

    @Override
    public void load() {}

    @Override
    public int getInt(String s, int i) {return 0;}

    @Override
    public String getString(String s, String s1) {return null;}

    @Override
    public boolean getBoolean(String s, boolean b) {return false;}

    @Override
    public Collection<?> getList(String s, Collection<?> collection) {return null;}


    @Override
    public Collection<String> getGroups(String s) {return null;}

    @Override
    public Collection<String> getPermissions(String s) {return null;}
}
