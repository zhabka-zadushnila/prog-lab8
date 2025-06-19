package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Outputs id : type
 */

public class PrintFieldDescendingTypeCommand extends BasicCommand {
    public PrintFieldDescendingTypeCommand(CollectionManager collectionManager) {
        super("print_field_descending_type", "print_field_descending_type : вывести значения поля type всех элементов в порядке убывания", collectionManager);
    }


}
