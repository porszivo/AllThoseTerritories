import java.util.ArrayList;

/**
 * Helper Class with static Methods
 */
public class AIHelper {

    /**
     * Returns the weight of a continent, coefficient
     * @param c
     * @param player
     * @return
     */
    public static double calcContinentWeight(Continent c, String player) {

        return ((ratioTroopsOnContinent(c,player) + ratioTerrInContinent(c,player)) /2) * getRating(c);

    }

    /**
     *
     * @param c
     * @return
     */
    public static double getRating(Continent c) {
        return (15 + c.getBonus() - 4 * getContinentBorder(c)) / c.getAmountOfTerritory();
    }

    /**
     * Amount of border. The more borders, the worse.
     * e.g. Australia one border, easy to hold
     * @param c
     * @return
     */
    public static int getContinentBorder(Continent c) {

        ArrayList<Territory> terrNeighbors = new ArrayList<>();
        for(Territory terr : c.getTerritoryList()) {
            for(Territory terrNeigh : terr.getNeighbours()) {
                if(!c.getTerritoryList().contains(terrNeigh)) {
                    terrNeighbors.add(terrNeigh);
                }
            }
        }

        return terrNeighbors.size();

    }

    /**
     * Returns the ratio troops on one Continent
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static double ratioTroopsOnContinent(Continent c, String player) {
        int troops = 0;
        int totalTroops = 0;
        for(Territory terr : c.getTerritoryList()) {
            if(!terr.getOwner().equals(player)) {
                troops += terr.getArmyCount();
            }
            totalTroops += terr.getArmyCount();
        }
        return troops/totalTroops;
    }

    /**
     * Returns the ratio of the owned Territories in one Continent
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static double ratioTerrInContinent(Continent c, String player) {
        if(!ownTerrInContinent(c,player)) return 0;
        int own = 0;
        for(Territory terr : c.getTerritoryList()) {
            if(terr.getOwner().equals(player)) own++;
        }
        return own/c.getTerritoryList().size();
    }

    /**
     * Returns true if at least one country is under the control of player
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static boolean ownTerrInContinent(Continent c, String player) {
        boolean ret = false;
        for(Territory terr : c.getTerritoryList()) {
            if(terr.getOwner().equals(player)) ret = true;
        }
        return ret;
    }
    
    /**
     * Returns the Territory with the most enemy neighbors
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static Territory mostEnemyNeighbor(Continent c, String player) {
        int max = 0;
        Territory retTer = null;
        for(Territory terr : c.getTerritoryList) {
            if(terr.getOwner().equals(player)) {
                int i = 0;
                for(Territory terrNeigh : terr.getNeighbours()) {
                    if(!terrNeigh.getOwner().equals(player)) {
                        i++;
                    }
                }
                if(max<i) {
                    retTer = terr;
                    max = i;
                }
            }
        }
        
        return retTer;
    }
    
}
