package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Removes elements with key greater than this one
 */
public class RemoveGreaterKeyCommand extends BasicCommand {
    public RemoveGreaterKeyCommand(CollectionManager collectionManager) {
        super("remove_greater_key", "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный", collectionManager);
    }


}
