package exceptions;


/**
 * Exception that is used to notify user when recursion occurs
 */
public class InfiniteFileRecursion extends CustomException {
    @Override
    public String toString(){
        return "Seems like you are trying to create a file loop. Bruh, for what?";
    }
}
