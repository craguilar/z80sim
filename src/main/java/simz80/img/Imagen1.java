package simz80.img;

import java.awt.*;
import java.net.URL;

import javax.swing.*;

public class Imagen1 extends JPanel {
  JButton B;

  public Imagen1() {
    setSize(new Dimension(200, 200));
    setVisible(true);
    repaint();
  }

  public void paintComponent(Graphics g) {
    ImageIcon IMAGE = new ImageIcon(getFileFromResources("DOS.jpg"));
    g.drawImage(IMAGE.getImage(), 0, 0, 60, 60, this);
    B = new JButton(IMAGE);
    add(B);
    super.paintComponent(g);
  }

  private URL getFileFromResources(String fileName) {

    ClassLoader classLoader = getClass().getClassLoader();

    URL resource = classLoader.getResource(fileName);
    if (resource == null) {
      throw new IllegalArgumentException("file is not found!");
    }
    return resource;
  }

}
