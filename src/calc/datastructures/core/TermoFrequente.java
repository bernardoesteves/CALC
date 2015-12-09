package calc.datastructures.core;

public class TermoFrequente {
	private String termo;
	private Integer frequencia;

	public TermoFrequente(String termo, Integer frequencia) {
		this.setTermo(termo);
		this.setFrequencia(frequencia);
	}

	public String getTermo() {
		return termo;
	}

	public void setTermo(String termo) {
		this.termo = termo;
	}

	public Integer getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Integer frequencia) {
		this.frequencia = frequencia;
	}

}
