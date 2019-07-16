import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class ControlCentre extends JPanel implements ActionListener {

    private JButton finishButton = new JButton("Finish Round");
    private JTextArea logText = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(logText);
    private JLabel instrLabel = new JLabel("INSTRUCTION LABEL");
    private Font instrLabelFont = new Font("Tahoma", 1, 24);
    private JLabel statusLabel = new JLabel("STATUS LABEL");
    private Font statusLabelFont = new Font("Tahoma", 0, 18);
    private Timer timer;
    private Deque<String> statusTexts = new LinkedList<>();
    private boolean playersTurn = true;
    private String currentInstructions = "INSTRUCTION LABEL";
    private GameBoard gameBoard;
    private ArrayList<Territory> terrList;
    private ArrayList<Continent> contList;



    public ControlCentre() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        this.add(finishButton);
        finishButton.setVisible(true);
        finishButton.setEnabled(false);
        finishButton.addActionListener(this);



        this.add(scrollPane);
        logText.setVisible(true);
        logText.setText("");
        Dimension dim = new Dimension(1000, 80);
        logText.setMaximumSize(dim);



        this.add(instrLabel);

        instrLabel.setFont(instrLabelFont);
        instrLabel.setSize(1000, 30);
        instrLabel.setVisible(true);


        this.add(statusLabel);
        statusLabel.setFont(statusLabelFont);
        statusLabel.setVisible(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(statusLabel)
                        .addComponent(instrLabel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(finishButton)
                                .addComponent(scrollPane)
                        )

        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(statusLabel)
                        .addComponent(instrLabel)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(finishButton)
                                .addComponent(scrollPane)
                        )
        );

        timer = new Timer(1600, this);
        timer.setRepeats(true);
    }

    public void log(String text) {
        this.log(text, true);
    }

    public void log(String text, boolean addBreak) {
        this.logText.setText(logText.getText() + text + (addBreak ? "\n" : ""));
        if(this.timer.isRunning()) {
            this.statusTexts.addLast(text);
        }
        else {
            this.statusLabel.setText(text);
            this.timer.start();
        }
    }

    public void setInstructions(String text) {
        this.currentInstructions = text;
        if(this.playersTurn) {
            this.instrLabel.setText(String.format("<html><div style=\"width:%dpx;text-align:center;\">%s</div><html>", 900, text));
        }
        else {
            this.instrLabel.setText("please wait, computer is working...");
        }
    }

    public String getInstructions() {
        return this.instrLabel.getText();
    }

    public void addBreak() {
        logText.setText(logText.getText() + "\n");
    }

    public void actionPerformed(ActionEvent ae) {
        if(Objects.equals(ae.getSource(), timer)) {
            if(this.statusTexts.size() > 0) {
                this.statusLabel.setText(this.statusTexts.removeFirst());
            }
            else {
                this.timer.stop();
            }
        }
        else if(Objects.equals(ae.getSource(), finishButton)) {
            this.setFinishButtonEnabled(false);
            gameBoard.setPlayersTurn(false);

            if(gameBoard.checkEndOfGame()) {
                gameBoard.gameWon();
            }

            do {
                gameBoard.cpu.conquer();
            } while ((int)(Math.random() * 4) <= 2);
            gameBoard.setPlayersTurn(false);
            gameBoard.setGamePhase(1);
            gameBoard.giveReinforcements();
            gameBoard.incrementRoundNumber();
            this.addBreak();
            this.log("Round " + gameBoard.getRoundNumber() + "!");
            this.log("Reinforcements have arrived! You got " + gameBoard.getPlayerReinforcements() + " reinforcements. Use them wisely!");
            this.setInstructions("Click on a territory to send a reinforcement!");
            gameBoard.setPlayersTurn(true);
        }

    }

    public void setPlayersTurn(boolean b) {
        this.playersTurn = b;
        this.setInstructions(this.currentInstructions);
    }

    public void setFinishButtonEnabled(boolean b) {
        this.finishButton.setEnabled(b);
    }

    public void assignGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void assignLists(ArrayList<Territory> terrList, ArrayList<Continent> contList) {
        this.terrList = terrList;
        this.contList = contList;
    }

}
