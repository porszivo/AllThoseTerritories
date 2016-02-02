import javax.swing.*;

// Main Class

public class Allthoseterritories extends JPanel {

	public static void main(String[] args) {

		JFrame frame = new JFrame("All Those Territories");
        GameBoard gb = new GameBoard(args[0]);
		Menu men = new Menu();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(gb);
		frame.setSize(1250, 650);
		frame.setVisible(true);

		JFrame menu = new JFrame("Menu");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setContentPane(men);
		menu.setSize(200, 650);
		frame.setVisible(true);


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
