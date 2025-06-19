package managers;

import Interpreters.ServerCommandInterpreter;
import handlers.ClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private final ServerCommandInterpreter serverCommandInterpreter;
    private final int port;

    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService processPool = Executors.newCachedThreadPool();
    private final ExecutorService writePool = Executors.newCachedThreadPool();

    private ServerSocketChannel serverSocketChannel;
    private volatile boolean running = true;

    public ConnectionManager(int port, ServerCommandInterpreter serverCommandInterpreter) {
        this.port = port;
        this.serverCommandInterpreter = serverCommandInterpreter;
    }

    public void serve() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("0.0.0.0" ,port));
            logger.info("Server started on port " + port);

            new Thread(this::handleConsoleInput).start();

            while (running) {
                try {
                    SocketChannel clientSocket = serverSocketChannel.accept();
                    readPool.execute(new ClientHandler(
                            clientSocket,
                            serverCommandInterpreter,
                            processPool,
                            writePool
                    ));
                } catch (IOException e) {
                    if (running) {
                        logger.severe("Error accepting client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("Server failed to start: " + e.getMessage());
            System.exit(1);
        } finally {
            shutdown();
        }
    }

    private void handleConsoleInput() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (running) {
                if (reader.ready()) {
                    String line = reader.readLine();
                    processConsoleCommand(line);
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.severe("Error in console handler: " + e.getMessage());
        }
    }

    private void processConsoleCommand(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length == 0) return;

        if (parts[0].equals("exit")) {
            System.out.println("Shutting down server.");
            System.exit(0);
        } else {
            System.out.println("Unknown command: " + parts[0]);
        }

    }

    private void saveToFile(String filename) {
        processPool.execute(() -> {
            try {
                FileManager.saveCollectionToFile(filename, serverCommandInterpreter.getCollectionManager().getCollection());
            } catch (Exception e) {
                logger.severe("Error saving collection: " + e.getMessage());
            }
        });
    }

    private void shutdown() {
        running = false;
        readPool.shutdown();
        processPool.shutdown();
        writePool.shutdown();
        logger.info("Server shutdown complete.");
    }
}