package simz80.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Carga {

  public static int er = 0;
  private static int lineas;

  public String[] LeeArchivoH(String NOM) throws IOException {
    //int lines=0;
    System.out.println();
    String nomarch, dat;
    String[] completo = new String[300];
    er = 0;
    lineas = 0;
    try {

      nomarch = NOM;
      BufferedReader sarchivo = new BufferedReader(new FileReader(nomarch));

      if (nomarch.endsWith(".HEX") != true) {
        er = 1;
        throw new ECarga("No puede leerse un archivo que no sea hexadecimal(.hex)  ");
      }
      File archivo = new File(nomarch);
      //getAbsolutePath()

      if (!archivo.exists()) {
        er = 2;
        throw new ECarga("No existe el archivo " + nomarch);
      }
      while ((dat = sarchivo.readLine()) != null) {
        completo[lineas] = dat;
        lineas++;
        //completo=completo+dat; Checar por que no concatena

      }
      sarchivo.close();
    } catch (ArrayIndexOutOfBoundsException e) {
      er = 3;
      System.out.println("El archivo es muy grande para su carga");
    } catch (FileNotFoundException e) {
      System.err.println(e);
    } catch (IOException e) {
      er = 4;
      System.err.println(e);
    }

    return completo;
  }

  public byte[] ModificarFormatoIntelHex(
      String[] strg) { //Carga si se quiere una posicion inicial aqui la agregamos
    StringBuffer contr = new StringBuffer();
    byte[] arr = new byte[(lineas * 16) - 1];
    byte[] falla = { -1 };
    short tama = 0x00;
    int passlinea = 0, a = 0x0000;
    boolean ctline = true;//Para verificar que no haya mas lineas despues del final de linea
    int carga = 0;//Nos puede indicar la posicion de carga que viene en el archivo
    int vc;
    int b = 0, c = 0;
    char c1, c2;
    // String contr="";
    //Checar y ver excepciones en este para que se extienda
    //Verificar que pueda lograr lo deseado con los strings sino objetos arreglo de chars
    if (GetError() == 0) {
      //System.out.println("Correcta lectura");
      try {
        do {
          if (strg[passlinea].charAt(0) == ':') {
            vc = 0x0000;
            tama = 0x00;
            //No queda de otra checar el to charArray
            c1 = strg[passlinea].charAt(1);
            c2 = strg[passlinea].charAt(2);
            if (Character.isDigit(c1) || Character.isLowerCase(c1)) {
            } else {
              c1 = Character.toLowerCase(c1);
            }
            if (Character.isDigit(c2) || Character.isLowerCase(c2)) {
            } else {
              c2 = Character.toLowerCase(c2);
            }
            contr.append(c1);
            contr.append(c2);
            tama = Short.parseShort(contr.toString(), 16);
            vc = vc + tama;
            contr.delete(0, 2);
            for (int i = 3; i < 7; i += 2) {
              c1 = strg[passlinea].charAt(i);  //Aqui esta el error
              c2 = strg[passlinea].charAt(i + 1);
              if (Character.isDigit(c1) || Character.isLowerCase(c1)) {
              } else {
                c1 = Character.toLowerCase(c1);
              }
              if (Character.isDigit(c2) || Character.isLowerCase(c2)) {
              } else {
                c2 = Character.toLowerCase(c2);
              }
              contr.append(c1);
              contr.append(c2);
              vc = vc + Short.parseShort(contr.toString(), 16);
            }
            carga = Integer.parseInt(contr.toString(), 16);
            contr.delete(0, 5);
            //vc=vc+carga;//vc=vc+(carga*16);
            c1 = strg[passlinea].charAt(7);
            c2 = strg[passlinea].charAt(8);  //checar concat()
            if (c1 == '0' && c2 == '0') {
              for (int i = 0; i < tama; i++) {//checar para tener
                c1 = strg[passlinea].charAt(9 + (2 * i));
                c2 = strg[passlinea].charAt(10 + (2 * i));
                if (Character.isDigit(c1) || Character.isLowerCase(c1)) {
                } else {
                  c1 = Character.toLowerCase(c1);
                }
                if (Character.isDigit(c2) || Character.isLowerCase(c2)) {
                } else {
                  c2 = Character.toLowerCase(c2);
                }
                contr.append(c1);
                contr.append(c2);//checar carga para ver si utilizo
                arr[(carga) + i] = (byte) Short.parseShort(contr.toString(),
                    16);//Aqui le agregas si deseas otra que no sea 0
                vc = vc + Short.parseShort(contr.toString(), 16);
                contr.delete(0, 2);
              }

            } else if (c1 == '0' && c2 == '1') {
              c1 = strg[passlinea].charAt(9);
              c2 = strg[passlinea].charAt(10);
              if (c1 == 'F' && c2 == 'F') {
              }
              break;
            } else {
              ctline = false;
              System.out.println("Error  no se puede manejar este tipo de archivo");
              er = 6;
            }
            c1 = strg[passlinea].charAt(9 + tama * 2);
            c2 = strg[passlinea].charAt(10 + tama * 2);
            if (Character.isDigit(c1) || Character.isLowerCase(c1)) {
            } else {
              c1 = Character.toLowerCase(c1);
            }
            if (Character.isDigit(c2) || Character.isLowerCase(c2)) {
            } else {
              c2 = Character.toLowerCase(c2);
            }
            contr.append(c1);
            contr.append(c2);
            a = Short.parseShort(contr.toString(), 16);
            if (a > 127) {
              a = a - 256;
              a = (~(a - 1));
            }
            b = vc;
            while (b > 256) {
              b = b - 256;
            }
            if (b > 127) {
              b = b - 256;
              c = (~(b - 1));
            } else {
              c = b;
            }
            //c=Short.parseShort(Integer.toString(c),16);

            if ((c) != (a)) {
              System.out.println("El archivo esta corrompido en la linea " + (passlinea + 1));
              ctline = false;
              er = 7;
            }
            contr.delete(0, 2);//vc
          } else {
            er = 5;
          }
          passlinea++;
        } while ((passlinea != lineas) || (ctline == false));

      } catch (NumberFormatException e) {
        er = 8;
        System.out.println(
            "El archivo puede estar corrompido verifique que sean puros valores hexadecimales");
        System.err.println(e);
      } catch (Exception e) {
        er = 9;
        System.err.println(e);
        System.out.println(e.getLocalizedMessage());
      }

      return arr;
    } else {
      return falla;
    }
  }

  public int GetError() {
    return er;
  }

}

