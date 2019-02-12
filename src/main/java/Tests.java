package main.java;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Tests {

    @Before
    public void setUp() {
        GameBean testGame = new GameBean();
        testGame.setMatrix(new int[][] { {0,2,2,0}, {4,0,4,2}, {0,2,4,8}, {2,0,4,4} });
        testGame.moveLeft();
        int[][] actual = testGame.getMatrix();
        int[][] expected = new int[][] { {4,0,0,0}, {8,2,0,0}, {2,4,8,0}, {2,8,0,0} };
        //test only known values
        Assert.assertEquals(actual[0][0], expected[0][0]);
        Assert.assertEquals(actual[1][0], expected[1][0]);
        Assert.assertEquals(actual[1][1], expected[1][1]);
        Assert.assertEquals(actual[2][0], expected[2][0]);
        Assert.assertEquals(actual[2][1], expected[2][1]);
        Assert.assertEquals(actual[2][2], expected[2][2]);
        Assert.assertEquals(actual[3][0], expected[3][0]);
        Assert.assertEquals(actual[3][1], expected[3][1]);

        Assert.assertEquals(testGame.getScore(), 20);

        testGame.setMatrix(new int[][] { {4,2,64,4}, {8,16,4,2}, {2,4,8,16}, {8,32,4,8} });
        Assert.assertTrue(testGame.gameOverCheck());
        testGame.setMatrix(new int[][] { {4,2,2,4}, {8,16,4,2}, {2,4,8,16}, {8,32,4,8} });
        Assert.assertFalse(testGame.gameOverCheck());
        testGame.setMaxReached(2048);
        Assert.assertTrue(testGame.gameOverCheck());







    }

    @Test
    public void testMoveLeft() {

    }
}
