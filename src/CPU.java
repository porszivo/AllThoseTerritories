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

    public void reinforce(int computerReinforcements) {
        Territory reinforceTerr;
        while(computerReinforcements > 0) {
            reinforceTerr = terrList.get((int)(Math.random() * terrList.size()));
            reinforceTerr.changeArmycount(1);
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
    public void conquer() {

    }

    public void acquire(ArrayList<Territory> terrList) {

        Territory chosenTerritory;
        do {
            chosenTerritory = terrList.get((int)(Math.random() * terrList.size())); // The computer selects a completely random territory (to be made more sophisticated! TODO!)
        } while(chosenTerritory.getOwner() != null); // Until it has managed to selectForAttack an unoccupied territory

        chosenTerritory.setArmyCount(1);
        chosenTerritory.changeOwner("COMPUTER");
        addTerritory(chosenTerritory);
        System.out.println("The computer has occupied " + chosenTerritory.getName() + " with 1 army!");

    }

    public void acquire(Territory terr) {
        addTerritory(terr);
    }

    /**
     * @TODO: AI for moving army
     */
    public void move() {

    }

}
