import db.DBConnection;
import engine.*;
import model.Trait;
import model.Person;
import engine.CompatibilityEngine;
import engine.CompatibilityResult;

import java.sql.Connection;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // 🔹 Step 1: DB Connection
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✓ Connection successful — DB is live.");
        } else {
            System.out.println("✗ Connection failed — check errors above.");
        }

        System.out.println("\n--- Trait Test ---");
        Trait t = new Trait("social", 8, "similar");
        System.out.println(t.getName());
        System.out.println(t.getValue());
        System.out.println(t.getComparisonMode());

        // 🔹 Step 2: QuestionBank
        QuestionBank qb = new QuestionBank();
        List<Question> qList = qb.getRandomQuestions(10);

        // 🔹 Step 3: Simulate Answers
        Map<String, Integer> answers = new HashMap<>();
        Random rand = new Random();

        System.out.println("\n--- Questions & Answers ---");

        for (Question q : qList) {
            System.out.println(q.getId() + ": " + q.getText());

            int choice = rand.nextInt(3); // 0,1,2
            int value = q.getValueForOption(choice);

            answers.put(q.getId(), value);

            System.out.println("Chosen: " + q.getOptions()[choice] + " (" + value + ")");
            System.out.println();
        }

        // 🔹 Step 4: Personality Engine
        PersonalityEngine pe = new PersonalityEngine();
        String code = pe.deriveCode(answers, qb);


        Person p1 = new Person("Alice");
        Person p2 = new Person("Bob");


        p1.addTrait(new Trait("social", 8, "similar"));
        p2.addTrait(new Trait("social", 6, "similar"));

        p1.addTrait(new Trait("empathy", 7, "high-combined"));
        p2.addTrait(new Trait("empathy", 9, "high-combined"));

        CompatibilityEngine ce = new CompatibilityEngine();
        CompatibilityResult result = ce.calculate(p1, p2);

// Print
        result.printResult();
        DBConnection.closeConnection();
    }
}