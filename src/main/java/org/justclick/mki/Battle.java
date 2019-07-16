package org.justclick.mki;

import java.util.List;

public class Battle {

    private Territory offender, defender;

    public Battle(Territory from, Territory to) {
        this.offender = from;
        this.defender = to;
    }

    public Territory getOffender() {
        return offender;
    }


    public Territory getDefender() {
        return defender;
    }

    /**
     * Very simple check to see how many enemies the "new" org.justclick.mki.Territory would have
     */
    private int simVictory() {
        int i = 0;
        for (Territory terr : this.getDefender().getNeighbours()) {
            if (!terr.getOwner().equals("COMPUTER")) {
                i++;
            }
        }
        return i;
    }

    /**
     * Very simple Ai to get the org.justclick.mki.Territory with the most enemy neighbors
     * Move to org.justclick.mki.AIHelper
     * Check if it is working!?
     *
     * @param battles
     */
    public static Battle chooseBestBattle(List<Battle> battles) {
        int min = -1;
        Battle ret = null;
        for (Battle bat : battles) {
            if (bat.simVictory() > min) {
                min = bat.simVictory();
                ret = bat;
            }
        }
        return ret;
    }

}