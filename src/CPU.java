import java.util.ArrayList;

/**
 * class for the CPU "AI"
 */
public class CPU {

    private ArrayList<Territory> terrList;
    private String name;

    public CPU(String name) {
        this.terrList = new ArrayList<>();
        this.name = name;
    }

    public void addTerritory(Territory terr) {
        terrList.add(terr);
    }

    public void removeTerritory(Territory terr) {
        terrList.remove(terr);
    }

    public void reinforce(int computerReinforcements, ArrayList<Continent> contList) {
    
        Continent cont = getMostValuable(contList);
        Territory reinforceTerr = AIHelper.mostEnemyNeighbor(cont, "COMPUTER");
        
        while(computerReinforcements > 0) {
            reinforceTerr.changeArmyCount(1);
            reinforceTerr.repaint();
            computerReinforcements -= 1;
            if(computerReinforcements > 0) {
                System.out.print(reinforceTerr.getName() + ", ");
            }
            else {
                System.out.println("and " + reinforceTerr.getName() + ".");
                System.out.println();
                System.out.println("It is now your turn. Attack a territory or transfer armies!");
                System.out.println();
            }
        }
    }

    /**
     * @todo: Attack AI
     * @param
     */
    public void conquer(ArrayList<Continent> contList) {
        Continent cont = getMostValuable(contList);
        Territory offender = AIHelper.mostEnemyNeighbor(cont, "COMPUTER");        
    }

    
    public void acquire(ArrayList<Continent> contList) {
        
        Continent cont = getMostValuableContinent(contList);
        
        Territory chosenTerritory;
        do {
            chosenTerritory = cont.getTerritoryList().get((int)(Math.random() * cont.getAmountOfTerritory()));
        } while(chosenTerritory.getOwner() != null);
        
        chosenTerritory.setArmyCount(1);
        chosenTerritory.changeOwner("COMPUTER")
        addTerritory(chosenTerritory); 
        System.out.println("The computer has occupied " + chosenTerritory.getName() + " with 1 army!"); 
        
    }

    public void acquire(Territory terr) {
        addTerritory(terr);
    }
    
    private Continent getMostValuableContinent(ArrayList<Continent> contList) {
        double min = 20;
        Continent cont = null;
        for(Continent c : contList) {
            if(AIHelper.calcContinentWeight(c,"COMPUTER") < min) {
                cont = c;
                min = AIHelper.calcContinentWeight(c,"COMPUTER");
            }
        }
        return cont;
    }

    /**
     * @TODO: AI for moving army
     */
    public void move() {

    }

}
