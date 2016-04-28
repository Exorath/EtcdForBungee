package com.exorath.etcdbungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

/**
 * Created by Toon Sevrin on 4/28/2016.
 */
public class JoinHandler implements Listener {
    public JoinHandler() {
        ProxyServer.getInstance().setReconnectHandler(new ReconnectHandler() {
            @Override
            public ServerInfo getServer(ProxiedPlayer proxiedPlayer) {
                Optional<MCService> service = EtcdBungee.getInstance().getRegistry().getServices().stream().filter(s -> (s.getServerInfo() != null && s.getImageName().endsWith("hub"))).findAny();
                return service.isPresent() ? service.get().getServerInfo() : null;
            }

            @Override
            public void setServer(ProxiedPlayer proxiedPlayer) {
            }

            @Override
            public void save() {
            }

            @Override
            public void close() {
            }
        });
    }
}
