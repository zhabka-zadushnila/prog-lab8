package commands;

import java.util.Map;

import exceptions.NoSuchElementException;
import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;
import structs.User;
import structs.classes.Dragon;
import structs.wrappers.DragonDisplayWrapper;

/**
 * Used for updating element with specific id
 */
public class UpdateCommand extends BasicCommand {
    CommandManager commandManager;

    public UpdateCommand(CollectionManager collectionManager, CommandManager commandManager) {
        super("update", "update id {element} : обновить значение элемента коллекции, id которого равен заданному", collectionManager);
        this.commandManager = commandManager;
    }


    @Override
    public String execute(Object arguments, User user) throws NullArgsForbiddenException, NoSuchElementException {
        if (user == null) {
            return "please register to do such things";
        }

        DragonDisplayWrapper entry = (DragonDisplayWrapper) arguments;

        if (collectionManager.hasElement(entry.getKey())) {

            if (entry.getValue() == null) {
                return "Got null, nothing changes";
            }
            Dragon dragon = entry.getValue();
            if (!collectionManager.getDbManager().isDragonUsers(entry.getKey(), user.getLogin())) {
                return "you cannot change someones dragon";
            }
            if (collectionManager.getDbManager().updateDragon(dragon, entry.getKey(), user.getLogin()).isExpectedBehabiour()) {
                collectionManager.replaceElement(entry.getKey(), dragon);

                return "element " + entry.getKey() + " was replaced successfully";
            }
            return "some troubles with DB";
        } else {
            return new NoSuchElementException().toString();
        }
    }
}
