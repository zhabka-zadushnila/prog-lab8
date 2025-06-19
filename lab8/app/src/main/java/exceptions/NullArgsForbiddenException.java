package exceptions;

/**
 * Exception that is usually thrown by {@link utils.InputChecker}.
 */
public class NullArgsForbiddenException extends CustomException {
    @Override
    public String toString(){
        return "Null obtained when command needs input!";
    }
}
