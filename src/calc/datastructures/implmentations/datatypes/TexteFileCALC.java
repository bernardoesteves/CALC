package calc.datastructures.implmentations.datatypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import datastructures.implementations.datatypes.TextFile;

public class TexteFileCALC extends TextFile {
	private Set<String> termosAnalisados = new HashSet<String>();
	private List<String> listaAreaConhecimento = new ArrayList<String>();

	public TexteFileCALC(String title, String content, int index) {
		super(title, content, index);
	}

	public Set<String> getTermosAnalisados() {
		return termosAnalisados;
	}

	public void setTermosAnalisados(Set<String> termosAnalisados) {
		this.termosAnalisados = termosAnalisados;
	}

	public void adicionaTermoAnalisado(String term) {
		termosAnalisados.add(term);

	}

	public List<String> getListaAreaConhecimento() {
		return listaAreaConhecimento;
	}

	public void setListaAreaConhecimento(List<String> listaAreaConhecimento) {
		this.listaAreaConhecimento = listaAreaConhecimento;
	}

}
