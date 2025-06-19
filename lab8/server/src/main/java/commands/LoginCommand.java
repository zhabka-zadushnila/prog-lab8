package commands;

import exceptions.NullArgsForbiddenException;
import managers.DBManager;
import structs.User;

public class LoginCommand extends BasicCommand {
    DBManager dbManager;

    public LoginCommand(DBManager dbManager) {
        super("login", "basically command to log u in");
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arguments, User user) throws NullArgsForbiddenException {
        if (dbManager.checkUserExists(user.getLogin())) {
            if (dbManager.validateUser(user.getLogin(), user.getPassword())) {
                return "nice, u r in!";
            }
            return "wrong password";
        }
        return "no such user";
    }

}
