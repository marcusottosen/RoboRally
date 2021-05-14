package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    Board board;
    Player player;
    Space space;

    @BeforeEach
    void setUp(){
        board = new Board(TEST_WIDTH, TEST_HEIGHT);
        player = new Player(board, "red", "Player " + 1);
        space = new Space(board, 0, 0);
    }

    @AfterEach
    void tearDown(){
        board = null;
    }

    @Test
    void setPlayer() {
        space.setPlayer(player);

        Assertions.assertEquals(player, space.getPlayer(), "player skal være lige med player");

    }
}