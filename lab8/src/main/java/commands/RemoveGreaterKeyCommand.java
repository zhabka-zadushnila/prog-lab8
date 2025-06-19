package commands;

import classes.Dragon;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Removes elements with key greater than this one
 */
public class RemoveGreaterKeyCommand extends BasicCommand{
    public RemoveGreaterKeyCommand(CollectionManager collectionManager){
        super("remove_greater_key", "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный", collectionManager);
    }


    @Override
    public void execute(String[] args) throws NullArgsForbiddenException {
        int deleted = 0;

        if(args.length == 0){
            throw new NullArgsForbiddenException();
        }

        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        String masterId = args[0];
        Map<String, Dragon> originalCollection = collectionManager.getCollection();
        Map<String, Dragon> newCollection = new HashMap<String, Dragon>(originalCollection);
        for(String comparableElement : collectionManager.getCollection().keySet()){
            if(masterId.equals(comparableElement)){
                continue;
            }
            if(masterId.compareTo(comparableElement) > 0){
                newCollection.remove(comparableElement);
                deleted++;
            }
        }
        collectionManager.setCollection(newCollection);
        System.out.println("Deleted " + deleted + " objects ^_^");
    }
}
