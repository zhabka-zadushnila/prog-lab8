package commands;

import classes.Dragon;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Removes greater elements (gets initial element by id)
 */
public class RemoveGreaterCommand extends  BasicCommand{
    public RemoveGreaterCommand(CollectionManager collectionManager){
        super("remove_greater", "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный", collectionManager);
    }

    /**
     * Uses its {@link CollectionManager} and would remind me of <b>RUST SUPREMACY</b> till the end of my life.
     * @param args an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException thrown when no args were provided.
     */
    @Override
    public void execute(String[] args) throws NullArgsForbiddenException {
        int deleted = 0;
        if(args.length == 0){
            throw new NullArgsForbiddenException();
        }
        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        String input = args[0];
        Dragon masterElement = collectionManager.getElement(input);


        if(masterElement == null){
            System.out.println("Такого элемента нет");
            return;
        }
        Map<String, Dragon> originalCollection = collectionManager.getCollection();
        Map<String, Dragon> newCollection = new HashMap<String, Dragon>(originalCollection);
        for(Map.Entry<String, Dragon> comparableElement : originalCollection.entrySet()){
            if(masterElement.equals(comparableElement.getValue())){
                continue;
            }
            if(masterElement.compareTo(comparableElement.getValue()) > 0){
                newCollection.remove(comparableElement.getKey());
                deleted++;
            }
        }
        collectionManager.setCollection(newCollection);
        System.out.println("Deleted " + deleted + " objects ^_^");
    }
}
