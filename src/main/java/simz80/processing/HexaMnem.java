package simz80.processing;

public class HexaMnem {
  private final Memoria mem;
  private int pos;

  public HexaMnem(Memoria mem) {
    pos = -1;
    this.mem = mem;
  }

  public String LEEHEX(int pos) {
    this.pos = pos;//POSICION DEL DATO QUE ENTRARA A LOS METODOS.
    return Busca(mem.GetMemoria(pos));
  }

  public int TamanoUOp() {
    mem.Setposaux1(pos);
    int aux = mem.TamanoUop() + 1;
    mem.Iniposaux2(pos);
    return aux;
  }

  public void InicializaTamanoUOp() {
    mem.Setposaux1(0);
    mem.Iniposaux2(0);
  }

  private String Busca(int datoabuscar) {
    if (GrupoCarga16(datoabuscar) != null)
      return GrupoCarga16(datoabuscar);
    if (GrupoBitRes(datoabuscar) != null)
      return GrupoBitRes(datoabuscar);
    if (GrupoRotacion(datoabuscar) != null)
      return GrupoRotacion(datoabuscar);
    if (GrupoCarga8(datoabuscar) != null)
      return GrupoCarga8(datoabuscar);
    if (GrupoArit8(datoabuscar) != null)
      return GrupoArit8(datoabuscar);
    if (GrupoInteryTrnas(datoabuscar) != null)
      return GrupoInteryTrnas(datoabuscar);
    if (GrupoA16(datoabuscar) != null)
      return GrupoA16(datoabuscar);
    if (ControlG(datoabuscar) != null)
      return ControlG(datoabuscar);
    if (JUMP(datoabuscar) != null)
      return JUMP(datoabuscar);
    if (CallRet(datoabuscar) != null)
      return CallRet(datoabuscar);
    return null;
  }

  public Memoria getTodaLaMemoria() {
    return mem;
  }

  private String GrupoCarga16(int datoH) {
    //System.out.println("SE PARO EN GC16");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0xF1)
      return "POP AF";
    if (datoH == 0xc1 || datoH == 0xd1 || datoH == 0xe1)
      return "POP " + Patron6(REGD);
    if (datoH == 0xF5)
      return "PUSH AF";
    if (datoH == 0xF9)
      return "LD SP, HL";
    if (datoH == 0xc5 || datoH == 0xd5 || datoH == 0xe5)
      return "PUSH " + Patron6(REGD);
    if (datoH == 0x22)
      return "LD (" + Integer.toHexString(mem.GetMemoria(pos + 2)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 1)) + "),HL";
    if (datoH == 0x2A)
      return "LD HL,(" + Integer.toHexString(mem.GetMemoria(pos + 2)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 1)) + ")";
    if (REGF == 0x1 && (REGD >= 0 && REGD <= 3))
      return "LD " + Patron6(REGD) + ", " + Integer.toHexString(mem.GetMemoria(pos + 2)) + ""
          + Integer.toHexString(mem.GetMemoria(pos + 1));
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0x21)
      return "LD " + Patron4(datoH) + ", " + Integer.toHexString(mem.GetMemoria(pos + 3)) + ""
          + Integer.toHexString(mem.GetMemoria(pos + 2));
    if (datoH == 0xED && (((mem.GetMemoria(pos + 1)) == 0x4B) || ((mem.GetMemoria(pos + 1)) == 0x5B)
        || ((mem.GetMemoria(pos + 1)) == 0x7B)))
      return "LD " + Patron6((mem.GetMemoria(pos + 1)) / 0x10) + ",(" + Integer.toHexString(
          mem.GetMemoria(pos + 3)) + "" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(+1) == 0x2A)
      return "LD " + Patron4(datoH) + ", (" + Integer.toHexString(mem.GetMemoria(pos + 3)) + ""
          + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
    if (datoH == 0xED &&/*REGF*/((mem.GetMemoria(pos + 1)) == 0x43 || (mem.GetMemoria(pos + 1))
        == 0x53 || (mem.GetMemoria(pos + 1)) == 0x73))
      return "LD (" + Integer.toHexString(mem.GetMemoria(pos + 3)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 2)) + ")," + Patron6(mem.GetMemoria(pos + 1) / 0x10);
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0x22)
      return "LD (" + Integer.toHexString(mem.GetMemoria(pos + 3)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 2)) + ")," + Patron4(datoH);
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xF9)
      return "LD SP," + Patron4(datoH);
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xE5)
      return "PUSH " + Patron4(datoH);
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xE1)
      return "POP " + Patron4(datoH);
    mem.Iniposaux2(pos);
    return null;
  }

  private String GrupoBitRes(int datoH) {
    //System.out.println("SE PARO EN GBR");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0xcb) {
      if (mem.GetMemoria(pos + 1) >= 0x40 && mem.GetMemoria(pos + 1) < 0x80)
        return "BIT " + Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)
            + " " + Patron1(mem.GetMemoria(pos + 1) % 0X10);
      if (mem.GetMemoria(pos + 1) >= 0x80 && mem.GetMemoria(pos + 1) < 0xC0)
        return "RES " + Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)
            + " " + Patron1(mem.GetMemoria(pos + 1) % 0X10);
      if (mem.GetMemoria(pos + 1) >= 0xC0 && mem.GetMemoria(pos + 1) <= 0xff)
        return "SET " + Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)
            + " " + Patron1(mem.GetMemoria(pos + 1) % 0X10);
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xCB) {
      if (mem.GetMemoria(pos + 3) == 0x46 || mem.GetMemoria(pos + 3) == 0x4E || mem.GetMemoria(
          pos + 3) == 0x56 || mem.GetMemoria(pos + 3) == 0x5E || mem.GetMemoria(pos + 3) == 0x66
          || mem.GetMemoria(pos + 3) == 0x6E || mem.GetMemoria(pos + 3) == 0x76 || mem.GetMemoria(
          pos + 3) == 0x7E)
        return "BIT " + Patron2(mem.GetMemoria(pos + 3) / 0X10, mem.GetMemoria(pos + 3) % 0X10)
            + " (" + Patron4(mem.GetMemoria(pos)) + "+" + Integer.toHexString(
            mem.GetMemoria(pos + 2)) + ")";
      if (mem.GetMemoria(pos + 3) == 0x86 || mem.GetMemoria(pos + 3) == 0x8E || mem.GetMemoria(
          pos + 3) == 0x96 || mem.GetMemoria(pos + 3) == 0x9E || mem.GetMemoria(pos + 3) == 0xA6
          || mem.GetMemoria(pos + 3) == 0xAE || mem.GetMemoria(pos + 3) == 0xB6 || mem.GetMemoria(
          pos + 3) == 0xBE)
        return "RES " + Patron2(mem.GetMemoria(pos + 3) / 0X10, mem.GetMemoria(pos + 3)) + " ("
            + Patron4(mem.GetMemoria(pos)) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2))
            + ")";
      if (mem.GetMemoria(pos + 3) == 0xC6 || mem.GetMemoria(pos + 3) == 0xCE || mem.GetMemoria(
          pos + 3) == 0xd6 || mem.GetMemoria(pos + 3) == 0xdE || mem.GetMemoria(pos + 3) == 0xe6
          || mem.GetMemoria(pos + 3) == 0xeE || mem.GetMemoria(pos + 3) == 0xf6 || mem.GetMemoria(
          pos + 3) == 0xfE)
        return "SET " + Patron2(mem.GetMemoria(pos + 3) / 0X10, mem.GetMemoria(pos + 3)) + " ("
            + Patron4(mem.GetMemoria(pos)) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2))
            + ")";
    }
    mem.Iniposaux2(pos);
    return null;
  }

  private String GrupoRotacion(int datoH) {
    //System.out.println("SE PARO EN GROT");
    if (datoH == 0x7)
      return "RLCA";
    if (datoH == 0x0F)
      return "RRCA";
    if (datoH == 0x17)
      return "RLA";
    if (datoH == 0x1F)
      return "RRA";
    if (datoH == 0xED) {
      if (mem.GetMemoria(pos + 1) == 0x67)
        return "RRD";
      if (mem.GetMemoria(pos + 1) == 0x6F)
        return "RLD";
    }
    int REGF = mem.GetMemoria(pos + 1) % 0X10;
    if (datoH == 0xCB) {
      if (mem.GetMemoria(pos + 1) >= 0x00 && mem.GetMemoria(pos + 1) < 0x10)
        return "R" + Patron3(REGF) + "C " + Patron1(REGF);
      if (mem.GetMemoria(pos + 1) >= 0x10 && mem.GetMemoria(pos + 1) < 0x20)
        return "R" + Patron3(REGF) + " " + Patron1(REGF);
      if (mem.GetMemoria(pos + 1) >= 0x20 && mem.GetMemoria(pos + 1) < 0x30)
        return "S" + Patron3(REGF) + "A " + Patron1(REGF);
      if (mem.GetMemoria(pos + 1) >= 0x38 && mem.GetMemoria(pos + 1) < 0x40)
        return "S" + Patron3(REGF) + "L " + Patron1(REGF);
    }
    if ((datoH == 0XDD || datoH == 0XFD) && mem.GetMemoria(pos + 1) == 0xCB) {
      if (mem.GetMemoria(pos + 3) == 0x06 || mem.GetMemoria(pos + 3) == 0x0E)
        return "R" + Patron3(mem.GetMemoria(pos + 1) % 0X10) + "C (" + Patron4(mem.GetMemoria(pos))
            + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
      if (mem.GetMemoria(pos + 3) == 0x16 || mem.GetMemoria(pos + 3) == 0x1E)
        return "R" + Patron3(mem.GetMemoria(pos + 1) % 0X10) + " (" + Patron4(mem.GetMemoria(pos))
            + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
      if (mem.GetMemoria(pos + 3) == 0x26 || mem.GetMemoria(pos + 3) == 0x2E)
        return "S" + Patron3(mem.GetMemoria(pos + 1) % 0X10) + "A ,(" + Patron4(mem.GetMemoria(pos))
            + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
      if (mem.GetMemoria(pos + 3) == 0x3e)
        return "SRL (" + Patron4(mem.GetMemoria(pos)) + "+" + Integer.toHexString(
            mem.GetMemoria(pos + 2)) + ")";
    }
    mem.Iniposaux2(pos);
    return null;
  }

  private String GrupoCarga8(int datoH) {
    //System.out.println("SE PARO EN GC8");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    String Nem = "LD ";
    if (datoH == 0x06 || datoH == 0x0E || datoH == 0x16 || datoH == 0x1E || datoH == 0x26
        || datoH == 0x2E || datoH == 0x3E)
      return Nem + Patron5(datoH) + ", " + Integer.toHexString(mem.GetMemoria(pos + 1));
    if (datoH == 0x36)
      return Nem + "(HL), " + Integer.toHexString(mem.GetMemoria(pos + 1));
    if (REGD == 0x4 && REGF < 8)
      return Nem + " B," + Patron1(REGF);
    if (REGD == 0x4 && REGF >= 8)
      return Nem + " C," + Patron1(REGF);
    if (REGD == 0x5 && REGF < 8)
      return Nem + " D," + Patron1(REGF);
    if (REGD == 0x5 && REGF >= 8)
      return Nem + " E," + Patron1(REGF);
    if (REGD == 0x6 && REGF < 8)
      return Nem + " H," + Patron1(REGF);
    if (REGD == 0x6 && REGF >= 8)
      return Nem + " L," + Patron1(REGF);
    if (REGD == 0x7 && REGF < 8) {
      if (REGF == 6)
        return Nem = "HALT";
      else
        return Nem + "  (HL)," + Patron1(REGF);
    }
    if (REGD == 0x7 && REGF >= 8)
      return Nem + "  A," + Patron1(REGF);
    if (datoH == 0x0A || datoH == 0x1A)
      return Nem + " A," + "(" + Patron6(REGD) + ")";
    if (datoH == 0x02 || datoH == 0x12)
      return Nem + "(" + Patron6(REGD) + "),A";
    if (datoH == 0x3A)
      return Nem + " A, (" + Integer.toHexString(mem.GetMemoria(pos + 2)) + "" + Integer
          .toHexString(mem.GetMemoria(pos + 1)) + ")";
    if (datoH == 0x32)
      return Nem + " (" + Integer.toHexString(mem.GetMemoria(pos + 2)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 1)) + "), A";
    if (datoH == 0xED) {
      if (mem.GetMemoria(pos + 1) == 0x47)
        return Nem + " I, A";
      if (mem.GetMemoria(pos + 1) == 0x57)
        return Nem + " A, I";
    }
    if (datoH == 0xDD || datoH == 0xFD) {
      if (mem.GetMemoria(pos + 1) == 0x36)
        return Nem + " (" + Patron4(datoH) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2))
            + ")," + Integer.toHexString(mem.GetMemoria(pos + 3));
      if (mem.GetMemoria(pos + 1) != 0x76 && ((mem.GetMemoria(pos + 1) / 0x10 <= 0x7
          && mem.GetMemoria(pos + 1) / 0x10 >= 0x4) && (mem.GetMemoria(pos + 1) % 0X10 == 0x6
          || mem.GetMemoria(pos + 1) % 0X10 == 0xE)))
        return Nem + "  " + Patron5(mem.GetMemoria(pos + 1)) + ", (" + Patron4(datoH) + "+"
            + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
      if (mem.GetMemoria(pos + 1) != 0x76 && (mem.GetMemoria(pos + 1) >= 0x70 && mem.GetMemoria(
          pos + 1) <= 0x77))
        return Nem + " (" + Patron4(datoH) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2))
            + "), " + Patron1(mem.GetMemoria(pos + 1) % 0x10);
    }
    mem.Iniposaux2(pos);
    return null;
  }

  private String GrupoArit8(int datoH) {
    //System.out.println("SE PARO EN ARIT8");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (REGD == 0x8 && REGF < 8)
      return "ADD A," + Patron1(REGF);
    if (REGD == 0x8 && REGF >= 8)
      return "ADC A," + Patron1(REGF);
    if (REGD == 0x9 && REGF < 8)
      return "SUB " + Patron1(REGF);
    if (REGD == 0x9 && REGF >= 8)
      return "SBC A," + Patron1(REGF);
    if (REGD == 0xA && REGF < 8)
      return "AND " + Patron1(REGF);
    if (REGD == 0xA && REGF >= 8)
      return "XOR " + Patron1(REGF);
    if (REGD == 0xB && REGF < 8)
      return "OR " + Patron1(REGF);
    if (REGD == 0xB && REGF >= 8)
      return "CP " + Patron1(REGF);
    if (REGD >= 0xC && REGD <= 0xF) {
      if ((REGD == 0xC || REGD == 0x0D) && (REGF == 0X6 || REGF == 0XE))
        return Patron7(REGD, REGF) + " A," + Integer.toHexString(mem.GetMemoria(pos + 1));
      if ((REGD == 0xE || REGD == 0x0F) && (REGF == 0X6 || REGF == 0XE))
        return Patron7(REGD, REGF) + " " + Integer.toHexString(mem.GetMemoria(pos + 1));
    }
    if (datoH == 0x34)
      return "INC (HL)";
    if (datoH == 0x35)
      return "DEC (HL)";
    if (REGD >= 0x0 && REGD <= 0x3) {
      if (REGF == 4 || REGF == 0xC)
        return "INC " + Patron5(datoH);
      if (REGF == 5 || REGF == 0xD)
        return "DEC " + Patron5(datoH);
    }
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x34)
      return "INC (" + Patron4(datoH) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x35)
      return "DEC (" + Patron4(datoH) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
    if (datoH == 0xDD || datoH == 0xFD) {
      if ((mem.GetMemoria(pos + 1) / 0x10 == 0x8 || mem.GetMemoria(pos + 1) / 0x10 == 0x09) && (
          mem.GetMemoria(pos + 1) % 0x10 == 0x6 || mem.GetMemoria(pos + 1) % 0x10 == 0x0e))
        return Patron7(mem.GetMemoria(pos + 1) / 0x10, mem.GetMemoria(pos + 1) % 0x10) + " A,("
            + Patron4(datoH) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
      if ((mem.GetMemoria(pos + 1) / 0x10 == 0xA || mem.GetMemoria(pos + 1) / 0x10 == 0x0B) && (
          mem.GetMemoria(pos + 1) % 0x10 == 0x6 || mem.GetMemoria(pos + 1) % 0x10 == 0x0e))
        return Patron7(mem.GetMemoria(pos + 1) / 0x10, mem.GetMemoria(pos + 1) % 0x10) + " A,("
            + Patron4(datoH) + "+" + Integer.toHexString(mem.GetMemoria(pos + 2)) + ")";
    }
    mem.Iniposaux2(pos);
    return null;
  }

  private String GrupoInteryTrnas(int datoH) {
    //System.out.println("SE PARO EN INTER Y TRANS");
    if (datoH == 0x8)
      return "EX AF,AF'";
    if (datoH == 0xD9)
      return "EXX";
    if (datoH == 0xeB)
      return "EX DE,HL";
    if (datoH == 0xe3)
      return "EX (SP),HL";
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xE3)
      return "EX (SP)," + Patron4(datoH);
    if (datoH == 0xED) {
      if (mem.GetMemoria(pos + 1) == 0xA0)
        return "LDI";
      if (mem.GetMemoria(pos + 1) == 0xA1)
        return "CPI";
      if (mem.GetMemoria(pos + 1) == 0xB0)
        return "LDIR";
      if (mem.GetMemoria(pos + 1) == 0xB1)
        return "CPIR";
      if (mem.GetMemoria(pos + 1) == 0xA8)
        return "LDD";
      if (mem.GetMemoria(pos + 1) == 0xA9)
        return "CPD";
      if (mem.GetMemoria(pos + 1) == 0xB8)
        return "LDDR";
      if (mem.GetMemoria(pos + 1) == 0xB9)
        return "CPDR";
    }
    mem.Iniposaux2(pos);
    return null;
  }

  private String GrupoA16(int datoH) {
    //System.out.println("SE PARO EN GA16");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0x09 || datoH == 0x19 || datoH == 0x29 || datoH == 0x39)
      return "ADD HL," + Patron6(REGD);
    if (datoH == 0XED && (mem.GetMemoria(pos + 1) == 0x4A || mem.GetMemoria(pos + 1) == 0x5A
        || mem.GetMemoria(pos + 1) == 0x6A || mem.GetMemoria(pos + 1) == 0x7A))
      return "ADC HL," + Patron6(mem.GetMemoria(pos + 1) / 0x10);
    if (datoH == 0XED && (mem.GetMemoria(pos + 1) == 0x42 || mem.GetMemoria(pos + 1) == 0x52
        || mem.GetMemoria(pos + 1) == 0x62 || mem.GetMemoria(pos + 1) == 0x72))
      return "SBC HL," + Patron6(mem.GetMemoria(pos + 1) / 0x10);
    if (datoH == 0XdD && (mem.GetMemoria(pos + 1) == 0x09 || mem.GetMemoria(pos + 1) == 0x19
        || mem.GetMemoria(pos + 1) == 0x29 || mem.GetMemoria(pos + 1) == 0x39))
      return "ADD " + Patron4(datoH) + "," + Patron8(mem.GetMemoria(pos + 1) / 0x10);
    if (datoH == 0xfd && (mem.GetMemoria(pos + 1) == 0x09 || mem.GetMemoria(pos + 1) == 0x19
        || mem.GetMemoria(pos + 1) == 0x29 || mem.GetMemoria(pos + 1) == 0x39))
      return "ADD " + Patron4(datoH) + "," + Patron9(mem.GetMemoria(pos + 1) / 0x10);
    if (REGD >= 0x0 && REGD <= 0x3) {
      if (REGF == 0x3)
        return "INC " + Patron6(REGD);
      if (REGF == 0xB)
        return "DEC " + Patron6(REGD);
    }
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x23)
      return "INC " + Patron4(datoH);
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x2B)
      return "DEC " + Patron4(datoH);
    mem.Iniposaux2(pos);
    return null;
  }

  private String ControlG(int datoH) {
    //System.out.println("SE PARO EN CG");
    if ((datoH == 0x00) && (datoH / 0x10 != 0xf))
      return "NOP";
    if (datoH == 0x27)
      return "DAA";
    if (datoH == 0x2f)
      return "CPL";
    if (datoH == 0x3f)
      return "CCF";
    if (datoH == 0x37)
      return "SCF";
    if (datoH == 0xF3)
      return "DI";
    if (datoH == 0xFB)
      return "EI";
    if (datoH == 0xED && mem.GetMemoria(pos + 1) == 0x44)
      return "NEG";
    if (datoH == 0xED && mem.GetMemoria(pos + 1) == 0x46)
      return "IM 0";
    if (datoH == 0xED && mem.GetMemoria(pos + 1) == 0x56)
      return "IM 1";
    if (datoH == 0xED && mem.GetMemoria(pos + 1) == 0x5E)
      return "IM 2";
    mem.Iniposaux2(pos);
    return null;
  }

  private String JUMP(int datoH) {
    //System.out.println("SE PARO EN JUMP");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0xc3)
      return "JP " + Integer.toHexString(mem.GetMemoria(pos + 2)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 1));
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x2 || REGF == 0xA))
      return "JP " + Patron10(datoH) + ", " + Integer.toHexString(mem.GetMemoria(pos + 2)) + ""
          + Integer.toHexString(mem.GetMemoria(pos + 1));
    if (datoH == 0x18)
      return "JR " + Integer.toHexString(mem.GetMemoria(pos + 1));
    if ((REGD >= 0x2 && REGD <= 0x3) && (REGF == 0x0 || REGF == 0x8))
      return "JR " + Patron10(datoH) + " ," + Integer.toHexString(mem.GetMemoria(pos + 1));
    if ((datoH == 0XFD || datoH == 0XDD) && mem.GetMemoria(pos + 1) == 0XE9)
      return "JP (" + Patron4(datoH) + ")";
    if (datoH == 0xE9)
      return "JP (HL)";
    if (datoH == 0x10)
      return "DJNZ " + Integer.toHexString(mem.GetMemoria(pos + 1));
    mem.Iniposaux2(pos);
    return null;
  }

  private String CallRet(int datoH) {
    //System.out.println("SE PARO EN CALL");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0xcd)
      return "CALL " + Integer.toHexString(mem.GetMemoria(pos + 2)) + "" + Integer.toHexString(
          mem.GetMemoria(pos + 1));
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x4 || REGF == 0xC))
      return "CALL " + Patron10(datoH) + " " + Integer.toHexString(mem.GetMemoria(pos + 2)) + ""
          + Integer.toHexString(mem.GetMemoria(pos + 1));
    if (datoH == 0xc9)
      return "RET";
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x0 || REGF == 0x8))
      return "RET " + Patron10(datoH);
    if (datoH == 0xED && (mem.GetMemoria(pos + 1)) == 0x4D)
      return "RETI";
    if (datoH == 0xED && (mem.GetMemoria(pos + 1)) == 0x45)
      return "RETN";
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x7 || REGF == 0xF))
      return "RST " + Patron11(REGD, REGF);
    mem.Iniposaux2(pos);
    return null;
  }

  private String Patron11(int dato, int dato2) {
    String aux = null;
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 <= 8)
      aux = "0";
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 > 8)
      aux = "8";
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 <= 8)
      aux = "10";
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 > 8)
      aux = "18";
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 <= 8)
      aux = "20";
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 > 8)
      aux = "28";
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 <= 8)
      aux = "30";
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 > 8)
      aux = "38";
    return aux;

  }

  private String Patron10(int dato) {
    int REGD = dato / 0x10;
    int REGF = dato % 0x10;
    if (REGF == 0x2 || REGF == 0x0 || REGF == 0x4) {
      if (REGD == 0xC || REGD == 0x2)
        return "NZ";
      if (REGD == 0xD || REGD == 0x3)
        return "NC";
      if (REGD == 0xE)
        return "PO";
      if (REGD == 0xF)
        return "P";
    }
    if (REGF == 0xA || REGF == 0x8 || REGF == 0xC) {
      if (REGD == 0xC || REGD == 0x2)
        return "Z";
      if (REGD == 0xD || REGD == 0x3)
        return "C";
      if (REGD == 0xE)
        return "PE";
      if (REGD == 0xF)
        return "M";
    }
    return null;
  }

  private String Patron9(int dato) {
    String aux = null;
    if (dato == 0x0 || dato == 0xC)
      aux = "BC";
    if (dato == 0x1 || dato == 0xD)
      aux = "DE";
    if (dato == 0x2 || dato == 0xE)
      aux = "IY";
    if (dato == 0x3 || dato == 0xF)
      aux = "SP";
    return aux;
  }

  private String Patron8(int dato) {
    String aux = null;
    if (dato == 0x0 || dato == 0xC)
      aux = "BC";
    if (dato == 0x1 || dato == 0xD)
      aux = "DE";
    if (dato == 0x2 || dato == 0xE)
      aux = "IX";
    if (dato == 0x3 || dato == 0xF)
      aux = "SP";
    return aux;
  }

  private String Patron7(int datoD, int datoF) {
    if (datoD == 0xC || datoD == 0x8) {
      if (datoF == 0x6)
        return "ADD";
      if (datoF == 0xE)
        return "ADC";
    }
    if (datoD == 0xD || datoD == 0x9) {
      if (datoF == 0x6)
        return "SUB";
      if (datoF == 0xE)
        return "SBC";
    }
    if ((datoD == 0xE || datoD == 0xA) && datoF == 0x6)
      return "AND";
    if ((datoD == 0xB || datoD == 0xf) && datoF == 0x6)
      return "OR";
    if ((datoD == 0xE || datoD == 0xA) && datoF == 0xE)
      return "XOR";
    if ((datoD == 0xF || datoD == 0xA || datoD == 0xB) && datoF == 0xE)
      return "CP";
    return null;
  }

  private String Patron6(int dato) {
    String aux = null;
    if (dato == 0x0 || dato == 0xC || dato == 0x4)
      aux = "BC";
    if (dato == 0x1 || dato == 0xD || dato == 0x5)
      aux = "DE";
    if (dato == 0x2 || dato == 0xE || dato == 0x6)
      aux = "HL";
    if (dato == 0x3 || dato == 0xF || dato == 0x7)
      aux = "SP";
    return aux;
  }

  private String Patron5(int dato) {
    int REGD = dato / 0x10;
    int REGF = dato % 0x10;
    String aux = null;
    if (REGF == 0x0E || REGF == 0x08 || REGF == 0X0D || REGF == 0X0c) {
      if ((REGD == 0x0 || REGD == 0x4))
        aux = "C";
      if ((REGD == 0x1 || REGD == 0x5))
        aux = "E";
      if ((REGD == 0x2 || REGD == 0x6))
        aux = "L";
      if ((REGD == 0x3 || REGD == 0x7))
        aux = "A";
    }
    if (REGF == 0x06 || REGF == 0x00 || REGF == 0X05 || REGF == 0x04) {
      if ((REGD == 0x0 || REGD == 0x4))
        aux = "B";
      if ((REGD == 0x1 || REGD == 0x5))
        aux = "D";
      if ((REGD == 0x2 || REGD == 0x6))
        aux = "H";
    }
    return aux;
  }

  private String Patron4(int dato) {
    if (dato == 0xDD)
      return "IX";
    return "IY";
  }

  private String Patron3(int dato) {
    if (dato >= 8)
      return "R";
    return "L";
  }

  private String Patron2(int dato, int dato2) {
    String aux = null;
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 <= 8)
      aux = "0";
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 > 8)
      aux = "1";
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 <= 8)
      aux = "2";
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 > 8)
      aux = "3";
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 <= 8)
      aux = "4";
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 > 8)
      aux = "5";
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 <= 8)
      aux = "6";
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 > 8)
      aux = "7";
    return aux;
  }

  private String Patron1(int dato) {
    String aux = null;
    if (dato == 0x0 || dato == 0x8)
      aux = "B";
    if (dato == 0x1 || dato == 0x9)
      aux = "C";
    if (dato == 0x2 || dato == 0xA)
      aux = "D";
    if (dato == 0x3 || dato == 0xB)
      aux = "E";
    if (dato == 0x4 || dato == 0xC)
      aux = "H";
    if (dato == 0x5 || dato == 0xD)
      aux = "L";
    if (dato == 0x6 || dato == 0xe)
      aux = "(HL)";
    if (dato == 0x7 || dato == 0xF)
      aux = "A";
    return aux;
  }
}