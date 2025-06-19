package commands.server;

import commands.BasicCommand;
import managers.CollectionManager;

/**
 * Outputs amount of elements in which character field contains value less than given
 */
public class CountLessThanCharacterCommand extends BasicCommand {
    public CountLessThanCharacterCommand(CollectionManager collectionManager){
        super("count_less_than_character", "count_less_than_character character : вывести количество элементов, значение поля character которых меньше заданного", collectionManager);
        }


}
