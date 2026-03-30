package model;

public class Trait{
    private String name;
    private int value;
    private String comparisonMode;

    public Trait(String name, int value, String comparisonMode) {
        this.name = name;
        this.value = value;
        this.comparisonMode = comparisonMode;
    }

    public String getName(){
        return name;
    }
    public int getValue() {
        return value;
    }
    public String getComparisonMode(){
        return comparisonMode;
    }
}