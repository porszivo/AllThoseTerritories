import java.util.ArrayList;
import java.util.Objects;

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
        double ret = ((troopRatioOnContinent(c, player) + ratioTerrInContinent(c, player)) / 2) * getRating(c);
        return ret;
    }

    /**
     *
     * @param c
     * @return
     */
    public static double getRating(Continent c) { // TODO - ??
        double ret = ((15. + (double)c.getBonus() - 4. *
                (double)getContinentBorderCount(c)) / (double)c.getAmountOfTerritory());
        return ret;
    }

    /**
     * Amount of border. The more borders, the worse.
     * e.g. Australia one border, easy to hold
     * @param c
     * @return
     */
    public static int getContinentBorderCount(Continent c) {

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
     * Returns the ratio of own troops on one continent in relation to all territories on the same continent
     * @param c: Continent
     * @param player: player of the Player
     * @return
     *
     *
     */
    public static double troopRatioOnContinent(Continent c, String player) {
        int troops = 0;
        int totalTroops = 0;
        for(Territory terr : c.getTerritoryList()) {
            if(terr.getOwner()!=null && terr.getOwner().equals(player)) {
                troops += terr.getArmyCount();
            }
            totalTroops += terr.getArmyCount();
        }
        if(totalTroops == 0) return 0;
        return (double)((double)troops / (double)totalTroops);
    }

    /**
     * Returns the ratio of own territories in one continent in relation to all territories on the same continent
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static double ratioTerrInContinent(Continent c, String player) {
        if(!existsOwnedTerritoryOnContinent(c, player)) {
            return 0;
        }
        
        int own = 0;
        for(Territory terr : c.getTerritoryList()) {
            if(Objects.equals(terr.getOwner(), player)) {
                own++;
            }
        }
        return ((double)own / (double)c.getTerritoryList().size());
    }

    /**
     * Returns true if at least one country on given continent is under the control of player
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static boolean existsOwnedTerritoryOnContinent(Continent c, String player) {
        boolean ret = false;
        for(Territory terr : c.getTerritoryList()) {
            if(terr.getOwner() != null && terr.getOwner().equals(player)) {
                return true;
            }
        }
        return ret;
    }
    
    /**
     * Returns the Territory with the most enemy neighbors
     * @param c: Continent
     * @param player: player of the Player
     * @return
     */
    public static Territory territoryWithMostEnemyNeighbours(Continent c, String player) {
        int max = -1;
        Territory retTer = null;
        System.out.println("Now calculating territory with most enemy neighbours:");
        System.out.println("Continent " + c.getName());
        for(Territory terr : c.getTerritoryList()) {
            System.out.println("---------");
            System.out.println("For step!");
            System.out.println("Territory " + terr.getName());
            System.out.println("Is belong to " + player + "?");
            if(Objects.equals(terr.getOwner(), player)) {
                System.out.println("yes!");
                int i = 0;
                for(Territory terrNeigh : terr.getNeighbours()) {
                    if(!terrNeigh.getOwner().equals(player)) {
                        System.out.println(terr.getName() + " has neighbour " + terrNeigh.getName());
                        i++;
                    }
                }
                System.out.println(i);
                if(max < i) {
                    retTer = terr;
                    max = i;
                }
            }
        }
        if(max == -1) return null;
        return retTer;
    }


    public static Territory selectOffender(Continent c, String player) {
        int max = 0;
        Territory retTer = null;
        System.out.println("Now calculating territory with most enemy neighbours:");
        for(Territory terr : c.getTerritoryList()) {
            System.out.println("For step!");
            if(Objects.equals(terr.getOwner(), player)) {
                int i = 0;
                for(Territory terrNeigh : terr.getNeighbours()) {
                    if(!terrNeigh.getOwner().equals(player)) {
                        System.out.println(terr.getName() + " has neighbour " + terrNeigh.getName());
                        i++;
                    }
                }
                System.out.println(i);
                if(max < i && terr.getArmyCount() > 1) {
                    retTer = terr;
                    max = i;
                }
            }
        }

        return retTer;
    }

    public static Territory selectAttacked(Territory offender) {
        int min = 9999;
        Territory retTer = null;
        for(Territory terrNeigh : offender.getNeighbours()) {
            if(!Objects.equals(terrNeigh.getOwner(), offender.getOwner())) {
                if(terrNeigh.getArmyCount() < min) {
                    min = terrNeigh.getArmyCount();
                    retTer = terrNeigh;
                }
            }
        }
        return retTer;
    }

}
