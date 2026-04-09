package engine;
import java.util.*;

public class PersonalityEngine {

    public String deriveCode(Map<String, Integer> answers, QuestionBank qb) {

        // Map to store dimension-wise values
        Map<String, List<Integer>> dimensionMap = new HashMap<>();

        // Initialize dimensions
        String[] dimensions = {"E_I", "S_N", "F_T", "P_J"};
        for (String dim : dimensions) {
            dimensionMap.put(dim, new ArrayList<>());
        }

        // Fill values based on question dimension
        for (Question q : qb.getAll()) {

            if (answers.containsKey(q.getId())) {
                String dim = q.getDimension();
                dimensionMap.get(dim).add(answers.get(q.getId()));
            }
        }

        // Build personality code
        StringBuilder code = new StringBuilder();

        // E vs I
        code.append(getLetter(dimensionMap.get("E_I"), 'E', 'I'));

        // S vs N
        code.append(getLetter(dimensionMap.get("S_N"), 'N', 'S'));

        // F vs T
        code.append(getLetter(dimensionMap.get("F_T"), 'F', 'T'));

        // P vs J
        code.append(getLetter(dimensionMap.get("P_J"), 'P', 'J'));

        return code.toString();
    }

    private char getLetter(List<Integer> values, char high, char low) {

        if (values.isEmpty()) return low; // fallback

        double avg = values.stream().mapToInt(i -> i).average().orElse(0);

        return (avg >= 5) ? high : low;
    }
    public String getArchetype(String code) {

        switch (code) {
            case "ENFP": return "The Campaigner";
            case "ENFJ": return "The Protagonist";
            case "ENTP": return "The Debater";
            case "ENTJ": return "The Commander";

            case "INFP": return "The Mediator";
            case "INFJ": return "The Advocate";
            case "INTP": return "The Thinker";
            case "INTJ": return "The Architect";

            case "ESFP": return "The Entertainer";
            case "ESFJ": return "The Consul";
            case "ESTP": return "The Entrepreneur";
            case "ESTJ": return "The Executive";

            case "ISFP": return "The Adventurer";
            case "ISFJ": return "The Defender";
            case "ISTP": return "The Virtuoso";
            case "ISTJ": return "The Logistician";

            default: return "Unknown Type";
        }
    }
}
