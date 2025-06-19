package exceptions;

public class EmptyRequestException extends CustomException {
    public EmptyRequestException() {}

    @Override
    public String toString(){
        return "Empty request was given.";
    }
}
