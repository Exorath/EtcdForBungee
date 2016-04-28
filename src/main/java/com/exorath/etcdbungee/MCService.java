package com.exorath.etcdbungee;


import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by Toon Sevrin on 4/25/2016.
 */
public class MCService {
    private static final int MC_PORT = 25565;
    private String imageName;
    private String hostName;
    private String containerName;
    private int publicPort;

    private InetSocketAddress address;

    private ServerInfo serverInfo;

    public MCService(String imageName, String hostName, String containerName, int publicPort) {
        this.imageName = imageName;
        this.hostName = hostName;
        this.containerName = containerName;
        this.publicPort = publicPort;
        address = new InetSocketAddress(InetAddress.getLoopbackAddress(), publicPort);
    }

    public void setServerInfo(ServerInfo serverInfo){
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public static MCService getService(String key, String value) {
        try {
            String[] parts = key.split(":");//Layout: "/{image}/{host}:{container}:{port}"
            if (parts.length == 3 && Integer.valueOf(parts[2]) == MC_PORT)
                return new MCService(
                        parts[0].split("/")[1],//{image}
                        parts[0].split("/")[2],//{host}
                        parts[1],//{container}
                        Integer.valueOf(value.split(":")[1]));//{port}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getName(){
        String[] splitImageName = imageName.split("_");
        return splitImageName[splitImageName.length - 1] + "-" + publicPort;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getHostName() {
        return hostName;
    }
}
