package calc.datastructures.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import datastructures.implementations.datatypes.TextFile;

public class TermosDocumento extends TextFile {
	private TextFile doc;
	private List<TermoFrequente> listaTermosFrequentes = new ArrayList<TermoFrequente>();
	private HashMap<String, Integer> mapTermosFrequentes = new HashMap<String, Integer>();
	private Set<String> palavrasFrequente = new HashSet<String>();

	public TermosDocumento(String title, String content, int index) {
		super(title, content, index);
		this.setMapTermosFrequentes(new HashMap<String, Integer>());
	}

	public TermosDocumento(TextFile doc) {
		super(null, null, 0);
		this.setDoc(doc);
		this.setMapTermosFrequentes(new HashMap<String, Integer>());
	}

	public void adicionaTermoFrequente(TermoFrequente termoFrequente) {
		listaTermosFrequentes.add(termoFrequente);
	}

	public void adicionaPalavraFrequente(String palavraFrequente) {
		palavrasFrequente.add(palavraFrequente);
	}

	public Set<String> getPalavrasFrequentes() {
		// TODO Auto-generated method stub
		return palavrasFrequente;
	}

	public TextFile getDoc() {
		return doc;
	}

	public void setDoc(TextFile doc) {
		this.doc = doc;
	}

	public HashMap<String, Integer> getMapTermosFrequentes() {
		return mapTermosFrequentes;
	}

	public void setMapTermosFrequentes(HashMap<String, Integer> mapTermosFrequentes) {
		this.mapTermosFrequentes = mapTermosFrequentes;
	}

	public void adicionaTermoFrequente(String term, Integer integer) {
		this.mapTermosFrequentes.put(term, integer);

	}

	public void setPalavrasFrequentes(Set<String> palavrasFrequentes) {
		this.palavrasFrequente = palavrasFrequentes;

	}
}
