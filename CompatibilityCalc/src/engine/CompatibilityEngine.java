package engine;

import model.Person;
import model.Trait;

import java.util.*;

public class CompatibilityEngine {

    // 🔹 Main method
    public int calculate(Person p1, Person p2) {

        List<Trait> t1 = p1.getTraits();
        List<Trait> t2 = p2.getTraits();

        double totalScore = 0;
        double maxScore = 0;

        for (int i = 0; i < t1.size(); i++) {

            Trait trait1 = t1.get(i);
            Trait trait2 = t2.get(i);

            double score = calculateDimensionScore(trait1, trait2);
            double weight = getWeight(trait1.getName());

            totalScore += score * weight;
            maxScore += 10 * weight;
        }

        // 🔹 Creativity bonus
        int creativityDiff = Math.abs(
                getTraitValue(t1, "creativity") -
                        getTraitValue(t2, "creativity")
        );

        int bonus = (creativityDiff <= 2) ? 5 : 0;

        // 🔹 Final score
        int finalScore = (int) Math.round((totalScore / maxScore) * 95 + bonus);

        return Math.min(100, finalScore);
    }

    // 🔹 Dimension score
    private double calculateDimensionScore(Trait t1, Trait t2) {

        int v1 = t1.getValue();
        int v2 = t2.getValue();
        String mode = t1.getComparisonMode();

        switch (mode) {

            case "similar":
                return Math.max(0, 10 - Math.abs(v1 - v2));

            case "complementary":
                return Math.min(10, Math.abs(v1 - v2) + 2);

            case "high-combined":
                return Math.min(10, (v1 + v2) / 2.0);

            default:
                return 0;
        }
    }

    // 🔹 Trait weights
    private double getWeight(String name) {

        switch (name) {
            case "empathy":
            case "decision":
                return 1.5;

            case "ambition":
                return 1.3;

            case "adventure":
                return 1.2;

            default:
                return 1.0;
        }
    }

    // 🔹 Helper to find trait value by name
    private int getTraitValue(List<Trait> traits, String name) {

        for (Trait t : traits) {
            if (t.getName().equals(name)) {
                return t.getValue();
            }
        }
        return 0;
    }
}