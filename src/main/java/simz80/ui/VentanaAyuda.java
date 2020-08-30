package simz80.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class VentanaAyuda extends JFrame implements ActionListener {

  public VentanaAyuda() {
    super("                                      ..::::AYUDA::::..");
    setSize(new Dimension(750, 600));
    setResizable(false);
    setLocation(new Point(50, 50));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    Construlle();

  }

  private void Construlle() {
    JTextArea AYUDA = new JTextArea();
    AYUDA.setEditable(false);
    AYUDA.setText("\t\t\t::::::::::::::::::::::::::::::::AYUDA:::::::::::::::::::::::::::::::::::"
        + "\n\n::GeneraMnemonicos::\n"
        + "\tLa posicion en memoria que deseas colocar debera estar en Hexadecimal y click en enviar. Si no"
        + "\n\t es una posicion valida se tomara como 0 (cero).Se mostrara en pantalla el desensamblado del codigo"
        + "\n\tobjeto en memoria. Se hara esto cada 13 instrucciones y cada vez que se apriete “adelante” o atrás"
        + "\n\n::creaASM::\n\tLa posicion en memoria que deseas colocar debera estar en Hexadecimal y click en enviar.\n"
        + "\n\tSi no es una posicion valida se tomara como 0 (cero).Se creara un archivo con el desensamblado del código objeto"
        + "\n\t en memoria   (deberas poner NombreArchivo.ASM donde corresponde) a partir de la posición en memoria que se"
        + "\n\tcoloque en el textBox hasta la primera instrucción HALT que exista.Condiciones para que no se cree el archivo:"
        + "\n\t\t>Cuando haya mas de 10 NOPs seguidos\n\t\t>Cuando haya un error de creación de archivo.\n\t\t>Cuando se quiera se enceuntre una instrucción a desensamblar no existente."
        + "\n\n::La ventana de carga::"
        + "\n\tBásicamente se dan los parámetros de donde esta el archivo .HEXcon el cuál se va cargar y en el de Posición se"
        + "\n\t dan el número en hexadecimal para cargarlo en esta dirección.Existen diferentes tipos de errores que pueden   "
        + "\n\tacontecer en este  proceso como que no no tiene terminación .HEX, que el archivo puede haber sido modificado o "
        + "\n\tque  no esta enformato intelhexadecimal u otro tipo de problema y estos se muestran en la parteinferior de la ventana."
        + "\n\n\t:::::PROYECTO ELABORADO POR::::::: \n\tCarlos Ruiz Aguilar.\n\tPablo Fernandez Garcia.\n\t Luis Mario Martinez\n\tDaniel Jimenez"
        + "\n\n \t:::::::::UNIVERSIDAD NACIONAL AUTONOMA DE MEXICO. FACULTA DE INGENIERIA .CIUDAD UNIVERSITARIA:::::::");
    add(AYUDA);

    setVisible(true);

  }

  public void actionPerformed(ActionEvent e) {

  }

}
