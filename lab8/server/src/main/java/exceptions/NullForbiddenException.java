package exceptions;

public class NullForbiddenException extends CustomException {
    public NullForbiddenException() {
    }

    @Override
    public String toString(){
        return "Null obtained in non-null field!";
    }

}
