package commands;

import managers.CommandManager;
import managers.DBManager;
import structs.SQLAnswer;
import structs.User;

public class RegisterCommand extends BasicCommand {
    CommandManager commandManager;
    DBManager dbManager;

    public RegisterCommand(CommandManager commandManager, DBManager dbManager) {
        super("register", "register: Буквально команда которая тебя зарегистрирует, лол");
        this.commandManager = commandManager;
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arguments, User user) {
        if (user == null) {
            return "Something strange, seems like you've provided null";
        }
        SQLAnswer sqlAnswer = dbManager.registerUser(user.getLogin(), user.getPassword());
        if (sqlAnswer.isExpectedBehabiour()) {
            return "User registered successfully";
        }
        return sqlAnswer.getDescription();

    }
}
