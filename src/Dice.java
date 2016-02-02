import javafx.scene.control.ChoiceDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Marcel on 31.01.2016.
 */
public class Dice {

    public Dice() {

    }

    public static int throwDice() {
        return (int)(Math.random() * 6) + 1;
    }

    public boolean pressTheAttack(Territory from, Territory to) {
        if(from.getArmyCount()>=to.getArmyCount() && to.getArmyCount() <= 2) {

            ArrayList<Integer> offenderThrows = new ArrayList<>();
            ArrayList<Integer> defenderThrows = new ArrayList<>();

            int numberOfOffender = from.getArmyCount() - 1 >= 3 ? 3 : from.getArmyCount() - 1;
            int numberOfDefender = to.getArmyCount() >= 2 ? 2 : to.getArmyCount();

            for (int i = 0; i < numberOfOffender; i++) {
                offenderThrows.add(throwDice());
            }

            for (int i = 0; i < numberOfDefender; i++) {
                defenderThrows.add(throwDice());
            }
            System.out.println("Attack from " + from.getName() + "(" + offenderThrows.size() + ")" + "to " + to.getName() + "(" + defenderThrows.size() + ")");
            offenderThrows.sort(Collections.reverseOrder());
            defenderThrows.sort(Collections.reverseOrder());

            System.out.println(offenderThrows);
            System.out.println(defenderThrows);


            while(offenderThrows.size() > 0 && defenderThrows.size() > 0) {
                if(offenderThrows.get(0) > defenderThrows.get(0)) {
                    to.changeArmycount(-1);
                    numberOfDefender--;
                } else {
                    from.changeArmycount(-1);
                    numberOfOffender--;
                }
                offenderThrows.remove(0);
                defenderThrows.remove(0);
            }


            if(to.getArmyCount()==0) {
                int moveArmyCount = numberOfOffender;
                if(numberOfOffender < from.getArmyCount()-1) {
                    int size = from.getArmyCount() - numberOfOffender;
                    String[] moveArmy = new String[size];

                    for (int i = 0; i < size; i++) {
                        moveArmy[i] = numberOfOffender + i + "";
                    }
                    moveArmyCount = Integer.parseInt((String) JOptionPane.showInputDialog(null, "How many troops do you want to move to " + to.getName() + "?", from.getName() + " won!", JOptionPane.QUESTION_MESSAGE, null, moveArmy, moveArmy[0]));
                }

                to.changeArmycount(moveArmyCount);
                from.changeArmycount(-moveArmyCount);
                to.changeOwner(from.getOwner());
                from.deselectForAttack();
                return true;
            }

            return false;
/*
            while (offenderThrows.size() > 0 || defenderThrows.size() > 0) {
                if ((int) offenderThrows.get(0) > (int) defenderThrows.get(0)) {
                    to.changeArmycount(-1);
                    numberOfDefender--;
                    System.out.println("Your " + offenderThrows.remove(0) + " is higher than the computer's " + defenderThrows.remove(0) + ", so you defeat one of its armies!");
                    System.out.println("__Own: " + from.getArmyCount() + ", Opposing: " + from.getArmyCount());
                } else {
                    from.changeArmycount(-1);
                    numberOfOffender--;
                    System.out.println("Your " + offenderThrows.remove(0) + " is not higher than the computer's " + defenderThrows.remove(0) + ", so one of your armies is defeated!");
                    System.out.println("__Own: " + from.getArmyCount() + ", Opposing: " + to.getArmyCount());
                }
            }

            System.out.println("__--------------DICE-THROWING FINISHED.------------");
            System.out.println("__Own: " + from.getArmyCount() + ", Opposing: " + to.getArmyCount());
            System.out.println("__Owner of own: " + from.getOwner() + ", owner of opposing: " + to.getOwner());

            if (to.getArmyCount() == 0) {
                System.out.println("__HANDLING OF CONQUERING OPPOSING TERRITORY STARTED");
                from.changeArmycount(-numberOfOffender);
                System.out.println("__deducting own armies left in combat from own army count - new own army count: " + from.getArmyCount());
                System.out.println("__Owner of own: " + from.getOwner() + ", owner of opposing: " + to.getOwner());
                System.out.println("__transferring own armies left in combat to conquered territory: " + numberOfOffender + " armies");
                to.changeArmycount(numberOfOffender);
                System.out.println("__Owner of own: " + from.getOwner() + ", owner of opposing: " + to.getOwner());
                System.out.println("__changing owner of conquered territory");
                to.changeOwner(from.getOwner());
                System.out.println("__owner changed.");
            }

            from.deselectForAttack();*/

        } else {
            System.out.println("__Attack is not possible, Armycount to low");
            return false;
        }
    }

}
