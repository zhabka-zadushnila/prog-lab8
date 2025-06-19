package commands;

import classes.Dragon;
import managers.CollectionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Outputs all the elements stored in its {@link CollectionManager} in ascending order
 */
public class PrintAscendingCommand extends BasicCommand{
    public PrintAscendingCommand(CollectionManager collectionManager) {
        super("print_ascending","print_ascending : вывести элементы коллекции в порядке возрастания", collectionManager);
    }


    /**
     * Uses basic {@link List} sort(null) method.
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public void execute(String[] args) {
        List<Dragon> localList = new ArrayList<Dragon>(this.collectionManager.getCollection().values());
        localList.sort(null);
        for (var dragon: localList){

            System.out.println(dragon.toString().replaceAll("\n", "\n\t") + "\n");

        }
    }
}
