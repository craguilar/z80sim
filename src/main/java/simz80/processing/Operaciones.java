package simz80.processing;

import java.util.Arrays;

public class Operaciones {

  private final Memoria mem;
  private int pos;

  public Operaciones(Memoria mem) {
    pos = -1;
    this.mem = mem;
  }

  public Memoria EjecutaInstruccion(int pos) {
    mem.APAGABANDERADEACCESO();
    this.pos = pos;//POSICION DEL DATO QUE ENTRARA A LOS METODOS.
    EJECUTA(mem.GetMemoria(pos));
    mem.ENCIENDEBADERADEACCESO();
    return mem;
  }

  private void EJECUTA(int datoabuscar) {
    GrupoCarga16(datoabuscar);
    GrupoBitRes(datoabuscar);
    GrupoRotacion(datoabuscar);
    GrupoCarga8(datoabuscar);
    GrupoArit8(datoabuscar);
    GrupoInteryTrnas(datoabuscar);
    GrupoA16(datoabuscar);
    ControlG(datoabuscar);
    JUMP(datoabuscar);
    CallRet(datoabuscar);
  }

  private void GrupoCarga16(int datoH) {
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (REGF == 0x1 && (REGD >= 0 && REGD <= 3)) {
      Patron6Set(REGD, mem.GetMemoriaO(pos + 2), mem.GetMemoriaO(pos + 1));
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0x21) {
      Patron4Set(datoH, mem.GetMemoria(pos + 3) * 0X100 + mem.GetMemoria(pos + 2));
    }
    if (datoH == 0x2A) {
      mem.SetHL(mem.GetMemoria(mem.GetMemoria(pos + 2) * 0X100 + mem.GetMemoria(pos + 1) + 1),
          mem.GetMemoriaO(mem.GetMemoriaO(pos + 2) * 0X100 + mem.GetMemoriaO(pos + 1)));

    }

    if (datoH == 0xED && (mem.GetMemoria(pos + 1)) % 0x10 == 0xB && ((mem.GetMemoria(pos + 1))
        / 0x10 == 0x4 || (mem.GetMemoria(pos + 1)) / 0x10 == 0x5 || ((mem.GetMemoria(pos + 1))
        / 0x10 == 0x7))) {
      Patron6Set((mem.GetMemoria(pos + 1)) / 0x10,
          mem.GetMemoriaO(mem.GetMemoriaO(pos + 3) * 0X100 + mem.GetMemoriaO(pos + 2) + 1),
          mem.GetMemoriaO(mem.GetMemoriaO(pos + 3) * 0X100 + mem.GetMemoriaO(pos + 2)));
      //System.out.println("SE PARO EN GC16");
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(1) == 0x2A) {
      mem.SetMemoria(Patron4Get(datoH),
          mem.GetMemoriaO(mem.GetMemoriaO(pos + 3) * 0x100 + mem.GetMemoriaO(pos + 2)));
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0x22) {
      mem.SetMemoria((mem.GetMemoria(pos + 2)) * 0x100 + mem.GetMemoria(pos + 1) + 1, mem.GetH());
      mem.SetMemoria((mem.GetMemoria(pos + 2)) * 0x100 + mem.GetMemoria(pos + 1), mem.GetL());
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xED && (mem.GetMemoria(pos + 1)) % 0x10 == 0x3 && ((mem.GetMemoria(pos + 1))
        / 0x10 == 0x4 || (mem.GetMemoria(pos + 1)) / 0x10 == 0x5 || ((mem.GetMemoria(pos + 1))
        / 0x10 == 0x7))) {
      short aux = Patron6Get(mem.GetMemoria(pos + 1) / 0x10);
      byte H = (byte) (aux / 0x100);
      byte L = (byte) (aux % 0x100);
      mem.SetMemoria((mem.GetMemoria(pos + 3)) * 0x100 + mem.GetMemoria(pos + 1) + 1, H);
      mem.SetMemoria((mem.GetMemoria(pos + 3)) * 0x100 + mem.GetMemoria(pos + 1), L);
      // System.out.println("SE PARO EN GC16");
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0x22) {
      short aux = Patron4Get(datoH);
      byte H = (byte) (aux / 0x100);
      byte L = (byte) (aux % 0x100);
      mem.SetMemoria((mem.GetMemoria(pos + 3)) * 0x100 + (mem.GetMemoria(pos + 2)) + 1, H);
      mem.SetMemoria((mem.GetMemoria(pos + 3)) * 0x100 + (mem.GetMemoria(pos + 2)), L);
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xF9) {
      mem.SetSP(mem.GetHL());
      //System.out.println("SE PARO EN GC16");
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xF9) {
      mem.SetSP(Patron4Get(datoH));
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xF5) {
      mem.SetMemoria(mem.GetSP() - 2, mem.GetF());
      mem.SetMemoria(mem.GetSP() - 1, mem.GetA());
      mem.SetSP((short) (mem.GetSP() - 2));
      //   System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xc5 || datoH == 0xd5 || datoH == 0xe5) {
      short aux = Patron6Get(REGD);
      byte L = (byte) (aux % 0x100);
      byte H = (byte) (aux / 0x100);
      mem.SetMemoria(mem.GetSP() - 2, L);
      mem.SetMemoria(mem.GetSP() - 1, H);
      mem.SetSP((short) (mem.GetSP() - 2));
      //  System.out.println("SE PARO EN GC16");
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xE5) {
      int aux = Patron4Get(datoH);
      byte L = (byte) (aux % 0x100);
      byte H = (byte) (aux / 0x100);
      mem.SetMemoria(mem.GetSP() - 2, L);
      mem.SetMemoria(mem.GetSP() - 1, H);
      mem.SetSP((short) (mem.GetSP() - 2));
      // System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xF1) {
      mem.SetF(mem.GetMemoriaO(mem.GetSP()));
      mem.SetA(mem.GetMemoriaO(mem.GetSP() + 1));
      mem.SetMemoria(mem.GetSP(), (byte) 0);
      mem.SetMemoria(mem.GetSP() - 1, (byte) 0);
      mem.SetSP((short) (mem.GetSP() + 2));
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xc1) {
      mem.SetC(mem.GetMemoriaO(mem.GetSP()));
      mem.SetB(mem.GetMemoriaO(mem.GetSP() + 1));
      mem.SetMemoria(mem.GetSP(), (byte) 0);
      mem.SetMemoria(mem.GetSP() - 1, (byte) 0);
      mem.SetSP((short) (mem.GetSP() + 2));
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xd1) {
      mem.SetE(mem.GetMemoriaO(mem.GetSP()));
      mem.SetD(mem.GetMemoriaO(mem.GetSP() + 1));
      mem.SetMemoria(mem.GetSP(), (byte) 0);
      mem.SetMemoria(mem.GetSP() - 1, (byte) 0);
      mem.SetSP((short) (mem.GetSP() + 2));
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xe1) {
      mem.SetL(mem.GetMemoriaO(mem.GetSP()));
      mem.SetH(mem.GetMemoriaO(mem.GetSP() + 1));
      mem.SetMemoria(mem.GetSP(), (byte) 0);
      mem.SetMemoria(mem.GetSP() - 1, (byte) 0);
      mem.SetSP((short) (mem.GetSP() + 2));
      //System.out.println("SE PARO EN GC16");
    }
    if (datoH == 0xDD && mem.GetMemoria(pos + 1) == 0xE1) {
      byte L = mem.GetMemoriaO(mem.GetSP());
      byte H = mem.GetMemoriaO(mem.GetSP() + 1);
      mem.SetMemoria(mem.GetSP(), (byte) 0);
      mem.SetMemoria(mem.GetSP() - 1, (byte) 0);
      mem.SetSP((short) (mem.GetSP() + 2));
      mem.SetIX((short) (H * 0x100 + L));
    }
    if (datoH == 0xFD && mem.GetMemoria(pos + 1) == 0xE1) {
      int L = mem.GetMemoria(mem.GetSP());
      int H = mem.GetMemoria(mem.GetSP() + 1);
      mem.SetMemoria(mem.GetSP(), (byte) 0);
      mem.SetMemoria(mem.GetSP() - 1, (byte) 0);
      mem.SetSP((short) (mem.GetSP() + 2));
      mem.SetIY((short) (H * 0x100 + L));
    }
  }

  private void GrupoBitRes(int datoH) {
    char[] aux2 = new char[8];//Aqui es donde prenderemos afectaremos el bit.
    String aux3 = "";

    Arrays.fill(aux2, '0');
    if (datoH == 0xcb) {
      int z = 0;
      String aux1 = "00000000000000000000000000000000" + Integer.toBinaryString(
          Patron1Get(mem.GetMemoria(pos + 1) % 0X10));

      for (int i = aux1.length() - 1; i > aux1.length() - 9; i--) {
        aux2[z] = aux1.charAt(i);
        z++;
      }

      if (mem.GetMemoria(pos + 1) >= 0x40 && mem.GetMemoria(pos + 1) < 0x80) {
        if (aux2[7 - Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)]
            == '1') {
          aux2[7 - Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)] = '0';
          mem.ENCIENDEZ();
        } else {
          aux2[7 - Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)] = '1';
          mem.APAGAZ();
        }
        for (int i = 0; i < aux2.length; i++) {
          aux3 = aux3 + aux2[i];
        }
        Patron1Set(mem.GetMemoria(pos + 1) % 0X10, (byte) Integer.parseInt(aux3));
        mem.ENCIENDEH();
        mem.APAGANEG();
      }

      if (mem.GetMemoria(pos + 1) >= 0x80 && mem.GetMemoria(pos + 1) < 0xC0) {
        aux2[7 - Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)] = '0';
        for (int i = 0; i < aux2.length; i++) {
          aux3 = aux3 + aux2[i];
        }
        Patron1Set(mem.GetMemoria(pos + 1) % 0X10, (byte) Integer.parseInt(aux3));

      }
      if (mem.GetMemoria(pos + 1) >= 0xC0 && mem.GetMemoria(pos + 1) <= 0xff) {
        aux2[7 - Patron2(mem.GetMemoria(pos + 1) / 0X10, mem.GetMemoria(pos + 1) % 0X10)] = '1';
        for (int i = 0; i < aux2.length; i++) {
          aux3 = aux3 + aux2[i];
        }
        Patron1Set(mem.GetMemoria(pos + 1) % 0X10, (byte) Integer.parseInt(aux3));
        //System.out.println("SE PARO EN GBR");
      }
    }

    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xCB) {
      int posi = Patron4Get(mem.GetMemoria(pos)) + mem.GetMemoriaO(pos + 2);
      int z = 0;
      byte valor = mem.GetMemoriaO(posi);
      String aux1 = "00000000000000000000000000000000000" + Integer.toBinaryString(valor);
      for (int i = aux1.length() - 1; i > aux1.length() - 9; i--) {
        aux2[z] = aux1.charAt(i);
        z++;
      }

      if (mem.GetMemoria(pos + 3) == 0x46 || mem.GetMemoria(pos + 3) == 0x4E || mem.GetMemoria(
          pos + 3) == 0x56 || mem.GetMemoria(pos + 3) == 0x5E || mem.GetMemoria(pos + 3) == 0x66
          || mem.GetMemoria(pos + 3) == 0x6E || mem.GetMemoria(pos + 3) == 0x76 || mem.GetMemoria(
          pos + 3) == 0x7E) {

        if (aux2[7 - Patron2(mem.GetMemoriaO(pos + 3) / 0X10, mem.GetMemoriaO(pos + 3) % 0X10)]
            == '1') {
          try {
            aux2[aux2[7 - Patron2(mem.GetMemoriaO(pos + 3) / 0X10,
                mem.GetMemoriaO(pos + 3) % 0X10)]] = '0';
          } catch (ArrayIndexOutOfBoundsException aer) {

          }
          mem.ENCIENDEZ();

          //  System.out.println("SE PARO EN GBR");
        } else {
          try {
            aux2[aux2[7 - Patron2(mem.GetMemoria(pos + 3) / 0X10,
                mem.GetMemoria(pos + 3) % 0X10)]] = '1';
          } catch (ArrayIndexOutOfBoundsException aer) {

          }
          mem.APAGAZ();
          //System.out.println("SE PARO EN GBR");
        }
        for (int i = 0; i < aux2.length; i++) {
          aux3 = aux3 + aux2[i];
        }
        mem.SetMemoria(posi, (byte) Integer.parseInt(aux3));
        mem.ENCIENDEH();
        mem.APAGANEG();

      }
      if (mem.GetMemoria(pos + 3) == 0x86 || mem.GetMemoria(pos + 3) == 0x8E || mem.GetMemoria(
          pos + 3) == 0x96 || mem.GetMemoria(pos + 3) == 0x9E || mem.GetMemoria(pos + 3) == 0xA6
          || mem.GetMemoria(pos + 3) == 0xAE || mem.GetMemoria(pos + 3) == 0xB6 || mem.GetMemoria(
          pos + 3) == 0xBE) {
        try {
          aux2[aux2[7 - Patron2(mem.GetMemoria(pos + 3) / 0X10,
              mem.GetMemoria(pos + 3) % 0X10)]] = '0';
        } catch (ArrayIndexOutOfBoundsException aer) {

        }
        for (int i = 0; i < aux2.length; i++) {
          aux3 = aux3 + aux2[i];
        }
        mem.SetMemoria(posi, (byte) Integer.parseInt(aux3));
        // System.out.println("SE PARO EN GBR");

      }
      if ((mem.GetMemoria(pos + 3) != 0xff) && mem.GetMemoria(pos + 3) == 0xC6 || mem.GetMemoria(
          pos + 3) == 0xCE || mem.GetMemoria(pos + 3) == 0xd6 || mem.GetMemoria(pos + 3) == 0xdE
          || mem.GetMemoria(pos + 3) == 0xe6 || mem.GetMemoria(pos + 3) == 0xeE || mem.GetMemoria(
          pos + 3) == 0xf6 || mem.GetMemoria(pos + 3) == 0xfE) {
        try {
          aux2[aux2[7 - Patron2(mem.GetMemoria(pos + 3) / 0X10,
              mem.GetMemoria(pos + 3) % 0X10)]] = '1';
        } catch (ArrayIndexOutOfBoundsException aer) {

        }
        for (int i = 0; i < aux2.length; i++) {
          aux3 = aux3 + aux2[i];
        }
        mem.SetMemoria(posi, (byte) Integer.parseInt(aux3));
        //System.out.println("SE PARO EN GBR");
      }
    }
  }

  private void GrupoRotacion(int datoH) {
    int REGF = mem.GetMemoria(pos + 1) % 0X10;
    if (datoH == 0xCB) {
      if (mem.GetMemoria(pos + 1) >= 0x00 && mem.GetMemoria(pos + 1) < 0x10) {
        if (Patron3(REGF) == 1) {//RRC
          if (Integer.lowestOneBit(Patron1Get(REGF)) == 1) {
            Patron1Set(REGF, (byte) (-1 * Patron1Get(REGF) / 2));
            ////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////// CHECAR PARIDAD///////////////////////////////////////////////////////////
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        } else {//RLC
          if (Integer.signum(Patron1Get(REGF)) == -1) {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2 + 1));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2));

            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        }
        mem.APAGAH();
        mem.APAGANEG();
      }

      if (mem.GetMemoria(pos + 1) >= 0x10 && mem.GetMemoria(pos + 1) < 0x20) {
        if (Patron3(REGF) == 1)//RR
        {
          if (Integer.lowestOneBit(Patron1Get(REGF)) == 1) {
            if (mem.GetBandera(0) == 1) {
              Patron1Set(REGF, (byte) (-1 * Patron1Get(REGF) / 2));
            } else {
              Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            }
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            if (mem.GetBandera(0) == 1) {
              Patron1Set(REGF, (byte) (-1 * Patron1Get(REGF) / 2));
            } else {
              Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            }

            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        } else {///////RL
          if (Integer.signum(Patron1Get(REGF)) == -1) {
            if (mem.GetBandera(0) == 1) {
              Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2 + 1));
            } else {
              Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2));
            }

            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2 + 1));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        }
        mem.APAGAH();
        mem.APAGANEG();
      }
      if (mem.GetMemoria(pos + 1) >= 0x20 && mem.GetMemoria(pos + 1) < 0x30) {
        if (Patron3(REGF) == 1)//SRA
        {
          if (Integer.lowestOneBit(Patron1Get(REGF)) == 1) {
            if (Integer.signum(Patron1Get(REGF)) == -1) {
              Patron1Set(REGF, (byte) (-1 * Patron1Get(REGF) / 2));
            } else {
              Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            }

            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            if (Integer.signum(Patron1Get(REGF)) == -1) {
              Patron1Set(REGF, (byte) (-1 * Patron1Get(REGF) / 2));
            } else {
              Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            }

            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        } else {//SLA
          if (Integer.signum(Patron1Get(REGF)) == -1) {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) * 2));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        }
        mem.APAGAH();
        mem.APAGANEG();

      }
      if (mem.GetMemoria(pos + 1) >= 0x38 && mem.GetMemoria(pos + 1) < 0x40) {
        if (Patron3(REGF) == 1)//SRL
        {
          if (Integer.lowestOneBit(Patron1Get(REGF)) == 1) {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.ENCIENDEC();
          } else {
            Patron1Set(REGF, (byte) (Patron1Get(REGF) / 2));
            mem.ALTERAFLAGS((byte) 0, Patron1Get(REGF));
            mem.APAGAC();
          }
        }

      }
    }
    if ((datoH == 0XDD || datoH == 0XFD) && mem.GetMemoria(pos + 1) == 0xCB) {
      int posi = Patron4Get(datoH) + (mem.GetMemoriaO(pos + 2));//Posicion de memoria deseada
      byte apoyo1 = mem.GetMemoriaO(posi);
      if (mem.GetMemoria(pos + 3) == 0x06 || mem.GetMemoria(pos + 3) == 0x0E) {
        if (Patron3(mem.GetMemoria(pos + 3) % 0X10) == 1) {//RRC
          if (Integer.lowestOneBit(apoyo1) == 1) {
            mem.SetMemoria(posi, (byte) ((-1 * apoyo1 / 2)));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.ENCIENDEC();
          } else {
            mem.SetMemoria(posi, (byte) ((apoyo1 / 2)));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.APAGAC();
          }
        } else {
          mem.SetMemoria(posi, (byte) ((apoyo1 * 2)));
          mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
          if (mem.GetBandera(7) == 1) {
            mem.SetMemoria(posi, (byte) (apoyo1 * 2 + 1));
            mem.ENCIENDEC();
          } else {
            mem.APAGAC();
          }
          mem.APAGAH();
          mem.APAGANEG();
        }

      }

      if (mem.GetMemoria(pos + 3) == 0x16 || mem.GetMemoria(pos + 3) == 0x1E) {
        if (Patron3(mem.GetMemoria(pos + 3) % 0X10) == 1)//RR
        {
          if (Integer.lowestOneBit(Patron1Get(REGF)) == 1) {
            if (mem.GetBandera(0) == 1) {
              mem.SetMemoria(posi, (byte) ((-1 * apoyo1 / 2)));
            } else {
              mem.SetMemoria(posi, (byte) ((apoyo1 / 2)));
            }
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.ENCIENDEC();
          } else {
            if (mem.GetBandera(0) == 1) {
              mem.SetMemoria(posi, (byte) (-1 * apoyo1 / 2));
            } else {
              mem.SetMemoria(posi, (byte) (apoyo1 / 2));
            }

            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.APAGAC();
          }
        } else {//RL
          if (Integer.signum(apoyo1) == -1) {
            mem.SetMemoria(posi, (byte) (apoyo1 * 2 + mem.GetBandera(0)));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.ENCIENDEC();
          } else {
            mem.SetMemoria(posi, (byte) (apoyo1 * 2 + mem.GetBandera(0)));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.APAGAC();
          }
        }
      }
      if (mem.GetMemoria(pos + 3) == 0x26 || mem.GetMemoria(pos + 3) == 0x2E) {
        if (Patron3(mem.GetMemoria(pos + 3) % 0X10) == 1)//SRA
        {
          if (Integer.lowestOneBit(apoyo1) == 1) {
            if (Integer.signum(apoyo1) == -1) {
              mem.SetMemoria(REGF, (byte) (-1 * apoyo1 / 2));
            } else {
              mem.SetMemoria(REGF, (byte) (apoyo1 / 2));
            }
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.ENCIENDEC();
          } else {
            if (Integer.signum(apoyo1) == -1) {
              mem.SetMemoria(posi, (byte) (-1 * apoyo1 / 2));
            } else {
              mem.SetMemoria(posi, (byte) (apoyo1 / 2));
            }
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.APAGAC();
          }
        } else {//SLA
          if (Integer.signum(apoyo1) == -1) {
            mem.SetMemoria(posi, (byte) (apoyo1 * 2));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.ENCIENDEC();
          } else {
            mem.SetMemoria(posi, (byte) (apoyo1 * 2));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.APAGAC();
          }
        }
      }
      if (mem.GetMemoria(pos + 3) == 0x3e) {
        if (Patron3(mem.GetMemoria(pos + 3) % 0X10) == 1) {
          if (Integer.lowestOneBit(apoyo1) == 1) {
            mem.SetMemoria(posi, (byte) (apoyo1 / 2));
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
            mem.ENCIENDEC();

          } else {
            mem.SetMemoria(posi, (byte) (apoyo1 / 2));
            mem.APAGAC();
            mem.ALTERAFLAGS((byte) 0, mem.GetMemoriaO(posi));
          }
        }
      }
      mem.APAGAH();
      mem.APAGANEG();
    }
    //RLCA
    if (datoH == 0x7) {

      mem.SetA((byte) (mem.GetA() * 2));
      mem.ALTERAFLAGS((byte) 0, mem.GetA());

      if (mem.GetBandera(7) == 1) {
        mem.SetA((byte) (mem.GetA() + 1));
        mem.ENCIENDEC();
      } else {
        mem.APAGAC();
      }
      mem.APAGAH();
      mem.APAGANEG();
    }
    //RRCA
    if (datoH == 0x0F) {
      if (Integer.lowestOneBit(mem.GetA()) == 1) {
        mem.SetA((byte) (-1 * mem.GetA() / 2));
        mem.ALTERAFLAGS((byte) 0, mem.GetA());
        mem.ENCIENDEC();
      } else {
        mem.SetA((byte) (mem.GetA() / 2));
        mem.ALTERAFLAGS((byte) 0, mem.GetA());
        mem.APAGAC();
      }
      mem.APAGAH();
      mem.APAGANEG();
    }

    if (datoH == 0x17) {//RLA

      mem.SetA((byte) (mem.GetA() * 2));

      mem.ALTERAFLAGS((byte) 0, mem.GetA());

      if (mem.GetA() < 0) {
        mem.SetA((byte) (mem.GetA() + 1));
        mem.ENCIENDEC();

      } else
        mem.APAGAC();
      mem.APAGAH();
      mem.APAGANEG();
    }
    if (datoH == 0x1F) {//RRA
      if (mem.GetA() % 2 != 0) {
        mem.SetA((byte) (-1 * mem.GetA() / 2));
        mem.ALTERAFLAGS((byte) 0, mem.GetA());
        mem.ENCIENDEC();
      } else {
        mem.SetA((byte) (mem.GetA() / 2));
        mem.ALTERAFLAGS((byte) 0, mem.GetA());
        mem.APAGAC();
      }
      mem.APAGAH();
      mem.APAGANEG();
    }

    ////////////////////////////////////////////////PENDIENTES////////////////
    //if(datoH==0xED){
    //if(mem.GetMemoria(pos+1)==0x67)
    //return "RRD";
    if (datoH == 0xED && mem.GetMemoria(pos + 1) == 0x6F) {
      byte AUX1 = mem.GetMemoriaO(mem.GetHL());
      byte H1 = (byte) (AUX1 / 0x10);
      byte L1 = (byte) (AUX1 % 0x10);
      byte AUX2 = mem.GetA();
      byte H2 = (byte) (AUX2 / 0x10);
      byte L2 = (byte) (AUX2 % 0x10);
      mem.SetA((byte) (H2 * 0x10 + L1));
      mem.SetMemoria(mem.GetHL(), (byte) (L2 * 0X10 + H1));

    }
    /////////////////////////////////////////////////////////////////////////

  }

  private void GrupoCarga8(int datoH) {
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0x06 || datoH == 0x0E || datoH == 0x16 || datoH == 0x1E || datoH == 0x26
        || datoH == 0x2E || datoH == 0x3E) {
      Patron5Set(datoH, mem.GetMemoriaO(pos + 1));
      //System.out.println("GRUPO CARGA");
    }
    if (datoH == 0x36) {
      mem.SetMemoria(mem.GetHL(), mem.GetMemoriaO(pos + 1));
      //System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x4 && REGF < 8) {
      mem.SetB(Patron1Get(REGF));
      //System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x4 && REGF >= 8) {
      mem.SetC(Patron1Get(REGF));
      //System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x5 && REGF < 8) {
      mem.SetD(Patron1Get(REGF));
      //System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x5 && REGF >= 8) {
      mem.SetE(Patron1Get(REGF));
      //System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x6 && REGF < 8) {
      mem.SetH(Patron1Get(REGF));
      //  System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x6 && REGF >= 8) {
      mem.SetL(Patron1Get(REGF));
      //System.out.println("GRUPO CARGA");
    }
    if (REGD == 0x7 && REGF < 8) {
      if (REGF != 6) {
        mem.SetMemoria(mem.GetHL(), Patron1Get(REGF));
        //  System.out.println("GRUPO CARGA");
      }
    }
    if (REGD == 0x7 && REGF >= 8) {
      mem.SetA(Patron1Get(REGF));
      //System.out.println("GRUPO CARGA");
    }
    if ((datoH == 0xDD || datoH == 0xFD) && mem.GetMemoria(pos + 1) != 0x21) {
      if (mem.GetMemoria(pos + 1) == 0x36) {
        mem.SetMemoria(Patron4Get((datoH) + mem.GetMemoria(pos + 2)), mem.GetMemoriaO(pos + 3));
        //System.out.println("GRUPO CARGA");
      }

      if ((mem.GetMemoria(pos + 1) != 0x76) && (mem.GetMemoria(pos + 1) / 0x10 <= 0x7
          && mem.GetMemoria(pos + 1) / 0x10 >= 0x4) && (mem.GetMemoria(pos + 1) % 0X10 == 0x6
          || mem.GetMemoria(pos + 1) % 0X10 == 0xE)) {
        Patron5Set(mem.GetMemoria(pos + 1),
            mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoria(pos + 2)));
        // System.out.println("GRUPO CARGA");
      }
      if ((mem.GetMemoria(pos + 1) != 0x76) && mem.GetMemoria(pos + 1) >= 0x70 && mem.GetMemoria(
          pos + 1) <= 0x77) {
        mem.SetMemoria(Patron4Get(datoH) + mem.GetMemoria(pos + 2),
            Patron1Get(mem.GetMemoria(pos + 1) % 0x10));
        //System.out.println("GRUPO CARGA");
      }
    }
    if (datoH == 0x0A || datoH == 0x1A) {
      mem.SetA(mem.GetMemoriaO(Patron6Get(REGD)));
      //     System.out.println("GRUPO CARGA");
    }
    if (datoH == 0x02 || datoH == 0x12) {
      mem.SetMemoria(Patron6Get(REGD), mem.GetA());
      //   System.out.println("GRUPO CARGA");
    }
    if (datoH == 0x3A) {
      // System.out.println("GRUPO CARGA");
      mem.SetA((mem.GetMemoriaO(mem.GetMemoria(pos + 2) * 0x100 + mem.GetMemoria(pos + 1))));

    }

    if (datoH == 0x32) {
      mem.SetMemoria(mem.GetMemoria(pos + 2) * 0x100 + mem.GetMemoria(pos + 1), mem.GetA());
      //System.out.println("GRUPO CARGA");
    }

    //CUAL ES LA BASCULA DE INTERRUPCION  DE OPERACIONES
    if (datoH == 0xED) {
      if (mem.GetMemoria(pos + 1) == 0x47) {
        mem.SetI(mem.GetA());
        mem.APAGANEG();
        if (Integer.signum(mem.GetI()) == -1)
          mem.ENCIENDESIGN();
        else
          mem.APAGASGN();
        if (mem.GetI() == 0)
          mem.ENCIENDEZERO();
        else
          mem.APAGAZERO();
        //   System.out.println("GRUPO CARGA");
      }

      if (mem.GetMemoria(pos + 1) == 0x57) {
        mem.SetA(mem.GetI());
        mem.APAGANEG();
        if (Integer.signum(mem.GetA()) == -1)
          mem.ENCIENDESIGN();
        else
          mem.APAGASGN();
        if (mem.GetA() == 0)
          mem.ENCIENDEZERO();
        else
          mem.APAGAZERO();
        // System.out.println("GRUPO CARGA");
      }
    }
  }

  private void GrupoArit8(int datoH) {
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (REGD == 0x8 && REGF < 8) {//ADD
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() + Patron1Get(REGF)));
      mem.APAGANEG();
      mem.ALTERAFLAGS(ant, mem.GetA());
      if (mem.GetA() > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //System.out.println("SE PARO EN ARIT8");
    }
    if (REGD == 0x8 && REGF >= 8) {//ADC
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() + Patron1Get(REGF) + mem.GetBandera(0)));
      mem.APAGANEG();
      mem.ALTERAFLAGS(ant, mem.GetA());
      if (mem.GetA() > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //System.out.println("SE PARO EN ARIT8");
    }

    if (REGD == 0x9 && REGF < 8) {
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() - Patron1Get(REGF)));
      mem.ALTERAFLAGS(ant, mem.GetA());
      if (mem.GetA() > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      mem.ENCIENDENEG();
      //System.out.println("SE PARO EN ARIT8");
    }
    if (REGD == 0x9 && REGF >= 8) {
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() - Patron1Get(REGF) - mem.GetBandera(0)));
      mem.ENCIENDENEG();
      mem.ALTERAFLAGS(ant, mem.GetA());
      if (mem.GetA() > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //     System.out.println("SE PARO EN ARIT8");
    }
    if (REGD == 0xA && REGF < 8) {//AND
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() & Patron1Get(REGF)));
      mem.ALTERAFLAGS(ant, mem.GetA());
      mem.APAGAC();
      mem.APAGANEG();
      int count = 0;
      String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
      for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
        if (cadena.charAt(i) == '1') {
          count++;
        }
      }
      if (count % 2 == 0) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //   System.out.println("SE PARO EN ARIT8");
    }
    if (REGD == 0xA && REGF >= 8) {
      //XOR
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() ^ Patron1Get(REGF)));
      mem.ALTERAFLAGS(ant, mem.GetA());
      mem.APAGAC();
      mem.APAGANEG();
      int count = 0;
      String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
      for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
        if (cadena.charAt(i) == '1') {
          count++;
        }
      }
      if (count % 2 == 0) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      // System.out.println("SE PARO EN ARIT8");
    }
    if (REGD == 0xB && REGF < 8) {
      //OR;
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() | Patron1Get(REGF)));
      mem.ALTERAFLAGS(ant, mem.GetA());
      mem.APAGAC();
      mem.APAGANEG();
      int count = 0;
      String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
      for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
        if (cadena.charAt(i) == '1') {
          count++;
        }
      }
      if (count % 2 == 0) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //    System.out.println("SE PARO EN ARIT8");
    }

    if (REGD == 0xB && REGF >= 8) {
      mem.ALTERAFLAGS(mem.GetA(), (byte) (mem.GetA() - Patron1Get(REGF)));
      mem.ENCIENDENEG();
      int count = 0;
      String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
      for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
        if (cadena.charAt(i) == '1') {
          count++;
        }
      }

      //  System.out.println(" SE PARO EN ARIT8");
    }
    if (REGD >= 0xC && REGD <= 0xF) {
      if (datoH == 0xc6) {
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() + mem.GetMemoriaO(pos + 1)));
        mem.APAGANEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //    System.out.println("SE PARO EN ARIT8");
      }

      if (datoH == 0xce) {
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() + mem.GetMemoria(pos + 1) + mem.GetBandera(0)));
        mem.APAGANEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //  System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xd6) {
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() - mem.GetMemoria(pos + 1)));
        mem.ENCIENDENEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xde) {
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() - mem.GetMemoria(pos + 1) - mem.GetBandera(0)));
        mem.ENCIENDENEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //    System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xe6) {//AND N
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() & mem.GetMemoriaO(pos + 1)));
        mem.ALTERAFLAGS(ant, mem.GetA());
        mem.APAGAC();
        mem.APAGANEG();
        int count = 0;
        String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
        for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
          if (cadena.charAt(i) == '1') {
            count++;
          }
        }
        if (count % 2 == 0) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //  System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xee) {//XOR N
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() ^ mem.GetMemoriaO(pos + 1)));
        mem.ALTERAFLAGS(ant, mem.GetA());
        mem.APAGAC();
        mem.APAGANEG();
        int count = 0;
        String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
        for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
          if (cadena.charAt(i) == '1') {
            count++;
          }
        }
        if (count % 2 == 0) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xF6) {//OR N
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() | mem.GetMemoriaO(pos + 1)));
        mem.ALTERAFLAGS(ant, mem.GetA());
        mem.APAGAC();
        mem.APAGANEG();
        int count = 0;
        String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
        for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
          if (cadena.charAt(i) == '1') {
            count++;
          }
        }
        if (count % 2 == 0) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xFE) {//CP N
        mem.ALTERAFLAGS(mem.GetA(), (byte) (mem.GetA() - mem.GetMemoriaO(pos + 1)));
        mem.ENCIENDENEG();
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //System.out.println("SE PARO EN ARIT8");
      }
    }
    if (datoH == 0xDD || datoH == 0xFD) {
      if (mem.GetMemoria(pos + 1) == 0x86) {
        byte ant = mem.GetA();
        mem.SetA(
            (byte) (mem.GetA() + mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoria(pos + 2))));
        mem.APAGANEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        // System.out.println("SE PARO EN ARIT8");
      }
      if (mem.GetMemoria(pos + 1) == 0x8e) {
        byte ant = mem.GetA();
        mem.SetA(
            (byte) (mem.GetA() + mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoria(pos + 2)) + mem
                .GetBandera(0)));
        mem.APAGANEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //    System.out.println("SE PARO EN ARIT8");
      }
      if (mem.GetMemoria(pos + 1) == 0x96) {
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() - (mem.GetMemoriaO(
            Patron4Get(datoH) + mem.GetMemoria(pos + 2)))));
        mem.ENCIENDENEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //    System.out.println("SE PARO EN ARIT8");
      }
      if (mem.GetMemoria(pos + 1) == 0x9E) {
        byte ant = mem.GetA();
        mem.SetA((byte) (mem.GetA() - (mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoria(pos + 2)))
            - mem.GetBandera(0)));
        mem.ENCIENDENEG();
        mem.ALTERAFLAGS(ant, mem.GetA());
        if (mem.GetA() > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        //  System.out.println("SE PARO EN ARIT8");
      }
      if (datoH == 0xDD || datoH == 0xFD) {

        if (mem.GetMemoria(pos + 1) == 0xa6) {//AND N
          byte aux = mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoriaO(pos + 2));
          byte ant = mem.GetA();
          mem.SetA((byte) (mem.GetA() & aux));
          mem.ALTERAFLAGS(ant, mem.GetA());
          mem.APAGAC();
          mem.APAGANEG();
          int count = 0;
          String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
          for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
            if (cadena.charAt(i) == '1') {
              count++;
            }
          }
          if (count % 2 == 0) {
            mem.ENCIENDEPARIDAD();
          } else {
            mem.APAGAPARIDAD();
          }
          //System.out.println("SE PARO EN ARIT8");

        }
        if (mem.GetMemoria(pos + 1) == 0xae) {//XOR N
          byte ant = mem.GetA();
          byte aux = mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoriaO(pos + 2));
          mem.SetA((byte) (mem.GetA() ^ aux));
          mem.ALTERAFLAGS(ant, mem.GetA());
          mem.APAGAC();
          mem.APAGANEG();
          int count = 0;
          String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
          for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
            if (cadena.charAt(i) == '1') {
              count++;
            }
          }
          if (count % 2 == 0) {
            mem.ENCIENDEPARIDAD();
          } else {
            mem.APAGAPARIDAD();
          }
          // System.out.println("SE PARO EN ARIT8");
        }
        if (mem.GetMemoria(pos + 1) == 0xb6) {//OR N
          byte aux = mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoriaO(pos + 2));
          byte ant = mem.GetA();
          mem.SetA((byte) (mem.GetA() | aux));
          mem.ALTERAFLAGS(ant, mem.GetA());
          mem.APAGAC();
          mem.APAGANEG();
          int count = 0;
          String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
          for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
            if (cadena.charAt(i) == '1') {
              count++;
            }
          }
          if (count % 2 == 0) {
            mem.ENCIENDEPARIDAD();
          } else {
            mem.APAGAPARIDAD();
          }
          //    System.out.println("SE PARO EN ARIT8");
        }
        if (mem.GetMemoria(pos + 1) == 0xbE) {//CP N
          byte aux = mem.GetMemoriaO(Patron4Get(datoH) + mem.GetMemoriaO(pos + 2));
          mem.ALTERAFLAGS(mem.GetA(), (byte) (mem.GetA() - aux));
          mem.ENCIENDENEG();
          if (mem.GetA() > 0xff) {
            mem.ENCIENDEPARIDAD();
          } else {
            mem.APAGAPARIDAD();
          }
          //  System.out.println("SE PARO EN ARIT8");
        }
      }

    }
    if (datoH == 0x34) {
      byte ant = mem.GetMemoriaO(mem.GetHL());
      mem.SetMemoria(mem.GetHL(), (byte) (mem.GetMemoriaO(mem.GetHL()) + 1));
      mem.APAGANEG();
      mem.ALTERAFLAGS(ant, mem.GetMemoriaO(mem.GetHL()));
      if (mem.GetMemoriaO(mem.GetHL()) > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //   System.out.println("SE PARO EN ARIT8");
    }
    if (datoH == 0x35) {
      byte ant = mem.GetMemoriaO(mem.GetHL());
      mem.SetMemoria(mem.GetHL(), (byte) (mem.GetMemoriaO(mem.GetHL()) - 1));
      mem.ENCIENDENEG();
      mem.ALTERAFLAGS(ant, mem.GetMemoriaO(mem.GetHL()));
      if (mem.GetMemoriaO(mem.GetHL()) > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //        System.out.println("SE PARO EN ARIT8");
    }
    if (datoH != 0x35 && datoH != 0x34 && (REGD >= 0x0 && REGD <= 0x3)) {
      if (REGF == 4 || REGF == 0xC) {
        byte ant = Patron5Get(datoH);
        Patron5Set(datoH, (byte) (Patron5Get(datoH) + 1));
        mem.APAGANEG();
        mem.ALTERAFLAGS(ant, Patron5Get(datoH));
        if (Patron5Get(datoH) > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        if (Patron5Get(datoH) == 0)
          mem.ENCIENDEZERO();
        else
          mem.APAGAZERO();
        if (Patron5Get(datoH) < 0)
          mem.ENCIENDESIGN();
        else
          mem.APAGASGN();
        //          System.out.println("SE PARO EN ARIT8");
      }
      if (REGF == 5 || REGF == 0xD) {
        byte ant = Patron5Get(datoH);
        Patron5Set(datoH, (byte) (Patron5Get(datoH) - 1));
        mem.ENCIENDENEG();
        mem.ALTERAFLAGS(ant, Patron5Get(datoH));
        if (Patron5Get(datoH) > 0xff) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        if (Patron5Get(datoH) == 0)
          mem.ENCIENDEZERO();
        else
          mem.APAGAZERO();
        if (Patron5Get(datoH) < 0)
          mem.ENCIENDESIGN();
        else
          mem.APAGASGN();
        //        System.out.println("SE PARO EN ARIT8");
      }
    }
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x34) {
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() + mem.GetMemoria(Patron4Get(datoH) + mem.GetMemoriaO(pos + 2))));
      mem.APAGANEG();
      mem.ALTERAFLAGS(ant, mem.GetA());
      if (mem.GetA() > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //  System.out.println("SE PARO EN ARIT8");
    }
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x35) {
      byte ant = mem.GetA();
      mem.SetA((byte) (mem.GetA() - mem.GetMemoria(Patron4Get(datoH) + mem.GetMemoriaO(pos + 2))));
      mem.ENCIENDENEG();
      mem.ALTERAFLAGS(ant, mem.GetA());
      if (mem.GetA() > 0xff) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //System.out.println("SE PARO EN ARIT8");
    }
  }

  private void GrupoInteryTrnas(int datoH) {
    byte aux1 = 0, aux2 = 0;
    if (datoH == 0x8) {
      aux1 = mem.GetA2();
      aux2 = mem.GetF2();
      mem.SetA2(mem.GetA());
      mem.SetF2(mem.GetF());
      mem.SetA(aux1);
      mem.SetF(aux2);
      //System.out.println("SE PARO EN INTER Y TRANS");

    }

    if (datoH == 0xD9) {
      aux1 = mem.GetB2();
      aux2 = mem.GetC2();
      mem.SetB2(mem.GetB());
      mem.SetC2(mem.GetC());
      mem.SetB(aux1);
      mem.SetC(aux2);
      aux1 = mem.GetD2();
      aux2 = mem.GetE2();
      mem.SetD2(mem.GetD());
      mem.SetE2(mem.GetE());
      mem.SetD(aux1);
      mem.SetE(aux2);
      aux1 = mem.GetH2();
      aux2 = mem.GetL2();
      mem.SetH2(mem.GetH());
      mem.SetL2(mem.GetL());
      mem.SetH(aux1);
      mem.SetL(aux2);
      // System.out.println("SE PARO EN INTER Y TRANS");
    }
    if (datoH == 0xeB) {
      aux1 = mem.GetH();
      aux2 = mem.GetL();
      mem.SetH(mem.GetD());
      mem.SetL(mem.GetE());
      mem.SetD(aux1);
      mem.SetE(aux2);
      // System.out.println("SE PARO EN INTER Y TRANS");
    }
    if (datoH == 0xe3) {
      aux1 = mem.GetH();
      aux2 = mem.GetL();
      mem.SetH((mem.GetMemoriaO(mem.GetSP() + 1)));
      mem.SetL((mem.GetMemoriaO(mem.GetSP())));
      mem.SetMemoria(mem.GetSP() + 1, aux1);
      mem.SetMemoria(mem.GetSP(), aux2);
      //            System.out.println("SE PARO EN INTER Y TRANS");
    }
    if ((datoH == 0xDD) && mem.GetMemoria(pos + 1) == 0xE3) {
      short IX = Patron4Get(datoH);
      aux1 = (byte) (IX / 0X100);//H
      aux2 = (byte) (IX % 0X100);//L
      mem.SetIX((short) ((mem.GetMemoriaO(mem.GetSP() + 1)) * 0X100 + (mem.GetMemoriaO(
          mem.GetSP()))));
      mem.SetMemoria(mem.GetSP() + 1, aux1);
      mem.SetMemoria(mem.GetSP(), aux2);
      //          System.out.println("SE PARO EN INTER Y TRANS");
    }
    if ((datoH == 0xFD) && mem.GetMemoria(pos + 1) == 0xE3) {
      short IY = Patron4Get(datoH);
      aux1 = (byte) (IY / 0X100);
      aux2 = (byte) (IY % 0X100);
      mem.SetIY((short) ((mem.GetMemoriaO(mem.GetSP() + 1)) * 0X100 + (mem.GetMemoriaO(
          mem.GetSP()))));
      mem.SetMemoria(mem.GetSP() + 1, aux1);
      mem.SetMemoria(mem.GetSP(), aux2);
      //        System.out.println("SE PARO EN INTER Y TRANS");

    }
    if (datoH == 0xED) {
      if (mem.GetMemoria(pos + 1) == 0xA0) {//LDI
        mem.ENCIENDEPARIDAD();
        mem.SetMemoria(mem.GetDE(), mem.GetMemoriaO(mem.GetHL()));
        mem.SetDE(mem.GetD(), (byte) (mem.GetE() + 1));
        mem.SetHL(mem.GetH(), (byte) (mem.GetL() + 1));
        mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
        if (mem.GetBC() <= 0) {
          mem.APAGAPARIDAD();
        } else {
          mem.ENCIENDEPARIDAD();
        }
        mem.APAGAH();
        mem.APAGANEG();
        //          System.out.println("SE PARO EN INTER Y TRANS");
      }
      if (mem.GetMemoria(pos + 1) == 0xA1) {//CPI
        byte aux = mem.GetA();
        mem.ENCIENDEPARIDAD();
        mem.SetA(mem.GetMemoriaO(mem.GetHL()));
        mem.SetHL(mem.GetH(), (byte) (mem.GetL() + 1));
        mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
        if (mem.GetBC() <= 0) {
          mem.APAGAPARIDAD();
        } else {
          mem.ENCIENDEPARIDAD();
        }
        mem.ALTERAFLAGS(aux, (byte) (mem.GetA() - mem.GetMemoria(mem.GetHL())));

        mem.ENCIENDENEG();
        //        System.out.println("SE PARO EN INTER Y TRANS");
      }
      if (mem.GetMemoria(pos + 1) == 0xB0) {//LDIR
        mem.APAGAH();
        mem.APAGANEG();
        mem.APAGAPARIDAD();
        while (mem.GetBC() > 0) {
          mem.SetMemoria(mem.GetDE(), mem.GetMemoriaO(mem.GetHL()));
          mem.SetDE(mem.GetD(), (byte) (mem.GetE() + 1));
          mem.SetHL(mem.GetH(), (byte) (mem.GetL() + 1));
          mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
        }
        //      System.out.println("SE PARO EN INTER Y TRANS");
      }
      if (mem.GetMemoria(pos + 1) == 0xB1) {//CPIR
        byte aux = mem.GetA();
        mem.ENCIENDEPARIDAD();
        while (mem.GetBC() > 0 || (mem.GetA() == mem.GetMemoria(mem.GetHL()))) {
          mem.SetA(mem.GetMemoriaO(mem.GetHL()));
          mem.SetHL(mem.GetH(), (byte) (mem.GetL() + 1));
          mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
          if (mem.GetBC() <= 0) {
            mem.APAGAPARIDAD();
          } else {
            mem.ENCIENDEPARIDAD();
          }
          mem.ALTERAFLAGS(aux, (byte) (mem.GetA() - mem.GetMemoriaO(mem.GetHL())));
          mem.ENCIENDENEG();

        }
        //    System.out.println("SE PARO EN INTER Y TRANS");
      }
      if (mem.GetMemoria(pos + 1) == 0xA8) {//LDD
        mem.APAGAH();
        mem.APAGANEG();
        mem.SetMemoria(mem.GetDE(), mem.GetMemoriaO(mem.GetHL()));
        mem.SetDE(mem.GetD(), (byte) (mem.GetE() - 1));
        mem.SetHL(mem.GetH(), (byte) (mem.GetL() - 1));
        mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
        if (mem.GetBC() <= 0) {
          mem.APAGAPARIDAD();
        } else {
          mem.ENCIENDEPARIDAD();
        }
        if (mem.GetBC() <= 0) {
          mem.APAGAPARIDAD();
        }
        //  System.out.println("SE PARO EN INTER Y TRANS");
      }
      if (mem.GetMemoria(pos + 1) == 0xA9)//CPD
      {
        byte aux = mem.GetA();
        mem.ENCIENDEPARIDAD();
        mem.SetA(mem.GetMemoriaO(mem.GetHL()));
        mem.SetHL(mem.GetH(), (byte) (mem.GetL() - 1));
        mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
        if (mem.GetBC() <= 0) {
          mem.APAGAPARIDAD();
        } else {
          mem.ENCIENDEPARIDAD();
        }
        mem.ALTERAFLAGS(aux, (byte) (mem.GetA() - mem.GetMemoriaO(mem.GetHL())));
        mem.ENCIENDENEG();

        //            System.out.println("SE PARO EN INTER Y TRANS");
      }

      if (mem.GetMemoria(pos + 1) == 0xB8) {//LDDR
        mem.APAGAH();
        mem.APAGANEG();
        mem.APAGAPARIDAD();
        while (mem.GetBC() > 0) {
          mem.SetMemoria(mem.GetDE(), mem.GetMemoriaO(mem.GetHL()));
          mem.SetDE(mem.GetD(), (byte) (mem.GetE() + 1));
          mem.SetHL(mem.GetH(), (byte) (mem.GetL() + 1));
          mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
        }
        //              System.out.println("SE PARO EN INTER Y TRANS");
      }
      if (mem.GetMemoria(pos + 1) == 0xB9) {//CPDR
        byte aux = mem.GetA();
        while (mem.GetBC() > 0 || (mem.GetA() == mem.GetMemoria(mem.GetHL()))) {
          mem.SetA(mem.GetMemoriaO(mem.GetHL()));
          mem.SetHL(mem.GetH(), (byte) (mem.GetL() - 1));
          mem.SetBC(mem.GetB(), (byte) (mem.GetC() - 1));
          if (mem.GetBC() <= 0) {
            mem.APAGAPARIDAD();
          } else {
            mem.ENCIENDEPARIDAD();
          }
          mem.ALTERAFLAGS(aux, (byte) (mem.GetA() - mem.GetMemoria(mem.GetHL())));
          mem.ENCIENDENEG();
        }
        //        System.out.println("SE PARO EN INTER Y TRANS");
      }
    }
  }

  private void GrupoA16(int datoH) {
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0x09 || datoH == 0x19 || datoH == 0x29 || datoH == 0x39) {
      byte aux1 = mem.GetL();
      short aux = (short) (mem.GetHL() + Patron6Get(REGD));
      byte High = (byte) (aux / 0x10);
      byte Low = (byte) (aux % 0x10);
      mem.SetHL(High, Low);
      if (aux1 < Low && Low > 0xff) {
        mem.ENCIENDEC();
      } else {
        mem.APAGAC();
      }
      mem.APAGANEG();
      if (aux > 0x10000) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //      System.out.println("SE PARO EN GA16");
    }
    if (datoH == 0XED && (mem.GetMemoria(pos + 1) == 0x4A || mem.GetMemoria(pos + 1) == 0x5A
        || mem.GetMemoria(pos + 1) == 0x6A || mem.GetMemoria(pos + 1) == 0x7A)) {
      short aux1 = mem.GetHL();
      short aux = (short) (mem.GetHL() + Patron6Get(REGD) + mem.GetBandera(0));
      byte High = (byte) (aux / 0x10);
      byte Low = (byte) (aux % 0x10);
      mem.SetHL(High, Low);
      mem.APAGANEG();
      if (Integer.signum(aux) == -1) {
        mem.ENCIENDESIGN();
      } else {
        mem.APAGASGN();
      }
      if (Integer.signum(aux) == 0) {
        mem.ENCIENDEZERO();
      } else {
        mem.APAGAZERO();
      }
      if (aux1 < Low && Low > 0xff) {
        mem.ENCIENDEC();
      }
      //    System.out.println("SE PARO EN GA16");
    }
    if (datoH == 0XED && (mem.GetMemoria(pos + 1) == 0x42 || mem.GetMemoria(pos + 1) == 0x52
        || mem.GetMemoria(pos + 1) == 0x62 || mem.GetMemoria(pos + 1) == 0x72)) {
      short aux1 = mem.GetHL();
      short aux = (short) (mem.GetHL() - Patron6Get(REGD) - mem.GetBandera(0));
      byte High = (byte) (aux / 0x10);
      byte Low = (byte) (aux % 0x10);
      mem.SetHL(High, Low);
      mem.ENCIENDENEG();
      if (aux1 < Low && Low > 0xff) {
        mem.ENCIENDEC();
      } else {
        mem.APAGAC();
      }
      if (Integer.signum(aux) == 0) {
        mem.ENCIENDEZERO();
      } else {
        mem.APAGAZERO();
      }
      if (Integer.signum(aux) == -1) {
        mem.ENCIENDESIGN();
      } else {
        mem.APAGASGN();
      }
      if (aux > 0x10000) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
      //  System.out.println("SE PARO EN GA16");
    }
    if (datoH == 0XdD && (mem.GetMemoria(pos + 1) == 0x09 || mem.GetMemoria(pos + 1) == 0x19
        || mem.GetMemoria(pos + 1) == 0x29 || mem.GetMemoria(pos + 1) == 0x39)) {
      short aux1 = mem.GetIX();
      short aux = (short) (aux1 + Patron8Get(mem.GetMemoria(pos + 1) / 0x10));
      byte Low = (byte) (aux % 0x10);
      mem.SetIX(aux);
      mem.APAGANEG();
      if (aux1 < Low && Low > 0xff) {
        mem.ENCIENDEC();
      } else {
        mem.APAGAC();
      }
    }
    if (datoH == 0xfd && (mem.GetMemoria(pos + 1) == 0x09 || mem.GetMemoria(pos + 1) == 0x19
        || mem.GetMemoria(pos + 1) == 0x29 || mem.GetMemoria(pos + 1) == 0x39)) {
      short aux1 = mem.GetIY();
      short aux = (short) (aux1 + Patron9Get(mem.GetMemoria(pos + 1) / 0x10));
      byte Low = (byte) (aux % 0x10);
      mem.SetIY(aux);
      mem.APAGANEG();
      if (aux1 < Low && Low > 0xff) {
        mem.ENCIENDEC();
      } else {
        mem.APAGAC();
      }
    }
    if (REGD >= 0x0 && REGD <= 0x3) {
      if (REGF == 0x3) {
        short aux1 = Patron6Get(REGD);
        Patron6Set(REGD, (byte) (aux1 / 0x100), (byte) (aux1 % 0x100 + 1));

      }
      if (REGF == 0xB) {
        short aux1 = Patron6Get(REGD);
        Patron6Set(REGD, (byte) (aux1 / 0x100), (byte) (aux1 % 0x100 - 1));

      }
    }
    if ((datoH == 0xdd || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x23) {
      short aux1 = (short) (Patron4Get(datoH) + 1);
      Patron4Set(datoH, aux1);
      //            System.out.println("SE PARO EN GA16");

    }
    if ((datoH == 0xDD || datoH == 0xfD) && mem.GetMemoria(pos + 1) == 0x2B) {
      short aux1 = (short) (Patron4Get(datoH) - 1);
      Patron4Set(datoH, aux1);
      //          System.out.println("SE PARO EN GA16");

    }
  }

  private void ControlG(int datoH) {
    if (datoH == 0x27) {
      if (mem.GetBandera(1) == 1) {
        byte aux = mem.GetA();
        int count = 0;
        if (mem.GetA() > 0x9)
          mem.SetA((byte) (mem.GetA() + 0x6));
        String cadena = "00000000000000000000000000000000" + Integer.toBinaryString(mem.GetA());
        for (int i = cadena.length() - 1; i > cadena.length() - 9; i--) {
          if (cadena.charAt(i) == '1') {
            count++;
          }
        }
        if (count % 2 == 0) {
          mem.ENCIENDEPARIDAD();
        } else {
          mem.APAGAPARIDAD();
        }
        mem.ALTERAFLAGS(aux, mem.GetA());
      }
    }

    if (datoH == 0x2f) {
      mem.SetA((byte) (~mem.GetA()));
      mem.ENCIENDENEG();
      mem.ENCIENDEH();
    }
    if (datoH == 0x3f) {
      if (mem.GetBandera(0) == 1) {
        mem.APAGAC();
      } else {
        mem.ENCIENDEC();
      }
      mem.APAGANEG();
    }
    if (datoH == 0x37) {
      mem.ENCIENDEC();
      mem.APAGANEG();
      mem.APAGAH();

    }
        /*
        ///////////// HABILITACION DE INTERRUPCIONES/////////
        if(datoH==0xF3)
        return "DI";
        if(datoH==0xFB)
        return "EI";
         */
    if (datoH == 0xED && mem.GetMemoria(pos + 1) == 0x44) {
      byte aux = mem.GetA();
      mem.SetA((byte) (0 - mem.GetA()));
      mem.ALTERAFLAGS(aux, mem.GetA());
      mem.ENCIENDENEG();
      if (mem.GetA() >= 0x100) {
        mem.ENCIENDEPARIDAD();
      } else {
        mem.APAGAPARIDAD();
      }
    }
        /*
         INTERRUPCIONES
        if(datoH==0xED&&mem.GetMemoria(pos+1)==0x46)
        return "IM 0";
        if(datoH==0xED&&mem.GetMemoria(pos+1)==0x56)
        return "IM 1";
        if(datoH==0xED&&mem.GetMemoria(pos+1)==0x5E)
        return "IM 2";
         */
  }

  private void JUMP(int datoH) {
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0xc3) {
      mem.SetPC((short) (mem.GetMemoria(pos + 2) * 0x100 + mem.GetMemoria(pos + 1)));

    }
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x2 || REGF == 0xA)) {
      if (Patron10(datoH)) {
        mem.SetPC((short) (mem.GetMemoria(pos + 2) * 0x100 + mem.GetMemoria(pos + 1)));
      }
    }
    if (datoH == 0x18) {
      mem.SetPC((short) (mem.GetPC() + mem.GetMemoriaO(pos + 1) + 2));
    }
    if (datoH != 0x18 && (REGD >= 0x2 && REGD <= 0x3) && (REGF == 0x0 || REGF == 0x8)) {
      if (Patron10(datoH)) {
        mem.SetPC((short) (mem.GetPC() + mem.GetMemoriaO(pos + 1) + 2));
      }
    }
    /////////// QUE ONDA CHECAR INSTRUCCIONES
    if ((datoH == 0XFD || datoH == 0XDD) && mem.GetMemoria(pos + 1) == 0XE9) {
      mem.SetPC(Patron4Get(datoH));
    }
    if (datoH == 0xE9) {
      mem.SetPC(mem.GetHL());
    }
    ///////////////////////////

    if (datoH == 0x10) {
      mem.SetB((byte) (mem.GetB() - 1));
      while (mem.GetB() > 0) {
        mem.SetPC((short) (mem.GetPC() + mem.GetMemoriaO(pos + 1)));
      }
    }

    //////////////////////////
  }

  private void CallRet(int datoH) {
    //System.out.println("SE PARO EN CALL");
    int REGD = datoH / 0x10;
    int REGF = datoH % 0x10;
    if (datoH == 0xcd) {
      byte H = (byte) (mem.GetPC() / 0X100);
      byte L = (byte) (mem.GetPC() % 0X100);
      mem.SetMemoria(mem.GetSP() - 1, H);
      mem.SetMemoria(mem.GetSP() - 2, L);
      mem.SetSP((short) (mem.GetSP() - 2));
      mem.SetPC((short) (mem.GetMemoriaO(pos + 2) * 0X100 + mem.GetMemoriaO(pos + 1)));

    }
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x4 || REGF == 0xC)) {
      if (Patron10(datoH)) {
        mem.SetPC((short) (mem.GetMemoriaO(pos + 2) * 0X100 + mem.GetMemoriaO(pos + 1)));
      }

    }
    if (datoH == 0xc9) {
      byte L = mem.GetMemoriaO(mem.GetSP());
      byte H = mem.GetMemoriaO(mem.GetSP() + 1);
      mem.SetSP((short) (mem.GetSP() + 2));
      mem.SetPC((short) (H * 0x100 + L));

    }
    if ((datoH != 0xc9) && (REGD >= 0xC && REGD <= 0xF) && (REGF == 0x0 || REGF == 0x8)) {
      if (Patron10(datoH)) {
        byte L = mem.GetMemoriaO(mem.GetSP());
        byte H = mem.GetMemoriaO(mem.GetSP() + 1);
        mem.SetSP((short) (mem.GetSP() + 2));
        mem.SetPC((short) (H * 0x100 + L));
      }
    }
        /*INTERRUPCIONES
        if(datoH==0xED&&(mem.GetMemoria(pos+1))==0x4D)
        return "RETI";
        if(datoH==0xED&&(mem.GetMemoria(pos+1))==0x45)
        return "RETN";

         */
    if ((REGD >= 0xC && REGD <= 0xF) && (REGF == 0x7 || REGF == 0xF)) {
      byte H = (byte) (mem.GetPC() / 0X100);
      byte L = (byte) (mem.GetPC() % 0X100);
      mem.SetMemoria(mem.GetSP() - 1, H);
      mem.SetMemoria(mem.GetSP() - 2, L);
      mem.SetSP((short) (mem.GetSP() - 2));
      mem.SetPC(Patron11(REGD, REGF));

    }
  }

  private short Patron11(int dato, int dato2) {
    String aux = null;
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 <= 8) {
      return 0;
    }
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 > 8) {
      return 8;
    }
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 <= 8) {
      return 10;
    }
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 > 8) {
      return 18;
    }
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 <= 8) {
      return 20;
    }
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 > 8) {
      return 28;
    }
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 <= 8) {
      return 30;
    }
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 > 8) {
      return 38;
    }
    return 0;

  }

  private boolean Patron10(int dato) {
    int REGD = dato / 0x10;
    int REGF = dato % 0x10;
    if (REGF == 0x2 || REGF == 0x0 || REGF == 0x4) {
      if (REGD == 0xC || REGD == 0x2) {
        return mem.GetBandera(6) == 0;
      }
      if (REGD == 0xD || REGD == 0x3) {
        return mem.GetBandera(0) == 0;
      }
      if (REGD == 0xE) {
        return mem.GetBandera(2) == 0;
      }
      if (REGD == 0xF) {
        return mem.GetBandera(7) == 0;
      }
    }
    if (REGF == 0xA || REGF == 0x8 || REGF == 0xC) {
      if (REGD == 0xC || REGD == 0x2) {
        return mem.GetBandera(6) == 1;
      }
      if (REGD == 0xD || REGD == 0x3) {
        return mem.GetBandera(0) == 1;
      }
      if (REGD == 0xE) {
        return mem.GetBandera(2) == 1;
      }
      if (REGD == 0xF) {
        return mem.GetBandera(7) == 1;
      }
    }
    return true;
  }

  private short Patron9Get(int dato) {
    if (dato == 0x0 || dato == 0xC) {
      return mem.GetBC();
    }
    if (dato == 0x1 || dato == 0xD) {
      System.out.println(mem.GetDE());
      return mem.GetDE();
    }
    if (dato == 0x2 || dato == 0xE) {
      return mem.GetIY();
    }
    if (dato == 0x3 || dato == 0xF) {
      return mem.GetSP();
    }
    return -1;
  }

  private short Patron8Get(int dato) {
    if (dato == 0x0 || dato == 0xC) {
      return mem.GetBC();
    }
    if (dato == 0x1 || dato == 0xD) {
      return mem.GetDE();
    }
    if (dato == 0x2 || dato == 0xE) {
      return mem.GetIX();
    }
    if (dato == 0x3 || dato == 0xF) {
      return mem.GetSP();
    }
    return -1;
  }

  private void Patron6Set(int dato, byte n1, byte n2) {
    if (dato == 0x0 || dato == 0xC || dato == 0x4) {
      mem.SetBC(n1, n2);
    } else if (dato == 0x1 || dato == 0xD || dato == 0x5) {
      mem.SetDE(n1, n2);
    } else if (dato == 0x2 || dato == 0xE || dato == 0x6) {
      mem.SetHL(n1, n2);
    } else if (dato == 0x3 || dato == 0xF || dato == 0x7) {
      mem.SetSP((short) (n1 * 0x100 + n2));
    }
  }

  private short Patron6Get(int dato) {
    if (dato == 0x0 || dato == 0xC || dato == 0x4) {
      return mem.GetBC();
    } else if (dato == 0x1 || dato == 0xD || dato == 0x5) {
      return mem.GetDE();
    } else if (dato == 0x2 || dato == 0xE || dato == 0x6) {
      return mem.GetHL();
    } else if (dato == 0x3 || dato == 0xF || dato == 0x7) {
      return mem.GetSP();
    }
    return -1;
  }

  private byte Patron5Get(int dato) {
    int REGD = dato / 0x10;
    int REGF = dato % 0x10;
    if (REGF == 0x0E || REGF == 0x08 || REGF == 0X0D || REGF == 0X0c) {
      if ((REGD == 0x0 || REGD == 0x4)) {
        return mem.GetC();
      }
      if ((REGD == 0x1 || REGD == 0x5)) {
        return mem.GetE();
      }
      if ((REGD == 0x2 || REGD == 0x6)) {
        return mem.GetL();
      }
      if ((REGD == 0x3 || REGD == 0x7)) {
        return mem.GetA();
      }
    }
    if (REGF == 0x06 || REGF == 0x00 || REGF == 0X05 || REGF == 0x04) {
      if ((REGD == 0x0 || REGD == 0x4)) {
        return mem.GetB();
      }
      if ((REGD == 0x1 || REGD == 0x5)) {
        return mem.GetD();
      }
      if ((REGD == 0x2 || REGD == 0x6)) {
        return mem.GetH();
      }
    }
    return -1;
  }

  private void Patron5Set(int dato, byte datoAPoner) {
    int REGD = dato / 0x10;
    int REGF = dato % 0x10;
    if (REGF == 0x0E || REGF == 0x08 || REGF == 0X0D || REGF == 0X0c) {
      if ((REGD == 0x0 || REGD == 0x4)) {
        mem.SetC(datoAPoner);
      }
      if ((REGD == 0x1 || REGD == 0x5)) {
        mem.SetE(datoAPoner);
      }
      if ((REGD == 0x2 || REGD == 0x6)) {
        mem.SetL(datoAPoner);
      }
      if ((REGD == 0x3 || REGD == 0x7)) {
        mem.SetA(datoAPoner);
      }
    }
    if (REGF == 0x06 || REGF == 0x00 || REGF == 0X05 || REGF == 0x04) {
      if ((REGD == 0x0 || REGD == 0x4)) {
        mem.SetB(datoAPoner);
      }
      if ((REGD == 0x1 || REGD == 0x5)) {
        mem.SetD(datoAPoner);
      }
      if ((REGD == 0x2 || REGD == 0x6)) {
        mem.SetH(datoAPoner);
      }
    }
  }

  private void Patron4Set(int dato, int datoAponer) {
    if (dato == 0xDD) {
      mem.SetIX((short) datoAponer);
    } else {
      mem.SetIY((short) datoAponer);
    }
  }

  private short Patron4Get(int dato) {
    if (dato == 0xDD) {
      return mem.GetIX();
    }
    return mem.GetIY();
  }

  private int Patron3(int dato) {
    if (dato >= 8) {
      return 1;
    }
    return 0;
  }

  private int Patron2(int dato, int dato2) {
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 <= 8) {
      return 0;
    }
    if ((dato == 0x4 || dato == 0x8 || dato == 0xC) && dato2 > 8) {
      return 1;
    }
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 <= 8) {
      return 2;
    }
    if ((dato == 0x5 || dato == 0x9 || dato == 0xD) && dato2 > 8) {
      return 3;
    }
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 <= 8) {
      return 4;
    }
    if ((dato == 0x6 || dato == 0xA || dato == 0xE) && dato2 > 8) {
      return 5;
    }
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 <= 8) {
      return 6;
    }
    if ((dato == 0x7 || dato == 0xB || dato == 0xF) && dato2 > 8) {
      return 7;
    }
    return 0;
  }

  private byte Patron1Get(int dato) {
    String aux = null;
    if (dato == 0x0 || dato == 0x8) {
      return mem.GetB();
    }
    if (dato == 0x1 || dato == 0x9) {
      return mem.GetC();
    }
    if (dato == 0x2 || dato == 0xA) {
      return mem.GetD();
    }
    if (dato == 0x3 || dato == 0xB) {
      return mem.GetE();
    }
    if (dato == 0x4 || dato == 0xC) {
      return mem.GetH();
    }
    if (dato == 0x5 || dato == 0xD) {
      return mem.GetL();
    }
    if (dato == 0x6 || dato == 0xe) {
      return mem.GetMemoriaO(mem.GetHL());
    }
    if (dato == 0x7 || dato == 0xF) {
      return mem.GetA();
    }
    return -1;
  }

  private void Patron1Set(int dato, byte datoAponer) {
    String aux = null;
    if (dato == 0x0 || dato == 0x8) {
      mem.SetB(datoAponer);
    }
    if (dato == 0x1 || dato == 0x9) {
      mem.SetC(datoAponer);
    }
    if (dato == 0x2 || dato == 0xA) {
      mem.SetD(datoAponer);
    }
    if (dato == 0x3 || dato == 0xB) {
      mem.SetE(datoAponer);
    }
    if (dato == 0x4 || dato == 0xC) {
      mem.SetH(datoAponer);
    }
    if (dato == 0x5 || dato == 0xD) {
      mem.SetL(datoAponer);
    }
    if (dato == 0x6 || dato == 0xe) {
      mem.SetMemoria(mem.GetHL(), datoAponer);
    }
    if (dato == 0x7 || dato == 0xF) {
      mem.SetA(datoAponer);
    }
  }
}
