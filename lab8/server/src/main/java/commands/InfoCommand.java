package commands;

import managers.CollectionManager;
import structs.User;

import java.util.Map;

/**
 * Command, that briefly takes basic info from the {@link CollectionManager} and ouputs it into console.
 */

public class InfoCommand extends BasicCommand {

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", collectionManager);
    }

    /**
     * Collects info from {@link CollectionManager} and outputs it.
     *
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public String execute(Object args, User user) {
        Map<String, Object> map = collectionManager.getCollectionInfoMap();

        return ("Тип коллекции: " + map.get("Type").toString() + ".\n" +
                "Дата инициализации коллекции: " + map.get("Date").toString() + ".\n" +
                "Количество элементов в коллекции: " + map.get("ElementsQuantity").toString()
        );
    }
}
