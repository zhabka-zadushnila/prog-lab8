package handlers;

import Interpreters.ServerCommandInterpreter;
import exceptions.CustomException;
import structs.Packet;
import structs.User;
import utils.RequestResponseTool;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class ReadHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ReadHandler.class.getName());
    private final SocketChannel socket;
    private final ExecutorService processingPool;
    private final Map<String, User> userSessions;
    private final ServerCommandInterpreter serverCommandInterpreter;

    public ReadHandler(SocketChannel socket, ExecutorService processingPool, Map<String, User> userSessions, ServerCommandInterpreter serverCommandInterpreter) {
        this.socket = socket;
        this.processingPool = processingPool;
        this.userSessions = userSessions;
        this.serverCommandInterpreter = serverCommandInterpreter;
    }

    @Override
    public void run() {
        Packet packet = RequestResponseTool.getRequest(socket);
        User user = packet.getUser();
        if (packet == null) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.info("Client disconnected");
            return;
        }
        try {
            processingPool.execute(new ProcessingHandler(socket, user.getLogin(), packet, userSessions, serverCommandInterpreter));
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}
