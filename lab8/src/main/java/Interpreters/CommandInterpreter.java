package Interpreters;

import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import commands.*;
import exceptions.*;
import exceptions.NoSuchElementException;
import managers.*;

/**
 * One of main classes in program. Basically it is used for executing anything.
 */
public class CommandInterpreter {

    CommandManager commandManager;
    Iterator<String> it;
    Set<String> executableFiles = new HashSet<String>();

    /**
     * Basic or initial constructor is used for CLI input. In this case CommandInterpreter needs only {@link CommandManager} and {@link Iterator<String>}
     * @param commandManager It is what it is
     * @param input Should be Iterator with String.
     */
    public CommandInterpreter(CommandManager commandManager, Iterator<String> input) {
        this.commandManager = commandManager;
        this.commandManager.setCommandInterpreter(this);
        this.it = input;
    }

    /**
     * In case you want to execute files, you might want to store them (just to exclude chanse infinite recursion). For this {@link Set} with filenames is stored.
     * @param commandManager It is what it is
     * @param input Iterator with String
     * @param executableFiles Set with all the files that were executed before.
     */
    public CommandInterpreter(CommandManager commandManager, Iterator<String> input, Set<String> executableFiles) {
        this.commandManager = commandManager;
        this.commandManager.setCommandInterpreter(this);
        this.it = input;
        this.executableFiles = executableFiles;
    }

    /**
     * Checks if this file was in execution sequence
     * @param filename name of file that we want to check
     * @return returns true if it did execute that file
     */
    public boolean executedFile(String filename){
        return executableFiles.contains(filename);
    }

    /**
     * Main loop that gets your input from CLI
     */
    public void loop_stdin() {
        System.out.print(">>> ");
        while (it.hasNext()) {
            String line = it.next();
            executeLine(line);
            System.out.print(">>> ");
        }
    }

    /**
     * loop_stdin() modification for file execution. Does not print out ">>> "
     */
    public void fileExecution(){
        while (it.hasNext()) {
            String line = it.next();
            executeLine(line);
        }
    }

    /**
     * Method that is used for initial execution of line. <br>
     * <i>public</i>, to be used in lab7 to execute line in server. <br>
     * <br>
     * <b>Note</b> that this method <b>DOES NOT</b> do anything with command args. It just splits line by spaces, uses first word as command and parses rest to the command. <br>
     * <b>Note</b> that it may send array of 0 length to your command. You should check input for such an option.
     * @param line basically a line with command.
     */
    public void executeLine(String line){

            if (line.trim().isBlank()) {
                return;
            }
            String[] parts = line.trim().split("[ \t]+");
            String commandName = parts[0];

            BasicCommand command = commandManager.getCommand(commandName);

            String[] args = (parts.length > 1) ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
            if (command != null) {
                try {
                command.execute(args);
            }catch (CustomException | FileNotFoundException e){
                System.out.println(e);
            }
            } else {
                System.out.println("Unknown command: " + commandName);
            }


    }

    public Iterator<String> getInputIterator() {
        return it;
    }
    public CollectionManager getCollectionManager(){
        return this.commandManager.getCollectionManager();
    }
    public Set<String> getExecutableFiles() {
        return executableFiles;
    }

}