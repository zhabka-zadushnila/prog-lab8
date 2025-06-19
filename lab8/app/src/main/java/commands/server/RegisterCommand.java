package commands.server;

import commands.BasicCommand;
import exceptions.NullForbiddenException;
import managers.CommandManager;
import structs.User;
import utils.InputChecker;

import java.io.IOError;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RegisterCommand extends BasicCommand {
    CommandManager commandManager;

    public RegisterCommand(CommandManager commandManager) {
        super("register", "register: Буквально команда которая тебя зарегистрирует, лол");
        this.commandManager = commandManager;
    }

    @Override
    public Object execute(String[] args) {
        User user = commandManager.getClientManager().getUser();
        if (user != null) {
            System.out.println("U r registered already, bruh");
            return null;
        }

        String login = null;
        String password = null;
        String tmpInput;
        Iterator<String> it = commandManager.getClientManager().getInputIterator();
        System.out.println("Your login:");

        while (login == null) {
            try {
                tmpInput = it.next();
            } catch (NoSuchElementException e) {
                System.out.println("Registration process was interrupted, nothing was added");
                return null;
            }
            try {
                login = InputChecker.inputNonNullChecker(tmpInput);
            } catch (NullForbiddenException e) {
                System.out.println(e);
            }

        }
        System.out.println("Your password:");
        while (password == null) {
            try {
                tmpInput = it.next();
            } catch (IOError e) {
                System.out.println("Registration process was interrupted, nothing was added");
                return null;
            }
            try {
                password = InputChecker.inputNonNullChecker(tmpInput);
            } catch (NullForbiddenException e) {
                System.out.println(e);
            }

        }
        user = new User(login, password);
        this.commandManager.getClientManager().setUser(user);
        return user;

    }
}
