package calc.interfacegrafica;

import java.awt.Component;

import javax.swing.JLabel;

public class Informativo {
	private static JLabel informativo = new JLabel();

	public static Component getJLabel() {

		return informativo;
	}

	public static void geraInfo(String informacao) {
		informativo.setText(informacao);
	}

}
