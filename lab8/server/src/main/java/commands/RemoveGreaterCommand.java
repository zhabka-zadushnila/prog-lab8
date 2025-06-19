package commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import structs.User;
import structs.classes.Dragon;

/**
 * Removes greater elements (gets initial element by id)
 */
public class RemoveGreaterCommand extends BasicCommand {
    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater", "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный", collectionManager);
    }


    @Override
    public String execute(Object arguments, User user) throws NullArgsForbiddenException {
        if (user == null) {
            return "no you cant, log in to do it";
        }

        String[] args = (String[]) arguments;


        if (args.length == 0 || args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        String input = args[0].trim();
        Dragon masterElement = collectionManager.getElement(input);

        if (masterElement == null) {
            return "Такого элемента нет";
        }

        Map<String, Dragon> originalCollection = collectionManager.getCollection();
        Map<String, Dragon> newCollection = new HashMap<>(originalCollection);

        Set<String> keysToDelete = originalCollection.entrySet().stream()
                .filter((entry) -> !masterElement.equals(entry.getValue()))
                .filter((entry) -> masterElement.compareTo(entry.getValue()) > 0)
                .filter((entry) -> collectionManager.getDbManager().isDragonUsers(entry.getKey(), user.getLogin()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());


        keysToDelete.forEach(newCollection::remove);
        int deleted = keysToDelete.size();
        
        if (collectionManager.getDbManager().deleteDragons(keysToDelete, user.getLogin()).isExpectedBehabiour()) {
            collectionManager.setCollection(newCollection);
        }
        return String.format("Deleted %d objects ^_^", deleted);
    }
}
