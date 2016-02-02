import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Marcel on 29.01.2016.
 */
public class Button extends JComponent {

    private String value;

    public Button(String text) {
        this.value = text;
        this.setBounds(10,10,50,20);
    }

    public void paintComponent(Graphics gf) {
        super.paintComponent(gf);
        Graphics2D g = (Graphics2D) gf;
        Rectangle2D button = new Rectangle2D.Double(10, 10, 50,20);
        g.setColor(Color.RED);
        g.draw(button);
        g.fill(button);
        g.setColor(Color.WHITE);
        g.drawString(value, 20,25);
    }




}
