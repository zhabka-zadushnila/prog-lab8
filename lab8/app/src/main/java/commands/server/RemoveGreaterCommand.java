package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Removes greater elements (gets initial element by id)
 */
public class RemoveGreaterCommand extends BasicCommand {
    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater", "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный", collectionManager);
    }


}
