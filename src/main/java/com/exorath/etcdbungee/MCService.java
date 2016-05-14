package com.exorath.etcdbungee;


import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by Toon Sevrin on 4/25/2016.
 */
public class MCService {
    private static final int MC_PORT = 25565;
    private static final int STARTING_PORT_RANGE = 32768;
    private String imageName;
    private String hostName;
    private String containerName;
    private InetAddress ip;
    private int publicPort;

    private InetSocketAddress address;

    private ServerInfo serverInfo;

    public MCService(String imageName, String hostName, String containerName, InetAddress ip, int publicPort) {
        this.imageName = imageName;
        this.hostName = hostName;
        this.containerName = containerName;
        this.ip = ip == null ? InetAddress.getLoopbackAddress() : ip;
        this.publicPort = publicPort;

        address = new InetSocketAddress(ip, publicPort);
    }

    public void setServerInfo(ServerInfo serverInfo){
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public static MCService getService(String key, String value) {
        System.out.println("Creating service: " + key + " w/v: " + value);
        try {
            String[] parts = key.split(":");//Layout: "/{image}/{host}:{container}:{port}"
            if (parts.length == 3 && Integer.valueOf(parts[2]) == MC_PORT) {
                String[] ipAndPort = value.split(":");
                InetAddress ip = ipAndPort == null || ipAndPort[0].equals("") ? null : InetAddress.getByName(ipAndPort[0]);
                return new MCService(
                        parts[0].split("/")[1],//{image}
                        parts[0].split("/")[2],//{host}
                        parts[1],//{container}
                        ip,
                        Integer.valueOf(ipAndPort[1]));//{port}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getName(){
        return containerName.substring(containerName.indexOf("_") + 1);
    }
    private int getShortenedPort(){
        return publicPort - STARTING_PORT_RANGE;
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
