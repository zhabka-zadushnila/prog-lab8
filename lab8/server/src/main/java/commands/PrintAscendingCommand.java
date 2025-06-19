package commands;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import managers.CollectionManager;
import structs.User;


/**
 * Outputs all the elements stored in its {@link CollectionManager} in ascending order
 */
public class PrintAscendingCommand extends BasicCommand {
    public PrintAscendingCommand(CollectionManager collectionManager) {
        super("print_ascending", "print_ascending : вывести элементы коллекции в порядке возрастания", collectionManager);
    }


    /**
     * Uses basic {@link List} sort(null) method.
     *
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public String execute(Object args, User user) {
        return this.collectionManager.getCollection().entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map((entry) -> "Key (" + entry.getKey() + "):\n\t" + entry.getValue().toString().replaceAll("\n", "\n\t") + "\n")
                .collect(Collectors.joining());
    }
}
