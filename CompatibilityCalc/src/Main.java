import engine.*;
import model.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        // 🔹 Step 1: Load questions
        QuestionBank qb = new QuestionBank();
        List<Question> questions = qb.getRandomQuestions(10);

        // 🔹 Step 2: Simulate answers (random for testing)
        Map<String, Integer> answers1 = new HashMap<>();
        Map<String, Integer> answers2 = new HashMap<>();

        Random rand = new Random();

        for (Question q : questions) {

            int optionIndex1 = rand.nextInt(q.getOptions().length);
            int optionIndex2 = rand.nextInt(q.getOptions().length);

            int value1 = q.getValueForOption(optionIndex1);
            int value2 = q.getValueForOption(optionIndex2);

            answers1.put(q.getDimension(), value1);
            answers2.put(q.getDimension(), value2);
        }

        // 🔹 Step 3: Build traits
        List<Trait> traits1 = buildTraits(answers1);
        List<Trait> traits2 = buildTraits(answers2);

        // 🔹 Step 4: Personality Engine
        PersonalityEngine pe = new PersonalityEngine();

        String code1 = pe.deriveCode(answers1, qb);
        String code2 = pe.deriveCode(answers2, qb);

        System.out.println("Person 1 Code: " + code1);
        System.out.println("Person 2 Code: " + code2);

        System.out.println("Archetype 1: " + pe.getArchetype(code1));
        System.out.println("Archetype 2: " + pe.getArchetype(code2));

        // 🔹 Step 5: Create Persons
        Person p1 = new Person("Alice");
        Person p2 = new Person("Bob");

        traits1.forEach(p1::addTrait);
        traits2.forEach(p2::addTrait);

        p1.setPersonalityCode(code1);
        p2.setPersonalityCode(code2);

        // 🔹 Step 6: Compatibility Engine
        CompatibilityEngine ce = new CompatibilityEngine();
        CompatibilityResult result = ce.calculate(p1, p2);

        // 🔹 Step 7: Print Result
        result.printResult();
    }

    // 🔹 Helper method to build traits
    private static List<Trait> buildTraits(Map<String, Integer> answers) {

        String[][] TRAIT_CONFIG = {
                {"social", "similar"},
                {"perceiving", "similar"},
                {"empathy", "high-combined"},
                {"creativity", "similar"},
                {"ambition", "high-combined"},
                {"adventure", "similar"},
                {"conflict", "complementary"},
                {"learning", "similar"},
                {"decision", "similar"},
                {"schedule", "similar"}
        };

        List<Trait> traits = new ArrayList<>();

        for (String[] config : TRAIT_CONFIG) {

            String name = config[0];
            String mode = config[1];

            int value = answers.getOrDefault(name, 5);

            traits.add(new Trait(name, value, mode));
        }

        return traits;
    }
}