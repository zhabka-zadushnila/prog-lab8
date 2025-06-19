package handlers;

import Interpreters.ServerCommandInterpreter;
import exceptions.CustomException;
import exceptions.EmptyRequestException;
import structs.Packet;
import structs.User;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessingHandler implements Runnable {
    private final String login;
    private final Packet packet;
    private final Map<String, User> userSessions;
    ServerCommandInterpreter serverCommandInterpreter;
    SocketChannel socketChannel;
    ExecutorService responseSender = Executors.newCachedThreadPool();

    public ProcessingHandler(SocketChannel socketChannel, String login, Packet packet, Map<String, User> userSessions, ServerCommandInterpreter serverCommandInterpreter) {
        this.login = login;
        this.packet = packet;
        this.userSessions = userSessions;
        this.socketChannel = socketChannel;
        this.serverCommandInterpreter = serverCommandInterpreter;
    }

    @Override
    public void run() {
        if (packet == null) {
            throw new EmptyRequestException();
        }
        
        try {
            if (packet.isCommand()) {

                responseSender.execute(new ResponseSender(socketChannel, serverCommandInterpreter.executeRequest(packet)));
            }
            if (packet.isObjectCommand()) {
                responseSender.execute(new ResponseSender(socketChannel, serverCommandInterpreter.executeRequest(packet)));
            }
        } catch (CustomException e) {
            System.out.println(e);
        }

    }
}
