package calc.datastructures.core;

import java.text.Normalizer;

public class NormalizadorString {
	private String texto;

	public String normalize(String texto) {
		// letra minuscula
		this.texto = texto.toLowerCase();

		removeAcentos();
		removeCaracterEspecial();
		removeCaracterRepetido();
		return this.texto;
	}

	private void removeCaracterRepetido() {
		texto = texto.replaceAll("\\s(\\w)\\1+\\s", " ");
	}

	private void removeCaracterEspecial() {
		texto = texto.replaceAll("[().;:]", "");
		texto = texto.replaceAll("[/:,'\"]", "");
	}

	private void removeAcentos() {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");
	}
}
