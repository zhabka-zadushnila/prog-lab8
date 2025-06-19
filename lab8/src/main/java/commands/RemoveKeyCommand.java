package commands;

import exceptions.NoSuchElementException;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;


/**
 * Removes element by key. That's all
 */
public class RemoveKeyCommand extends BasicCommand{
    public RemoveKeyCommand(CollectionManager collectionManager){
        super("remove_key", "remove_key null : удалить элемент из коллекции по его ключу", collectionManager);
    }

    /**
     * Deletes it via {@link managers.CommandManager}
     * @param args an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException doesn't like when input is blank.
     * @throws NoSuchElementException thrown when no such element was found in collection.
     */
    @Override
    public void execute(String[] args) throws NullArgsForbiddenException, NoSuchElementException {
        int deleted = 0;
        if (args.length == 0) {
            throw new NullArgsForbiddenException();
        }

        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        String masterId = args[0];

        if(collectionManager.hasElement(masterId)){
            collectionManager.killElement(masterId);
            System.out.println("Элемент " + masterId + " удалён успешно.");
        }else{
            throw new NoSuchElementException();
        }
    }

}
