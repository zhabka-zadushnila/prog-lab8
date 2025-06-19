package Interpreters;

import commands.BasicCommand;
import exceptions.CustomException;
import exceptions.EmptyRequestException;
import managers.CollectionManager;
import managers.CommandManager;
import structs.Packet;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * One of main classes in program. Basically it is used for executing anything.
 */
public class ServerCommandInterpreter {

    private static final Logger logger = Logger.getLogger(ServerCommandInterpreter.class.getName());

    CommandManager commandManager;
    Iterator<String> it;
    Set<String> executableFiles = new HashSet<String>();

    /**
     * Basic or initial constructor is used for CLI input. In this case CommandInterpreter needs only {@link CommandManager} and {@link Iterator<String>}
     *
     * @param commandManager It is what it is
     * @param input          Should be Iterator with String.
     */
    public ServerCommandInterpreter(CommandManager commandManager, Iterator<String> input) {
        this.commandManager = commandManager;
        this.commandManager.setCommandInterpreter(this);
        this.it = input;
    }

    /**
     * In case you want to execute files, you might want to store them (just to exclude chanse infinite recursion). For this {@link Set} with filenames is stored.
     *
     * @param commandManager  It is what it is
     * @param input           Iterator with String
     * @param executableFiles Set with all the files that were executed before.
     */
    public ServerCommandInterpreter(CommandManager commandManager, Iterator<String> input, Set<String> executableFiles) {
        this.commandManager = commandManager;
        this.commandManager.setCommandInterpreter(this);
        this.it = input;
        this.executableFiles = executableFiles;
    }


    public String executeRequest(Packet packet) {
        if (packet == null) {
            throw new EmptyRequestException();
        }

        BasicCommand command = commandManager.getCommand(packet.getCommand());

        if (command != null) {
            try {
                logger.info("Client executed " + command.getName() + " command;");
                if (packet.isCommand()) {
                    return command.execute(packet.getArguments(), packet.getUser());
                }
                if (packet.isObjectCommand()) {
                    logger.info("Server got " + command.getName() + " command");
                    return command.execute(packet.getArgsObjectWrapper(), packet.getUser());
                }
            } catch (CustomException | FileNotFoundException e) {
                return e.toString();
            }
        }
        return "";
    }

    public Iterator<String> getInputIterator() {
        return it;
    }

    public CollectionManager getCollectionManager() {
        return this.commandManager.getCollectionManager();
    }

    public Set<String> getExecutableFiles() {
        return executableFiles;
    }

}