import commands.local.ExecuteScriptCommand;
import commands.local.ExitCommand;
import commands.local.HelpCommand;
import commands.server.*;
import gui.AppStarter;
import managers.ClientManager;
import managers.CollectionManager;
import managers.CommandManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

class Main {
    public static void main(String[] args) {
        CollectionManager collectionManager;
        if (args.length > 0) {
            collectionManager = new CollectionManager(args[0]);
        } else {
            collectionManager = new CollectionManager();
        }
        CommandManager commandManager = new CommandManager(collectionManager);

        commandManager.addLocalCommand(new HelpCommand(commandManager));
        commandManager.addServerCommand(new InfoCommand(collectionManager));
        commandManager.addServerCommand(new ShowCommand(collectionManager));
        commandManager.addServerCommand(new InsertCommand(collectionManager, commandManager));
        commandManager.addServerCommand(new UpdateCommand(collectionManager, commandManager));
        commandManager.addServerCommand(new RemoveKeyCommand(collectionManager));
        commandManager.addServerCommand(new ClearCommand(collectionManager));
        commandManager.addLocalCommand(new ExecuteScriptCommand(commandManager));
        commandManager.addLocalCommand(new ExitCommand());
        commandManager.addServerCommand(new RemoveGreaterCommand(collectionManager));
        commandManager.addServerCommand(new ReplaceIfLowerCommand(collectionManager, commandManager));
        commandManager.addServerCommand(new RemoveGreaterKeyCommand(collectionManager));
        commandManager.addServerCommand(new CountLessThanCharacterCommand(collectionManager));
        commandManager.addServerCommand(new PrintAscendingCommand(collectionManager));
        commandManager.addServerCommand(new PrintFieldDescendingTypeCommand(collectionManager));
        commandManager.addServerCommand(new RegisterCommand(commandManager));
        commandManager.addServerCommand(new LoginCommand(commandManager));

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Iterator<String> it = in.lines().iterator();

        ClientManager clientManager = new ClientManager(it, "localhost", 52946, commandManager);

        AppStarter.main(args);

    /*
        "  - help : вывести справку по доступным командам\n" +
        "  - info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
        "  - show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
        "  - insert null {element} : добавить новый элемент с заданным ключом\n" +
        "  - update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
        "  - remove_key null : удалить элемент из коллекции по его ключу\n" +
        "  - clear : очистить коллекцию\n" +
        "  - save : сохранить коллекцию в файл\n" +
        "  - execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
        "  - exit : завершить программу (без сохранения в файл)\n" +
        "  - remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
        "  - replace_if_lowe null {element} : заменить значение по ключу, если новое значение меньше старого\n" +
        "  - remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный\n" +
        "  - count_less_than_character character : вывести количество элементов, значение поля character которых меньше заданного\n" +
        "  - print_ascending : вывести элементы коллекции в порядке возрастания\n" +
        "  - print_field_descending_type : вывести значения поля type всех элементов в порядке убывания";
    */


    }
}
