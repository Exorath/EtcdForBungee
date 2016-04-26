package com.exorath.etcdbungee;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;

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
            if (dirNode.isJsonObject() && dirNode.getAsJsonObject().has("nodes"))
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

    private void updateBungee() {

        ProxyServer.getInstance().getConfigurationAdapter().getListeners().forEach(l -> l.getServerPriority().clear());
        ProxyServer.getInstance().getServers().clear();
        System.out.println("PRIORITIES -V1:");
        ProxyServer.getInstance().getConfigurationAdapter().getListeners().forEach(s -> s.getServerPriority().forEach(p -> System.out.println(p)));
        for (MCService service : services) {
            String name = service.getName();
            try {
                System.out.print("Found server: " + name + ", adding it...");
                ProxyServer.getInstance().getServers().put(name, ProxyServer.getInstance().constructServerInfo(name, service.getAddress(), "", false));
                ProxyServer.getInstance().getConfigurationAdapter().getListeners().forEach(l -> {l.getServerPriority().add(name); System.out.println("Listener: " + l.getHost().getPort());});
                System.out.println(" Added!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("SERVERS:");
        ProxyServer.getInstance().getServers().values().forEach(s -> System.out.println(s.getAddress().getHostName() + ":" + s.getAddress().getPort()));
        System.out.println("PRIORITIES:");
        ProxyServer.getInstance().getConfigurationAdapter().getListeners().forEach(s -> s.getServerPriority().forEach(p -> System.out.println(p)));
    }

    private static JsonObject getDirsRecursive(String body) {
        return new JsonParser().parse(body).getAsJsonObject();
    }

}
