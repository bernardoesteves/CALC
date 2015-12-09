package calc.interfacegrafica;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import calc.main.Calc;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class JanelaPrincipal extends JFrame {
	private JTextField caminhoDiretorio;

	private static final long serialVersionUID = 1L;

	public JanelaPrincipal() {
		super("CALC");

		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			Dialog.erro("Não foi possível carregar todos os estilos da aplicação. Continuando sem estilos.");
		}
		FormLayout formLayout = new FormLayout("10dlu, 100dlu, 50dlu, 50dlu, 10dlu", "20dlu, 10dlu ,20dlu ,50dlu, 10dlu");
		JPanel panel = new JPanel();
		PanelBuilder panelBuilder = new PanelBuilder(formLayout, panel);
		CellConstraints cc = new CellConstraints();
		panelBuilder.addLabel("Diretório dos currículos Lattes", cc.xyw(2, 2, 2));

		caminhoDiretorio = new JTextField();
		caminhoDiretorio.setEditable(false);
		caminhoDiretorio.setBackground(Color.WHITE);
		panelBuilder.add(caminhoDiretorio, cc.xyw(2, 3, 2));

		panelBuilder.add(botaoProcurar(), cc.xy(4, 3));
		panelBuilder.add(botaoAgruparCurriculos(), cc.xyw(3, 4, 2, "right, bottom"));
		panelBuilder.add(Informativo.getJLabel(), cc.xyw(2, 5, 2, "left, bottom"));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panelBuilder.getPanel());
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		setVisible(true);
	}

	private Component botaoAgruparCurriculos() {
		JButton botao = new JButton("Agrupar currrículos");
		botao.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
					public void uncaughtException(Thread th, Throwable ex) {
						Dialog.erro("Ocorreu um erro durante a leitura dos currículos...");
						Informativo.geraInfo("Ocorreu um erro durante a leitura dos currículos...");
					}
				};

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (caminhoDiretorio.getText().equals("")) {
								Dialog.erro("Informe um diretório válido.");
								return;
							}
							Informativo.geraInfo("Aguarde enquanto os currículos são agrupados...");
							Calc calc = new Calc(caminhoDiretorio.getText());
							calc.agrupaCurriculos();
							Informativo.geraInfo("Os currículos foram agrupados com sucesso!");
							Desktop.getDesktop().open(new File(caminhoDiretorio.getText() + File.separator + "Clusters"));
							Dialog.alerta("Os currículos foram agrupados com sucesso!");
						} catch (IOException e) {
							e.printStackTrace();
							Dialog.erro(e.getMessage());
						}
					}
				});
				t.setUncaughtExceptionHandler(h);
				t.start();
			}

		});

		return botao;
	}

	private Component botaoProcurar() {
		JButton botao = new JButton("Procurar");
		botao.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser caminho = new JFileChooser();
				caminho.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = caminho.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					caminhoDiretorio.setText(caminho.getSelectedFile().toString());
				}

			}
		});

		return botao;
	}

}
