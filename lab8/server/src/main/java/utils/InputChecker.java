package utils;

import exceptions.*;
import exceptions.RangeExceededException;

import java.math.BigDecimal;

/**
 * Class that is used just to check user's input
 */
public class InputChecker {
    /**
     * Checks if string is blank
     * @param input String
     * @return String if it is fine
     * @throws NullForbiddenException if String is not fine.
     */
    static public String inputNonNullChecker(String input) throws NullForbiddenException {
        if(input.trim().isBlank()){
            throw new NullForbiddenException();
        }
        else{
            return input;
        }
    }

    /**
     * MAGNUM OPUS <br>
     * MEME ITSELF <br>
     * PIECE OF ART (no)
     * @param input String
     * @param min min value (might be null for <em>-inf</em>)
     * @param max max value (might be null for <i>+inf</i>)
     * @param nonNull boolean parameter to check number for null
     * @param returnType Class which has to be returned
     * @return returns object of class according to returnType
     * @throws NullForbiddenException cause null is bad
     * @throws RangeExceededException cause getting out of limits is halal
     */
    static public <T extends Number> T inputRangeChecker(String input, Double min, Double max, boolean nonNull, Class<T> returnType) throws NullForbiddenException, RangeExceededException {
        if(nonNull){
            if(input.trim().isBlank()){throw new NullForbiddenException();}
        }

        if(input.trim().isBlank()){return null;}

        BigDecimal inputValue = new BigDecimal(input);

        if (min != null && inputValue.compareTo(new BigDecimal(min)) < 0) {
            throw new RangeExceededException(min, max);
        }

        if (max != null && inputValue.compareTo(new BigDecimal(max)) > 0) {
            throw new RangeExceededException(min, max);
        }

        if(returnType == Integer.class){
            return returnType.cast(inputValue.intValue());
        }
        if(returnType == Double.class){
            return returnType.cast(inputValue.doubleValue());
        }
        if(returnType == Float.class){
            return returnType.cast(inputValue.floatValue());
        }
        if(returnType == Long.class){
            return returnType.cast(inputValue.longValue());
        }
        return null;
    }

    /**
     * Method that is used to pick enum, works in any way
     * @param input String
     * @param enumClass Generic. Put here Enum that you want
     * @return returns enum if it was picked correctly
     */
    public static <E extends Enum<E>> E getEnum(String input, Class<E> enumClass) {
        try {
            return Enum.valueOf(enumClass, input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException();
        }
    }
}
