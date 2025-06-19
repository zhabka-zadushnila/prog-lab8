package gui.managers;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import commands.server.InsertCommand;
import commands.server.RemoveGreaterKeyCommand;
import commands.server.RemoveKeyCommand;
import commands.server.ReplaceIfLowerCommand;
import commands.server.UpdateCommand;
import managers.ConnectionManager;
import structs.Packet;
import structs.User;
import structs.wrappers.DragonDisplayWrapper;
import utils.RequestConstructor;
import utils.RequestResponseTool;

public class CommandsManager {

    static int MAX_RECONNECT_ATTEMPTS = 5;
    static int RECONNECT_TIMEOUT = 2000; //millis
    String hostname = "188.242.233.237";
    int port = 52947;
    SocketChannel channel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT);

    public CommandsManager() {
    }


    public CommandsManager(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String insertDragon(DragonDisplayWrapper dragonEntry, User user) {
        Packet packet = RequestConstructor.createRequest(new InsertCommand(null, null), null, dragonEntry, user);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return "Some troubles";
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            return packet.getText();
        } else {
            return "Response is not text";
        }
    }

    public String updateDragon(DragonDisplayWrapper dragonEntry, User user) {
        Packet packet = RequestConstructor.createRequest(new UpdateCommand(null, null), null, dragonEntry, user);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return "Some troubles";
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            return packet.getText();
        } else {
            return "Response is not text";
        }
    }

    public String replaceIfLowerDragon(DragonDisplayWrapper dragonEntry, User user) {
        Packet packet = RequestConstructor.createRequest(new ReplaceIfLowerCommand(null, null), null, dragonEntry, user);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return "Some troubles";
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            return packet.getText();
        } else {
            return "Response is not text";
        }
    }

    public String removeKeyDragon(String[] args, User user) {
        Packet packet = RequestConstructor.createRequest(new RemoveKeyCommand(null), args, null, user);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return "Some troubles";
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            return packet.getText();
        } else {
            return "Response is not text";
        }
    }

    public String removeKeyGrDragon(String[] args, User user) {
        Packet packet = RequestConstructor.createRequest(new RemoveGreaterKeyCommand(null), args, null, user);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return "Some troubles";
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            return packet.getText();
        } else {
            return "Response is not text";
        }
    }
}
