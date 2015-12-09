package calc.utility.clustering;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;

import main.ClusteringProcess;

import org.apache.commons.io.FileUtils;

import utility.clustering.ClusteringUtility;
import calc.datastructures.implmentations.datatypes.TexteFileCALC;
import calc.utility.files.GeradorClusterCSV;
import calc.utility.files.LogUtil;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;

public class ClusteringUtilityCALC extends ClusteringUtility {

	public static void writeClusterOfTextFiles(ClusteringProcess process, String strategy, String identifier, String caminhoDoCurriculosXML) {
		criaPastaDeClusters(caminhoDoCurriculosXML);

		String s;
		System.out.println("\nClusters: ");
		int totalDeClusterComMaisDeUmArquivo = 0;
		int numeroDoCluster = 1;
		for (DataCluster cluster : process.getDataClusters()) {
			String caminhoDoCluster = criaPastaCluster(numeroDoCluster, caminhoDoCurriculosXML);
			List<String> areasDeConhecimento = new ArrayList<String>();
			LogUtil.gravaLog(" ------------------------ Tamanho do cluster: " + cluster.getDataObjects().size() + " ---------------------");
			s = Integer.toString(cluster.getDataObjects().size()) + "\n";
			System.out.printf(s);
			if (cluster.getDataObjects().size() > 1)
				totalDeClusterComMaisDeUmArquivo++;
			for (DataObject object : cluster.getDataObjects()) {

				TexteFileCALC doc = (TexteFileCALC) object;
				s = doc.getTitle() + "\n";
				copiaArquivoParaPasta(caminhoDoCluster, doc.getTitle(), caminhoDoCurriculosXML);

				List<String> areaDeConhecimentoDoCurriculo = doc.getListaAreaConhecimento();
				for (String areaDeConhecimento : areaDeConhecimentoDoCurriculo) {
					if (!areasDeConhecimento.contains(areaDeConhecimento))
						areasDeConhecimento.add(areaDeConhecimento);
				}

				System.out.println(s);
				// LogUtil.gravaLog("Termos do curriculo: "+doc.getTitle()+": "+doc.getTermosAnalisados());
				// LogUtil.gravaLog("");
			}
			criaArquivoAreasConhecimento(caminhoDoCluster, areasDeConhecimento);
			numeroDoCluster++;
		}
		System.out.println(" ----- Total de clusters: " + process.getDataClusters().size());
		System.out.println(" ----- Total de clusters com mais de um arquivo: " + totalDeClusterComMaisDeUmArquivo);

		// Iteração apenas para colocar no log todas as interseccoes de termos
		/*
		 * for (DataCluster cluster : process.getDataClusters()) { for (int i =
		 * 0; i < cluster.getDataObjects().size(); i++) { TexteFileCALC doc =
		 * (TexteFileCALC) cluster.getDataObjects().get(i); Set<String>
		 * palavrasFrequentes1 = doc.getTermosAnalisados(); for (int j = i + 1;
		 * j < cluster.getDataObjects().size(); j++) { TexteFileCALC doc2 =
		 * (TexteFileCALC) cluster.getDataObjects().get(j); Set<String>
		 * palavrasFrequentes2 = doc2.getTermosAnalisados(); Set<String>
		 * intersect = new HashSet<>(); intersect.clear();
		 * intersect.addAll(palavrasFrequentes1);
		 * intersect.retainAll(palavrasFrequentes2);
		 * 
		 * LogUtil.gravaLog(
		 * " ========================================================== ");
		 * LogUtil.gravaLog("Intersecção(" + doc.getTitle() + " e " +
		 * doc2.getTitle() + ") - total " + intersect.size() + " : " +
		 * intersect.toString()); LogUtil.gravaLog(""); } }
		 * 
		 * }
		 */

		// Iteracao apenas para verificar quantidade de termos em comum entre
		// todos os arquivos de um cluster
		/*
		 * for (DataCluster cluster : process.getDataClusters()) { StringBuffer
		 * nomesDoCurriculos = new StringBuffer(); Set<String>
		 * interseccaoDeTodosOsArquivosDoCluster = new HashSet<>();
		 * interseccaoDeTodosOsArquivosDoCluster.clear(); TexteFileCALC
		 * primeiroDocDoCluster = (TexteFileCALC)
		 * cluster.getDataObjects().get(0);
		 * interseccaoDeTodosOsArquivosDoCluster
		 * .addAll(primeiroDocDoCluster.getTermosAnalisados()); for (int i = 0;
		 * i < cluster.getDataObjects().size(); i++) { TexteFileCALC doc =
		 * (TexteFileCALC) cluster.getDataObjects().get(i);
		 * nomesDoCurriculos.append(doc.getTitle() + ", "); if (i != 0)
		 * interseccaoDeTodosOsArquivosDoCluster
		 * .retainAll(doc.getTermosAnalisados()); } LogUtil.gravaLog("");
		 * LogUtil.gravaLog("");
		 * LogUtil.gravaLog(" -------------------- Existem " +
		 * interseccaoDeTodosOsArquivosDoCluster.size() +
		 * " termos comuns entre todos os curriculos do cluster que contem: " +
		 * nomesDoCurriculos); LogUtil.gravaLog("");
		 * LogUtil.gravaLog(interseccaoDeTodosOsArquivosDoCluster.toString()); }
		 */

		GeradorClusterCSV.geraArquivoCSV(process.getDataClusters());

	}

	private static void criaArquivoAreasConhecimento(String caminhoDoCluster, List<String> areasDeConhecimento) {
		try {
			FileWriter fileWriter = new FileWriter(caminhoDoCluster + File.separator + "Areas de conhecimento.txt");
			fileWriter.append(areasDeConhecimento.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void copiaArquivoParaPasta(String caminhoDoCluster, String title, String caminhoDoCurriculosXML) {
		int index = title.lastIndexOf('/');
		String nomeDoArquivo = title.substring(index + 1, title.length());
		nomeDoArquivo = nomeDoArquivo.replace(".txt", ".xml");
		File origem = new File(caminhoDoCurriculosXML + File.separator + nomeDoArquivo);
		File destino = new File(caminhoDoCluster + File.separator + nomeDoArquivo);
		try {
			FileUtils.copyFile(origem, destino);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void criaPastaDeClusters(String caminhoDoCurriculosXML) {
		File file = new File(caminhoDoCurriculosXML + File.separator + "Clusters" + File.separator);
		file.getParentFile().mkdirs();
	}

	private static String criaPastaCluster(int numeroDoCluster, String caminhoDoCurriculosXML) {
		String caminhoDaPastaDoCluster = caminhoDoCurriculosXML + File.separator + "Clusters" + File.separator + "Cluster_" + numeroDoCluster + File.separator;
		File file = new File(caminhoDaPastaDoCluster);
		file.getParentFile().mkdirs();
		return caminhoDaPastaDoCluster;

	}
}
