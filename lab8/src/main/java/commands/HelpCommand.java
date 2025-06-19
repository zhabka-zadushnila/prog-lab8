package commands;

import managers.CommandManager;

/**
 * Command that outputs name and description for all the commands in its {@link CommandManager}.
 */
public class HelpCommand extends BasicCommand {
    CommandManager commandManager;

    public HelpCommand(CommandManager commandManager){
        super("help", "help : вывести справку по доступным командам");
        this.commandManager = commandManager;
    }

    /**
     * Gets all the commands that were registered in its {@link CommandManager}.
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */

    @Override
    public void execute(String[] args) {
        for(var command : commandManager.getCommandsMap().entrySet()){
            System.out.println("  - " + command.getValue().getDescription());
        }
    }
}
