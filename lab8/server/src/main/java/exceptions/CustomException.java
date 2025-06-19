package exceptions;

import Interpreters.ServerCommandInterpreter;

/**
 * Class that is being caught by {@link ServerCommandInterpreter}, use it to create new exceptions for your commands
 */
public abstract class CustomException extends RuntimeException {
    public CustomException() {}

    /**
     * Any exception that is being caught during runtime will be displayed using toString().
     * @return String that explains main idea of exception
     */
    public String toString(){
        return "CustomException";
    }
}
