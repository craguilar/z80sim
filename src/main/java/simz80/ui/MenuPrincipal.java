package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.img.Imagen1;
import simz80.processing.GeneraMnemonicos;
import simz80.processing.HexaMnem;
import simz80.processing.Memoria;
import simz80.processing.Operaciones;

public class MenuPrincipal extends JFrame implements ActionListener {
  ////
  static Memoria M = new Memoria();
  ////
  JButton[] B = new JButton[7];
  GeneraMnemonicos GM;
  HexaMnem HM;
  Operaciones OP;

  public MenuPrincipal() {
    super("                                      ..::::SIM Z80::::..");
    setSize(new Dimension(560, 500));
    setResizable(false);
    setLocation(new Point(50, 50));
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    GM = new GeneraMnemonicos();
    HM = new HexaMnem(M);
    OP = new Operaciones(M);
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    construlle();
  }

  private void construlle() {

    JPanel ARRIBA = new JPanel(new GridLayout(4, 0));
    JPanel MEDIO = new JPanel(new GridLayout(1, 3));
    JPanel INFERIOR = new JPanel(new GridLayout(1, 3));
    INFERIOR.setBackground(Color.cyan);

    JPanel CBotonesP = new JPanel(new GridLayout(5, 1, 0, 10));
    JPanel CBotonesAS = new JPanel(new GridLayout(1, 2, 5, 10));

    JLabel NOMBRE = new JLabel(
        "                                                      :::::::::::::::::::::::::::SIMULADOR Z80::::::::::::::::::::::::::::::");
    B[0] = new JButton("CARGAR A MEMORIA");
    B[0].addActionListener(this);
    B[1] = new JButton("REVISAR MEMORIA");
    B[1].addActionListener(this);
    B[2] = new JButton("VER MNEMONICOS");
    B[2].addActionListener(this);
    B[3] = new JButton("IR A SIMULACION");
    B[3].addActionListener(this);
    B[4] = new JButton("CREA ARCHIVO ASM");
    B[4].addActionListener(this);
    B[5] = new JButton("SALIR");
    B[5].addActionListener(this);
    B[6] = new JButton("AYUDA");
    B[6].addActionListener(this);

    for (int i = 0; i < 5; i++)
      CBotonesP.add(B[i]);

    CBotonesAS.add(B[5]);
    CBotonesAS.add(B[6]);

    ARRIBA.add(NOMBRE);

    MEDIO.add(new Imagen1());
    MEDIO.add(CBotonesP);
    MEDIO.add(new Imagen1());

    INFERIOR.add(new JPanel());
    INFERIOR.add(CBotonesAS);
    INFERIOR.add(new JPanel());

    add(ARRIBA, BorderLayout.NORTH);
    add(MEDIO, BorderLayout.CENTER);
    add(INFERIOR, BorderLayout.SOUTH);

    setVisible(true);
  }

  public void actionPerformed(ActionEvent ae) {
    VentanaCarga v;
    VentanaMemoria v1;
    VentanaMnemonicos v2;
    VentanaMenuSimulacion v3;
    VentanaCreaASM v4;
    VentanaAyuda v6;
    if (ae.getSource() == B[0])///////////CARGA{
      v = new VentanaCarga();
    if (ae.getSource() == B[1])///////////REVISA MEMORIA
      v1 = new VentanaMemoria(M);
    if (ae.getSource() == B[2])/////////VER NMEMONICOS
      v2 = new VentanaMnemonicos(GM, M);
    if (ae.getSource() == B[3])/////////SIMULAR
      v3 = new VentanaMenuSimulacion(M);
    if (ae.getSource() == B[4])/////////CREA ARCHIVO ASM
      v4 = new VentanaCreaASM(M);
    if (ae.getSource() == B[5])/////////SALIR
      System.exit(0);
    if (ae.getSource() == B[6])
      v6 = new VentanaAyuda();

  }

}
