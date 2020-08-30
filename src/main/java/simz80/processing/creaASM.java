package simz80.processing;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class creaASM {
  //private long posicion;
  private static String archivo;//archivo debe tenr una extecion.asm

  public static String CreaArchivo(int PosicionCarga, Memoria memoria) {

    String CADENADEESTAD0 = "CARGA EXITOSA";
    HexaMnem Mnemonico = new HexaMnem(memoria);
    int getTamanoMemoria = 65535, contadorNOP = 0;
    boolean error = false, activadoNOP = false;
    /////

    String mnemonicoactual, mnemonicopasado = "NOP";
    FileOutputStream file = null;//=new FileOutputStream(archivo);
    BufferedOutputStream buffer = null;
    int tamano = 0;
    try {
      file = new FileOutputStream(archivo);
      buffer = new BufferedOutputStream(file);
    } catch (IOException e) {
      CADENADEESTAD0 = "Error: " + e.getMessage();
    }
    while (memoria.GetMemoriaO(PosicionCarga) != (byte) (0x76) && PosicionCarga <= (getTamanoMemoria
        - 1)) {//NOTA EL 76 HEXADECIMAL ES EL 118 DECIMLA

      mnemonicoactual = Mnemonico.LEEHEX(PosicionCarga);

      //CUIDADO AQUI DEVUELVE EL TAMAÑO DEL ULTIMO MNEMOICO
      tamano = Mnemonico.TamanoUOp();// ANTES HexaMnemonico.Tamano(mnemonicoactual);
      if (mnemonicoactual == null) {
        CADENADEESTAD0 = MostrarError(PosicionCarga, memoria, tamano);
        error = true;
        break;
      }
      if (mnemonicoactual.contains("NOP")) {
        mnemonicopasado = "NOP";
        activadoNOP = true;
      }
      if (activadoNOP) {
        if (mnemonicopasado.contains("NOP") && mnemonicoactual.contains("NOP"))
          contadorNOP++;
        else {
          activadoNOP = false;
          contadorNOP = 0;
        }
      }
      if (contadorNOP > 10) {
        CADENADEESTAD0 = MostrarErrorNOP();
        error = true;
        break;
      }

      try {
        buffer.write(mnemonicoactual.getBytes());
        buffer.write("\r\n".getBytes());

      } catch (IOException e) {
        CADENADEESTAD0 = "Error: " + e.getMessage();
      }

      for (int i = 0; i
          < tamano; i++)//busco la siguente instruccion correcta que estara despues del tamañao de la anterior
        PosicionCarga++;
      mnemonicopasado = mnemonicoactual;
    }

    try {
      buffer.write("HALT".getBytes());
      buffer.close();
      file.close();
    } catch (IOException e) {
      CADENADEESTAD0 = "Error: " + e.getMessage();
    }
    if (error) {
      File fichero = new File(archivo);
      if (!fichero.delete())
        CADENADEESTAD0 = "No se ha podido borrar el archivo que se estaba creando";
    }
    return CADENADEESTAD0;
  }

  public static void setArchivo(String file) {
    archivo = file;
  }

  private static String MostrarError(int pos, Memoria memoria, int tam) {
    StringBuffer cadena = new StringBuffer();
    cadena.append(
        "Ha habido un error con el codigo en la posicion: " + Integer.toHexString(pos).toUpperCase()
            + "\n");
    cadena.append(" La instruccion: ");
    for (int i = 0; i < tam; i++) {
      cadena.append(" " + Integer.toHexString(memoria.GetMemoria(pos)).toUpperCase() + " ");
      pos++;
    }
    cadena.append(" , no existe\n");
    return cadena.toString();

  }

  private static String MostrarErrorNOP() {
    return "Se han encontrado mas de 10 instrucciones NOP seguidas en la creacion del archivo. El archivo no se crear";
  }

}
