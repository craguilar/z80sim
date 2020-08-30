package simz80.ui;

import java.awt.*;

import javax.swing.*;

import simz80.processing.Memoria;
import simz80.processing.Simular;

public class GraficaRegistros extends JPanel {
  Memoria mem;
  Simular SIM;
  JLabel TITULO = new JLabel(":::::::::::::::::REGISTROS DEL Z-80::::::::::::::::::");
  JLabel PIE = new JLabel(
      "::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
  JLabel[] ETIQUETA = new JLabel[22];
  JPanel[] contenedor = new JPanel[8];
  JPanel Big = new JPanel(new GridLayout(10, 1));

  public GraficaRegistros(Memoria M) {
    ETIQUETA[0] = new JLabel("");
    ETIQUETA[1] = new JLabel("");
    ETIQUETA[2] = new JLabel("");
    ETIQUETA[3] = new JLabel("");
    ETIQUETA[4] = new JLabel("");
    ETIQUETA[5] = new JLabel("");
    ETIQUETA[6] = new JLabel("");
    ETIQUETA[7] = new JLabel("");
    ETIQUETA[8] = new JLabel("");
    ETIQUETA[9] = new JLabel("");
    ETIQUETA[10] = new JLabel("");
    ETIQUETA[11] = new JLabel("");
    ETIQUETA[12] = new JLabel("");
    ETIQUETA[13] = new JLabel("");
    ETIQUETA[14] = new JLabel("");
    ETIQUETA[15] = new JLabel("");
    ETIQUETA[16] = new JLabel("");
    ETIQUETA[17] = new JLabel("");
    ETIQUETA[18] = new JLabel("");
    ETIQUETA[19] = new JLabel("");
    ETIQUETA[20] = new JLabel("");
    ETIQUETA[21] = new JLabel("");
    for (int i = 0; i < 8; i++)
      contenedor[i] = new JPanel();

    contenedor[0].add(ETIQUETA[0]);
    contenedor[0].add(ETIQUETA[1]);
    contenedor[1].add(ETIQUETA[2]);
    contenedor[1].add(ETIQUETA[4]);
    contenedor[1].add(ETIQUETA[3]);
    contenedor[1].add(ETIQUETA[5]);
    contenedor[2].add(ETIQUETA[6]);
    contenedor[2].add(ETIQUETA[8]);
    contenedor[2].add(ETIQUETA[7]);
    contenedor[2].add(ETIQUETA[9]);
    contenedor[3].add(ETIQUETA[10]);
    contenedor[3].add(ETIQUETA[12]);
    contenedor[3].add(ETIQUETA[11]);
    contenedor[3].add(ETIQUETA[13]);
    contenedor[4].add(ETIQUETA[15]);
    contenedor[5].add(ETIQUETA[14]);
    contenedor[5].add(ETIQUETA[16]);
    contenedor[6].add(ETIQUETA[17]);
    contenedor[6].add(ETIQUETA[18]);
    contenedor[7].add(ETIQUETA[19]);
    contenedor[7].add(ETIQUETA[20]);
    contenedor[7].add(ETIQUETA[21]);

    SIM = new Simular(M);
    mem = M;
    construlle(mem);
  }

  final public void construlle(Memoria M) {
    PINTA(M);
    Big.add(TITULO);
    for (int i = 0; i < 8; i++)
      Big.add(contenedor[i]);
    Big.add(PIE);
    add(Big);
    try {

      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    setVisible(true);
  }

  public void PINTA(Memoria M) {
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

    String hA = new String();
    hA = Integer.toHexString((int) A).toUpperCase();
    String hB = new String();
    hB = Integer.toHexString((int) B).toUpperCase();
    String hC = new String();
    hC = Integer.toHexString((int) C).toUpperCase();
    String hD = new String();
    hD = Integer.toHexString((int) D).toUpperCase();
    String hE = new String();
    hE = Integer.toHexString((int) E).toUpperCase();
    String hH = new String();
    hH = Integer.toHexString((int) H).toUpperCase();
    String hL = new String();
    hL = Integer.toHexString((int) L).toUpperCase();
    String hA2 = new String();
    hA2 = Integer.toHexString((int) A2).toUpperCase();
    String hB2 = new String();
    hB2 = Integer.toHexString((int) B2).toUpperCase();
    String hC2 = new String();
    hC2 = Integer.toHexString((int) C2).toUpperCase();
    String hD2 = new String();
    hD2 = Integer.toHexString((int) D2).toUpperCase();
    String hE2 = new String();
    hE2 = Integer.toHexString((int) E2).toUpperCase();
    String hH2 = new String();
    hH2 = Integer.toHexString((int) H2).toUpperCase();
    String hL2 = new String();
    hL2 = Integer.toHexString((int) L2).toUpperCase();
    String hI = new String();
    hI = Integer.toHexString((int) I).toUpperCase();
    String hR = new String();
    hR = Integer.toHexString((int) R).toUpperCase();
    String hIX = new String();
    hIX = Integer.toHexString((int) IX).toUpperCase();
    String hIY = new String();
    hIY = Integer.toHexString((int) IY).toUpperCase();
    String hPC = new String();
    hPC = Integer.toHexString((int) PC).toUpperCase();
    String hSP = new String();
    hSP = Integer.toHexString((int) SP).toUpperCase();
    ETIQUETA[0].setText("A =" + hA);
    ETIQUETA[1].setText(hA2 + "=A'");
    ETIQUETA[2].setText("B =" + hB);
    ETIQUETA[3].setText("B' =" + hB2);
    ETIQUETA[4].setText(hC + "= C");
    ETIQUETA[5].setText(hC2 = "=C'");
    ETIQUETA[6].setText("D =" + hD);
    ETIQUETA[7].setText("D' =" + hD2);
    ETIQUETA[8].setText(hE + "= E ");
    ETIQUETA[9].setText(hE2 = "= E'");
    ETIQUETA[10].setText("H =" + hH);
    ETIQUETA[11].setText("H' =" + hH2);
    ETIQUETA[12].setText(hL + "= L ");
    ETIQUETA[13].setText(hL2 + "= L'");
    ETIQUETA[14].setText(
        "    F=" + mem.GetBandera(7) + mem.GetBandera(6) + mem.GetBandera(5) + mem.GetBandera(4)
            + mem.GetBandera(3) + mem.GetBandera(2) + mem.GetBandera(1) + mem.GetBandera(0));
    ETIQUETA[15].setText("SZ*H*PNC");
    ETIQUETA[16].setText("IX =" + hIX);
    ETIQUETA[17].setText("IY =" + hIY);
    ETIQUETA[18].setText("I =" + hI);
    ETIQUETA[19].setText("PC =" + hPC);
    ETIQUETA[20].setText("SP =" + hSP);
    ETIQUETA[21].setText("R =" + hR);

  }

}