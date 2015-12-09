package calc.datastructures.core;

import java.util.List;

public class Documento {

	private String nomeDoDocumento;
	private List<String> listaAreaDeConhecimento;
	private StringBuilder valoresDosAtributos;

	public Documento(String nomeDoDocumento) {
		this.nomeDoDocumento = nomeDoDocumento;
	}

	public String getNomeDoDocumento() {
		return nomeDoDocumento;
	}

	public void setNomeDoDocumento(String nomeDoDocumento) {
		this.nomeDoDocumento = nomeDoDocumento;
	}

	public StringBuilder getValoresDosAtributos() {
		return valoresDosAtributos;
	}

	public void setValoresDosAtributos(StringBuilder valoresDosAtributos) {
		this.valoresDosAtributos = valoresDosAtributos;
	}

	public List<String> getListaAreaDeConhecimento() {
		return listaAreaDeConhecimento;
	}

	public void setListaAreaDeConhecimento(List<String> listaAreaDeConhecimento) {
		this.listaAreaDeConhecimento = listaAreaDeConhecimento;
	}
}
