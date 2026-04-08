package model;
import java.util.ArrayList;
import java.util.List;


public class Person {
    private String name;
    private List<Trait> traits;
    private String personalityCode;

    public Person(String name){
        this.name=name;
        this.traits= new ArrayList<>();
    }
    public void addTrait(Trait trait) {
        traits.add(trait);
    }

    public String getName(){return name;}
    public String getPersonalityCode(){return personalityCode;}

    public List<Trait> getTraits() {
        return traits;
    }
    public void setPersonalityCode(String code){
        this.personalityCode=code;
    }
}
