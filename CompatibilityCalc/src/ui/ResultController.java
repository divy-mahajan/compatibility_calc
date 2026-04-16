package ui;

import db.*;
import engine.*;
import model.*;

import javax.swing.*;
import java.util.*;

public class ResultController {

    private MainFrame frame;
    private String name1, name2;
    private Map<String, Integer> answers1, answers2;

    private static final String[][] TRAIT_CONFIG = {
            {"social",      "similar",       "1.0"},
            {"perceiving",  "similar",       "1.0"},
            {"empathy",     "high-combined", "1.5"},
            {"creativity",  "similar",       "1.0"},
            {"ambition",    "high-combined", "1.3"},
            {"adventure",   "similar",       "1.2"},
            {"conflict",    "complementary", "1.0"},
            {"learning",    "similar",       "1.0"},
            {"decision",    "similar",       "1.5"},
            {"schedule",    "similar",       "1.0"}
    };

    public ResultController(MainFrame frame, String name1, String name2,
                            Map<String, Integer> answers1,
                            Map<String, Integer> answers2) {
        this.frame = frame;
        this.name1 = name1;
        this.name2 = name2;
        this.answers1 = answers1;
        this.answers2 = answers2;
    }

    public void run() {

        SwingWorker<CompatibilityResult, Void> worker = new SwingWorker<>() {

            @Override
            protected CompatibilityResult doInBackground() {

              protected CompatibilityResult doInBackground() {

    // Create QB FIRST
    QuestionBank qb = new QuestionBank();

    //  Step 1: Traits
    List<Trait> traits1 = buildTraits(answers1, qb);
    List<Trait> traits2 = buildTraits(answers2, qb);

    // Step 2: Personality
    PersonalityEngine pe = new PersonalityEngine();

    String code1 = pe.deriveCode(answers1, qb);
    String code2 = pe.deriveCode(answers2, qb);

                String code1 = pe.deriveCode(answers1, qb);
                String code2 = pe.deriveCode(answers2, qb);

                // 🔹 Step 3: Persons
                Person person1 = new Person(name1);
                Person person2 = new Person(name2);

                traits1.forEach(person1::addTrait);
                traits2.forEach(person2::addTrait);

                person1.setPersonalityCode(code1);
                person2.setPersonalityCode(code2);

                // 🔹 Step 4: Compatibility
                CompatibilityEngine engine = new CompatibilityEngine();
                CompatibilityResult result = engine.calculate(person1, person2);

                // 🔹 Step 5–8: DB (SAFE MODE)
                try {
                    UserDAO userDAO = new UserDAO();
                    TraitDAO traitDAO = new TraitDAO();
                    ResponseDAO responseDAO = new ResponseDAO();
                    ResultDAO resultDAO = new ResultDAO();

                    int id1 = userDAO.insertUser(name1, code1);
                    int id2 = userDAO.insertUser(name2, code2);

                    if (id1 != -1 && id2 != -1) {
                        traitDAO.insertAll(id1, traits1);
                        traitDAO.insertAll(id2, traits2);

                        responseDAO.insertAll(id1, answers1);
                        responseDAO.insertAll(id2, answers2);

                        resultDAO.insertResult(id1, id2, result.getScore());
                    }

                } catch (Exception e) {
                    System.out.println("DB skipped: " + e.getMessage());
                }

                return result;
            }

            @Override
            protected void done() {
                try {
                    CompatibilityResult result = get();

                    ResultPanel resultPanel = new ResultPanel(frame, result);
                    frame.addAndShow(resultPanel, MainFrame.RESULT_PANEL);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            "Something went wrong: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private List<Trait> buildTraits(Map<String, Integer> answers, QuestionBank qb) {

    // Group answers by dimension
    Map<String, List<Integer>> dimValues = new HashMap<>();

    for (Question q : qb.getAll()) {
        if (answers.containsKey(q.getId())) {
            dimValues
                .computeIfAbsent(q.getDimension(), k -> new ArrayList<>())
                .add(answers.get(q.getId()));
        }
    }

    // Dimension → Trait mapping
    Map<String, String> dimToTrait = Map.of(
        "E_I", "social",
        "S_N", "perceiving",
        "F_T", "empathy",
        "P_J", "schedule"
    );

    List<Trait> traits = new ArrayList<>();

    for (String[] config : TRAIT_CONFIG) {

        String traitName = config[0];
        String mode = config[1];

        // Find corresponding dimension
        String dim = dimToTrait.entrySet().stream()
                .filter(e -> e.getValue().equals(traitName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        int value = 5; // default

        if (dim != null && dimValues.containsKey(dim)) {
            List<Integer> vals = dimValues.get(dim);

            value = (int) Math.round(
                    vals.stream().mapToInt(i -> i).average().orElse(5)
            );
        }

        traits.add(new Trait(traitName, value, mode));
    }

    return traits;
} {

        List<Trait> traits = new ArrayList<>();

        for (String[] config : TRAIT_CONFIG) {

            String traitName = config[0];
            String mode = config[1];

            int value = answers.getOrDefault(traitName, 5);

            traits.add(new Trait(traitName, value, mode));
        }

        return traits;
    }
}
