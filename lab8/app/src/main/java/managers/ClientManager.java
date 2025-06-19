package managers;

import exceptions.CustomException;
import structs.Packet;
import structs.User;
import utils.InputTools;
import utils.RequestConstructor;
import utils.RequestResponseTool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClientManager {

    static int MAX_RECONNECT_ATTEMPTS = 5;
    static int RECONNECT_TIMEOUT = 2000; //millis

    Iterator<String> it;
    String hostname = "188.242.233.237";
    int port = 25947;
    CommandManager commandManager;
    Set<String> executableFiles = new HashSet<String>();
    boolean fileNotEmpty = true;
    SocketChannel socketChannel;
    User user = null;

    public ClientManager(Iterator<String> it, String hostname, int port, CommandManager commandManager) {
        this.it = it;
        this.hostname = hostname;
        this.port = port;
        this.commandManager = commandManager;
        commandManager.setClientManager(this);
    }

    public ClientManager(Iterator<String> it, String hostname, int port, CommandManager commandManager, Set<String> executableFiles, SocketChannel socketChannel) {
        this(it, hostname, port, commandManager);
        this.executableFiles = executableFiles;
        this.socketChannel = socketChannel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void run() {
        while (true) {
            try (SocketChannel channel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT)) {
                this.socketChannel = channel;
                handleConnection(channel);
            } catch (IOException e) {
                System.out.println("Похоже произошёл дисконнект. Переподключаемся. (после подключения введите команду снова)");
            }
        }
    }

    public void runFile() {
        boolean fileEnded = false;
        while (socketChannel.isConnected() && !fileEnded) {
            try {
                fileEnded = fileWriteOperation(socketChannel);
                if (!fileEnded) {
                    readOperation(socketChannel);
                }
            } catch (IOException e) {
                System.out.println("Похоже произошёл дисконнект. Переподключаемся.");
                socketChannel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT);
            }
        }
    }


    private void handleConnection(SocketChannel channel) throws IOException {
        while (channel.isConnected()) {
            writeOperation(channel);
            readOperation(channel);

        }
    }


    private void readOperation(SocketChannel channel) throws IOException {
        //System.out.println("read is awaited");
        try {
            Packet packet = RequestResponseTool.getPacket(channel);
            if (packet == null) {
                throw new IOException("Server closed the connection");
            }
            if (packet.isText()) {
                if (packet.getText().equals("no such user") || packet.getText().equals("wrong password")) {
                    this.user = null;
                }
                System.out.println(packet.getText());
            } else {
                System.out.println("Request can not be processed for now");
            }
        } catch (CustomException e) {
            System.out.println(e.toString());
        }

    }

    private void writeOperation(SocketChannel channel) throws IOException {
        String line;
        String command;
        String[] args;
        String[] list;
        boolean requestSent = false;
        while (!requestSent) {
            System.out.print(">>> ");
            if (it.hasNext()) {
                line = it.next();

                if (line.trim().isBlank()) {
                    continue;
                }
                list = InputTools.splitLine(line);
                if (!commandManager.hasCommand(list[0])) {
                    System.out.println("Unknown command: " + list[0]);
                    continue;
                }

                command = list[0];
                args = list.length > 1 ? Arrays.copyOfRange(list, 1, list.length) : new String[0];

                try {
                    if (commandManager.isLocalCommand(command)) {
                        commandManager.getCommand(command).execute(args);
                    } else {
                        Packet packet = RequestConstructor.createRequest(commandManager.getCommand(command), args, commandManager.getCommand(command).execute(args), user);

                        RequestResponseTool.sendPacket(channel, packet);
                        requestSent = true;
                    }
                } catch (CustomException e) {
                    System.out.println(e);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }


            } else {
                System.out.println("\nInput finished. Suicide!");
                System.exit(1);
            }
        }
        //System.out.println("Waiting for response");
    }

    private boolean fileWriteOperation(SocketChannel channel) throws IOException {
        String line;
        String command;
        String[] args;
        String[] list;
        boolean requestSent = false;
        while (!requestSent) {
            if (it.hasNext()) {
                line = it.next();

                if (line.trim().isBlank()) {
                    continue;
                }
                list = InputTools.splitLine(line);
                if (!commandManager.hasCommand(list[0])) {
                    System.out.println("Unknown command: " + list[0]);
                    continue;
                }

                command = list[0];
                args = list.length > 1 ? Arrays.copyOfRange(list, 1, list.length) : new String[0];

                try {
                    if (commandManager.isLocalCommand(command)) {
                        commandManager.getCommand(command).execute(args);
                    } else {
                        Packet packet = RequestConstructor.createRequest(commandManager.getCommand(command), args, commandManager.getCommand(command).execute(args), user);
                        RequestResponseTool.sendPacket(channel, packet);
                        requestSent = true;
                    }
                } catch (CustomException e) {
                    System.out.println(e);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }

            } else {
                System.out.println("\nFile execution finished!");
                return true;
            }
        }
        return false;
    }


    public Iterator<String> getInputIterator() {
        return it;
    }

    public void setIt(Iterator<String> it) {
        this.it = it;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean executedFile(String filename) {
        return executableFiles.contains(filename);
    }


    public Set<String> getExecutableFiles() {
        return executableFiles;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
