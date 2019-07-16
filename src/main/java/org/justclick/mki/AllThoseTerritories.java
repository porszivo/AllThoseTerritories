package org.justclick.mki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AllThoseTerritories extends JPanel {

    private static final Logger LOG = LoggerFactory.getLogger(AllThoseTerritories.class);

    public static void main(String[] args) {

        JFrame mapFrame = new JFrame("Game Map - ATT");
        ControllCenter controllCenter = new ControllCenter();
        GameBoard gameBoard = new GameBoard(args[0], controllCenter);
        controllCenter.assignGameBoard(gameBoard);
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapFrame.setContentPane(gameBoard);
        mapFrame.setSize(1250, 650);
        mapFrame.setLocation(80, 80);
        mapFrame.setVisible(true);

        JFrame ccFrame = new JFrame("Control Centre - ATT");
        ccFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ccFrame.setContentPane(controllCenter);
        ccFrame.setSize(1250, 240);
        ccFrame.setLocation(80, 730);
        ccFrame.setAlwaysOnTop(true);
        ccFrame.setVisible(true);

        LOG.info("----------- THE GAME HAS STARTED! ------------");
        LOG.info("The start phase is active. Click on a territory you want to occupy!");

    }

}
