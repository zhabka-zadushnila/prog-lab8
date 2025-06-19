package commands;

import Interpreters.CommandInterpreter;
import exceptions.InfiniteFileRecursion;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Command that is used to execute scripts.
 */

public class ExecuteScriptCommand extends BasicCommand{
    CommandManager commandManager;
    //place command manager in here

    /**
     * Its constructor gets {@link CommandManager}, that manages commands and contains {@link CommandInterpreter}
     * @param commandManager - thing that manages commands, operates with them. Needed for some commands to access managers and stuff.
     */

    public ExecuteScriptCommand(CommandManager commandManager){
        super("execute_script","execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.commandManager = commandManager;
    }


    /**
     * In this command it basically checks if there's no recursion and starts new instance of {@link CommandInterpreter}, that gets things done
     * @param args an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws InfiniteFileRecursion throws just in case if user lost his mind and tries to create infinite file recursion.
     * @throws NullArgsForbiddenException thrown when no arguments were provided (they are exceptional).
     */
    @Override
    public void execute(String[] args) throws InfiniteFileRecursion, NullArgsForbiddenException, FileNotFoundException {
        if(args.length == 0){
            throw new NullArgsForbiddenException();
        }
        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }


        String filename = args[0];
        if(commandManager.getCommandInterpreter().executedFile(filename)){
            throw new InfiniteFileRecursion();
        }
        else{
            Set<String> executableFiles = commandManager.getCommandInterpreter().getExecutableFiles();
            executableFiles.add(filename);
            Scanner sc = new Scanner(new File(filename)).useDelimiter("\n");
            new CommandInterpreter(commandManager, sc, executableFiles).fileExecution();
            executableFiles.remove(filename);

        }
    }


}
