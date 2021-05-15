package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


/**
 * Test af spiller.
 *
 * @author Victor Kongsbak
 */
class PlayerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    Board board;
    Player player;


    @BeforeEach
    void setUp() {
        board = new Board(TEST_WIDTH, TEST_HEIGHT);
        player = new Player(board, PLAYER_COLORS.get(1), "Player " + (1));

        board.addPlayer(player);
        player.setSpace(board.getSpace(0, 0));
    }

    @AfterEach
    void tearDown() {
        board.getPlayers().removeAll(board.getPlayers());
    }


    @Test
    void setHeading() {
        player.setHeading(Heading.EAST);

        Assertions.assertEquals(Heading.EAST, player.getHeading(), "Player should be heading EAST!");
    }

    @Test
    void setSpace() {
        player.setSpace(board.getSpace(0, 1));

        Assertions.assertEquals(board.getSpace(0, 1), player.getSpace(), "Player should be at space (0, 1)");
    }

    @Test
    void setScore() {
        player.setScore(1);

        Assertions.assertEquals(1, player.getScore(), "Players score should be 1");
    }

    @Test
    void setHealth() {
        player.setHealth(1);

        Assertions.assertEquals(1, player.getHealth(), "Players health should be 1");
    }

    @Test
    void takeHealth() {
        player.setHealth(3);
        player.takeHealth(1);

        Assertions.assertEquals(2, player.getHealth(), "Players health should be 2(3-1=2)");
    }

    @Test
    void setOptainedEnergyCube() {


    }


}