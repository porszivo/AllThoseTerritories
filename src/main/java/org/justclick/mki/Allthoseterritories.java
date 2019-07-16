import javax.swing.*;

// Main Class

public class AllThoseTerritories extends JPanel {



	public static void main(String[] args) {

		JFrame mapFrame = new JFrame("Game Map - ATT");
		ControlCentre controlCentre = new ControlCentre();
		GameBoard gameBoard = new GameBoard(args[0], controlCentre);
		controlCentre.assignGameBoard(gameBoard);
		mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapFrame.setContentPane(gameBoard);
		mapFrame.setSize(1250, 650);
		mapFrame.setLocation(80, 80);
		mapFrame.setVisible(true);

		JFrame ccFrame = new JFrame("Control Centre - ATT");
        ccFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ccFrame.setContentPane(controlCentre);
		ccFrame.setSize(1250, 240);
		ccFrame.setLocation(80, 730);
		ccFrame.setAlwaysOnTop(true);
		ccFrame.setVisible(true);


		//frame.add(new InfoText());
		System.out.println("----------- THE GAME HAS STARTED! ------------");
		System.out.println("The start phase is active. Click on a territory you want to occupy!");
		System.out.println();
	}

	/*static class InfoText extends JPanel {
		private String text;

		public InfoText() {
			text = "TEST";
			setBorder(BorderFactory.createLineBorder(Color.black, 3));
		}

		public Dimension getPreferredSize() {
			return new Dimension(250, 200);
		}

		public void setText(String s) {
			this.text = s;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			// Draw Text
			g.drawString(this.text, 10, 20);
		}
	}*/

}
