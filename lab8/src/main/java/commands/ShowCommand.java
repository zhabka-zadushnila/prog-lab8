package commands;

import classes.Dragon;
import managers.CollectionManager;

import java.util.Map;

/**
 * Shows the collection
 */
public class ShowCommand extends BasicCommand{
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении", collectionManager);
    }

    /**
     * Uses its {@link CollectionManager} to access map.
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public void execute(String[] args){
        Map<String, Dragon> localMap = this.collectionManager.getCollection();
        for (var pair: localMap.entrySet()){
            System.out.println("Key (" + pair.getKey() +")" + ":");
            System.out.println("\t" + pair.getValue().toString().replaceAll("\n", "\n\t") + "\n");

        }
    }
}
