package exceptions;

public class ConnectionInterruptedException extends CustomException {
    @Override
    public String toString() {
        return "Соединение было прервано в процессе передачи информации.";
    }
}
