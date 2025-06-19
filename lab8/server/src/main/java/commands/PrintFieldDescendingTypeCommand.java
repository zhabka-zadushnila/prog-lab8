package commands;

import java.util.stream.Collectors;

import managers.CollectionManager;
import structs.User;
import utils.TypeComparator;

/**
 * Outputs id : type
 */

public class PrintFieldDescendingTypeCommand extends BasicCommand {
    public PrintFieldDescendingTypeCommand(CollectionManager collectionManager) {
        super("print_field_descending_type", "print_field_descending_type : вывести значения поля type всех элементов в порядке убывания", collectionManager);
    }

    /**
     * Uses default List sort({@link TypeComparator})
     *
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public String execute(Object args, User user) {
        return this.collectionManager.getCollection().values().stream()
                .sorted(new TypeComparator())
                .map((dragon) -> dragon.getType().toString() + "\n")
                .collect(Collectors.joining());
    }
}
