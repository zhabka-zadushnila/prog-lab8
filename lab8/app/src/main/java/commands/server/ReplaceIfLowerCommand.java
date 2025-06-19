package commands.server;

import commands.BasicCommand;
import exceptions.NoSuchElementException;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DragonCreationManager;
import structs.classes.Dragon;


/**
 * Replaces all the elements that are lower
 */
public class ReplaceIfLowerCommand extends BasicCommand {
    CommandManager commandManager;

    public ReplaceIfLowerCommand(CollectionManager collectionManager, CommandManager commandManager) {
        super("replace_if_lowe", "replace_if_lowe null {element} : заменить значение по ключу, если новое значение меньше старого", collectionManager);
        this.commandManager = commandManager;
    }


    /**
     * Uses basic {@link CollectionManager}
     *
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException - basically do not give empty input
     * @throws NoSuchElementException     - basically didn't find an element
     */
    @Override
    public Object execute(String[] args) throws NullArgsForbiddenException, NoSuchElementException {
        if (args.length == 0) {
            throw new NullArgsForbiddenException();
        }

        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        Dragon dragon = DragonCreationManager.inputDragon(commandManager.getClientManager().getInputIterator());

        return dragon;

    }
}
