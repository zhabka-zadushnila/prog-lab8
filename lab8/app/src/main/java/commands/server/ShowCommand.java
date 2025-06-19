package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Shows the collection
 */
public class ShowCommand extends BasicCommand {
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении", collectionManager);
    }


}
