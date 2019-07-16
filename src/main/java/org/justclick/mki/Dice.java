package org.justclick.mki;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Dice {

    private ControllCenter controllCenter;

    public Dice(ControllCenter controllCenter) {
        this.controllCenter = controllCenter;
    }

    public ArrayList<Integer> throwDice(int number) {
        ArrayList<Integer> out = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            out.add(throwDice());
        }
        out.sort(Collections.reverseOrder());
        return out;
    }

    public int throwDice() {
        return (int) ((Math.random() * 6) + 1);
    }

    public boolean attack(Territory offender, Territory defender) {
        if (offender.getArmyCount() > 1) {
            int offendingTroopCount = ((offender.getArmyCount() - 1 >= 3) ? 3 : offender.getArmyCount() - 1);
            int defendingTroopCount = ((defender.getArmyCount() >= 2) ? 2 : defender.getArmyCount());
            int originalOffendingTroopCount = offendingTroopCount;
            int originalDefendingTroopCount = defendingTroopCount;

            ArrayList<Integer> offenderThrows = throwDice(offendingTroopCount);
            ArrayList<Integer> defenderThrows = throwDice(defendingTroopCount);

            System.out.println("__Attack offender " + offender.getName() + "(" + offenderThrows.size() + ")" + "defender " + defender.getName() + "(" + defenderThrows.size() + ")");
            System.out.println("__" + offenderThrows);
            System.out.println("__" + defenderThrows);

            while (offenderThrows.size() > 0 && defenderThrows.size() > 0) {
                if (offenderThrows.get(0) > defenderThrows.get(0)) {
                    defender.changeArmyCount(-1);
                    defendingTroopCount--;
                } else {
                    offender.changeArmyCount(-1);
                    offendingTroopCount--;
                }
                offenderThrows.remove(0);
                defenderThrows.remove(0);
            }

            int offendingTroopChange = originalOffendingTroopCount - offendingTroopCount;
            int defendingTroopChange = originalDefendingTroopCount - defendingTroopCount;

            controllCenter.log(offender.getName() + "(" + offender.getOwner() + ") attacks " + defender.getName() + "(" + defender.getOwner() + ").");
            String msg = "";
            msg += offender.getOwner() + " troops: " + originalOffendingTroopCount + ", " + defender.getOwner() + " troops: " + originalDefendingTroopCount + ". ";
            if (offendingTroopChange != 0) {
                msg += offender.getOwner() + " lost " + offendingTroopChange + (offendingTroopChange == 1 ? " troop. " : " troops. ");
            }
            if (defendingTroopCount < originalDefendingTroopCount) {
                msg += defender.getOwner() + " lost " + defendingTroopChange + (defendingTroopChange == 1 ? " troop. " : " troops. ");
            }
            controllCenter.log(msg);
            msg = "";
            if (defender.getArmyCount() == 0) {
                msg += "Attack has succeeded, " + offender.getOwner() + " takes over " + defender.getName() + "!";
            } else {
                msg += "Attack has failed.";
            }
            controllCenter.log(msg);
            controllCenter.addBreak();

            /** Won the fight? In that case show a dialog, if the amount of offender > offender.army + 1, defender increase the movement **/
            if (defender.getArmyCount() == 0) {
                int moveArmyCount = offendingTroopCount;
                if (offendingTroopCount < offender.getArmyCount() - 1) {
                    int size = offender.getArmyCount() - offendingTroopCount;
                    String[] moveArmy = new String[size];

                    for (int i = 0; i < size; i++) {
                        moveArmy[i] = offendingTroopCount + i + "";
                    }

                    if (Objects.equals(offender.getOwner(), "PLAYER")) {
                        String answer = null;
                        String tempInstr = controllCenter.getInstructions();
                        controllCenter.setInstructions("Please observe the dialogue box!");
                        do {
                            answer = (String) JOptionPane.showInputDialog(null, "You won the attack! How many troops do you want to move from " + defender.getName() + "?", "Move Armies", JOptionPane.QUESTION_MESSAGE, null, moveArmy, moveArmy[0]);
                        } while (answer == null);
                        controllCenter.setInstructions(tempInstr);
                        moveArmyCount = Integer.parseInt(answer);
                    } else {
                        moveArmyCount = (int) (Math.random() * (offender.getArmyCount() - offendingTroopCount) + offendingTroopCount); //TODO - temporarily random selection
                    }
                }
                System.out.println("Attack won. Transferring conquered terr to new owner");
                defender.changeArmyCount(moveArmyCount);
                offender.changeArmyCount(-moveArmyCount);
                defender.changeOwner(offender.getOwner());
                offender.deselectForAttack();
                return true;
            }

            return false;
        } else {
            System.out.println("__Attack is not possible, not enough troops");
            return false;
        }
    }

}
