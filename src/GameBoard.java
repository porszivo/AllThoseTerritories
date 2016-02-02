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
	public ArrayList<Territory> terrList;
	public String initMap;
	public ArrayList<Continent> contList;
	public Button ok = new Button("OK");

	public boolean startPhase;
	public boolean playersTurn; //player's or computer's turn

	public int playerReinforcements;
	public int computerReinforcements;
	public int roundNumber;

	public Dice dice = new Dice();

	public CPU cpu = new CPU("COMPUTER");


	private Territory selectedTerritory;

	private ArrayList<Territory> movableTerritories = null;


	/**
	 * gamePhase:
	 * 0: Acquisition
	 * 1: Reinforce
	 * 2: Attack
	 * 3: Move
	 */
	public int gamePhase = 0;


	public GameBoard(String initMap) {
		this.startPhase = true;
		this.roundNumber = 1;
		this.initMap = initMap;
		terrList = new ArrayList<>();
		contList = new ArrayList<>();
		parseFile();
		this.add(ok);
		MouseAdapter myMA = new MouseAdapterMod();
        this.addMouseListener(myMA); //adds a MouseListener to the whole GameBoard

		for (Territory element : terrList) {
			//element.addMouseListener(myMA);
			this.add(element);
			//this.addMouseListener((MouseListener) element);
		}

		System.out.println("__Component count: " + this.getComponentCount());
		this.setLayout(null);
		playersTurn = true; //it is now the player's turn to occupy a territory

	}

	public void paintComponent(Graphics gf) {
        //System.out.println("__paintComponent is triggered"); //for debug purposes
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

		ok.paintComponent(g);

	}

	public ArrayList<Territory> getTerrList() {
		return terrList;
	}

	public void parseFile() {
		String line;
		InputStream fis;
		try

		{
			fis = new FileInputStream(this.initMap);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				if (line.startsWith("patch-of ")) {
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
					if (isNew) {
						String[] coordsTemp = line.substring(position - 1).split(" ");
						int[] coords = new int[coordsTemp.length];
						for (int i = 0; i < coordsTemp.length; i++) {
							coords[i] = Integer.parseInt(coordsTemp[i]);
						}
						Territory ter = new Territory(name, coords);
						terrList.add(ter);
					}
				} else if (line.startsWith("capital-of ")) {
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

				else if (line.startsWith("neighbors-of ")) {
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

				else if (line.startsWith("continent ")) {
					line = line.replace("continent ", "");
					Matcher m = Pattern.compile("\\d").matcher(line);
					int position = m.find() ? m.start() + 1 : 0;
					String name = line.substring(0, position - 2);
					line = line.replace(name + " ", "");
					String[] split = line.split(" : ");
					int bonus = Integer.parseInt(split[0]);
					Continent continent = new Continent(name, bonus);
					String[] territories = split[1].split(" - ");
					for (Territory element : terrList) {
						for (String ter : territories) {
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
		} catch (FileNotFoundException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)

		{
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
		if (x2 - x1 > 625)
			return true;
		else
			return false;
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
			int mouseButton = e.getButton();
			Point p = e.getPoint();
			Territory clickedTerritory = null;

			/**
			 * OK Button
			 *
			 */
			if(ok.contains((int)p.getX(),(int)p.getY())) {

				if(gamePhase > 0) {
					if(gamePhase==3) {
						cpu.move();
						startRound();
					}
					if(gamePhase==2) {
						System.out.println("Movephase");
						playersTurn = true;

					}
					changeGamePhase();

				} else {  /** ONLY FOR DEBUGGING! **/
					String[] player = new String[]{"PLAYER","COMPUTER"};
					int c = 0;
					for(Territory terr : terrList) {
						terr.changeArmycount(1);
						terr.changeOwner(player[c % 2]);
						if(c%2==1) cpu.acquire(terr);
						c++;
					}
				}
			}




			for(Territory terr : terrList) {
				if(terr.containsClick(p)) {
					clickedTerritory = terr;
				}
			}
			if(clickedTerritory!=null) {
				switch (mouseButton) {
					case (1): // Left

						mouseLeft(clickedTerritory);
						break;
					case (3): // Right

						mouseRight(clickedTerritory);
						break;
				}
			}

		}

		private void mouseRight(Territory clickedTerritory) {
			switch(gamePhase) {
				case(0): // Acquire

					break;
				case(1): // Reinforce

					break;
				case(2): // Conquer

					break;
				case(3): // Move


					if(playersTurn && clickedTerritory.getOwner().equals("PLAYER")) {

						if (selectedTerritory == null) {

							clickedTerritory.selectForTransfer();
							selectedTerritory = clickedTerritory;

						} else if (selectedTerritory.equals(clickedTerritory)) {

							selectedTerritory.deselectForTransfer();
							selectedTerritory = null;

						} else if (clickedTerritory.isNeighbour(selectedTerritory) && selectedTerritory.getArmyCount() > 1) {

							moveArmy(selectedTerritory, clickedTerritory);

						}
					}
					break;
				case(4):
					break;
			}
		}

		private void mouseLeft(Territory clickedTerritory) {

			switch(gamePhase) {

				case(0): // Acquire
					if(Objects.equals(clickedTerritory.getOwner(), null)) {

						clickedTerritory.setArmyCount(1);
						clickedTerritory.changeOwner("PLAYER");
						System.out.println("The player has occupied " + clickedTerritory.getName() + " with 1 army!");

						playersTurn = false;
						cpu.acquire(terrList);
						playersTurn = true;

					}

					if(wholeMapIsOccupied()) {
						startPhase = false;
						startRound();
						gamePhase = 1;
					}

					break;

				case(1): // Reinforce

					if(playerReinforcements==0) {

						playersTurn = false;
						gamePhase = 2;
						cpu.reinforce(computerReinforcements);


					}

					if(playersTurn && Objects.equals(clickedTerritory.getOwner(), "PLAYER")) {

						clickedTerritory.changeArmycount(1);
						playerReinforcements -= 1;

					}
					playersTurn = true;

					break;


				case(2): // Conquer

					if(playersTurn) {
						if(selectedTerritory==null && Objects.equals(clickedTerritory.getOwner(), "PLAYER")) {
							clickedTerritory.selectForAttack();
							selectedTerritory = clickedTerritory;

						} else if(clickedTerritory.equals(selectedTerritory)) {

							clickedTerritory.deselectForAttack();
							selectedTerritory = null;

						} else if(clickedTerritory.getNeighbours().contains(selectedTerritory) && !Objects.equals(clickedTerritory.getOwner(), "PLAYER") && selectedTerritory.getArmyCount() > 1) {

							if(dice.pressTheAttack(selectedTerritory, clickedTerritory)) {
								cpu.removeTerritory(clickedTerritory);
								selectedTerritory = null;
							}

						}
						int c = 0;
						for(Territory terr : terrList) {
							if(Objects.equals(terr.getOwner(), "PLAYER") && terr.getArmyCount() > 1) 	c++; // Possible attacks?
						}
						if(c == 0) {
							playersTurn = false;
							cpu.conquer();
							gamePhase = 3;
						}

					}

					break;


				case(3): // Move

					if(movableTerritories==null) {
						movableTerritories = getMovableTerritories("PLAYER");
					}

					if (playersTurn && Objects.equals(clickedTerritory.getOwner(),"PLAYER")) {

						if(selectedTerritory == null) {

							selectedTerritory = clickedTerritory;
							selectedTerritory.selectForTransfer();

						} else if(selectedTerritory != null && clickedTerritory.equals(selectedTerritory)) {

							selectedTerritory.deselectForTransfer();
							selectedTerritory = null;

						}

					}

					break;


			}
		}

		public void startRound() {
			if(!startPhase) {
				int terrCounter = 0;

				for (Territory terr : terrList) { //iterates through all territories, gives the player 1 reinforcement for every 3 territories
					if(Objects.equals(terr.getOwner(), "PLAYER")) {
						terrCounter += 1;
						if(terrCounter == 3) {
							playerReinforcements += 1;
							terrCounter = 0;
						}
					}
				}

				for (Continent cont : contList) { //iterates through all continents, gives the player 3 reinforcements for every owned continent
					if(cont.completeOwner("PLAYER")) {
						playerReinforcements += cont.getBonus();
					}
					if(cont.completeOwner("COMPUTER")) {
						computerReinforcements += cont.getBonus();
					}
				}

				terrCounter = 0;

				for (Territory terr : terrList) { //iterates through all territories, gives the computer 1 reinforcement for every 3 territories
					if(Objects.equals(terr.getOwner(), "COMPUTER")) {
						terrCounter += 1;
						if(terrCounter == 3) {
							computerReinforcements += 1;
							terrCounter = 0;
						}
					}
				}

				System.out.println();
				System.out.println("Round " + roundNumber + "!");
				System.out.println("__ " + playerReinforcements + " <- PR; CR -> " + computerReinforcements); //debug
				System.out.println("Reinforcements have arrived! You got " + playerReinforcements + " reinforcements. Use them wisely!");
				playersTurn = true;
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

	private void changeGamePhase() {

		if(checkEndOfGame()) {
			gamePhase = 4;
			System.out.println("GAME OVER " + terrList.get(0).getOwner() + " WON");
			playersTurn = false;
		} else if(gamePhase==3) {
			gamePhase = 1;
			playersTurn = true;
		} else {
			gamePhase += 1;
			playersTurn = true;
		}
		selectedTerritory = null;
		for(Territory terr : terrList) {
			terr.deselect();
		}

	}

	private void moveArmy(Territory selectedTerritory, Territory clickedTerritory) {

		if(movableTerritories.contains(selectedTerritory) || movableTerritories.contains(clickedTerritory)) {
			selectedTerritory.changeArmycount(-1);
			clickedTerritory.changeArmycount(1);
			System.out.println("Transfer: " + selectedTerritory.getName() + " -> " + clickedTerritory.getName());
		}

	}

	/**
	 *
	 * @param name of Player
	 * @return all possible source territories
	 */
	public ArrayList<Territory> getMovableTerritories(String name) {
		ArrayList<Territory> out = new ArrayList<>();
		for(Territory terr : terrList) {
			if(terr.getOwner().equals(name) && terr.getArmyCount() > 1) {
				out.add(terr);
			}
		}
		return out;
	}


	public boolean checkEndOfGame() {
		String name = terrList.get(0).getOwner();
		for(Territory terr : terrList) {
			if(!name.equals(terr.getOwner())) return false;
		}
		return true;
	}

}