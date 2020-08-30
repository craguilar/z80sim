package simz80.ui;

import javax.swing.*;

import simz80.processing.GeneraMnemonicos;
import simz80.processing.HexaMnem;
import simz80.processing.Memoria;

public class GraficoMnemonicos extends JTextArea {
  static HexaMnem HM;
  GeneraMnemonicos GM;
  int contadorSig = 0;

  public GraficoMnemonicos(GeneraMnemonicos MM, Memoria M) {
    GM = MM;
    HM = new HexaMnem(M);
    contadorSig = 0;

    setEditable(false);
    setText(GeneraMnemonicos.Lista(0, HM, 0));
  }

  public static void REFRESH(Memoria M) {
    HM = new HexaMnem(M);
  }

  public void REPINTAADELANTE() {
    contadorSig++;
    //GeneraMnemonicos.posant=GeneraMnemonicos.Actualizaposant(HM.getTodaLaMemoria(), HM, contadorSig-1);
    GeneraMnemonicos.pos1 = GeneraMnemonicos.Actualizaposant(HM.getTodaLaMemoria(), HM,
        contadorSig);
    setText(GeneraMnemonicos.Lista(GeneraMnemonicos.pos1, HM, contadorSig));
  }

  public void REPINTAATRAS() {
    contadorSig--;
    if (contadorSig < 0)
      contadorSig = 0;
    GeneraMnemonicos.posant = GeneraMnemonicos.Actualizaposant(HM.getTodaLaMemoria(), HM,
        contadorSig);
    //GeneraMnemonicos.pos1=GeneraMnemonicos.Actualizaposant(HM.getTodaLaMemoria(), HM, contadorSig+1);

    if (GeneraMnemonicos.posant >= 0) {
      setText(GeneraMnemonicos.Lista(GeneraMnemonicos.posant, HM, contadorSig));
    }
  }

  public void REPINTACONDICIONAL(int pos) {
    GeneraMnemonicos.posant = 0;
    contadorSig = 0;
    while (GeneraMnemonicos.pos1 < pos)
      REPINTAADELANTE();

    setText(GeneraMnemonicos.Lista(pos, HM, contadorSig));
    //  GeneraMnemonicos.pos1=pos;

  }

}
