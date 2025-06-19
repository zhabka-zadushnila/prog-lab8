package handlers;

import managers.CommandManager;
import structs.Packet;
import utils.RequestConstructor;
import utils.RequestResponseTool;

import java.nio.channels.SocketChannel;

public class ResponseSender implements Runnable {
    private final String response;
    CommandManager commandManager;
    SocketChannel socketChannel;

    public ResponseSender(SocketChannel socketChannel, String response) {
        this.response = response;
        this.socketChannel = socketChannel;
    }


    @Override
    public void run() {
        Packet packet = RequestConstructor.createRequest(response);
        RequestResponseTool.sendRequest(socketChannel, packet);
    }
}
