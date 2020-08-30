package simz80.processing;

public class GeneraMnemonicos {
  //Se debe hacer todo con la memoria que tiene Mnemonico
  public static int posant = 00;
  public static int pos1 = 00;
  public static boolean error = false;

  public static String Lista(int pos, HexaMnem Mnemonico, int contadorSig) {
    //posant=pos-0x13;

    //
    //posant=pos1=0;
    String cadena = "\t";
    int getTamanoMemoria = 65535;
    Memoria memoria = Mnemonico.getTodaLaMemoria();
    error = false;

    //posant=Actualizaposant(memoria,Mnemonico,contadorSig-1);
    // if(posant<0)
    //   posant=0;

    //

    String CL;//contador de localidades
    //System.out.print(CL.getBytes().length); ATENCION AQUI ESTO AYUDARA
    String mnemonico;//mnemonico
    int tamano;//tamañao del mnemonico
    //"limiar pantalla"

    for (int i = 0; i < 13; i++) {
      if (pos > getTamanoMemoria - 1)// si ya revaso el limite de la memoria sale
        break;
      Mnemonico.InicializaTamanoUOp();
      mnemonico = Mnemonico.LEEHEX(pos);// obtengo el mnemonico (tercera columna)
      CL = Integer.toHexString(pos);//Obtengo el CL en he3xadecimal
      //Imprimo segunda columna (Info de memoria)
      tamano = Mnemonico.TamanoUOp();
      //tamtotal=tamtotal+tamano+1;

      //CONTROLO EL NULL
      //Para manjerar el null OJO EN REALIDAD DEVLVIA: nullA,(IX+5)
      if (mnemonico == null) {
        cadena = MostrarError(pos, memoria, tamano);
        error = true;
        break;
      }

      //Imprimo primera columna
      cadena = cadena + ImprimeCL(CL) + " : ";
      //En realidad quie actulaizo pos
      pos = ImprimeInstruccionHexa(tamano, pos, memoria);
      cadena = cadena + mnemonico + "\t\t \n\t";
      //pos1=pos;

      //Imprimo tercera columna (Mnemonico);
      //cadena=cadena+ImprimeCL(CL)+" : "+mnemonico+"\t\t \n\t";

    }

    //pos1=Actualizaposant(memoria,Mnemonico,contadorSig+1);

    //pos1=pos;

    if (!error) {
      return cadena;
    }

    return cadena;

  }

  public static int Actualizaposant(Memoria memoria, HexaMnem Mnemonico, int contadorSig) {
    int pos = 0;
    int getTamanoMemoria = 65535;
    String mnemonico;
    int tamano = 0;
    for (int i = 0; i < contadorSig; i++) {

      ///
      for (int j = 0; j < 13; j++) {
        if (pos > getTamanoMemoria - 1)// si ya revaso el limite de la memoria sale
          break;
        Mnemonico.InicializaTamanoUOp();
        mnemonico = Mnemonico.LEEHEX(pos);// obtengo el mnemonico (tercera columna)
        //CL=Integer.toHexString(pos);//Obtengo el CL en he3xadecimal
        //Imprimo segunda columna (Info de memoria)
        tamano = Mnemonico.TamanoUOp();

        //tamtotal=tamtotal+tamano+1;

        //CONTROLO EL NULL
        //Para manjerar el null OJO EN REALIDAD DEVLVIA: nullA,(IX+5)
        if (mnemonico == null) {
          MostrarError(pos, memoria, tamano);
          error = true;
          break;
        }

        //Imprimo primera columna
        // ImprimeCL(CL);

        pos = ImprimeInstruccionHexa(tamano, pos, memoria);

        //Imprimo tercera columna (Mnemonico);
        //cadena=cadena+ImprimeCL(CL)+" : "+mnemonico+"\t\t \n\t";

        //Imprimo un salto de linea

      }

      ///

    }

    return pos;
  }

  public static int ImprimeInstruccionHexa(int tamins, int pos, Memoria memoria) {
    for (int k = 0; k < tamins; k++) {

      pos++;
    }
    return pos;
  }

  private static String ImprimeCL(String CL) {
    int n = CL.getBytes().length;
    CL = CL.toUpperCase();
    switch (n) {
    case 1:
      return "000" + CL;
    case 2:
      return "00" + CL;
    case 3:
      return "0" + CL;
    case 4:
      return CL;
    default:
      return null;
    }
    //System.out.print("00"+CL);
  }

  private static String MostrarError(int pos, Memoria memoria, int tam) {
    String auxiliarisismo = "";
    for (int i = 0; i < tam; i++) {

      auxiliarisismo = auxiliarisismo + "\n" + Integer.toHexString(memoria.GetMemoria(pos))
          .toUpperCase() + " ";
      pos++;
    }

    return auxiliarisismo;
  }

}
