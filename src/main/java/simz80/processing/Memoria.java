package simz80.processing;

public class Memoria {
  private final byte[] memoria;
  private final int[] FLAGS;
  private boolean BANDERADEACCESO = true;
  private byte A, B, C, D, E, F, H, L;
  private byte A2, B2, C2, D2, E2, F2, H2, L2;
  private byte I, R;
  private short IX, IY, SP, PC;
  private int posaux1, posaux2;//ESTOS ESTAN RELACIONADOS CON EL TAMANO DE LA INSTRUCCION.

  public Memoria() {
    A = B = C = D = E = F = H = L = A2 = B2 = C2 = D2 = E2 = F2 = H2 = L2 = I = R = 0x0;
    IX = IY = PC = 0x0;
    SP = 0x7fff;
    posaux1 = posaux2 = 0;
    FLAGS = new int[8];
    memoria = new byte[65535 + 1];
  }

  public void SetA(byte dato) {
    if (dato <= 127 && dato >= -128)
      A = dato;
    else
      A = 0;
  }

  public void SetB(byte dato) {
    if (dato <= 127 && dato >= -128)
      B = dato;
    else
      B = 0;
  }

  public void SetC(byte dato) {
    if (dato <= 127 && dato >= -128)
      C = dato;
    else
      C = 0;
  }

  public void SetD(byte dato) {
    if (dato <= 127 && dato >= -128)
      D = dato;
    else
      D = 0;
  }

  public void SetE(byte dato) {
    if (dato <= 127 && dato >= -128)
      E = dato;
    else
      E = 0;
  }

  public void SetH(byte dato) {
    if (dato <= 127 && dato >= -128)
      H = dato;
    else
      H = 0;
  }

  public void SetL(byte dato) {
    if (dato <= 127 && dato >= -128)
      L = dato;
    else
      L = 0;
  }

  public void SetI(byte dato) {
    if (dato <= 127 && dato >= -128)
      I = dato;
    else
      I = 0;
  }

  public void SetF(byte dato) {
    if (dato <= 127 && dato >= -128)
      F = dato;
    else
      F = 0;
  }

  public void SetA2(byte dato) {
    if (dato <= 127 && dato >= -128)
      A2 = dato;
    else
      A2 = 0;
  }

  public void SetB2(byte dato) {
    if (dato <= 127 && dato >= -127)
      B2 = dato;
    else
      B2 = 0;
  }

  public void SetC2(byte dato) {
    if (dato <= 127 && dato >= -127)
      C2 = dato;
    else
      C2 = 0;
  }

  public void SetD2(byte dato) {
    if (dato <= 127 && dato >= -127)
      D2 = dato;
    else
      D2 = 0;
  }

  public void SetE2(byte dato) {
    if (dato <= 127 && dato >= -127)
      E2 = dato;
    else
      E2 = 0;
  }

  public void SetH2(byte dato) {
    if (dato <= 127 && dato >= -127)
      H2 = dato;
    else
      H2 = 0;
  }

  public void SetL2(byte dato) {
    if (dato <= 127 && dato >= -127)
      L2 = dato;
    else
      L2 = 0;
  }

  public void SetF2(byte dato) {
    if (dato <= 127 && dato >= -127)
      F2 = dato;
    else
      F2 = 0;
  }

  public void SetR(byte dato) {
    R = dato;
  }

  public void SetIX(short dato) {
    IX = dato;
  }

  public void SetIY(short dato) {
    IY = dato;
  }

  public void SetSP(short dato) {
    SP = dato;
  }

  public void SetPC(short dato) {
    PC = dato;
  }

  public void SetBC(byte n1, byte n2) {
    B = n1;
    C = n2;
  }

  public void SetDE(byte n1, byte n2) {
    D = n1;
    E = n2;
  }

  public void SetHL(int n1, int n2) {
    H = (byte) n1;
    L = (byte) n2;
  }

  public void ALTERAFLAGS(byte datoant, byte dato) {
    if (dato > 0xff)//CARRY
      FLAGS[0] = 1;
    else if (dato <= 0xff)
      FLAGS[0] = 0;
    if (dato == 0)
      FLAGS[6] = 1;
    else if (dato != 0)
      FLAGS[6] = 0;
    if (dato < 0)
      FLAGS[7] = 1;
    else if (dato >= 0)
      FLAGS[7] = 0;
    if (datoant <= 15 && dato > 15)
      FLAGS[4] = 1;
    else
      FLAGS[4] = 0;

  }

  public void ENCIENDEZERO() {
    FLAGS[6] = 1;
  }

  public void APAGAZERO() {
    FLAGS[6] = 0;
  }

  public void ENCIENDEC() {
    FLAGS[0] = 1;
  }

  public void ENCIENDEPARIDAD() {
    FLAGS[2] = 1;
  }

  public void APAGAPARIDAD() {
    FLAGS[2] = 0;
  }

  public void ENCIENDENEG() {
    FLAGS[1] = 1;
  }

  public void APAGASGN() {
    FLAGS[7] = 0;
  }

  public void ENCIENDESIGN() {
    FLAGS[7] = 1;
  }

  public void APAGANEG() {
    FLAGS[1] = 0;
  }

  public void ENCIENDEH() {
    FLAGS[4] = 1;
  }

  public void APAGAH() {
    FLAGS[4] = 0;
  }

  public void APAGAC() {
    FLAGS[0] = 0;
  }

  public void APAGAZ() {
    FLAGS[6] = 0;
  }

  public void ENCIENDEZ() {
    FLAGS[6] = 1;
  }

  public void SetMemoria(int pos, byte dato) {
    if (pos >= 0 && pos < 65535 + 1)
      memoria[pos] = dato;
  }

  public byte GetA() {
    return A;
  }

  public byte GetB() {
    return B;
  }

  public byte GetC() {
    return C;
  }

  public byte GetD() {
    return D;
  }

  public byte GetE() {
    return E;
  }

  public byte GetH() {
    return H;
  }

  public byte GetA2() {
    return A2;
  }

  public byte GetB2() {
    return B2;
  }

  public byte GetC2() {
    return C2;
  }

  public byte GetD2() {
    return D2;
  }

  public byte GetE2() {
    return E2;
  }

  public byte GetH2() {
    return H2;
  }

  public byte GetL2() {
    return L2;
  }

  public byte GetL() {
    return L;
  }

  public byte GetI() {
    return I;
  }

  public byte GetR() {
    return R;
  }

  public short GetIX() {
    return IX;
  }

  public short GetIY() {
    return IY;
  }

  public short GetSP() {
    return SP;
  }

  public short GetPC() {
    return PC;
  }

  public short GetBC() {
    int L1 = C;
    int H1 = (B * 0x100);
    short AUX = (short) (H1 + L1);
    return AUX;
  }

  public short GetDE() {
    int L1 = D;
    int H1 = (E * 0x100);
    short AUX = (short) (H1 + L1);
    return AUX;
  }

  public short GetHL() {
    int L1 = L;
    int H1 = (H * 0x100);
    short AUX = (short) (H1 + L1);
    return AUX;
  }

  public int GetBandera(int pos) {
    if (pos > -1 && pos < 8)
      return FLAGS[pos];
    return -1;
  }

  public byte GetF() {
    for (int i = 0; i < FLAGS.length; i++)
      F = (byte) (FLAGS[i] * (int) (Math.pow(2, i)));
    return F;
  }

  public byte GetF2() {
    return F2;
  }

  //ESTAS NOS SIRVE PARA REALIZAR LA BUSQUEDA DE LOS MNEMONICOS
  public int GetMemoria(int pos) {
    if (pos >= 0 && pos <= 65535) {
      if (BANDERADEACCESO) {
        Setposaux2(pos);
      }
      int i = memoria[pos];
      String aux = Integer.toHexString(i);
      aux = "0" + aux;
      String cadena = "0x" + aux.charAt(aux.length() - 2) + aux.charAt(aux.length() - 1);
      i = Integer.decode(cadena).intValue();
      return i;
    } else
      return -1;
  }

  public void ENCIENDEBADERADEACCESO() {
    BANDERADEACCESO = true;
  }

  public void APAGABANDERADEACCESO() {
    BANDERADEACCESO = false;
  }

  //ESTA NOS SIRVE PARA REALIZAR LAS OPERACIONES
  public byte GetMemoriaO(int pos) {
    if (pos >= 0 && pos < 65535)
      return memoria[pos];
    else
      return -1;
  }

  public void Iniposaux2(int dato) {
    posaux2 = dato;
  }

  public void Setposaux1(int pos) {
    posaux1 = pos;
  }

  public void Setposaux2(int posA) {
    if (posA > posaux2)
      posaux2 = posA;
  }

  public int TamanoUop() {
    return posaux2 - posaux1;
  }

  public void Carga(byte[] a, int pos) {
    if (pos >= 0 && pos < 65535 - a.length) {
      for (int i = 0; i < a.length; i++)
        if (a[i] > 127)
          memoria[pos + i] = (byte) (a[i] - 256);
        else
          memoria[pos + i] = a[i];
    }
  }
}
