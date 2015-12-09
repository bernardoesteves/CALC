package calc.utility.datatypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import calc.datastructures.core.Documento;
import calc.datastructures.implmentations.datatypes.TexteFileCALC;
import main.ClusteringProcess;
import utility.datatypes.TextFilesUtility;

public class TextFilesUtilityCALC extends TextFilesUtility {
	public static void addDataObjects(ClusteringProcess process, String filespath, List<Documento> documentos) {
		try {
			File docFolder = new File(filespath);
			int index = 0;
			for (File doc : docFolder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(doc));
				String line = "";
				StringBuffer content = new StringBuffer();
				while (null != (line = reader.readLine())) {
					content.append(line);
				}
				String[] name = doc.toString().split("\\\\");
				String title = name[name.length - 1];
				TexteFileCALC dataObject = new TexteFileCALC(title, content.toString(), index);
				title = title.replace(".txt", ".xml");
				for (Documento documento : documentos) {
					if (title.equals(documento.getNomeDoDocumento())) {
						dataObject.setListaAreaConhecimento(documento.getListaAreaDeConhecimento());
						break;
					}
				}

				process.addDataObject(dataObject);
				index++;
				reader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
