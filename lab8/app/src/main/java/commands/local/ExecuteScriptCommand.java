package commands.local;

import commands.BasicCommand;
import exceptions.InfiniteFileRecursion;
import exceptions.NullArgsForbiddenException;
import managers.ClientManager;
import managers.CommandManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

/**
 * Command that is used to execute scripts.
 */

public class ExecuteScriptCommand extends BasicCommand {
    CommandManager commandManager;
    //place command manager in here


    public ExecuteScriptCommand(CommandManager commandManager){
        super("execute_script","execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.commandManager = commandManager;
    }


    @Override
    public Object execute(String[] args) throws InfiniteFileRecursion, NullArgsForbiddenException, FileNotFoundException {
        if(args.length == 0){
            throw new NullArgsForbiddenException();
        }
        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }


        String filename = args[0];
        if(commandManager.getClientManager().executedFile(filename)){
            throw new InfiniteFileRecursion();
        }
        else{
            Set<String> executableFiles = commandManager.getClientManager().getExecutableFiles();
            executableFiles.add(filename);
            Scanner sc = new Scanner(new File(filename)).useDelimiter("\n");
            new ClientManager(sc, commandManager.getClientManager().getHostname(), commandManager.getClientManager().getPort(), commandManager, executableFiles, commandManager.getClientManager().getSocketChannel()).runFile();
            executableFiles.remove(filename);

        }
        return null;
    }


}
