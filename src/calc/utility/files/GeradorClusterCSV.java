package calc.utility.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import calc.datastructures.implmentations.datatypes.TexteFileCALC;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;

public class GeradorClusterCSV {
	private static final String COMMA_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "id;label;totalDeCurriculos; areasDeConhecimento";

	public static void main(String[] args) throws IOException {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("clusterGephi.csv");
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (int i = 0; i < 10; i++) {
				fileWriter.append(String.valueOf(i));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("Label_" + i);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(i * 2));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("Quimica, fisica, historia");
				fileWriter.append(NEW_LINE_SEPARATOR);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			fileWriter.flush();
			fileWriter.close();
		}

	}

	public static void geraArquivoCSV(List<DataCluster> clusters) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("clusterGephi.csv");
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
			System.out.println(clusters.size());
			for (int clusterID = 0; clusterID < clusters.size(); clusterID++) {

				List<DataObject> dataObjects = clusters.get(clusterID).getDataObjects();
				List<String> areasDeConhecimento = new ArrayList<String>();
				Integer totalDeCurriculos = dataObjects.size();

				fileWriter.append(String.valueOf(clusterID));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("Cluster_" + clusterID);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(totalDeCurriculos));
				fileWriter.append(COMMA_DELIMITER);

				for (DataObject curriculo : dataObjects) {
					TexteFileCALC doc = (TexteFileCALC) curriculo;
					List<String> areaDeConhecimentoDoCurriculo = doc.getListaAreaConhecimento();
					for (String areaDeConhecimento : areaDeConhecimentoDoCurriculo) {
						if (!areasDeConhecimento.contains(areaDeConhecimento))
							areasDeConhecimento.add(areaDeConhecimento);
					}
				}
				fileWriter.append(areasDeConhecimento.toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
