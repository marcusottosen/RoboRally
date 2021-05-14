package dk.dtu.compute.se.pisd.roborally.model.specialFields;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Håndterer aktionen af et PushPanel.
 * Skal sættes ovenpå en væg i samme Heading.
 *
 * @author Marcus Ottosen
 * @author Victor Kongsbak
 */
public class PushPanel extends FieldAction {
    private Heading heading;

    /**
     * Returnerer retningen af pushpanel.
     *
     * @return retning som Heading.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sætter retningen af pushpanel.
     *
     * @param heading Den ENUM Heading/retning man ønsker pushpanel skal sættes til.
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * PushPanels aktion. Kører når en spillet står ved et pushPanel felt.
     * Skubber spilleren i den modsatte retning af pushpanelens heading.
     *
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * @return hvorvidt doAction blev udført.
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        try {
            Space target = gameController.board.getNeighbour(space, heading.next().next());
            gameController.moveToSpace(space.getPlayer(), target, heading.next().next());
            return true;
        } catch (GameController.ImpossibleMoveException e) {
            return false;
        }
    }
}
