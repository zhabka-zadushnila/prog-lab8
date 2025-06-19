package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Command to clear the collection.
 */
public class ClearCommand extends BasicCommand {
    public ClearCommand(CollectionManager collectionManager){
        super("clear", "clear : очистить коллекцию", collectionManager);
    }

}
