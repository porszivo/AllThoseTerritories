package org.justclick.mki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * class for the org.justclick.mki.CPU "AI"
 */
public class CPU {

    private static final Logger LOG = LoggerFactory.getLogger(CPU.class);
    private static final String COMPUTER = "COMPUTER";

    private List<Territory> terrList;
    private ControllCenter controllCenter = null;

    public CPU(ControllCenter cc) {
        if (cc != null) {
            this.controllCenter = cc;
        }
        this.terrList = new ArrayList<>();
    }

    public void addTerritory(Territory terr) {
        terrList.add(terr);
    }

    public void removeTerritory(Territory terr) {
        terrList.remove(terr);
    }

    public void distributeReinforcements(int computerReinforcements, List<Continent> contList) {

        Continent mostValuableContinent = getMostValuableContinent(contList);
        Territory tempTerr;

        List<Continent> tempList = contList;

        tempTerr = AIHelper.territoryWithMostEnemyNeighbours(mostValuableContinent, COMPUTER);

        if (tempTerr == null) {
            tempList.remove(mostValuableContinent);
            distributeReinforcements(computerReinforcements, tempList);
        } else {

            LOG.info("__reinforceTerr = {}", tempTerr.getName());

            while (computerReinforcements > 0) {
                tempTerr.changeArmyCount(1);
                tempTerr.repaint();
                computerReinforcements -= 1;
            }
        }
    }

    public void conquer() {
        Dice dice = new Dice(controllCenter);

        Territory offender = null;
        int max = -1;
        ArrayList<Battle> battles = new ArrayList<>();

        // Attack till it's done
        for (Territory terr : terrList) {
            if (terr.getArmyCount() > max && terr.getArmyCount() > 1) {
                offender = terr;
                max = offender.getArmyCount();
            }
        }

        if (max == -1) return;

        // Get all enemies and create a battle
        for (Territory defender : offender.getNeighbours()) {
            if (!defender.getOwner().equals(COMPUTER)) {
                battles.add(new Battle(offender, defender));
            }
        }

        Battle best = Battle.chooseBestBattle(battles);
        if (best == null) return;

        Territory from = best.getOffender();
        Territory to = best.getDefender();

        if (dice.attack(from, to)) {
            terrList.add(to);
        }

        int rand = (int) ((Math.random() * 6) + 1);

        if (rand > 2) {
            conquer();
        }

    }

    public void occupyTerritory(List<Continent> contList) {
        ArrayList<Continent> tempCont = new ArrayList<>();
        for (Continent c : contList) {
            if (!c.isOccupied()) {
                tempCont.add(c);
            }
        }

        Continent cont = getMostValuableContinent(tempCont);

        Territory chosenTerritory;
        do {
            chosenTerritory = cont.getTerritoryList().get((int) (Math.random() * cont.getTerritoryList().size()));
        } while (chosenTerritory.getOwner() != null);

        chosenTerritory.setArmyCount(1);
        chosenTerritory.changeOwner(COMPUTER);
        addTerritory(chosenTerritory);
        LOG.info("The computer has occupied {} with 1 army!", chosenTerritory.getName());

    }

    private Continent getMostValuableContinent(List<Continent> contList) {
        double min = 9999;
        LOG.info("----------");
        LOG.info("Start calculation of most valuable continent:");
        Continent cont = null;
        for (Continent c : contList) {
            LOG.info("For step with continent: {}", c.getName());
            double temp = AIHelper.calcContinentWeight(c, COMPUTER);
            LOG.info("Its weight: {}", temp);
            LOG.info("Current value of min: {}", min);
            if (temp < min) {
                LOG.info("ASSIGN return continent: {}", c.getName());
                cont = c;
                min = temp;
            }
        }
        assert cont != null;
        return cont;
    }

}
