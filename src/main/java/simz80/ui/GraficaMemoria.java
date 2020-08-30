package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.Memoria;

public class GraficaMemoria extends JPanel implements ActionListener {

  Memoria MEM;
  int ESTIM = 0;
  JLabel[] ETIQUETAx = new JLabel[16];
  JPanel VECTORPOSICIONES = new JPanel(new GridLayout(16, 16));
  JTextField[] CASILLA = new JTextField[256];
  JLabel[] ETIQUETAy = new JLabel[16];
  JButton ADELANTE = new JButton("ADELANTE");
  JButton ATRAS = new JButton("ATRAS");

  public GraficaMemoria(Memoria M) {
    Dimension RES = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    if (RES.width == 800)
      setSize(new Dimension(700, 300));
    if (RES.width == 1024)
      setSize(new Dimension(700, 580));
    if (RES.width == 1280)
      setSize(new Dimension(500, 650));
    else
      setSize(new Dimension(500, 600));
    setLocation(new Point(50, 50));
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    this.MEM = M;
    construlle();

  }

  final public void construlle() {
    ADELANTE.addActionListener(this);
    ATRAS.addActionListener(this);

    String m;
    JPanel BOTONES = new JPanel();
    JPanel Marco = new JPanel();
    JPanel EJEX = new JPanel();
    JPanel EJEY = new JPanel(new GridLayout(17, 1, 0, 12));

    EJEX.add(new JLabel("                "));
    for (int i = 0; i < 16; i++) {
      ETIQUETAx[i] = new JLabel("" + Integer.toHexString(i).toUpperCase() + "      ");
      EJEX.add(ETIQUETAx[i]);
    }
    // EJEY.add(EJEX);
    for (int i = 0; i < 16; i++) {
      ETIQUETAy[i] = new JLabel("00" + Integer.toHexString(i).toUpperCase() + "0    ");
      EJEY.add(ETIQUETAy[i]);
    }
    for (int i = 0; i < 256; i++) {
      if (MEM.GetMemoria(i) < 0x10)
        m = "0" + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
      else
        m = "" + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
      CASILLA[i] = new JTextField(m);
      CASILLA[i].setEditable(false);
      VECTORPOSICIONES.add(CASILLA[i]);
    }
    JPanel PRIMARIO = new JPanel();

    BOTONES.add(ATRAS);
    BOTONES.add(ADELANTE);

    PRIMARIO.add(EJEX);
    Marco.add(EJEY, BorderLayout.WEST);
    Marco.add(VECTORPOSICIONES, BorderLayout.CENTER);
    PRIMARIO.add(Marco);
    PRIMARIO.add(BOTONES, BorderLayout.SOUTH);
    add(PRIMARIO);
    setVisible(true);

  }

  public void REPINTA(Memoria mem) {
    MEM = mem;
    String m;
    int count = 0;
    for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
      if (MEM.GetMemoria(i) == 0)
        m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "";
      else
        m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "";
      CASILLA[count].setText(m);
      count++;
    }

  }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == ADELANTE) {
      String m;
      int count = 0;
      ESTIM++;
      String cadenaDesicion = "";
      if (ESTIM == 0)
        cadenaDesicion = "00";
      if (16 * ESTIM + 16 < 256 && ESTIM != 0)
        cadenaDesicion = "0";
      if (16 * ESTIM + 16 < 4096 && 16 * ESTIM + 16 >= 256)
        cadenaDesicion = "";
      for (int i = 16 * ESTIM; i < 16 * ESTIM + 16; i++) {
        ETIQUETAy[count].setText(cadenaDesicion + Integer.toHexString(i).toUpperCase() + "0    ");
        count++;
      }
      count = 0;
      for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
        if (MEM.GetMemoria(i) == 0)
          m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "";
        else
          m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "";
        CASILLA[count].setText(m);
        count++;
      }

    }
    if (ae.getSource() == ATRAS) {
      int count = 0;
      String m;
      ESTIM = ESTIM - 1;
      if (ESTIM < 0)
        ESTIM = 0;
      String cadenaDesicion = "";
      if (ESTIM == 0)
        cadenaDesicion = "00";
      if (16 * ESTIM + 16 < 256 && ESTIM != 0)
        cadenaDesicion = "0";
      if (16 * ESTIM + 16 < 4096 && 16 * ESTIM + 16 >= 256)
        cadenaDesicion = "";
      for (int i = 16 * ESTIM; i < 16 * ESTIM + 16; i++) {
        ETIQUETAy[count].setText(cadenaDesicion + Integer.toHexString(i).toUpperCase() + "0    ");
        count++;
      }
      count = 0;
      for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
        if (MEM.GetMemoria(i) == 0)
          m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "";
        else
          m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "";
        CASILLA[count].setText(m);
        count++;

      }

    }
  }

}