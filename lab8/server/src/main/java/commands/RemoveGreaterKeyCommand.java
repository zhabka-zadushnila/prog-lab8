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
 * Removes elements with key greater than this one
 */
public class RemoveGreaterKeyCommand extends BasicCommand {
    public RemoveGreaterKeyCommand(CollectionManager collectionManager) {
        super("remove_greater_key", "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный", collectionManager);
    }


    @Override
    public String execute(Object arguments, User user) {
        if (user == null) {
            return "bruh, register";
        }

        String[] args = (String[]) arguments;

        if (args.length == 0 || args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        String masterId = args[0].trim();
        Map<String, Dragon> originalCollection = collectionManager.getCollection();
        Map<String, Dragon> newCollection = new HashMap<>(originalCollection);

        Set<String> keysToDelete = originalCollection.keySet().stream()
                .filter(key -> !key.equals(masterId))
                .filter(key -> masterId.compareTo(key) > 0)
                .filter(key -> collectionManager.getDbManager().isDragonUsers(key, user.getLogin()))
                .collect(Collectors.toSet());


        keysToDelete.forEach(newCollection::remove);
        int deleted = keysToDelete.size();
        if (collectionManager.getDbManager().deleteDragons(keysToDelete, user.getLogin()).isExpectedBehabiour()) {
            collectionManager.setCollection(newCollection);
        }
        return String.format("Deleted " + deleted + " objects ^_^");
    }
}
