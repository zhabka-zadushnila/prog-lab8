package gui.managers;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import commands.server.InfoCommand;
import managers.ConnectionManager;
import structs.Packet;
import utils.RequestConstructor;
import utils.RequestResponseTool;

public class ActionsManager {

    static int MAX_RECONNECT_ATTEMPTS = 5;
    static int RECONNECT_TIMEOUT = 2000; //millis
    String hostname = "188.242.233.237";
    int port = 52947;
    SocketChannel channel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT);


    public String getInfo() {
        Packet packet = RequestConstructor.createRequest(new InfoCommand(null), null, null, null);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return "Error Connecting to the server";
        }
        packet = RequestResponseTool.getPacket(channel);
        return packet.getText();
    }
}
