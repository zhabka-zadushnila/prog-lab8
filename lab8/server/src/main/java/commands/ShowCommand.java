package commands;

import managers.CollectionManager;
import structs.User;

import java.util.stream.Collectors;

/**
 * Shows the collection
 */
public class ShowCommand extends BasicCommand {
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении", collectionManager);
    }


    @Override
    public String execute(Object args, User user) {
        return this.collectionManager.getCollection().entrySet().stream()
                .map((pair) -> ("Key (" + pair.getKey() + ")" + ":") + "\n" + ("\t" + pair.getValue().toString().replaceAll("\n", "\n\t") + "\n"))
                .collect(Collectors.joining());
    }
}
