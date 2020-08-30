package simz80.processing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LEE {
  public static String lee() {
    String cadena = null;
    try {
      InputStreamReader isr;
      BufferedReader br;
      isr = new InputStreamReader(System.in);
      br = new BufferedReader(isr);
      cadena = br.readLine();
    } catch (IOException e) {
      System.out.println("Error al leer del teclado..");
    }
    return cadena;
  }

  public static short corto(String mensaje) {
    short corto = 0;
    System.out.print(mensaje);
    try {
      corto = Short.parseShort(lee());
      return corto;
    } catch (NumberFormatException e) {
      System.out.println("Error del formato..");
    }
    return corto;
  }

  public static long largo(String mensaje) {
    long largo = 0;
    System.out.print(mensaje);
    try {
      largo = Long.parseLong(lee());
    } catch (NumberFormatException e) {
      System.out.println("Error del formato..");
    }
    return largo;
  }

  public static int entero(String mensaje) {
    int entero = 0;

    System.out.print(mensaje);
    try {
      entero = Integer.parseInt(lee());
    } catch (NumberFormatException e) {
      System.out.println("Error del formato..");
    }
    return entero;
  }

  public static float real(String mensaje) {
    float real = 0;
    System.out.print(mensaje);
    try {
      real = Float.parseFloat(lee());
    } catch (NumberFormatException e) {
      System.out.println("Error del formato..");
    }
    return real;
  }

  public static double doble(String mensaje) {
    double doble = 0;
    System.out.print(mensaje);
    try {
      doble = Double.parseDouble(lee());
    } catch (NumberFormatException e) {
      System.out.println("Error del formato..");
    }
    return doble;
  }
}
