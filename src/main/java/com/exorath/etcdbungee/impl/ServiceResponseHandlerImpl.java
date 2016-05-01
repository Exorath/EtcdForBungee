package com.exorath.etcdbungee.impl;

import com.exorath.etcdbungee.EtcdBungee;
import com.exorath.etcdbungee.MCService;
import com.exorath.etcdbungee.api.ServiceResponseHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Toon Sevrin on 4/25/2016.
 */
public class ServiceResponseHandlerImpl implements ServiceResponseHandler {
    private List<MCService> services = new ArrayList<>();

    @Override
    public void onResponse(String body) {
        JsonObject obj = getDirsRecursive(body);
        if (!obj.has("node") || !obj.get("node").getAsJsonObject().has("nodes"))
            return;
        services.clear();
        handleAllDirs(obj);
        updateBungee();
    }

    private void handleAllDirs(JsonObject obj) {
        for (JsonElement dirNode : obj.get("node").getAsJsonObject().get("nodes").getAsJsonArray())
            if (dirNode.isJsonObject() && dirNode.getAsJsonObject().has("nodes") && dirNode.getAsJsonObject().has("key"))
                if (areServicesAllowedByConfig(dirNode.getAsJsonObject().get("key").getAsString()))
                    handleServiceNodes(dirNode.getAsJsonObject().get("nodes").getAsJsonArray());
    }

    private void handleServiceNodes(JsonArray nodes) {
        for (JsonElement node : nodes)
            if (node.isJsonObject() && node.getAsJsonObject().has("key") && node.getAsJsonObject().has("value"))
                handleServiceNode(node.getAsJsonObject());

    }

    private void handleServiceNode(JsonObject node) {
        MCService service = MCService.getService(node.get("key").getAsString(), node.get("value").getAsString());
        if (service != null)
            services.add(service);
    }

    private boolean areServicesAllowedByConfig(String dirKey) {
        if(!dirKey.contains("/"))
            return false;
        dirKey = dirKey.split("/")[1];
        return dirKey.contains("=") && dirKey.split("=")[0].equals(EtcdBungee.getInstance().getConfiguration().getString("service-filter", "GAME"));
    }

    private void updateBungee() {
        ProxyServer.getInstance().getServers().clear();
        EtcdBungee.getInstance().getRegistry().clearServices();
        for (MCService service : services) {
            try {
                ServerInfo info = ProxyServer.getInstance().constructServerInfo(service.getName(), service.getAddress(), "", false);
                service.setServerInfo(info);
                ProxyServer.getInstance().getServers().put(info.getName(), info);
                EtcdBungee.getInstance().getRegistry().addService(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("SERVERS:");
        ProxyServer.getInstance().getServers().values().forEach(s -> System.out.println(s.getAddress().getHostName() + ":" + s.getAddress().getPort()));
    }

    private static JsonObject getDirsRecursive(String body) {
        return new JsonParser().parse(body).getAsJsonObject();
    }

}
