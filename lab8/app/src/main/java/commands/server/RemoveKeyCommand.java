package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;


/**
 * Removes element by key. That's all
 */
public class RemoveKeyCommand extends BasicCommand {
    public RemoveKeyCommand(CollectionManager collectionManager){
        super("remove_key", "remove_key null : удалить элемент из коллекции по его ключу", collectionManager);
    }


}
