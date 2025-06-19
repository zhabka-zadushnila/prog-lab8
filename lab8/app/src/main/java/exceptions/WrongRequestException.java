package exceptions;

public class WrongRequestException extends CustomException {
    public WrongRequestException() {}

    @Override
    public String toString() {
        return "Seems like wrong request provided";
    }
}
