import db.DBConnection;
import engine.Question;
import engine.QuestionBank;
import model.Trait;

import java.sql.Connection;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✓ Connection successful — DB is live.");
        } else {
            System.out.println("✗ Connection failed — check errors above.");
        }
        Trait t = new Trait("social", 8, "similar");
        System.out.println(t.getName());
        System.out.println(t.getValue());
        System.out.println(t.getComparisonMode());

        QuestionBank qb = new QuestionBank();
        List<Question> qList = qb.getRandomQuestions(10);

        for (Question q : qList) {
            System.out.println(q.getId() + " - " + q.getDimension());
        }

        DBConnection.closeConnection();
    }
}
