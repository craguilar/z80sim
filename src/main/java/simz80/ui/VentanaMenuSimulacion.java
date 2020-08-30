package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.Memoria;
import simz80.processing.Simular;

public class VentanaMenuSimulacion extends JFrame implements ActionListener {

  Memoria M;
  Simular SIM;

  JButton ENVIAR = new JButton("ENVIAR POS");

  JCheckBoxMenuItem CLIBRE = new JCheckBoxMenuItem("CORRIDA LIBRE");

  JCheckBoxMenuItem CPASOAPASO = new JCheckBoxMenuItem("CORRIDA PASO A PASO");
  JTextField POSICION = new JTextField(7);
  JPanel CONTENEDOR = new JPanel();
  JLabel PosicionMemoria = new JLabel(" POSICION EN LA QUE QUIERES CARGAR = ");
  JLabel ESTADO = new JLabel("ESTADO :");
  int posicion;

  public VentanaMenuSimulacion(Memoria M1) {
    super("                                      ..::::SIMULACION::::..");
    setSize(new Dimension(500, 200));
    setResizable(false);
    setLocation(new Point(50, 50));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
    SIM = new Simular(M1);
    M = M1;
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    construlle();
  }

  final public void construlle() {
    JPanel CBOX = new JPanel();
    ENVIAR.addActionListener(this);
    CLIBRE.addActionListener(this);
    CPASOAPASO.addActionListener(this);

    CONTENEDOR.add(PosicionMemoria);
    CONTENEDOR.add(POSICION);
    CONTENEDOR.add(ENVIAR);

    CBOX.add(CLIBRE);
    CBOX.add(CPASOAPASO);

    add(ESTADO, BorderLayout.CENTER);
    add(CBOX, BorderLayout.NORTH);
    add(CONTENEDOR, BorderLayout.SOUTH);
    setVisible(true);

  }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == ENVIAR) {
      if (CLIBRE.getState() ^ CPASOAPASO.getState()) {
        try {
          // int pos=Integer.parseInt(POSICION.getText());
          int pos = ValidaString(POSICION.getText());
          if (pos < 0 || pos > 65534)
            ESTADO.setText("ESTADO : FUERA DE RANGO");
          else {
            posicion = pos;
            if (CLIBRE.getState() == true) {
              ESTADO.setText("ESTADO : EXITOSO CORRIDA LIBRE");
              posicion = SIM.corridaLibre(posicion);
              SIM.desenEjecucion(posicion);
              M = SIM.RETURNMEMORIA();
              MenuPrincipal.M = M;
            }
            if (CPASOAPASO.getState() == true) {
              ESTADO.setText("ESTADO : EXITOSO CORRIDA PASO A PASO");
              PasoaPaso SIMPP = new PasoaPaso(M, pos);
            }
          }
        } catch (NumberFormatException NFE) {
          ESTADO.setText("ESTADO : ERROR DE FORMATO");
        }
      } else
        ESTADO.setText("ESTADO : MULTIPLICIDAD DE OPCIONES O NO SELECCIONO NADA");
    }
  }

  public int ValidaString(String num) {
    num = num.trim();
    num = "000" + num;
    int i = (Integer.decode("0x" + num)).intValue();
    return (int) i;
  }

}