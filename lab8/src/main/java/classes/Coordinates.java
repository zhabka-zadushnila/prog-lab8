package classes;


/**
 * Dragon's coordinates
 */
public class Coordinates {
    public double x;
    public Long y; //Максимальное значение поля: 984, Поле не может быть null

    public Coordinates(){
    }
    public Coordinates(double x, Long y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return "(x: " + x + "; y: " + y + ")";
    }
}