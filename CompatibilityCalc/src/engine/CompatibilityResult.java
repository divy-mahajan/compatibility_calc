package engine;

import model.Person;
import model.Trait;

import java.util.List;

public class CompatibilityResult {
    private Person p1;
    private Person p2;
    private int score;
    private int[] dimensionScores;
    CompatibilityResult(Person p1,Person p2,int score,int[] dimensionScores){
        this.p1=p1;
        this.p2=p2;
        this.score=score;
        this.dimensionScores=dimensionScores;
    }
    public String getLabel(){
        if (score <= 39) {
            return "Challenging pairing";
        } else if (score <= 54) {
            return "Some friction";
        } else if (score <= 69) {
            return "Moderate compatibility";
        } else if (score <= 84) {
            return "Strong compatibility";
        } else {
            return "Excellent match";
        }
    }
    public String getInsight(){
        String n1 = p1.getName();
        String n2 = p2.getName();

        if (score <= 39) {
            return n1 + " and " + n2 + " may struggle due to very different instincts. Strong effort is needed.";
        } else if (score <= 54) {
            return n1 + " and " + n2 + " have noticeable differences, but can improve with patience.";
        } else if (score <= 69) {
            return n1 + " and " + n2 + " share some common ground. Communication will strengthen compatibility.";
        } else if (score <= 84) {
            return n1 + " and " + n2 + " have strong compatibility with productive differences.";
        } else {
            return n1 + " and " + n2 + " are an excellent match with natural alignment.";
        }
    }
    public void printResult() {

        System.out.println("\n===== Compatibility Result =====");

        System.out.println("Person 1: " + p1.getName());
        System.out.println("Person 2: " + p2.getName());

        System.out.println("\nScore: " + score + "/100");
        System.out.println("Label: " + getLabel());
        System.out.println("Insight: " + getInsight());

        // Trait + dimension score
        System.out.println("\n--- Trait Breakdown ---");

        List<Trait> t1 = p1.getTraits();

        for (int i = 0; i < t1.size(); i++) {
            System.out.println(
                    t1.get(i).getName() + " → Score: " + dimensionScores[i]
            );
        }

        System.out.println("================================\n");
    }

}
