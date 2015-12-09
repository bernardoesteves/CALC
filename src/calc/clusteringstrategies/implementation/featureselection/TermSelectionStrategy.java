package calc.clusteringstrategies.implementation.featureselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import calc.datastructures.core.TermosDocumento;
import calc.datastructures.implmentations.datatypes.TexteFileCALC;
import clusteringstrategies.core.FeatureSelectionStrategy;
import datastructures.core.DataObject;

public class TermSelectionStrategy extends FeatureSelectionStrategy {

	private Set<String> stopWords;
	private List<TermosDocumento> listaTermosDocumentos = new ArrayList<TermosDocumento>();

	public TermSelectionStrategy(Set<String> stopWords) {
		this.stopWords = stopWords;
	}

	public List<DataObject> executeFeatureSelection(List<DataObject> dataObjects) {

		for (DataObject dataObject : dataObjects) {
			TexteFileCALC doc = (TexteFileCALC) dataObject;
			TermosDocumento termosDocumento = new TermosDocumento(doc);
			geraListaDeTermosDoDataObject(doc, termosDocumento);
		}

		return dataObjects;
	}

	private List<String> geraListaDeTermosDoDataObject(TexteFileCALC doc, TermosDocumento termosDocumento) {
		final HashMap<String, Integer> bagOfWords = new HashMap<String, Integer>();

		String[] tokens = doc.getContent().toLowerCase().split("[\\s,;.\\n\\t]+");
		for (String token : tokens) {
			if (!token.isEmpty()) {
				token = token.trim();
				if (this.stopWords.contains(token) || ehNumero(token)) {
					continue;
				}
				Integer frequency = bagOfWords.get(token);
				if (frequency == null)
					frequency = 0;
				frequency++;
				bagOfWords.put(token, frequency);
			}
		}

		List<String> listaDeTermos = new ArrayList<String>(bagOfWords.keySet());
		Collections.sort(listaDeTermos, new Comparator<String>() {
			@Override
			public int compare(String term1, String term2) {
				return bagOfWords.get(term2) - bagOfWords.get(term1);
			}
		});

		System.out.println("Total de termos analisados do curriculo " + doc.getTitle() + " é: " + listaDeTermos.size());

		for (String term : listaDeTermos) {

			termosDocumento.adicionaPalavraFrequente(term);

			doc.adicionaTermoAnalisado(term);
		}
		listaTermosDocumentos.add(termosDocumento);
		return listaDeTermos;
	}

	private boolean ehNumero(String term) {
		boolean ehNumero = term.matches("-?\\d+(\\.\\d+)?");
		return ehNumero;
	}

	public List<TermosDocumento> getListaTermosDocumento() {
		return this.listaTermosDocumentos;
	}
}
