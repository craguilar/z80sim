package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simz80.processing.Carga;
import simz80.processing.Memoria;

public class VentanaCarga extends JFrame implements ActionListener {

  /////////////////////////////////////////////////
  JButton ENVIAR = new JButton("ENVIAR POS");
  JTextField POSICION = new JTextField(5);
  JTextField NOMBRE = new JTextField(5);
  JPanel CONTENEDOR = new JPanel();
  JLabel PosicionMemoria = new JLabel(" POSICION EN LA QUE QUIERES CARGAR = ");
  JLabel NOMBREARCHIVO = new JLabel(" NOMBRE DEL ARCHIVO (INCLUYE LA EXTENSION .HEX) = ");
  JLabel ESTADO = new JLabel(" ESTADO DE CARGA = ");
  /////////////////////////////////////////////////
  Memoria CAMBIANTE = new Memoria();

  Carga CARGA = new Carga();

  public VentanaCarga() {
    super("                                      ..::::CARGA::::..");
    setSize(new Dimension(500, 200));
    setResizable(true);
    setLocation(new Point(50, 50));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setBackground(Color.GRAY);
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
        //int pos=Integer.parseInt(POSICION.getText(),16);
        int pos = ValidaString(POSICION.getText());

        String NOM = NOMBRE.getText();
        if (!NOM.isEmpty()) {
          if (pos < 0 || pos > 65536)
            ESTADO.setText("ESTADO DE CARGA = FUERA DEL RANGO");
          else {

            byte[] a = CARGA.ModificarFormatoIntelHex(CARGA.LeeArchivoH(NOM));

            if (CARGA.GetError() == 0) {

              ESTADO.setText("ESTADO DE CARGA = EXITOSO");
              CAMBIANTE.Carga(a, pos);
              MenuPrincipal.M = CAMBIANTE;
            } else {
              switch (CARGA.GetError()) {
              case 1:
                ESTADO.setText("ESTADO DE CARGA =...:::ERROR AL CARGAR EL ARCHIVO NO ES.HEX:::..");
                break;
              case 2:
                ESTADO.setText("ESTADO DE CARGA =...:::NO EXISTE ARCHIVO:::..");
                break;
              case 3:
                ESTADO.setText("ESTADO DE CARGA =...:::EL ARCHIVO ES DEMASIADO GRANDE:::..");
                break;
              case 4:
                ESTADO.setText("ESTADO DE CARGA =...:::ERROR AL CARGAR ARCHIVO:::..");
                break;
              case 5:
                ESTADO.setText(
                    "ESTADO DE CARGA =...:::ARCHIVO NO ESTA EN FORMATO INTELHEXADECIMAL:::..");
                break;
              case 6:
                ESTADO.setText("ESTADO DE CARGA =...:::NO EXISTE ARCHIVO:::..");
                break;
              case 7:
                ESTADO.setText("ESTADO DE CARGA =...:::ARCHIVO ESTA MODIFICADO:::..");
                break;
              case 8:
                ESTADO.setText(
                    "ESTADO DE CARGA =...:::EL ARCHIVO CONTIENE VALORES DIFERENTES A HEXADECIMAL:::..");
                break;
              case 9:
                ESTADO.setText("ESTADO DE CARGA =...:::ERROR FATAL:::..");
                break;
              default:
                ESTADO.setText("ESTADO DE CARGA =...::::HAN AFECTADO EL PROGRAMA PRINCIPAL");
                break;
              }

            }
          }
        }
      } catch (NumberFormatException NFE) {
        ESTADO.setText("ESTADO DE CARGA =...:::ERROR FATAL:::..");
      } catch (Exception e) {
        ESTADO.setText("ESTADO DE CARGA =...:::ERROR :::..");
      }

    }

  }

  public int ValidaString(String num) {
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
    return (int) i;
  }

}
