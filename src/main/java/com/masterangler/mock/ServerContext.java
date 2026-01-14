package com.masterangler.mock;

public class ServerContext {

    public void sendPacket(Player player, Packet packet) {
        // Mock implementation of sending packet
        System.out.println("Sending packet " + packet.getClass().getSimpleName() + " to player " + player.getName());
    }
}
