package exceptions;

/**
 * Exception that is being thrown when element was not found in collection
 */
public class NoSuchElementException extends CustomException {
    @Override
    public String toString(){
        return "No such element in collection";
    }
}
