package commands;


import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.FileManager;

/**
 * Used to save something to file.
 */
public class SaveCommand extends BasicCommand{
    public SaveCommand(CollectionManager collectionManager){
        super("save", "save : сохранить коллекцию в файл", collectionManager);
    }


    /**
     * fun fact - all the errors are getting catched in {@link FileManager}.
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     * @throws NullArgsForbiddenException thrown when no args were provided.
     */
    @Override
    public void execute(String[] args) throws NullArgsForbiddenException {

        if (args.length == 0) {
            throw new NullArgsForbiddenException();
        }

        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        FileManager.saveCollectionToFile(args[0], collectionManager.getCollection());

    }


}
