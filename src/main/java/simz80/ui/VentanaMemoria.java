package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.Memoria;

public class VentanaMemoria extends JFrame implements ActionListener {

  Memoria MEM;
  int ESTIM = 0;
  JLabel[] ETIQUETAx = new JLabel[16];
  JPanel VECTORPOSICIONES = new JPanel(new GridLayout(16, 17));
  JTextField[] CASILLA = new JTextField[258];
  JLabel[] ETIQUETAy = new JLabel[16];
  JButton ADELANTE = new JButton("ADELANTE");
  JButton ATRAS = new JButton("ATRAS");

  JPanel INFERIOR = new JPanel(new GridLayout(1, 2));

  JButton ENVIAR = new JButton("ENVIAR DATOS");
  JPanel CONTENEDORENVIARINICI = new JPanel(new GridLayout(2, 1));

  JPanel TOTALINICI = new JPanel(new GridLayout(1, 2));
  JPanel INICI = new JPanel(new GridLayout(2, 2));
  JTextField INICIO = new JTextField("INICIO", 6);
  JLabel BEG = new JLabel("POS INICIO");
  JTextField FINALIZAR = new JTextField("FINAL", 6);
  JLabel END = new JLabel("POS FINAL");
  JButton INICIALIZARBOT = new JButton("INICIALIZAR");

  String aux = "";
  char[] CARAC = new char[257];
  JTextArea ASCII = new JTextArea();
  JPanel CONTENEDORINFERIOR = new JPanel(new GridLayout(1, 2));

  public VentanaMemoria(Memoria M) {
    super("                                      ..:::: REVISION DE MEMORIA::::..");
    Dimension RES = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    if (RES.width == 800)
      setSize(new Dimension(700, 600));
    if (RES.width == 1024)
      setSize(new Dimension(700, 750));
    if (RES.width == 1280)
      setSize(new Dimension(1000, 770));
    else
      setSize(new Dimension(1200, 770));
    setResizable(false);
    setLocation(new Point(0, 0));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    this.MEM = M;
    construlle();

  }

  private void construlle() {
    ASCII.setEditable(false);
    ADELANTE.addActionListener(this);
    ATRAS.addActionListener(this);
    ENVIAR.addActionListener(this);
    INICIALIZARBOT.addActionListener(this);

    String m;
    JPanel BOTONES = new JPanel();
    JPanel Marco = new JPanel();
    JPanel EJEX = new JPanel();
    JPanel EJEY = new JPanel(new GridLayout(17, 1, 0, 12));

    EJEX.add(new JLabel("                    "));
    for (int i = 0; i < 16; i++) {
      ETIQUETAx[i] = new JLabel(" " + Integer.toHexString(i).toUpperCase() + "        ");
      EJEX.add(ETIQUETAx[i]);
    }
    EJEY.add(EJEX);

    for (int i = 0; i < 16; i++) {
      ETIQUETAy[i] = new JLabel("00" + Integer.toHexString(i).toUpperCase() + "0    ");
      EJEY.add(ETIQUETAy[i]);
    }
    for (int i = 0; i < 256; i++) {
      if (MEM.GetMemoria(i) == 0)
        m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
      else
        m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
      CASILLA[i] = new JTextField(m);
      VECTORPOSICIONES.add(CASILLA[i]);
    }
    int entero;
    for (int i = 0; i < 256; i++) {
      try {
        entero = Integer.decode("0x" + CASILLA[i].getText().trim()).intValue();
      } catch (NumberFormatException NFE) {
        entero = 0;
      }
      if (i % 16 == 0 && i != 0) {
        aux = aux + "\n\n" + (char) entero;
      } else {
        aux = aux + (char) entero;
      }
    }
    aux = "::::::::::CODIGO ASCII::::::::::\n\n" + aux;
    ASCII.setText(aux);

    JPanel PRIMARIO = new JPanel();

    BOTONES.add(ATRAS);
    BOTONES.add(ADELANTE);

    INICI.add(BEG);
    INICI.add(INICIO);
    INICI.add(END);
    INICI.add(FINALIZAR);

    TOTALINICI.add(INICIALIZARBOT);
    TOTALINICI.add(INICI);

    CONTENEDORENVIARINICI.add(TOTALINICI);
    CONTENEDORENVIARINICI.add(ENVIAR);
    CONTENEDORINFERIOR.add(CONTENEDORENVIARINICI);
    PRIMARIO.add(EJEX);
    Marco.add(EJEY, BorderLayout.WEST);
    Marco.add(VECTORPOSICIONES, BorderLayout.NORTH);
    PRIMARIO.add(Marco);
    PRIMARIO.add(BOTONES);
    add(PRIMARIO);
    add(ASCII, BorderLayout.EAST);
    add(CONTENEDORINFERIOR, BorderLayout.SOUTH);
    setVisible(true);

  }

  public void actionPerformed(ActionEvent ae) {
    String m;
    int count;
    int entero;

    if (ae.getSource() == ADELANTE) {
      count = 0;
      for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
        MEM.SetMemoria(i, ValidaString(CASILLA[count].getText()));
        count++;
      }
      MenuPrincipal.M = MEM;
      count = 0;
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
        if (MEM.GetMemoria(i) <= 0xf)
          m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
        else
          m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
        try {
          CASILLA[count].setText(m);
        } catch (NullPointerException NPE) {

        }
        count++;
      }
      //////////CUIDADO
      aux = "";
      for (int i = 0; i < 256; i++) {
        try {
          entero = Integer.decode("0x" + CASILLA[i].getText().trim()).intValue();
        } catch (NumberFormatException NFE) {
          entero = 0;
        }
        if (i % 16 == 0 && i != 0) {
          aux = aux + "\n\n" + (char) entero;
        } else {
          aux = aux + (char) entero;
        }
      }
      aux = "::::::::::CODIGO ASCII::::::::::\n\n" + aux;
      ASCII.setText(aux);
      //////////////CUIDADO

    }
    if (ae.getSource() == ATRAS) {
      count = 0;
      for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
        MEM.SetMemoria(i, ValidaString(CASILLA[count].getText()));
        count++;
      }

      MenuPrincipal.M = MEM;
      count = 0;
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
        if (MEM.GetMemoria(i) < 0xf)
          m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
        else
          m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
        CASILLA[count].setText(m);
        count++;
      }
      ///////////CUIADO
      aux = "";
      for (int i = 0; i < 256; i++) {
        try {
          entero = Integer.decode("0x" + CASILLA[i].getText().trim()).intValue();
        } catch (NumberFormatException NFE) {
          entero = 0;
        }
        if (i % 16 == 0 && i != 0) {
          aux = aux + "\n\n" + (char) entero;
        } else {
          aux = aux + (char) entero;
        }
      }
      aux = "::::::::::CODIGO ASCII::::::::::\n\n" + aux;
      ASCII.setText(aux);
      ///////////////////
    }
    if (ae.getSource() == ENVIAR) {
      count = 0;
      for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
        try {
          MEM.SetMemoria(i, ValidaString(CASILLA[count].getText()));
        } catch (NullPointerException NFE) {

        } catch (ArrayIndexOutOfBoundsException aer) {

        }
        count++;
      }

      aux = "";
      for (int i = 0; i < 256; i++) {
        try {
          entero = Integer.decode("0x" + CASILLA[i].getText().trim()).intValue();
        } catch (NumberFormatException NFE) {
          entero = 0;
        }
        if (i % 16 == 0 && i != 0) {
          aux = aux + "\n\n" + (char) entero;
        } else {
          aux = aux + (char) entero;
        }
      }
      aux = "::::::::::CODIGO ASCII::::::::::\n\n" + aux;
      ASCII.setText(aux);
      MenuPrincipal.M = MEM;
    }
    if (ae.getSource() == INICIALIZARBOT) {
      int inicio;
      int fin;
      try {
        inicio = Valida(INICIO.getText());
        fin = Valida(FINALIZAR.getText());
      } catch (NumberFormatException NFE) {
        inicio = 0;
        fin = 0;
      }
      if (fin > 0xffff)
        fin = 0xffff;
      for (int i = inicio; i <= fin; i++) {
        MEM.SetMemoria(i, (byte) 0);
      }
      count = 0;
      for (int i = 256 * ESTIM; i <= 256 * ESTIM + 255; i++) {
        if (MEM.GetMemoria(i) <= 0xf)
          m = "  0" + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
        else
          m = "  " + Integer.toHexString(MEM.GetMemoria(i)) + "  ";
        CASILLA[count].setText(m);
        count++;
      }

      MenuPrincipal.M = MEM;
      aux = "";
      for (int i = 0; i < 256; i++) {
        try {
          entero = Integer.decode("0x" + CASILLA[i].getText().trim()).intValue();
        } catch (NumberFormatException NFE) {
          entero = 0;
        }
        if (i % 16 == 0 && i != 0) {
          aux = aux + "\n\n" + (char) entero;
        } else {
          aux = aux + (char) entero;
        }
      }
      aux = "::::::::::CODIGO ASCII::::::::::\n\n" + aux;
      ASCII.setText(aux);
    }
  }

  public byte ValidaString(String num) {
    num = num.trim();
    num = "000" + num;
    String Sub;
    if (num.contains("0") || num.contains("1") || num.contains("2") || num.contains("3") || num
        .contains("4") || num.contains("5") || num.contains("6") || num.contains("7") || num
        .contains("8") || num.contains("9") || num.contains("A") || num.contains("B") || num
        .contains("C") || num.contains("D") || num.contains("E") || num.contains("F"))
      Sub = num.substring(num.length() - 2, num.length() - 1);
    else
      Sub = "00";
    int i = (Integer.decode("0x" + num)).intValue();
    return (byte) i;
  }

  public int Valida(String num) {
    num = num.trim();
    num = "000" + num;
    int i = (Integer.decode("0x" + num)).intValue();
    return (int) i;
  }

}
