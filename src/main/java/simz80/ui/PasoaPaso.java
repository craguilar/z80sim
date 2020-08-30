package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.Memoria;
import simz80.processing.Simular;

public class PasoaPaso extends JFrame implements ActionListener {
  /////////////////// ATRIBUTOS DE PROCESAMIENTO/////////////////////
  ////////////////////////////////////////////////////////////////
  GraficaMemoria GM;
  GraficaRegistros GR;
  Memoria M;
  Simular SIMULADOR;
  ///////////////////////////////////////////////////////////////////
  JTextArea INSTRUCCIONES = new JTextArea();
  JButton SIGUIENTEINSTRUCCION = new JButton("SIGUIENTE INSTRUCCION");
  int posicion;
  ///////////////////////////////////////////////////////////////////
  JTextField[] REGISTROS = new JTextField[11];
  JLabel[] REGISTROSNOMBRE = new JLabel[11];
  JLabel MODIFICA = new JLabel("...::::::::::::::::::::::MODIFICA REGISTROS::::::::::::::::...");
  JButton ENVIA = new JButton(":::::ENVIAR ACTUALIZACION:::::");
  ////////////PARA EL STACK //////////////////
  JPanel PILA = new JPanel(new GridLayout(1, 11));
  JLabel[] DATOSAPILADOS = new JLabel[11];
  String aux = "";

  public PasoaPaso(Memoria M1, int pos) {
    super("                                      ..::::SIMULACION PASO A PASO::::..");
    setSize(new Dimension(1250, 600));
    setResizable(false);
    setLocation(new Point(05, 05));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    M = M1;
    SIMULADOR = new Simular(M1);
    GM = new GraficaMemoria(M);
    GR = new GraficaRegistros(M);
    posicion = pos;
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    construlle();
  }

  final public void construlle() {
    ENVIA.addActionListener(this);
    INSTRUCCIONES.setEditable(false);
    SIGUIENTEINSTRUCCION.addActionListener(this);

    JPanel ACTUALIZAREGISTROS = new JPanel(new GridLayout(6, 4));
    REGISTROSNOMBRE[0] = new JLabel("A :");
    REGISTROS[0] = new JTextField(Integer.toHexString(M.GetA()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[0]);
    ACTUALIZAREGISTROS.add(REGISTROS[0]);
    REGISTROSNOMBRE[1] = new JLabel("B :");
    REGISTROS[1] = new JTextField(Integer.toHexString(M.GetB()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[1]);
    ACTUALIZAREGISTROS.add(REGISTROS[1]);
    REGISTROSNOMBRE[2] = new JLabel("C :");
    REGISTROS[2] = new JTextField(Integer.toHexString(M.GetC()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[2]);
    ACTUALIZAREGISTROS.add(REGISTROS[2]);
    REGISTROSNOMBRE[3] = new JLabel("D :");
    REGISTROS[3] = new JTextField(Integer.toHexString(M.GetD()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[3]);
    ACTUALIZAREGISTROS.add(REGISTROS[3]);
    REGISTROSNOMBRE[4] = new JLabel("E :");
    REGISTROS[4] = new JTextField(Integer.toHexString(M.GetE()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[4]);
    ACTUALIZAREGISTROS.add(REGISTROS[4]);
    REGISTROSNOMBRE[5] = new JLabel("H :");
    REGISTROS[5] = new JTextField(Integer.toHexString(M.GetH()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[5]);
    ACTUALIZAREGISTROS.add(REGISTROS[5]);
    REGISTROSNOMBRE[6] = new JLabel("L :");
    REGISTROS[6] = new JTextField(Integer.toHexString(M.GetL()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[6]);
    ACTUALIZAREGISTROS.add(REGISTROS[6]);
    REGISTROSNOMBRE[7] = new JLabel("IX :");
    REGISTROS[7] = new JTextField(Integer.toHexString(M.GetIX()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[7]);
    ACTUALIZAREGISTROS.add(REGISTROS[7]);
    REGISTROSNOMBRE[8] = new JLabel("IY :");
    REGISTROS[8] = new JTextField(Integer.toHexString(M.GetIY()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[8]);
    ACTUALIZAREGISTROS.add(REGISTROS[8]);
    REGISTROSNOMBRE[9] = new JLabel("PC :");
    REGISTROS[9] = new JTextField(Integer.toHexString(M.GetPC()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[9]);
    ACTUALIZAREGISTROS.add(REGISTROS[9]);
    REGISTROSNOMBRE[10] = new JLabel("SP :");
    REGISTROS[10] = new JTextField(Integer.toHexString(M.GetSP()));
    ACTUALIZAREGISTROS.add(REGISTROSNOMBRE[10]);
    ACTUALIZAREGISTROS.add(REGISTROS[10]);

    JPanel ACTUALIZAMEDIO = new JPanel();
    ACTUALIZAMEDIO.add(MODIFICA, BorderLayout.LINE_START);
    ACTUALIZAMEDIO.add(ACTUALIZAREGISTROS, BorderLayout.LINE_END);
    ACTUALIZAMEDIO.add(ENVIA);

    JPanel CONJUNTO = new JPanel(new GridLayout(1, 2));
    JPanel BOTONES = new JPanel();

    DATOSAPILADOS[0] = new JLabel(" PILA: ");
    PILA.add(DATOSAPILADOS[0]);
    int r = 0;
    for (int i = 1; i < 10; i++) {
      try {
        aux = Integer.toHexString(
            M.GetMemoria(M.GetSP() + 1 + r) * 0x100 + M.GetMemoria(M.GetSP() + r));
      } catch (NumberFormatException NFE) {

      }
      DATOSAPILADOS[i] = new JLabel("| " + aux + " |");
      r = r + 2;
      PILA.add(DATOSAPILADOS[i]);
    }

    BOTONES.add(SIGUIENTEINSTRUCCION);
    JPanel PANELEJECUCIONJPanel = new JPanel(new GridLayout(2, 1));

    JPanel SUPERIORDERECHO = new JPanel(new GridLayout(2, 1));

    JPanel MEDIO = new JPanel(new GridLayout(1, 2));
    MEDIO.add(GR);
    MEDIO.add(ACTUALIZAMEDIO);
    SUPERIORDERECHO.add(MEDIO);

    INSTRUCCIONES.setText(SIMULADOR.desenEjecucion(posicion));

    PANELEJECUCIONJPanel.add(INSTRUCCIONES);
    PANELEJECUCIONJPanel.add(BOTONES);

    SUPERIORDERECHO.add(PANELEJECUCIONJPanel);

    CONJUNTO.add(SUPERIORDERECHO);
    CONJUNTO.add(GM);
    add(CONJUNTO);
    add(PILA, BorderLayout.SOUTH);
    setVisible(true);

  }

  public void REPINTA() {
    int r = 0;
    for (int i = 1; i < 10; i++) {
      try {
        aux = Integer.toHexString(
            M.GetMemoria(M.GetSP() + 1 + r) * 0x100 + M.GetMemoria(M.GetSP() + r));
      } catch (NumberFormatException NFE) {

      }
      r = r + 2;
      DATOSAPILADOS[i].setText("| " + aux + " |");
    }

  }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == SIGUIENTEINSTRUCCION) {

      posicion = SIMULADOR.pasoAPaso(posicion);
      INSTRUCCIONES.setText(SIMULADOR.desenEjecucion(posicion));
      M = SIMULADOR.RETURNMEMORIA();
      GR.PINTA(M);
      GM.REPINTA(M);
      REPINTA();
      MenuPrincipal.M = M;

    }
    if (ae.getSource() == ENVIA) {
      try {
        M.SetA((byte) Valida(REGISTROS[0].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetB((byte) Valida(REGISTROS[1].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetC((byte) Valida(REGISTROS[2].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetD((byte) Valida(REGISTROS[3].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetE((byte) Valida(REGISTROS[4].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetH((byte) Valida(REGISTROS[5].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetL((byte) Valida(REGISTROS[6].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetIX((short) Valida(REGISTROS[7].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetIY((short) Valida(REGISTROS[8].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetPC((short) Valida(REGISTROS[9].getText()));

      } catch (NumberFormatException NFE) {

      }
      try {
        M.SetSP((short) Valida(REGISTROS[10].getText()));

      } catch (NumberFormatException NFE) {
      }

      REGISTROS[0].setText(Integer.toHexString(M.GetA()));

      REGISTROS[1].setText(Integer.toHexString(M.GetB()));

      REGISTROS[2].setText(Integer.toHexString(M.GetC()));

      REGISTROS[3].setText(Integer.toHexString(M.GetD()));

      REGISTROS[4].setText(Integer.toHexString(M.GetE()));

      REGISTROS[5].setText(Integer.toHexString(M.GetH()));

      REGISTROS[6].setText(Integer.toHexString(M.GetL()));

      REGISTROS[7].setText(Integer.toHexString(M.GetIX()));

      REGISTROS[8].setText(Integer.toHexString(M.GetIY()));

      REGISTROS[9].setText(Integer.toHexString(M.GetPC()));

      REGISTROS[10].setText(Integer.toHexString(M.GetSP()));
      MenuPrincipal.M = M;
      GR.PINTA(M);

    }

  }

  public int Valida(String num) {
    num = num.trim();
    num = "000" + num;
    int i = (Integer.decode("0x" + num)).intValue();
    System.out.println(i);
    return (int) i;
  }
}
