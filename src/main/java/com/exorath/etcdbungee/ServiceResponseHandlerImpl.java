package com.exorath.etcdbungee;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by twan1 on 4/25/2016.
 */
public class ServiceResponseHandlerImpl implements ServiceResponseHandler {
    private List<Integer> ports;

    @Override
    public void onResponse(String body) {
        JsonObject obj = getDirsRecursive(body);
        if (!obj.has("node") || !obj.get("node").getAsJsonObject().has("nodes"))
            return;
        ports = new ArrayList<>();
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
        if (node.get("key").getAsString().endsWith("25565") && node.get("value").getAsString().contains(":")) {
            try {
                ports.add(Integer.valueOf(node.get("value").getAsString().split(":")[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBungee() {
        ProxyServer.getInstance().getServers().clear();
        for (int port : ports) {
            try {
                ProxyServer.getInstance().getServers().put(getName(port), ProxyServer.getInstance().constructServerInfo(
                        getName(port), new InetSocketAddress(InetAddress.getLocalHost(), port), "", false));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getName(int port) {
        return "MC-" + port;
    }

    private static JsonObject getDirsRecursive(String body) {
        return new JsonParser().parse(body).getAsJsonObject();
    }

}
