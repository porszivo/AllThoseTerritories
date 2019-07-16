import java.util.ArrayList;

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
     * Very simple check to see how many enemies the "new" Territory would have
     *
     */
    private int simVictory() {
        int i = 0;
        for(Territory terr : this.getDefender().getNeighbours()) {
            if(!terr.getOwner().equals("COMPUTER")) {
                i++;
            }
        }
        return i;
    }

    /**
     * Very simple Ai to get the Territory with the most enemy neighbors
     * Move to AIHelper
     * Check if it is working!?
     *
     */
    public static Battle chooseBestBattle(ArrayList<Battle> battles) {
        int min = -1;
        Battle ret = null;
        for(Battle bat : battles) {
            if(bat.simVictory() > min) {
                min = bat.simVictory();
                ret = bat;
            }
        }
        return ret;
    }

}