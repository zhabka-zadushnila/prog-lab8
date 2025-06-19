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
 * Replaces all the elements that are lower
 */
public class ReplaceIfLowerCommand extends BasicCommand {
    CommandManager commandManager;

    public ReplaceIfLowerCommand(CollectionManager collectionManager, CommandManager commandManager) {
        super("replace_if_lowe", "replace_if_lowe null {element} : заменить значение по ключу, если новое значение меньше старого", collectionManager);
        this.commandManager = commandManager;
    }


    @Override
    public String execute(Object arguments, User user) throws NullArgsForbiddenException, NoSuchElementException {
        if (user == null) {
            return "register before it plz";
        }
        if (arguments == null) {
            return "Something strange, seems like you've provided null";
        }
        DragonDisplayWrapper entry = (DragonDisplayWrapper) arguments;
        Dragon dragon = entry.getValue();
        if (dragon == null) {
            return "";
        }


        if (collectionManager.hasElement(entry.getKey())) {

            if (collectionManager.getElement(entry.getKey()).compareTo(dragon) < 0) {
                if (collectionManager.getDbManager().updateDragon(dragon, entry.getKey(), user.getLogin()).isExpectedBehabiour()) {
                    collectionManager.replaceElement(entry.getKey(), dragon);
                    return "Successfully replaced an element";
                }
            } else {
                return ("New dragon is not less than new one, nothing has changed :)");
            }
        } else {
            throw new NoSuchElementException();
        }
        return "";
    }
}
