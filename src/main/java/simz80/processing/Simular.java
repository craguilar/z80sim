package simz80.processing;

public class Simular {
  static public int POSANTERIOR = 00;
  static public int POSACTUAL = 0xff;
  private Memoria mem;
  private int CL;
  private int POSICIONGLOBAL;

  public Simular(Memoria mem1) {
    mem = mem1;
  }

  ////////////////////////////////////////////////////////////////////////
  //////////////ATRA VES DE ESTO SE LOGRA VER LA MEMORIA//////////////////
  ////////////////////////////////////////////////////////////////////////
  public String revisarMemoria() {

    String cadena = "\t\t\tREVISION DE MEMORIA\n\n\tDireccion\t               Contenido Numerico\n         \t\t 0   1    2   3   4   5    6   7    8   9   A   B   C   D   E   F     \t\n\n";
    cadena = cadena + verContenidoMemoria();
    return cadena;
  }

  private String verContenidoMemoria() {
    String aux = "\t";
    int j;
    int k = 0, c = 0;
    for (j = 0; j <= 0XFF; j++) {
      char ascii;
      String cc = Integer.toHexString(c);
      if (j < 16)
        aux = aux + "00" + cc + "0     \t";
      else if (j >= 16 && j < 256)
        aux = aux + "0" + cc + "0     \t";
      else
        aux = aux + cc + "0     \t";

      for (int i = 0; i < 16; i++) {
        int a = mem.GetMemoria(k + i);
        String ca = Integer.toHexString(a);
        ca = ca.toUpperCase();
        if (mem.GetMemoria(k + i) < 16)
          aux = aux + "0" + ca + " ";
        else
          aux = aux + ca + " ";
      }
      aux = aux + "    ";
      for (int i = 0; i < 16; i++) {
        ascii = (char) mem.GetMemoria(k + i);
        if (ascii == 10)
          aux = aux + " ";
      }
      aux = aux + "\n\t";
      c++;
      k = k + 16;
    }
    return aux;
  }

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  ///////////////////// YA IMPLEMENTADA/////////////////////////////////////////////
  public int corridaLibre(int lc) {
    int tam;
    int loc = lc;
    Memoria M = mem;
    String auxiliando = "";
    String aux2 = "";
    HexaMnem convierte = new HexaMnem(M);
    Operaciones operacion = new Operaciones(M);

    while (M.GetMemoria(loc) != 118) {

      aux2 = desenEjecucion(loc);
      M = operacion.EjecutaInstruccion(loc);
      auxiliando = convierte.LEEHEX(loc);
      System.out.println(auxiliando);
      tam = POSICIONGLOBAL;
      if (!esSalto(auxiliando)) {
        loc = loc + tam;
      } else {
        if (auxiliando.contains("RET"))
          loc = M.GetPC() + 3;
        else if (loc == M.GetPC())
          loc = loc + tam;
        else if (!auxiliando.contains("RET"))
          loc = M.GetPC();
      }
      convierte.InicializaTamanoUOp();
      mem = M;
      M.SetPC((short) loc);
    }

    return loc;
  }

  ////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////
  public int pasoAPaso(int lc) {
    int tam;
    int loc = lc;
    Memoria M = mem;
    String auxiliando = "";
    HexaMnem convierte = new HexaMnem(M);
    Operaciones operacion = new Operaciones(M);
    M = operacion.EjecutaInstruccion(loc);
    auxiliando = convierte.LEEHEX(loc);
    tam = POSICIONGLOBAL;
    if (!esSalto(auxiliando)) {
      loc = loc + tam;
    } else {
      if (auxiliando.contains("RET")) {
        loc = M.GetPC() + 3;
      } else if (loc == M.GetPC()) {
        loc = loc + tam;
      } else if (!auxiliando.contains("RET")) {
        loc = M.GetPC();
      }
    }
    convierte.InicializaTamanoUOp();
    mem = M;
    M.SetPC((short) loc);
    return loc;
  }

  public Memoria RETURNMEMORIA() {
    return mem;
  }

  public void modificaRegistros(Memoria mem) {
    byte A = mem.GetA();
    byte B = mem.GetB();
    byte C = mem.GetC();
    byte D = mem.GetD();
    byte E = mem.GetE();
    byte H = mem.GetH();
    byte L = mem.GetL();
    byte A2 = mem.GetA2();
    byte B2 = mem.GetB2();
    byte C2 = mem.GetC2();
    byte D2 = mem.GetD2();
    byte E2 = mem.GetE2();
    byte H2 = mem.GetH2();
    byte L2 = mem.GetL2();
    byte I = mem.GetI();
    byte R = mem.GetR();
    short IX = mem.GetIX();
    short IY = mem.GetIY();
    short PC = mem.GetPC();
    short SP = mem.GetSP();

    String hA = "";
    hA = Integer.toHexString(A);
    String hB = "";
    hB = Integer.toHexString(B);
    String hC = "";
    hC = Integer.toHexString(C);
    String hD = "";
    hD = Integer.toHexString(D);
    String hE = "";
    hE = Integer.toHexString(E);
    String hH = "";
    hH = Integer.toHexString(H);
    String hL = "";
    hL = Integer.toHexString(L);
    String hA2 = "";
    hA2 = Integer.toHexString(A2);
    String hB2 = "";
    hB2 = Integer.toHexString(B2);
    String hC2 = "";
    hC2 = Integer.toHexString(C2);
    String hD2 = "";
    hD2 = Integer.toHexString(D2);
    String hE2 = "";
    hE2 = Integer.toHexString(E2);
    String hH2 = "";
    hH2 = Integer.toHexString(H2);
    String hL2 = "";
    hL2 = Integer.toHexString(L2);
    String hI = "";
    hI = Integer.toHexString(I);
    String hR = "";
    hR = Integer.toHexString(R);
    String hIX = "";
    hIX = Integer.toHexString(IX);
    String hIY = "";
    hIY = Integer.toHexString(IY);
    String hPC = "";
    hPC = Integer.toHexString(PC);
    String hSP = "";
    hSP = Integer.toHexString(SP);
  }

  private boolean esSalto(String oper) {
    char[] aux = new char[5];
    aux = oper.toCharArray();
    if ((aux[0] == 'J' && (aux[1] == 'P' || aux[1] == 'R')) || (aux[0] == 'D' && aux[1] == 'J'
        && aux[2] == 'N' && aux[3] == 'Z'))
      return true;
    return (aux[0] == 'C' && aux[1] == 'A' && aux[2] == 'L') || (aux[0] == 'R' && aux[1] == 'E'
        && aux[2] == 'T');
  }

  ///////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////// DESEJECUCION ////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////
  public String desenEjecucion(int loc) {//Metodo para imprimir varias instrucciones

    String cadena = "";
    Memoria M = mem;
    HexaMnem convierte = new HexaMnem(M);
    convierte.InicializaTamanoUOp();
    cadena =
        ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::INSTRUCCIONES A EJECUTAR:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n\n                PROXIMA INSTRUCCION A EJECUTAR:"
            + Integer.toHexString(loc).toUpperCase() + ":" + cadena + "" + convierte.LEEHEX(loc);
    POSICIONGLOBAL = convierte.TamanoUOp();
    loc = loc + POSICIONGLOBAL;
    cadena =
        cadena + "\n                                                  \t\t" + Integer.toHexString(
            loc).toUpperCase() + ":" + convierte.LEEHEX(loc) + "          ";
    loc = loc + convierte.TamanoUOp();
    cadena =
        cadena + "\n                                                  \t\t" + Integer.toHexString(
            loc).toUpperCase() + ":" + convierte.LEEHEX(loc) + "          ";
    loc = loc + convierte.TamanoUOp();
    cadena =
        cadena + "\n                                                  \t\t" + Integer.toHexString(
            loc).toUpperCase() + ":" + convierte.LEEHEX(loc) + "          ";
    loc = loc + convierte.TamanoUOp();
    cadena =
        cadena + "\n                                                  \t\t" + Integer.toHexString(
            loc).toUpperCase() + ":" + convierte.LEEHEX(loc) + "          ";
    loc = loc + convierte.TamanoUOp();
    cadena =
        cadena + "\n                                                  \t\t" + Integer.toHexString(
            loc).toUpperCase() + ":" + convierte.LEEHEX(loc) + "          ";
    convierte.InicializaTamanoUOp();
    return cadena;
  }
  //////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////

}