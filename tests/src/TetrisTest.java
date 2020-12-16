import com.mygdx.tetslv.model.GameField;
import com.mygdx.tetslv.model.Piece;
import org.junit.Assert;
import org.junit.Test;

public class TetrisTest {

    @Test
    public void testClearLine() {
        int[][] fieldMatrix = new int[][] {
                new int[] {1, 1, 1, 1, 1, 1, 1, 0, 1, 1},
                new int[] {1, 1, 1, 1, 1, 1, 1, 0, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        GameField gameField = new GameField(fieldMatrix);
        Piece piece = new Piece(0, 7, Piece.Type.I);
        gameField.update(piece);
        gameField.clearLine(false);
        gameField.clearLine(false);
        int[][] expected = new int[][] {
                new int[] {0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                new int[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        Assert.assertArrayEquals(expected, gameField.getMatrix());
    }

    @Test
    public void testRotation() {
        Piece piece = new Piece(0, 0, Piece.Type.S);
        piece.rotate();
        int[][] expected = new int[][] {
                new int[]{1, 0, 0, 0},
                new int[]{1, 1, 0, 0},
                new int[]{0, 1, 0, 0},
                new int[]{0, 0, 0, 0},
        };
        Assert.assertArrayEquals(expected, piece.getMatrix());
    }

    @Test
    public void testCanPlace(){
        Piece piece0 = new Piece(15, 21);
        Assert.assertFalse(piece0.canPlace(0));
        Piece piece1 = new Piece(15, 15);
        Assert.assertTrue(piece1.canPlace(0));
    }


}
