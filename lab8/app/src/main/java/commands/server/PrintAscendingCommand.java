package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;


/**
 * Outputs all the elements stored in its {@link CollectionManager} in ascending order
 */
public class PrintAscendingCommand extends BasicCommand {
    public PrintAscendingCommand(CollectionManager collectionManager) {
        super("print_ascending","print_ascending : вывести элементы коллекции в порядке возрастания", collectionManager);
    }


}
