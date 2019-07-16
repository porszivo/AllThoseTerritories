import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;


/**
 *
 * Territory class
 */


public class Territory extends JComponent {
	private String name;
	private ArrayList<Territory> neighbours;
	private int[] capital;
	private int armyCount;
	private String owner;
	private ArrayList<Polygon> polylist = new ArrayList<Polygon>();
	private ArrayList<int[]> polyXCoords;
	private ArrayList<int[]> polyYCoords;
	private Continent motherContinent;
	private ControlCentre controlCentre;
	private boolean isSelectedForAttack;
	private boolean isSelectedForTransfer;

	public Territory(String name, int[] coords, ControlCentre controlCentre) {
		super();
		this.controlCentre = controlCentre;
		this.name = name;
		this.owner = null;
		this.polyXCoords = new ArrayList<>();
		this.polyYCoords = new ArrayList<>();
		addPatch(coords);
		this.armyCount = 0;
		this.neighbours = new ArrayList<>();
	}

	public void selectForAttack() {
		this.isSelectedForAttack = true;
		this.repaint();
	}

	public void deselectForAttack() {
		this.isSelectedForAttack = false;
		this.repaint();
	}

	public boolean isSelectedForAttack() {
		return this.isSelectedForAttack;
	}

	public void selectForTransfer() {
		this.isSelectedForTransfer = true;
		repaint();
	}

	public void deselectForTransfer() {
		this.isSelectedForTransfer = false;
		repaint();
	}

	public boolean isSelectedForTransfer() {
		return this.isSelectedForTransfer;
	}

	public void setMotherContinent(Continent c) {
		this.motherContinent = c;
	}

	public Continent getMotherContinent() {
		return this.motherContinent;
	}

	public void setCapital(int[] coords) {
		this.capital = coords;
        this.setBounds(capital[0] - 1000, capital[1] - 1000, 2000, 2000);
	}

	public String getName() {
		return name;
	}

	public void addPatch(int[] patch) {
		int[] polyXCoordsSingle = new int[patch.length / 2];
		int[] polyYCoordsSingle = new int[patch.length / 2];
		int j = 0;
		for (int i = 0; i < patch.length; i++) {
			if (i % 2 == 0) {
				polyXCoordsSingle[j] = patch[i];
			} else {
				polyYCoordsSingle[j] = patch[i];
				j++;
			}
		}
		polyXCoords.add(polyXCoordsSingle);
		polyYCoords.add(polyYCoordsSingle);
		Polygon polytemp = new Polygon();
		for (int i = 0; i < polyXCoordsSingle.length; i++) {
			polytemp.addPoint(polyXCoordsSingle[i], polyYCoordsSingle[i]);
		}
		polylist.add(polytemp);

	}

	public ArrayList<int[]> getPolyYCoords() {
		return polyYCoords;
	}

	public int getCapitalX() {
		return capital[0];
	}

	public int getCapitalY() {
		return capital[1];
	}

	public void setNeighbours(ArrayList<Territory> neighbours) {
		this.neighbours = neighbours;
	}

	public ArrayList<Territory> getNeighbours() {
		return neighbours;
	}

	public void addNeighbour(Territory t) {
		this.neighbours.add(t);
	}

	public boolean isNeighbour(Territory t) {
		for (Territory neighbour : neighbours) {
			if(neighbour == t) return true;
		}

		return false;
	}

	public int getArmyCount() {
		return this.armyCount;
	}

	public void setArmyCount(int v) {
		this.armyCount = v;
	}

	public String getOwner() {
		return owner;
	}

	public void paintComponent(Graphics gf) {
		super.paintComponent(gf);
		Graphics2D g = (Graphics2D) gf;
		for (Polygon polygon : polylist) {
			Color c = Color.gray;
			if(Objects.equals(this.owner, "PLAYER")) c = new Color(0, 170, 0);
			if(Objects.equals(this.owner, "PLAYER") && this.isSelectedForAttack) c = new Color(250, 200, 100);
			if(Objects.equals(this.owner, "COMPUTER")) c = new Color(210, 0, 0);
			if(Objects.equals(this.owner, "COMPUTER") && this.isSelectedForAttack) c = new Color(255, 140, 0);
			g.setColor(c);
			g.fillPolygon(polygon);
			g.setColor(new Color(110, 77, 87));
			g.setStroke(new BasicStroke(2));
			g.drawPolygon(polygon);
		}

		g.setColor(new Color(150, 224, 255));
		g.setStroke(new BasicStroke(1));
		g.drawRoundRect(capital[0] - 7, capital[1] - 15, 20, 20, 5, 5);

		Font font1 = new Font("Verdana", 0, 11);
		g.setFont(font1);
		g.drawString(Integer.toString(armyCount), capital[0], capital[1]);
		Font font2 = new Font("Verdana", 0, 9);
		g.setFont(font2);
		g.drawString(name, capital[0] + 15, capital[1] + 0);

	}

	public void changeArmyCount(int v) {
		armyCount = armyCount + v;
		this.repaint();
	}

	public boolean containsClick(Point p) {
		for (Polygon polygon : polylist) {
			if(polygon.contains(p)) {
                return true;
			}
		}
		return false;
	}

	public void changeOwner(String ow) {
		this.owner = ow;
		this.repaint();
	}

	public void deselect() {
		if(this.isSelectedForAttack) {
			this.deselectForAttack();
		}
		if(this.isSelectedForTransfer) {
			this.deselectForTransfer();
		}
	}
}