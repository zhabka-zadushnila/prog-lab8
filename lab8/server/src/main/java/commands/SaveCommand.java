package commands;


import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.FileManager;
import structs.User;

/**
 * Used to save something to file.
 */
public class SaveCommand extends BasicCommand {
    public SaveCommand(CollectionManager collectionManager) {
        super("save", "save : сохранить коллекцию в файл", collectionManager);
    }


    @Override
    public String execute(Object arguments, User user) throws NullArgsForbiddenException {
        String[] args = (String[]) arguments;

        if (args.length == 0) {
            throw new NullArgsForbiddenException();
        }

        if (args[0].trim().isBlank()) {
            throw new NullArgsForbiddenException();
        }

        FileManager.saveCollectionToFile(args[0], collectionManager.getCollection());
        return "saved";
    }


}
