package classes;

import exceptions.NoSuchColorException;

public enum Color {
    BLACK("Black"),
    YELLOW("Yellow"),
    ORANGE("Orange"),
    BROWN("Brown");

    private final String name;

    Color(String name){
        this.name = name;
    }
    public String toString(){
        return this.name;
    }



}