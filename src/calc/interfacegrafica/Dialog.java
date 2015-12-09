package calc.interfacegrafica;

import javax.swing.JOptionPane;

public class Dialog {

	public static void erro(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "CALC", JOptionPane.ERROR_MESSAGE);
	}

	public static void alerta(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "CALC", JOptionPane.WARNING_MESSAGE);
	}

}