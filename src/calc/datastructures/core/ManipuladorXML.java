package calc.datastructures.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import calc.interfacegrafica.Informativo;

public class ManipuladorXML {
	private static List<String> atributosEspecificos = Arrays.asList("NOME-DA-ESPECIALIDADE", "AREA-DO-CONHECIMENTO", "TEXTO-RESUMO-CV-RH", "TITULO", "PALAVRA-CHAVE");

	private static String nomeAreaDeConhecimento = "NOME-DA-AREA-DO-CONHECIMENTO";

	static List<Documento> listaDeDocumentos = new ArrayList<Documento>();

	public static List<Documento> geraListaDeDocumentosComTermosDeTagsEspecificas(List<File> listaDeCurriculoXML) {
		try {
			for (File curriculo : listaDeCurriculoXML) {
				if (curriculo.isDirectory())
					continue;
				Documento documentoIndexado = new Documento(curriculo.getName());
				Informativo.geraInfo("Lendo arquivo " + curriculo.getName());
				Document document = criaDocument(curriculo);
				Node nodoPai = document.getFirstChild();
				NodeList filhosDiretos = nodoPai.getChildNodes();
				StringBuilder valoresDosAtributos = new StringBuilder();
				List<String> listaAreaDeConhecimento = new ArrayList<String>();
				for (int i = 0; i < filhosDiretos.getLength(); i++) {
					Node filhoDireto = filhosDiretos.item(i);
					if (filhoDireto.getNodeType() == Node.ELEMENT_NODE) {
						obtemValoresDosAtributosEspecificos(filhoDireto, valoresDosAtributos, listaAreaDeConhecimento);
						if (filhoDireto.getChildNodes().getLength() > 0) {
							obtemValoresDosAtributosEspecificosDosFilhos(filhoDireto.getChildNodes(), valoresDosAtributos, listaAreaDeConhecimento);
						}
					}
				}
				documentoIndexado.setValoresDosAtributos(valoresDosAtributos);
				documentoIndexado.setListaAreaDeConhecimento(listaAreaDeConhecimento);
				listaDeDocumentos.add(documentoIndexado);
			}
			return listaDeDocumentos;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void obtemValoresDosAtributosEspecificosDosFilhos(NodeList filhos, StringBuilder valoresDosAtributos, List<String> listaAreaDeConhecimento) {
		for (int i = 0; i < filhos.getLength(); i++) {
			Node filho = filhos.item(i);
			if (filho.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (filho != null) {
				obtemValoresDosAtributosEspecificos(filho, valoresDosAtributos, listaAreaDeConhecimento);
				if (filho.getChildNodes().getLength() > 0) {
					obtemValoresDosAtributosEspecificosDosFilhos(filho.getChildNodes(), valoresDosAtributos, listaAreaDeConhecimento);
				}
			}
		}
	}

	private static StringBuilder obtemValoresDosAtributosEspecificos(Node nodo, StringBuilder valorDoField, List<String> listaAreaDeConhecimento) {
		NamedNodeMap atributos = nodo.getAttributes();
		if (atributos == null || nodo.getNodeType() != Node.ELEMENT_NODE) {
			return valorDoField;
		}
		for (int i = 0; i < atributos.getLength(); i++) {
			Attr atributo = (Attr) atributos.item(i);

			if (atributo.getNodeName().equals(nomeAreaDeConhecimento) && !atributo.getValue().equals("")) {
				if (!listaAreaDeConhecimento.contains(atributo.getValue())) {
					if (atributo.getOwnerElement().getParentNode().getParentNode().getParentNode().getNodeName().equals("FORMACAO-ACADEMICA-TITULACAO")) {
						listaAreaDeConhecimento.add(atributo.getValue());
					}
				}
			}

			for (String atributoEspecifico : atributosEspecificos) {
				if (!atributo.getNodeName().contains(atributoEspecifico))
					continue;
				if (atributo.getNodeName().contains("-INGLES") || atributo.getNodeName().contains("-EN"))
					continue;
				String valorDoAtributo = atributo.getValue();
				valorDoField.append(valorDoAtributo + " ");
			}
		}
		return valorDoField;
	}

	private static Document criaDocument(File curriculo) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Document document = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(curriculo);
			document.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currículos");
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currículos");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currículos");
		}
		return document;
	}
}