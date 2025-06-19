package commands;

import java.util.Map;

import exceptions.NullArgsForbiddenException;
import managers.CollectionManager;
import managers.CommandManager;
import structs.SQLAnswer;
import structs.User;
import structs.classes.Dragon;
import structs.wrappers.DragonDisplayWrapper;

/**
 * Command that is used for adding some stuff in collection. Operates with both {@link CommandManager} and {@link CollectionManager}.
 */

public class InsertCommand extends BasicCommand {
    CommandManager commandManager;

    public InsertCommand(CollectionManager collectionManager, CommandManager commandManager) {
        super("insert", "insert null {element} : добавить новый элемент с заданным ключом", collectionManager);
        this.commandManager = commandManager;
    }

    @Override
    public String execute(Object arguments, User user) throws NullArgsForbiddenException {
        if (user == null) {
            return "you are not registered yet";
        }

        if (arguments == null) {
            return "Something strange, seems like you've provided null";
        }

        DragonDisplayWrapper entry = (DragonDisplayWrapper) arguments;
        Dragon dragon = entry.getValue();
        if (entry.getValue() == null) {
            return "";
        }
        if (collectionManager.hasElement(entry.getKey())) {
            return "such element already exists, nothing changed";
        } else {
            SQLAnswer sqlAnswer = collectionManager.getDbManager().addDragon(entry.getKey(), dragon, user.getLogin());
            if (sqlAnswer.isExpectedBehabiour()) {
                collectionManager.addElement(entry.getKey(), entry.getValue());
            } else {
                return sqlAnswer.getDescription();
            }
        }

        return "element added";
    }


}
