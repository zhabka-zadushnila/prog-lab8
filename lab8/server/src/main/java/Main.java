import Interpreters.ServerCommandInterpreter;
import commands.*;
import managers.CollectionManager;
import managers.CommandManager;
import managers.ConnectionManager;
import managers.DBManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        int port = 52947;

        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        try {
            LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        DBManager dbm = new DBManager("localhost", 5432);

        CollectionManager collectionManager;
        if (args.length > 0) {
            collectionManager = new CollectionManager(args[0]);
        } else {
            collectionManager = new CollectionManager(dbm);
        }


        CommandManager commandManager = new CommandManager(collectionManager);

        commandManager.addCommand(new InfoCommand(collectionManager));
        commandManager.addCommand(new ShowCommand(collectionManager));
        commandManager.addCommand(new InsertCommand(collectionManager, commandManager));
        commandManager.addCommand(new UpdateCommand(collectionManager, commandManager));
        commandManager.addCommand(new RemoveKeyCommand(collectionManager));
        commandManager.addCommand(new ClearCommand(collectionManager));
        commandManager.addCommand(new SaveCommand(collectionManager));
        commandManager.addCommand(new ExitCommand());
        commandManager.addCommand(new RemoveGreaterCommand(collectionManager));
        commandManager.addCommand(new ReplaceIfLowerCommand(collectionManager, commandManager));
        commandManager.addCommand(new RemoveGreaterKeyCommand(collectionManager));
        commandManager.addCommand(new CountLessThanCharacterCommand(collectionManager));
        commandManager.addCommand(new PrintAscendingCommand(collectionManager));
        commandManager.addCommand(new PrintFieldDescendingTypeCommand(collectionManager));
        commandManager.addCommand(new RegisterCommand(commandManager, dbm));
        commandManager.addCommand(new LoginCommand(dbm));

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Iterator<String> it = in.lines().iterator();


        //dbm.registerUser("anon", "1234");
        //dbm.addDragon(new Dragon("Alex", new Coordinates(12.0, 12L), 12, Color.BLACK, DragonType.FIRE, DragonCharacter.CHAOTIC_EVIL), "anon");


        ServerCommandInterpreter serverCommandInterpreter = new ServerCommandInterpreter(commandManager, it);
        logger.info("CommandInterpreter started");
        ConnectionManager connectionManager = new ConnectionManager(port, serverCommandInterpreter);
        logger.info("Connection manager started with port: " + port);
        connectionManager.serve();


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
