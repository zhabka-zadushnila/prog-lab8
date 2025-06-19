package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Command, that briefly takes basic info from the {@link CollectionManager} and ouputs it into console.
 */

public class InfoCommand extends BasicCommand {

    public InfoCommand(CollectionManager collectionManager){
        super("info","info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", collectionManager);
    }

}
