import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameBoard extends JPanel{
	private ArrayList<Territory> terrList;
	private String initMap;
	private ArrayList<Continent> contList;


	/**
	 * gamePhase:
	 * 0: Acquisition
	 * 1: Reinforce
	 * 2: Attack
	 * 3: Move
	 */
	private int gamePhase = 0; //see above
	private boolean playersTurn; //player's or computer's turn

	private int playerReinforcements; //current number of player's reinforcements
	private int computerReinforcements; //current number of computer's reinforcements
	private int roundNumber = 0; //round counter

	public CPU cpu;


	private Territory selectedTerritory = null;
	private ArrayList<Territory> movableTerritories = new ArrayList<>();

	private ControlCentre controlCentre;

	private Dice dice;
	private MouseAdapterMod mouseAdapter;





	public GameBoard(String initMap, ControlCentre controlCentre) {
		this.controlCentre = controlCentre;
		this.dice = new Dice(controlCentre);
		this.cpu = new CPU("COMPUTER", this.controlCentre);
		this.gamePhase = 0; //set the starting game phase to acquisition
		this.roundNumber = 0; //start counting the round numbers with 0
		this.initMap = initMap; //set the map of the game
		this.controlCentre.log("Welcome to All Those Territories!");
		this.controlCentre.setInstructions("Acquire: Click on a territory to occupy it!");
		terrList = new ArrayList<>(); //initialise the terrain list
		contList = new ArrayList<>(); //initialise the continent list
		parseFile(); //parse the game files and fill the terrain list, contintent list, etc.
		controlCentre.assignLists(terrList, contList);

		mouseAdapter = new MouseAdapterMod(); //initialise MouseAdapter
		this.addMouseListener(mouseAdapter); //adds a MouseListener to the whole GameBoard

		for (Territory element : terrList) {
			this.add(element);
		}
		System.out.println(contList);
		System.out.println("__Component count: " + this.getComponentCount());
		this.setLayout(null);
		setPlayersTurn(true); //it is now the player's turn to occupy a territory

		assert Objects.equals(this.controlCentre, controlCentre);

	}

	public void paintComponent(Graphics gf) {
		Graphics2D g = (Graphics2D) gf;
		super.paintComponent(g);

		for (Territory element : terrList) {
			g.setColor(Color.black);
			for (Territory neighbour : element.getNeighbours()) {
				if (overEdge(neighbour.getCapitalX(), element.getCapitalX())) {
					if (element.getCapitalX() > neighbour.getCapitalX()) {
						g.drawLine(neighbour.getCapitalX() + 1250, neighbour.getCapitalY(), element.getCapitalX(),
								element.getCapitalY());
						g.drawLine(neighbour.getCapitalX(), neighbour.getCapitalY(), element.getCapitalX() - 1250,
								element.getCapitalY());
					}
					else {
						g.drawLine(neighbour.getCapitalX() - 1250, neighbour.getCapitalY(), element.getCapitalX(),
								element.getCapitalY());
						g.drawLine(neighbour.getCapitalX(), neighbour.getCapitalY(), element.getCapitalX() + 1250,
								element.getCapitalY());
					}
				} else {
					g.drawLine(neighbour.getCapitalX(), neighbour.getCapitalY(), element.getCapitalX(),
							element.getCapitalY());
				}
			}
		}

		for (Territory element : terrList) {
			element.paintComponent(g);
		}
	}

	public ArrayList<Territory> getTerrList() {
		return terrList;
	}

	public void parseFile() {
		String line;
		InputStream fis;
		try {
			fis = new FileInputStream(this.initMap);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				if(line.startsWith("patch-of ")) {
					line = line.replace("patch-of ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					boolean isNew = true;
					for (Territory element : terrList) {
						if (element.getName().equals(name)) {
							isNew = false;
							String[] coordsTemp = line.substring(position - 1).split(" ");
							int[] coords = new int[coordsTemp.length];
							for (int i = 0; i < coordsTemp.length; i++) {
								coords[i] = Integer.parseInt(coordsTemp[i]);
							}
							element.addPatch(coords);

						}
					}
					if(isNew) {
						String[] coordsTemp = line.substring(position - 1).split(" ");
						int[] coords = new int[coordsTemp.length];
						for (int i = 0; i < coordsTemp.length; i++) {
							coords[i] = Integer.parseInt(coordsTemp[i]);
						}
						Territory ter = new Territory(name, coords, this.controlCentre);
						terrList.add(ter);
					}
				}
				else if (line.startsWith("capital-of ")) {
					line = line.replace("capital-of ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					String[] coordsTemp = line.substring(position - 1).split(" ");
					int[] coords = new int[coordsTemp.length];
					for (int i = 0; i < coordsTemp.length; i++) {
						coords[i] = Integer.parseInt(coordsTemp[i]);
					}
					for (Territory element : terrList) {
						if (element.getName().equals(name))
							element.setCapital(coords);
					}
				}
				else if(line.startsWith("neighbors-of ")) {
					line = line.replace("neighbors-of ", "");
					String[] split = line.split(" : ");
					String name = split[0]; //this is the name, all the rest is in split[1]
					List<String> neighbours = Arrays.asList(split[1].split(" - ")); //splits all the neighbours up
					ArrayList<Territory> tempNeighbour = new ArrayList<>();
					Territory owner = null;
					for (Territory element : terrList) {
						if (element.getName().equals(name))
							owner = element;
						for (String item : neighbours) {
							if (element.getName().equals(item))
								tempNeighbour.add(element);
						}
					}
					owner.setNeighbours(tempNeighbour);
					//System.out.println("__" + owner.getName() + ": " + tempNeighbour);
				}

				else if(line.startsWith("continent ")) {
					line = line.replace("continent ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					System.out.println(line);
					//line = line.replace(name + " ", "");
					System.out.println(line);
					String[] split = line.split(":");
					System.out.println(split[0]);
					split[0] = split[0].replace(name + " ", "");
					int bonus = Integer.parseInt(split[0].trim());
					System.out.println(split[1]);
					Continent continent = new Continent(name, bonus, this.controlCentre);
					String[] territories = split[1].split(" - ");

					for (Territory element : terrList) {
						for (String ter : territories) {
							//System.out.println(ter);
							ter = ter.trim();

							if (element.getName().equals(ter))
								continent.addTerritory(element);
						}
					}

					contList.add(continent);
				}

			}

			for (Territory terr : terrList) {
				for (Territory possibleNeighbour : terrList) {
					for (Territory possibleNeighboursNeighbours : possibleNeighbour.getNeighbours()) {
						if(possibleNeighboursNeighbours == terr) {
							terr.addNeighbour(possibleNeighbour);
						}
					}
				}
			}
			System.out.println("__List size: " + terrList.size());
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean overEdge(int x1, int x2) {
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		return x2 - x1 > 625;
	}

	class MouseAdapterMod extends MouseAdapter {
		/**
		 *
		 * @param e
		 *
		 * Sort of Gamelogic:
		 * Checking if all Territories are conquered (with an owner)
		 * 1) All Territories are conquered (with an owner)
		 * 		In that case, click on a Territory not own by the "clicker" means to attack
		 * 2) Not all Territories have owner
		 * 		In that case a user is only allowed to click on a "neutral" Territory
		 * 	    Clicking on a free territory will change the owner and color to the player's.
		 *
		 */

		@SuppressWarnings("ConstantConditions")
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			int clickedButton = e.getButton();
			Point p = e.getPoint();
			Territory clickedTerritory = null;

			for(Territory terr : terrList) { //iterates through all territories
				if(terr.containsClick(p)) { //determines if the location of the mouse click is inside the territory
					clickedTerritory = terr; //sets the clicked territory
				}
			}
			if(clickedTerritory != null && playersTurn) {
				switch (clickedButton) {
					case (1): // Left
						mouseLeft(clickedTerritory);
						break;
					case (3): // Right
						mouseRight(clickedTerritory);
						break;
				}
			}
		}


		private void mouseLeft(Territory clickedTerritory) {
			assert playersTurn;
			switch(gamePhase) {
				case(0): // Acquire
					if(Objects.equals(clickedTerritory.getOwner(), null)) {
						clickedTerritory.setArmyCount(1);
						clickedTerritory.changeOwner("PLAYER");
						setPlayersTurn(false);
						cpu.occupyTerritory(contList);
						setPlayersTurn(true);
					}

					if(wholeMapIsOccupied()) { //if occupation of the whole map is finished
						setPlayersTurn(false);
						System.out.println("__occupation of map finished, reinforcements to be given");
						giveReinforcements();
						System.out.println("__reinforcements given");
						gamePhase = 1;
						System.out.println("__GAME PHASE 1");
						roundNumber += 1;
						controlCentre.addBreak();
						resetMovableTerritories();
						controlCentre.log("Round " + roundNumber + "!");
						controlCentre.log("Reinforcements have arrived! You got " + playerReinforcements + " reinforcements. Use them wisely!");
						controlCentre.setInstructions("Click on a territory to send a reinforcement!");
						setPlayersTurn(true);
					}

					break;
				case(1): // Reinforce
					assert playerReinforcements > 0;

					if(Objects.equals(clickedTerritory.getOwner(), "PLAYER")) {
						System.out.println("__player clicked on territory - assigning reinforcement to territory");
						clickedTerritory.changeArmyCount(1);
						playerReinforcements -= 1;
						controlCentre.log("Reinforcements remaining: " + playerReinforcements);
					}

					if(playerReinforcements == 0) { //if player has already used all of his reinforcements, let the computer do its reinforcements and proceed to the next phase
						System.out.println("__player has used all available reinforcements, computer now distributes its reinforcements (it has got " + computerReinforcements + ")");
						setPlayersTurn(false);
						cpu.distributeReinforcements(computerReinforcements, contList);
						System.out.println("__computer finished distributing reinforcements");
						gamePhase = 2;
						System.out.println("__GAME PHASE 2");
						controlCentre.setInstructions("Conquer: Left-click on a territory from which you want to start an attack or move troops, or click on the \"Finish Round\" button!");
						setPlayersTurn(true);
					}

					break;
				case(2): // Conquer
					if(selectedTerritory ==  null && Objects.equals(clickedTerritory.getOwner(), "PLAYER")) { //if clicked on unselected own territory, select it
						controlCentre.setFinishButtonEnabled(false);
						clickedTerritory.selectForAttack();
						selectedTerritory = clickedTerritory;
						controlCentre.setInstructions("Conquer: Left-click on an adjacent opposing territory to start an attack or right click on an adjacent own territory to move a troop!");
					}
					else if(clickedTerritory.equals(selectedTerritory)) { //if clicked on selected territory, deselect it
						clickedTerritory.deselectForAttack();
						selectedTerritory = null;
						controlCentre.setFinishButtonEnabled(true);
						controlCentre.setInstructions("Conquer: Left-click on a territory from which you want to start an attack or move troops, or click on the \"Finish Round\" button!");
					}
					else if(clickedTerritory.getNeighbours().contains(selectedTerritory) && !Objects.equals(clickedTerritory.getOwner(), "PLAYER") && selectedTerritory.getArmyCount() > 1) { //if own territory is selected and valid opposing territory is clicked
						if(dice.attack(selectedTerritory, clickedTerritory)) { //if attack succeeded
							cpu.removeTerritory(clickedTerritory);
							selectedTerritory.deselectForAttack();
							selectedTerritory = null;
							controlCentre.setFinishButtonEnabled(true);
							controlCentre.setInstructions("Conquer: Left-click on a territory from which you want to start an attack or move troops, or click on the \"Finish Round\" button!");
						}
					}

					break;
			}
		}

		private void mouseRight(Territory clickedTerritory) {
			switch(gamePhase) {
				case(0): // Acquire

					break;
				case(1): // Reinforce

					break;
				case(2): // Conquer
					if(playersTurn && selectedTerritory != null) {
						System.out.println("Okay, a territory is selected...");
						if(Objects.equals(clickedTerritory.getOwner(), "PLAYER")) {
							System.out.println("Okay, the clicked territory belongs to the player...");
							if(clickedTerritory.getNeighbours().contains(selectedTerritory)) {
								System.out.println("Okay, the clicked territory is a neighbour of the selected territory...");
								if(selectedTerritory.getArmyCount() > 1) {
									System.out.println("Okay, army count is above 1...");
									if(movableTerritories.size() == 0) {
										movableTerritories.add(selectedTerritory);
										movableTerritories.add(clickedTerritory);
									}
									if(movableTerritories.contains(selectedTerritory) && movableTerritories.contains(clickedTerritory)) {
										moveArmy(selectedTerritory, clickedTerritory);
									}
								}
							}
						}
					}
					break;
				/*case(3): // Move
					if(clickedTerritory.getNeighbours().contains(selectedTerritory) && !Objects.equals(clickedTerritory.getOwner(), "PLAYER") && selectedTerritory.getArmyCount() > 1) { //if own territory is selected and valid opposing territory is clicked
						if(dice.attack(selectedTerritory, clickedTerritory)) { //if attack succeeded
							cpu.removeTerritory(clickedTerritory);
							selectedTerritory.deselectForAttack();
							selectedTerritory = null;
							controlCentre.setFinishButtonEnabled(true);
							controlCentre.setInstructions("Conquer: Left-click on a territory from which you want to start an attack or move troops, or click on the \"Finish Round\" button!");
						}
					}

					break;*/
			}
		}

		public boolean wholeMapIsOccupied() {
			for (Territory terr : terrList) {
				if(terr.getOwner() == null) {
					return false;
				}
			}
			return true;
		}

		private Territory selectedOwnTerritory() {
			for (Territory terr : terrList) {
				if(terr.isSelectedForAttack() && Objects.equals(terr.getOwner(), "PLAYER")) return terr;
			}

			return null;
		}

		private Territory selectedOpposingTerritory() {
			for (Territory terr : terrList) {

				if(terr.isSelectedForAttack() && Objects.equals(terr.getOwner(), "COMPUTER")) return terr;

			}

			return null;
		}

		private boolean existsSelectedOwnTerritory() {
			for (Territory terr : terrList) {
				if(terr.isSelectedForAttack() && Objects.equals(terr.getOwner(), "PLAYER")) return true;
			}

			return false;
		}

		private boolean existsSelectedOpposingTerritory() {
			for (Territory terr : terrList) {
				if(terr.isSelectedForAttack() && Objects.equals(terr.getOwner(), "COMPUTER")) return true;
			}

			return false;
		}



	}

	private void moveArmy(Territory from, Territory to) {
		from.changeArmyCount(-1);
		to.changeArmyCount(1);
		controlCentre.log("Army transfer: " + from.getName() + " -> " + to.getName());
	}


	/**
	 *
	 * @param name of Player
	 * @return all possible source territories
	 */

	public boolean checkEndOfGame() {
		String name = terrList.get(0).getOwner();
		for(Territory terr : terrList) {
			if(!name.equals(terr.getOwner())) return false;
		}
		return true;
	}

	public void setPlayersTurn(boolean b) {
		this.playersTurn = b;
		controlCentre.setPlayersTurn(b);
		System.out.println("__---IT IS NOW THE " + (b ? "PLAYER'S" : "COMPUTER'S") + "TURN");
	}

	public void setGamePhase(int gamePhase) {
		this.gamePhase = gamePhase;
	}

	public int getGamePhase() {
		return this.gamePhase;
	}

	public void incrementRoundNumber() {
		this.roundNumber += 1;
	}

	public int getRoundNumber() {
		return this.roundNumber;
	}

	public void setPlayerReinforcements(int n) {
		this.playerReinforcements = n;
	}

	public int getPlayerReinforcements() {
		return this.playerReinforcements;
	}

	public void setComputerReinforcements(int n) {
		this.computerReinforcements = n;
	}

	public int getComputerReinforcements() {
		return this.computerReinforcements;
	}

	public void giveReinforcements() {
		System.out.println("GIVING REINFORCEMENTS! GAME PHASE: " + gamePhase);
		this.playerReinforcements = 0;
		this.computerReinforcements = 0;

		if(gamePhase == 0 || gamePhase == 1) {
			int terrCounter = 0;

			System.out.println("__calculating territory reinforcements for player");

			for (Territory terr : this.terrList) { //iterates through all territories, gives the player 1 reinforcement for every 3 territories
				if(Objects.equals(terr.getOwner(), "PLAYER")) {
					terrCounter += 1;
					if(terrCounter == 3) {
						this.playerReinforcements += 1;
						terrCounter = 0;
					}
				}
			}

			System.out.println("__calculating contintent bonus for both player and computer");

			for (Continent cont : this.contList) { //iterates through all continents, gives the player 3 reinforcements for every owned continent
				if(cont.completeOwner("PLAYER")) {
					this.playerReinforcements += cont.getBonus();
				}
				if(cont.completeOwner("COMPUTER")) {
					this.computerReinforcements += cont.getBonus();
				}
			}

			System.out.println("__calculating territory reinforcements for computer");

			terrCounter = 0;

			for (Territory terr : this.terrList) { //iterates through all territories, gives the computer 1 reinforcement for every 3 territories
				if(Objects.equals(terr.getOwner(), "COMPUTER")) {
					terrCounter += 1;
					if(terrCounter == 3) {
						this.computerReinforcements += 1;
						terrCounter = 0;
					}
				}
			}
		}
	}

	public void resetMovableTerritories() {
		this.movableTerritories = new ArrayList<>();
	}

	public void gameWon() {
		String winner = terrList.get(0).getOwner();
		JOptionPane.showMessageDialog(null,
				"The winner is: " + winner,
				"Game Over!",
				JOptionPane.WARNING_MESSAGE);

		System.exit(0);
	}
}