package classes;

/**
 * Dragon's cave
 */
public class DragonCave {
    public int depth;
    public double numberOfTreasures; //Значение поля должно быть больше 0

    public DragonCave(){
    }

    public DragonCave(int depth, double numberOfTreasures) {
        this.depth = depth;
        this.numberOfTreasures = numberOfTreasures;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double getNumberOfTreasures() {
        return numberOfTreasures;
    }

    public void setNumberOfTreasures(double numberOfTreasures) {
        this.numberOfTreasures = numberOfTreasures;
    }

    @Override
    public String toString(){
        return "Depth: " + depth + "; Number of treasures: " + numberOfTreasures;
    }
}