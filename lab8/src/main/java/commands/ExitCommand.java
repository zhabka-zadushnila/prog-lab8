package commands;

/**
 * Some sort of wrapper for System.exit(0).
 */

public class ExitCommand extends BasicCommand{

    public ExitCommand(){
        super("exit", "exit : завершить программу (без сохранения в файл)");
    }

    /**
     * {@link System} check out an exit method
     * @param args - an array of strings (words that were separated by spaces). Usually it is ignored in commands that do not need any args, and those who need, get only as much as they need (others are being ignored)
     */
    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}