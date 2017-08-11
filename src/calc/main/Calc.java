package calc.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.ClusteringProcess;
import utility.featureselection.StopWords;
import utility.similarity.ParDocumento;
import calc.clusteringstrategies.implementation.featureselection.TermSelectionStrategy;
import calc.clusteringstrategies.implementation.similarity.JaccardSimilarityStrategy;
import calc.datastructures.core.CalcFilesUtil;
import calc.datastructures.core.Documento;
import calc.datastructures.core.ManipuladorXML;
import calc.datastructures.core.NormalizadorString;
import calc.exceptions.CalcException;
import calc.interfacegrafica.Informativo;
import calc.utility.clustering.ClusteringUtilityCALC;
import calc.utility.datatypes.TextFilesUtilityCALC;
import calc.utility.files.LogUtil;
import clusteringstrategies.implementation.clustering.BestStarClusteringStrategy;
import clusteringstrategies.implementation.clustering.KmedoidsClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.Matrix2D;

public class Calc {
	private String caminhoDosCurriculosXML;
	private CalcFilesUtil calcFilesUtil;

	public Calc(String caminhoDosCurriculosXML) {
		this.caminhoDosCurriculosXML = caminhoDosCurriculosXML;
	}

	public void agrupaCurriculos() throws CalcException {
		try {
			calcFilesUtil = new CalcFilesUtil(caminhoDosCurriculosXML);
			List<File> listaDeCurriculoXML = calcFilesUtil.getCurriculosXML();
			List<Documento> listaDeDocumentosComTermosDeTagsEspecificas = ManipuladorXML.geraListaDeDocumentosComTermosDeTagsEspecificas(listaDeCurriculoXML);

			criaArquivosTxtComTermosNormalizados(listaDeDocumentosComTermosDeTagsEspecificas);

			// TODO parametro do usuario
			String threshold = "0.5";
			geraClusters(Double.parseDouble(threshold), listaDeDocumentosComTermosDeTagsEspecificas);
		} catch (Exception e) {
			throw new CalcException("Ocorreu um erro no agrupador de currículos. ", e);
		}

	}

	private void geraClusters(double threshold, List<Documento> documentos) {
		Set<String> stopwords = new HashSet<String>();
		stopwords.addAll(StopWords.getStopwords("portugues"));
		stopwords.addAll(StopWords.getStopwords("consoantes"));
		stopwords.addAll(StopWords.getStopwords("english"));

		TermSelectionStrategy selectionStartegy = new TermSelectionStrategy(stopwords);

		JaccardSimilarityStrategy strategy = new JaccardSimilarityStrategy();

		// Primeiro algoritmo de clusterizacao
		ClusteringProcess primeiroProcesso = new ClusteringProcess();
		primeiroProcesso.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		primeiroProcesso.setClusteringStrategy(new BestStarClusteringStrategy(threshold));
		TextFilesUtilityCALC.addDataObjects(primeiroProcesso, calcFilesUtil.getCaminhoCurriculosTxt(), documentos);
		selectionStartegy.executeFeatureSelection(primeiroProcesso.dataObjects);
		Informativo.geraInfo("Calculando similaridades...");
		Matrix2D matrizDeSimilaridades = strategy.executeSimilarity(selectionStartegy.getListaTermosDocumento());
		primeiroProcesso.similarityMatrix = matrizDeSimilaridades;
		Informativo.geraInfo("Calculando valor do K...");
		primeiroProcesso.dataClusters = primeiroProcesso.clusteringStrategy.executeClustering(primeiroProcesso.dataObjects, primeiroProcesso.similarityMatrix);

		// Segundo algoritmo de clusterizacao
		int valorDeK = 0;
		for (DataCluster cluster : primeiroProcesso.getDataClusters()) {
			if (cluster.getDataObjects().size() > 1)
				valorDeK++;
		}
		LogUtil.gravaLog("------ Valor de K = " + valorDeK);

		ClusteringProcess segundoProcesso = new ClusteringProcess();
		segundoProcesso.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		segundoProcesso.setClusteringStrategy(new KmedoidsClusteringStrategy(valorDeK, -1, 2, 0));
		TextFilesUtilityCALC.addDataObjects(segundoProcesso, calcFilesUtil.getCaminhoCurriculosTxt(), documentos);
		segundoProcesso.similarityMatrix = matrizDeSimilaridades;
		segundoProcesso.dataClusters = segundoProcesso.clusteringStrategy.executeClustering(primeiroProcesso.dataObjects, segundoProcesso.similarityMatrix);

		imprimeSimilaridadeEntreCadaArquivo(strategy);
		Informativo.geraInfo("Agrupando os currículos...");
		ClusteringUtilityCALC.writeClusterOfTextFiles(segundoProcesso, "K-MEDOIDS", "curriculos", caminhoDosCurriculosXML);
	}

	private static void imprimeSimilaridadeEntreCadaArquivo(JaccardSimilarityStrategy strategy) {
		for (ParDocumento parDocumento : strategy.retornaSimilaridadeEOParDeDocumentos()) {
			LogUtil.gravaLog("Similaridade entre " + parDocumento.getDoc1().getTitle() + " e " + parDocumento.getDoc2().getTitle() + ": " + parDocumento.getSimilaridade());
		}
	}

	private void criaArquivosTxtComTermosNormalizados(List<Documento> listaDeDocumentosComTermosDeTagsEspecificas) {
		NormalizadorString normalizadorString = new NormalizadorString();

		for (Documento documento : listaDeDocumentosComTermosDeTagsEspecificas) {
			FileWriter arquivo;
			try {
				File file = new File(calcFilesUtil.getCaminhoCurriculosTxt() + documento.getNomeDoDocumento().replace(".xml", ".txt"));
				file.getParentFile().mkdirs();
				Informativo.geraInfo("Normalizando arquivo " + file.getName());
				arquivo = new FileWriter(file);
				String textoNormalizado = normalizadorString.normalize(documento.getValoresDosAtributos().toString());
				arquivo.append(textoNormalizado);
				arquivo.flush();
				arquivo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}