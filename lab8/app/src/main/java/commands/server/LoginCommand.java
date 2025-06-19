package commands.server;

import commands.BasicCommand;
import exceptions.NullForbiddenException;
import managers.CommandManager;
import structs.User;
import utils.InputChecker;

import java.io.Console;
import java.io.IOError;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LoginCommand extends BasicCommand {
    CommandManager commandManager;

    public LoginCommand(CommandManager commandManager) {
        super("login", "login: Буквально команда которая тебя залогинит");
        this.commandManager = commandManager;
    }

    @Override
    public Object execute(String[] args) {
        String login = null;
        String password = null;
        String tmpInput;
        Iterator<String> it = commandManager.getClientManager().getInputIterator();
        System.out.println("Your login:");

        while (login == null) {
            try {
                tmpInput = it.next();
            } catch (NoSuchElementException e) {
                System.out.println("Login process was interrupted, nothing was added");
                return null;
            }
            try {
                login = InputChecker.inputNonNullChecker(tmpInput);
            } catch (NullForbiddenException e) {
                System.out.println(e);
            }

        }
        System.out.println("Your password:");
        Console console = System.console();
        char[] passwordArray;
        while (password == null) {
            try {
                passwordArray = console.readPassword();
            } catch (IOError e) {
                System.out.println("Login process was interrupted, nothing was added");
                return null;
            }
            try {
                password = InputChecker.inputNonNullChecker(new String(passwordArray));
            } catch (NullForbiddenException e) {
                System.out.println(e);
            }

        }
        User user = new User(login, password);
        this.commandManager.getClientManager().setUser(user);
        return user;

    }
}
