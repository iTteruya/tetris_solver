import com.mygdx.tetslv.model.GameField;
import com.mygdx.tetslv.model.Piece;
import com.mygdx.tetslv.model.Strategist;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AlgorithmTest {

    @Test
    public void getBetterScore() {
        ArrayList<Integer> scores1 = new ArrayList<>();
        ArrayList<Integer> scores2 = new ArrayList<>();
        scores1.add(5);
        scores1.add(4);
        scores1.add(-1);
        scores2.add(5);
        scores2.add(-5);
        scores2.add(1);
        Strategist s1 = new Strategist(scores1);
        Strategist s2 = new Strategist(scores2);
        Strategist result = s1.getBetter(s2);
        Assert.assertEquals(s1.getScoreList(), result.getScoreList());
    }

    @Test
    public void highterMarkTest(){
        int[][] ogField = new int[][] {
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}

        };
        int[][] newField = new int[][] {
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Assert.assertEquals(expected(0, 0, -1), actual(ogField, newField));
        newField = new int[][] {
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Assert.assertEquals(expected(0, 1, 0), actual(ogField, newField));
        newField = new int[][] {
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Assert.assertEquals(expected(0, 0, 0), actual(ogField, newField));
        newField = new int[][] {
                new int[] {1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Assert.assertEquals(expected(-2, -2, 0), actual(ogField, newField));
    }

    private ArrayList<Integer> expected(int score1, int score2, int score3){
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(score1);
        expected.add(score2);
        expected.add(score3);
        return expected;
    }
    private ArrayList<Integer> actual(int[][] ogField, int[][] newField) {
        GameField oldGameField = new GameField(ogField);
        GameField newGameField = new GameField(newField);
        Piece piece = new Piece(30, 0);
        Strategist s = new Strategist(oldGameField, newGameField, piece);
        return s.getScoreList();
    }

}
