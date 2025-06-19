package exceptions;

/**
 * Exception that is being thrown when user tries to pick color that does not exist
 */
public class NoSuchColorException extends CustomException {

  @Override
  public String toString(){
    return "Seems like there is no such color";
  }
}
