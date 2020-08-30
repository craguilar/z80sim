package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.Memoria;
import simz80.processing.creaASM;

public class VentanaCreaASM extends JFrame implements ActionListener {
  ///////////////////////////////////////
  JPanel CONTENEDOR = new JPanel();
  //////////////////////
  JButton ENVIAR = new JButton("ENVIAR POS");
  //////////////////////////////////////////
  JTextField POSICION = new JTextField(5);
  JTextField NOMBRE = new JTextField(5);
  /////////////////////////////////////////////
  JLabel PosicionMemoria = new JLabel(
      " POSICION A PARTIR DE LA CUAL QUIERAS CREAR EL ARCHIVO ASM = ");
  JLabel NOMBREARCHIVO = new JLabel(" NOMBRE DEL ARCHIVO A CREAR INCLUE EXTENSION .ASM = ");
  JLabel ESTADO = new JLabel(" ESTADO DE CREACION = ");
  /////////////////////////////////////////////////
  Memoria M;

  public VentanaCreaASM(Memoria m) {
    super("                                      ..::::CREA ASM::::..");
    setSize(new Dimension(800, 200));
    setResizable(false);
    setLocation(new Point(50, 50));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    M = m;
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    Construlle();
  }

  private void Construlle() {
    ENVIAR.addActionListener(this);

    CONTENEDOR.add(PosicionMemoria);
    CONTENEDOR.add(POSICION);
    CONTENEDOR.add(NOMBREARCHIVO);
    CONTENEDOR.add(NOMBRE);
    CONTENEDOR.add(ENVIAR);

    add(ESTADO, BorderLayout.SOUTH);
    add(CONTENEDOR, BorderLayout.CENTER);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == ENVIAR) {
      try {
        // int pos=Integer.parseInt(POSICION.getText())
        int pos = ValidaString(POSICION.getText());
        String NOM = NOMBRE.getText();
        if (!NOM.isEmpty()) {
          if (pos < 0 || pos > 65536)
            ESTADO.setText("ESTADO DE CREACION = FUERA DEL RANGO");
          else {
            String aux;
            creaASM.setArchivo(NOM);
            aux = creaASM.CreaArchivo(pos, M);
            if (aux != null)
              ESTADO.setText("ESTADO DE CREACION = " + aux);

          }
        } else
          ESTADO.setText("ESTADO DE CREACION  = NO INCLUYO ARCHIVO");
      } catch (NumberFormatException NFE) {
        ESTADO.setText("ESTADO DE CREACION =...:::ERROR FATAL:::..");
      } catch (Exception e) {
        ESTADO.setText("ESTADO DE CREACION =...:::ERROR :::..");
      }

    }

  }

  public int ValidaString(String num) {
    num = num.trim();
    num = "000" + num;
    String Sub;
    int i = (Integer.decode("0x" + num)).intValue();
    return (int) i;
  }

}


