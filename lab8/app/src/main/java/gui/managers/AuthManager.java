package gui.managers;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import commands.server.LoginCommand;
import commands.server.RegisterCommand;
import managers.ConnectionManager;
import structs.Packet;
import structs.User;
import utils.RequestConstructor;
import utils.RequestResponseTool;

public class AuthManager {

    static int MAX_RECONNECT_ATTEMPTS = 5;
    static int RECONNECT_TIMEOUT = 2000; //millis
    String hostname = "188.242.233.237";
    int port = 52947;
    SocketChannel channel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT);

    public AuthManager() {
    }


    public AuthManager(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public boolean login(String login, String password) {
        if(login == null){
            return false;
        }
        if (login.trim().isBlank()) {
            return false;
        }
        if(password == null){
            return false;
        }
        if (password.trim().isBlank()) {
            return false;
        }
        Packet packet = RequestConstructor.createRequest(new LoginCommand(null), null, new User(login, password), null);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return false;
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            if (packet.getText().equals("no such user") || packet.getText().equals("wrong password")) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean register(String login, String password) {
        if(login == null){
            return false;
        }
        if (login.trim().isBlank()) {
            return false;
        }
        if(password == null){
            return false;
        }
        if (password.trim().isBlank()) {
            return false;
        }
        SocketChannel channel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT);
        Packet packet = RequestConstructor.createRequest(new RegisterCommand(null), null, new User(login, password), null);
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            return false;
        }
        packet = RequestResponseTool.getPacket(channel);
        if (packet.isText()) {
            if (packet.getText().equals("User registered successfully")) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

}
