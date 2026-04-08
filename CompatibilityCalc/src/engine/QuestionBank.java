package engine;

import java.util.*;

public class QuestionBank {
    private List<Question> questions;
    public QuestionBank(){
        questions= new ArrayList<>();
        loadQuestions();
    }
    private void loadQuestions() {
        questions.add(new Question("social1", "How do you feel in large social gatherings?",
                new String[]{"Energized", "Neutral", "Drained"},
                new int[]{8,5,2}, "E_I"));

        questions.add(new Question("social2", "Do you enjoy meeting new people?",
                new String[]{"Yes", "Sometimes", "No"},
                new int[]{8,5,2}, "E_I"));

        questions.add(new Question("social3", "Do you prefer spending weekends alone?",
                new String[]{"No", "Sometimes", "Yes"},
                new int[]{8,5,2}, "E_I"));

        questions.add(new Question("social4", "Do you speak up first in group discussions?",
                new String[]{"Often", "Sometimes", "Rarely"},
                new int[]{8,5,2}, "E_I"));

        questions.add(new Question("social5", "Do you feel drained after social interaction?",
                new String[]{"No", "Sometimes", "Yes"},
                new int[]{8,5,2}, "E_I"));

        questions.add(new Question("social6", "Do you prefer group activities over solo ones?",
                new String[]{"Yes", "Depends", "No"},
                new int[]{8,5,2}, "E_I"));
        questions.add(new Question("perceiving1", "Do you focus more on details or big ideas?",
                new String[]{"Details", "Both", "Big ideas"},
                new int[]{2,5,8}, "S_N"));

        questions.add(new Question("perceiving2", "Do you trust experience or imagination more?",
                new String[]{"Experience", "Both", "Imagination"},
                new int[]{2,5,8}, "S_N"));

        questions.add(new Question("perceiving3", "Do you prefer practical or creative solutions?",
                new String[]{"Practical", "Balanced", "Creative"},
                new int[]{2,5,8}, "S_N"));

        questions.add(new Question("perceiving4", "Do you enjoy abstract thinking?",
                new String[]{"No", "Sometimes", "Yes"},
                new int[]{2,5,8}, "S_N"));

        questions.add(new Question("perceiving5", "Do you rely on facts or possibilities?",
                new String[]{"Facts", "Both", "Possibilities"},
                new int[]{2,5,8}, "S_N"));

        questions.add(new Question("perceiving6", "Do you prefer routines or exploring new ideas?",
                new String[]{"Routine", "Both", "Explore"},
                new int[]{2,5,8}, "S_N"));
        questions.add(new Question("empathy1", "Do you make decisions based on emotions?",
                new String[]{"Yes", "Sometimes", "No"},
                new int[]{8,5,2}, "F_T"));

        questions.add(new Question("empathy2", "Do you prioritize logic over feelings?",
                new String[]{"No", "Sometimes", "Yes"},
                new int[]{8,5,2}, "F_T"));

        questions.add(new Question("empathy3", "Do you consider others' feelings when deciding?",
                new String[]{"Always", "Sometimes", "Rarely"},
                new int[]{8,5,2}, "F_T"));

        questions.add(new Question("empathy4", "Do you handle conflicts emotionally or logically?",
                new String[]{"Emotionally", "Both", "Logically"},
                new int[]{8,5,2}, "F_T"));

        questions.add(new Question("empathy5", "Do you value empathy over efficiency?",
                new String[]{"Yes", "Sometimes", "No"},
                new int[]{8,5,2}, "F_T"));

        questions.add(new Question("empathy6", "Do you follow your heart or mind?",
                new String[]{"Heart", "Both", "Mind"},
                new int[]{8,5,2}, "F_T"));
        questions.add(new Question("schedule1", "Do you prefer planning or spontaneity?",
                new String[]{"Planning", "Both", "Spontaneity"},
                new int[]{2,5,9}, "P_J"));

        questions.add(new Question("schedule2", "Do you like fixed schedules?",
                new String[]{"Yes", "Sometimes", "No"},
                new int[]{2,5,9}, "P_J"));

        questions.add(new Question("schedule3", "Do you finish tasks early or last minute?",
                new String[]{"Early", "Depends", "Last minute"},
                new int[]{2,5,9}, "P_J"));

        questions.add(new Question("schedule4", "Do you prefer structure or flexibility?",
                new String[]{"Structure", "Both", "Flexibility"},
                new int[]{2,5,9}, "P_J"));

        questions.add(new Question("schedule5", "Do you plan trips or go with the flow?",
                new String[]{"Plan", "Mix", "Flow"},
                new int[]{2,5,9}, "P_J"));

        questions.add(new Question("schedule6", "Do you like having deadlines?",
                new String[]{"Yes", "Neutral", "No"},
                new int[]{2,5,9}, "P_J"));

    }
    public List<Question> getAll(){
        return new ArrayList<>(questions);
    }
    public List<Question> getByDimension(String dimension) {
        List<Question> result = new ArrayList<>();

        for (Question q : questions) {
            if (q.getDimension().equals(dimension)) {
                result.add(q);
            }
        }

        return result;
    }
    public List<Question> getRandomQuestions(int count) {

        List<Question> selected = new ArrayList<>();
        String[] dimensions = {"E_I", "S_N", "F_T", "P_J"};

        int perDimension = count / dimensions.length;

        // Step 1: pick from each dimension
        for (String dim : dimensions) {

            List<Question> list = getByDimension(dim);
            Collections.shuffle(list);

            for (int i = 0; i < perDimension && i < list.size(); i++) {
                selected.add(list.get(i));
            }
        }

        // Step 2: fill remaining
        List<Question> remaining = new ArrayList<>(questions);
        remaining.removeAll(selected);
        Collections.shuffle(remaining);

        while (selected.size() < count && !remaining.isEmpty()) {
            selected.add(remaining.remove(0));
        }

        // Step 3: final shuffle
        Collections.shuffle(selected);

        return selected;
    }

}
