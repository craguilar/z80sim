package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.GeneraMnemonicos;
import simz80.processing.Memoria;

public class VentanaMnemonicos extends JFrame implements ActionListener {

  GraficoMnemonicos GM;
  Memoria M;
  GeneraMnemonicos GENM;

  JButton ENVIAR = new JButton("ENVIAR POS");
  JTextField POSICION = new JTextField(5);
  JPanel CONTENEDOR = new JPanel();
  JLabel PosicionMemoria = new JLabel(" POSICION EN LA QUE QUIERES CARGAR = ");

  JButton SIGUIENTE = new JButton("SIGUIENTE");
  JButton ATRAS = new JButton("ATRAS");

  public VentanaMnemonicos(GeneraMnemonicos M1, Memoria M2) {
    super("                                      ..::::MNEMONICOS::::..");
    setSize(new Dimension(500, 500));
    setResizable(false);
    setLocation(new Point(50, 50));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    M = M2;
    GENM = M1;
    GM = new GraficoMnemonicos(GENM, M);
    construlle();
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    setVisible(true);
  }

  final public void construlle() {
    JPanel TITULO = new JPanel(new GridLayout(5, 1));
    JPanel VNM = new JPanel();
    JPanel Botones = new JPanel(new GridLayout(2, 1));

    JPanel Intermedio = new JPanel();
    JLabel MNENMO = new JLabel(
        "                                                       VISTA DE MNEMONICOS");

    SIGUIENTE.addActionListener(this);
    ATRAS.addActionListener(this);
    ENVIAR.addActionListener(this);

    Intermedio.add(ATRAS);
    Intermedio.add(SIGUIENTE);

    VNM.add(GM);
    TITULO.add(MNENMO);

    CONTENEDOR.add(PosicionMemoria);
    CONTENEDOR.add(POSICION);
    CONTENEDOR.add(ENVIAR);

    Botones.add(CONTENEDOR);
    Botones.add(Intermedio);

    add(TITULO, BorderLayout.NORTH);
    add(VNM, BorderLayout.CENTER);
    add(Botones, BorderLayout.SOUTH);

  }

  public void actionPerformed(ActionEvent ae) {

    if (ae.getSource() == SIGUIENTE)
      GM.REPINTAADELANTE();
    if (ae.getSource() == ATRAS)
      GM.REPINTAATRAS();
    if (ae.getSource() == ENVIAR) {
      try {
        int pos = ValidaString(POSICION.getText());
        if (pos >= 0 && pos <= 65534)
          GM.REPINTACONDICIONAL(pos);
      } catch (NumberFormatException NFE) {
        //RECOGE ERROR
      }
      POSICION.setText("");
    }

  }

  public int ValidaString(String num) {
    num = num.trim();
    num = "000" + num;
    int i = (Integer.decode("0x" + num)).intValue();
    return (int) i;
  }

}
