package calc.datastructures.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CalcFilesUtil {
	private List<File> listaDeCurriculoXML = new ArrayList<File>();
	private List<File> listaDeCurriculoTxt = new ArrayList<File>();
	private String caminhoDoCurriculosXML;

	public CalcFilesUtil(String caminhoDoCurriculosXML) {
		this.caminhoDoCurriculosXML = caminhoDoCurriculosXML;
	}

	public List<File> getCurriculosXML() {
		File diretorioDosCurriculos = new File(caminhoDoCurriculosXML);
		for (File curriculo : diretorioDosCurriculos.listFiles()) {
			listaDeCurriculoXML.add(curriculo);
		}
		return listaDeCurriculoXML;
	}

	public List<File> getCurriculosTxt() {

		File diretorioDosCurriculos = new File(getCaminhoCurriculosTxt());
		for (File curriculo : diretorioDosCurriculos.listFiles()) {
			listaDeCurriculoTxt.add(curriculo);
		}
		return listaDeCurriculoTxt;
	}

	public String getCaminhoCurriculosTxt() {

		return caminhoDoCurriculosXML + File.separator + "Termos dos curriculos" + File.separator;
	}

}
