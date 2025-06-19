package exceptions;


/**
 * Exception that you can use to remind user to use specific range of numbers
 */
public class RangeExceededException extends CustomException {//checked/unchecked
    Double min;
    Double max;

    /**
     * You have to set borders in constructor of this exception. If you need only one-side restriction, put null for +-inf.
     * @param min Double (min num in range)
     * @param max Double (max num in range)
     */
    public RangeExceededException(Double min, Double max) {
        this.max = max;
        this.min = min;
    }

    @Override
    public String toString() {
        if(min == null){
            return "Expected range is less than " + max;
        }
        if(max == null){
            return "Expected range is bigger than " + min;
        }
        return "Expected range is a number between " + min + " and " + max;
    }
}
