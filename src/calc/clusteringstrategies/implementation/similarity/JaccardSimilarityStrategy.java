package calc.clusteringstrategies.implementation.similarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.similarity.ParDocumento;
import calc.datastructures.core.TermosDocumento;
import datastructures.core.Matrix2D;

public class JaccardSimilarityStrategy {
	private List<ParDocumento> listaParDocumento = new ArrayList<ParDocumento>();
	private double maximo = 0.0;

	public Matrix2D executeSimilarity(List<TermosDocumento> termosDocumento) {

		int objectsCount = termosDocumento.size();
		Matrix2D similarityMatrix = new Matrix2D(objectsCount);

		for (int i = 0; i < objectsCount; i++) {
			for (int j = i + 1; j < objectsCount; j++) {
				TermosDocumento doc1 = termosDocumento.get(i);
				TermosDocumento doc2 = termosDocumento.get(j);

				ParDocumento parDocumento = new ParDocumento(doc1.getDoc(), doc2.getDoc());

				Double similaridade = computaSimilaridade(doc1, doc2);

				parDocumento.setSimilaridade(similaridade);
				listaParDocumento.add(parDocumento);

				similarityMatrix.set(i, j, similaridade);
			}
		}
		System.out.println("O valor maximo eh: " + maximo);

		normalizaSimilaridades(similarityMatrix);

		return similarityMatrix;
	}

	private void normalizaSimilaridades(Matrix2D similarityMatrix) {
		int tamanhoDaMatriz = similarityMatrix.getWidth();
		for (int linha = 0; linha < tamanhoDaMatriz; linha++) {
			for (int coluna = linha + 1; coluna < tamanhoDaMatriz; coluna++) {
				double original = similarityMatrix.get(linha, coluna);
				double normalizado = original / maximo;
				if (original != 1.0) {
					similarityMatrix.set(linha, coluna, normalizado);
				}

			}
		}

	}

	private Double computaSimilaridade(TermosDocumento doc1, TermosDocumento doc2) {
		{
			Set<String> palavrasFrequentesDoc1 = doc1.getPalavrasFrequentes();
			Set<String> palavrasFrequentesDoc2 = doc2.getPalavrasFrequentes();

			Set<String> intersect = new HashSet<>();
			Set<String> union = new HashSet<>();

			intersect.clear();
			intersect.addAll(palavrasFrequentesDoc1);
			intersect.retainAll(palavrasFrequentesDoc2);

			union.clear();

			union.addAll(palavrasFrequentesDoc1);
			union.addAll(palavrasFrequentesDoc2);

			int max = Math.max(palavrasFrequentesDoc1.size(), palavrasFrequentesDoc2.size());

			double resultado = (double) intersect.size() / max;

			if (resultado > maximo && resultado < 1.0) {
				maximo = resultado;
			}
			return resultado;
		}
	}

	public List<ParDocumento> retornaSimilaridadeEOParDeDocumentos() {
		return listaParDocumento;
	}

}
