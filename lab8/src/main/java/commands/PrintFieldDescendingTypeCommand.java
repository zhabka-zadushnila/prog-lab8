package commands;

import classes.Dragon;
import managers.CollectionManager;
import utils.TypeComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Outputs id : type
 */

public class PrintFieldDescendingTypeCommand extends BasicCommand{
    public PrintFieldDescendingTypeCommand(CollectionManager collectionManager) {
        super("print_field_descending_type", "print_field_descending_type : вывести значения поля type всех элементов в порядке убывания", collectionManager);
    }

    /**
     * Uses default List sort({@link TypeComparator})
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public void execute(String[] args){
        List<Dragon> localList = new ArrayList<Dragon>(this.collectionManager.getCollection().values());
        localList.sort(new TypeComparator());
        for (var dragon: localList){
            System.out.println(dragon.getId() + " : " + dragon.getType().toString());

        }
    }
}
