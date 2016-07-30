import java.util.ArrayList;
import java.util.Objects;

/**
 * class for the CPU "AI"
 */
public class CPU {

    private ArrayList<Territory> terrList;
    private String name;
    private ControlCentre controlCentre = null;

    public CPU(String name, ControlCentre cc) {
        assert cc != null;
        this.controlCentre = cc;
        assert Objects.equals(cc, this.controlCentre);
        assert this.controlCentre != null;

        this.terrList = new ArrayList<>();
        this.name = name;
    }

    public void addTerritory(Territory terr) {
        terrList.add(terr);
    }

    public void removeTerritory(Territory terr) {
        terrList.remove(terr);
    }

    public void distributeReinforcements(int computerReinforcements, ArrayList<Continent> contList) {
    
        Continent mostValuableContinent = getMostValuableContinent(contList);
        Territory tempTerr;

        ArrayList<Continent> tempList = contList;

        tempTerr = AIHelper.territoryWithMostEnemyNeighbours(mostValuableContinent, "COMPUTER");

        if(tempTerr == null) {
            tempList.remove(mostValuableContinent);
            distributeReinforcements(computerReinforcements, tempList);
        } else {

            System.out.println("__reinforceTerr = " + tempTerr.getName());

            while (computerReinforcements > 0) {
                tempTerr.changeArmyCount(1);
                tempTerr.repaint();
                computerReinforcements -= 1;
            }
        }
    }

    /**
     * @todo: Attack AI
     * @param
     */
    public void conquer() {
        Dice dice = new Dice(controlCentre);

        Territory offender = null;
        int max = -1;
        ArrayList<Battle> battles = new ArrayList<>();

        // Attack till it's done
        for(Territory terr : terrList) {
            if(terr.getArmyCount() > max && terr.getArmyCount() > 1) {
                offender = terr;
                max = offender.getArmyCount();
            }
        }

        if(max==-1) return;

        // Get all enemies and create a battle
        for(Territory defender : offender.getNeighbours()) {
            if(!defender.getOwner().equals("COMPUTER")) {
                battles.add(new Battle(offender, defender));
            }
        }

        if(battles==null) return;

        Battle best = Battle.chooseBestBattle(battles);
        if(best==null) return;

        Territory from = best.getOffender();
        Territory to = best.getDefender();

        if(dice.attack(from, to)) {
            terrList.add(to);
        }

        int rand = (int)((Math.random() * 6 ) + 1);

        if(rand > 2) {
            conquer();
        }

    }
    
    public void occupyTerritory(ArrayList<Continent> contList) {
        ArrayList<Continent> tempCont = new ArrayList<>();
        for(Continent c : contList) {
            if(!c.isOccupied()) {
                tempCont.add(c);
            }
        }

        Continent cont = getMostValuableContinent(tempCont);
        
        Territory chosenTerritory;
        do {
            chosenTerritory = cont.getTerritoryList().get((int)(Math.random() * cont.getTerritoryList().size()));
        } while(chosenTerritory.getOwner() != null);
        
        chosenTerritory.setArmyCount(1);
        chosenTerritory.changeOwner("COMPUTER");
        addTerritory(chosenTerritory);
        System.out.println("The computer has occupied " + chosenTerritory.getName() + " with 1 army!"); 
        
    }

    private Continent getMostValuableContinent(ArrayList<Continent> contList) {
        double min = 9999;
        System.out.println("----------");
        System.out.println("Start calculation of most valuable continent:");
        Continent cont = null;
        for(Continent c : contList) {
            System.out.println("For step with continent: " + c.getName());
            double temp = AIHelper.calcContinentWeight(c, "COMPUTER");
            System.out.println("Its weight: " + temp);
            System.out.println("Current value of min: " + min);
            if(temp < min) {
                System.out.println("ASSIGN return continent: " + c.getName());
                cont = c;
                min = temp;
            }
        }
        assert cont != null;
        return cont;
    }

    /**
     * @TODO: AI for moving army
     */
    public void move() {

    }

}
