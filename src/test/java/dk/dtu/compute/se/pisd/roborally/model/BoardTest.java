package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    Board board;
    Player player;

    @BeforeEach
    void setup(){
        board = new Board(TEST_WIDTH, TEST_HEIGHT);
        player = new Player(board, "red", "Player " + 1);

        board.addPlayer(player);
    }
    @AfterEach
    void tearDown(){
        board = null;
    }


    @Test
    void setBoardName() {
        board.setBoardName("Test");

        Assertions.assertEquals("Test", board.getBoardName(), "The name of the board should be 'Test' ");
    }

    @Test
    void setGameId() {
        board.setGameId(5);

        Assertions.assertEquals(5, board.getGameId(), "Board ID should be 5");
    }

    @Test
    void setCurrentPlayer() {
        board.setCurrentPlayer(player);

        Assertions.assertEquals(player, board.getCurrentPlayer(), "Currentplayer should be player 1");
    }

    @Test
    void setPhase() {
        board.setPhase(Phase.ACTIVATION);

        Assertions.assertEquals(Phase.ACTIVATION, board.getPhase(), "Phase should be ACTIVATION");
    }
}