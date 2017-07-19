import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class EnviromentsUI extends JFrame implements MouseListener, ActionListener  {
	private static final long serialVersionUID = 1L;
	
	private EnviromentManager manager;
	private ArrayList<String> envTexts = new ArrayList<String>();
	private ArrayList<String> devTexts = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> logEnvs = new HashMap<String, ArrayList<String>>();
	
	private JTextArea envArea;
	private JTextArea devArea;
	private JTextArea logArea;
	private JComboBox<String> logSelector;
	
	private JButton[] envBtns = {
		new JButton("Criar Amb."),
		new JButton("Remover Amb.")
	};
	private JButton[] devBtns = {
		new JButton("Criar Disp."),
		new JButton("Remover Disp."),
		new JButton("Transferir Disp.")
	};
	
	public EnviromentsUI(int width, int height) {
		super("JavaSpace - Ambiente Ubiquo");
		this.manager = new EnviromentManager(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, width, height);
        this.getContentPane().setLayout(null);
        this.setResizable(false);


		JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMaximumSize(new Dimension(300-30, 200));
        JLabel logLabel = new JLabel("Ambientes <nome, qtd_dispositivos>");
        logLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        logLabel.setBackground(Color.BLACK);
        logLabel.setForeground(Color.BLUE);
		this.envArea = new JTextArea();
		this.envArea.setEditable(false);
		this.envArea.setText("Nenhum Ambiente Criado");
		this.envArea.setLineWrap(true);
		this.envArea.setBackground(Color.WHITE);
		((DefaultCaret)this.envArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(this.envArea);
		scrollPane.setColumnHeaderView(logLabel);
		JPanel envPanel = new JPanel();
        envPanel.setLayout(new BoxLayout(envPanel, BoxLayout.Y_AXIS));
        envPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        envPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        envPanel.setBackground(Color.WHITE);
        envPanel.setBounds(0,0,272,height);
		JPanel envBtnsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
		envBtnsPanel.setBackground(Color.WHITE);
		envBtnsPanel.setMaximumSize(new Dimension(270, 50));
		for(JButton button : this.envBtns) {
			button.addMouseListener(this);
			envBtnsPanel.add(button);
		}
		envPanel.add(scrollPane, BorderLayout.CENTER);
		envPanel.add(envBtnsPanel);


		JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setMaximumSize(new Dimension(300-30, 200));
        JLabel logLabel2 = new JLabel("Dispositivos <nome, ambiente>");
        logLabel2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        logLabel2.setBackground(Color.BLACK);
        logLabel2.setForeground(Color.BLUE);
		this.devArea = new JTextArea();
		this.devArea.setEditable(false);
		this.devArea.setText("Nenhum Dispositivo Criado");
		this.devArea.setLineWrap(true);
		this.devArea.setBackground(Color.WHITE);
		((DefaultCaret)this.devArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane2.setViewportView(this.devArea);
		scrollPane2.setColumnHeaderView(logLabel2);
		JPanel devPanel = new JPanel();
        devPanel.setLayout(new BoxLayout(devPanel, BoxLayout.Y_AXIS));
        devPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        devPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        devPanel.setBackground(Color.WHITE);
        devPanel.setBounds(274,0,273,height);
		JPanel devBtnsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
		devBtnsPanel.setBackground(Color.WHITE);
		devBtnsPanel.setMaximumSize(new Dimension(300, 100));
		for(JButton button : this.devBtns) {
			button.addMouseListener(this);
			devBtnsPanel.add(button);
		}
		devPanel.add(scrollPane2, BorderLayout.CENTER);
		devPanel.add(devBtnsPanel);
		
		
		JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane3.setMaximumSize(new Dimension(200, 275));
        this.logSelector = new JComboBox<String>(new String[]{"Escolha um log"});
        this.logSelector.addActionListener(this);
		this.logArea = new JTextArea("Nenhum log selecionado");
		this.logArea.setEditable(false);
		this.logArea.setText("Nenhum Log");
		this.logArea.setLineWrap(true);
		this.logArea.setBackground(Color.WHITE);
		((DefaultCaret)this.logArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane3.setViewportView(this.logArea);
		scrollPane3.setColumnHeaderView(logSelector);
		JPanel logPanel = new JPanel();
        logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
        logPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        logPanel.setBackground(Color.WHITE);
        logPanel.setBounds(550,0,200,height);
        logPanel.add(scrollPane3);

		this.getContentPane().add(envPanel);
		this.getContentPane().add(devPanel);
		this.getContentPane().add(logPanel);
		this.setVisible(true);
	}

	private void updateInterface() {
		try {
			String envTxt = "";
			String devTxt = "";
			this.envArea.setText("Nenhum Ambiente Criado");
			this.devArea.setText("Nenhum Dispositivo Criado");
			this.envTexts.clear();
			this.devTexts.clear();
			
			for(EnviromentCoordinator env : this.manager.getAllEnviroments()) {
				String envLbl = "<"+env.enviromentName+","+env.totalDevices+">";
				this.envTexts.add(envLbl);
				envTxt += envLbl+"\n";
				
			}
			
			for(Device dev : this.manager.getAllDevices()) {
				String devLbl = "<"+dev.deviceName+","+dev.enviromentName+">";
				this.devTexts.add(devLbl);
				devTxt += devLbl+"\n";
			}
			
			if(envTxt.length() > 0) this.envArea.setText(envTxt);
			if(devTxt.length() > 0) this.devArea.setText(devTxt);
			this.envArea.updateUI();
			this.devArea.updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateLogEnvs(String envName, String msg) {
		System.out.println("Update: " + msg);
		this.logEnvs.get(envName).add(msg);
		this.updateLogText(envName);
	}
	
	public void updateLogText(String selected) {
		if(!((String)this.logSelector.getSelectedItem()).equals(selected)) {
			return;
		}
		String txt = "";
		for(String msg : this.logEnvs.get(selected)) {
			txt += msg + "\n";
		}
		this.logArea.setText(txt);
		this.logArea.updateUI();
	}
	
	private void alertShow(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
	}

	private String alertInput(String title, String msg, String[] list) {
		try {
			return (String)JOptionPane.showInputDialog(this, msg, title, JOptionPane.QUESTION_MESSAGE, null, list, list[0]);	
		} catch(Exception e) {
			this.alertShow("Erro", "Tente novamente");
		}
		
		return "";
	}
	
	private String[] removeFromArray(String[] array, String toRemove) {
		int j=0;
		String aux = "";
		String[] newArr = new String[array.length-1];
		System.out.println("To remove: " + toRemove);
		
		for(int i=0; i<array.length; i++) {
			aux = array[i].split(",")[0].split("<")[1];
			if(!aux.equals(toRemove)){
				newArr[j++] = array[i];
			}
		}
		return newArr;
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() instanceof JComboBox) {
			String envSelected = (String)((JComboBox<String>)ev.getSource()).getSelectedItem();
			if(this.logEnvs.get(envSelected) != null) {
				this.updateLogText(envSelected);
			} else if(envSelected.trim().equals("Escolha um log")) {
				this.logArea.setText("Escolha um log");
				this.logArea.updateUI();
			}
		}
	}

	public void mousePressed(MouseEvent e) {
        if(e.getSource() instanceof JButton) {
        	JButton button = (JButton)e.getSource();
        	
        	String[] listEnvs = this.envTexts.toArray(new String[]{});
        	String[] listDevs = this.devTexts.toArray(new String[]{});
        	System.out.println("Env e devs: " + listEnvs.length + "  -  " + listDevs.length);
        	
            switch(button.getText()) {
				case "Criar Amb.":
					try {
						String newEnv = "amb"+EnviromentManager.envId;
						if(this.manager.createEnviroment(newEnv)) {
							EnviromentManager.envId++;
							this.logEnvs.put(newEnv, new ArrayList<String>());
							this.logSelector.addItem(newEnv);
							this.alertShow("Criação de Ambiente", "Ambiente criado com sucesso!");
						} else {
							this.alertShow("Erro na Criação", "Ocorreu algum erro e o ambiente não pôde ser criado. :(");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					break;
				case "Remover Amb.":
					try{
						if(listEnvs.length > 0){
							String env = this.alertInput("Remover Ambiente", "Qual ambiente deseja remover?", listEnvs);
							
							if(env != null && env != "") {
								env = env.split(",")[0].split("<")[1];
								if(this.manager.removeEnviroment(env)) {
									this.logEnvs.remove(env);
									this.logSelector.removeItem(env);
									this.alertShow("Remoção de Ambiente", "Ambiente removido com sucesso!");
								} else {
									this.alertShow("Erro na Remoção", "Ocorreu algum erro e o ambiente não pôde ser removido. :(");
								}
							} else {
								return;
							}
						} else {
							this.alertShow("Nenhum Ambiente", "Não existem ambientes para serem removidos");
						}						
					} catch(Exception e1){
						e1.printStackTrace();
					}

					break;
				case "Criar Disp.":
					try {
						if(listEnvs.length > 0) {
							String env = this.alertInput("Criar Dispositvo", "Em qual ambiente deseja inserir o novo dispositivo?", listEnvs);
							if(env != null && env != "") {
								env = env.split(",")[0].split("<")[1];
								if(this.manager.insertDevice("disp"+EnviromentManager.deviceId++, env)) {
									this.alertShow("Criação de Dispositivo", "Dispositivo criado com sucesso!");
								} else {
									this.alertShow("Erro na Criação", "Ocorreu algum erro e o dispositivo não pôde ser criado. :(");
								}
							} else {
								return;
							}
						} else {
							this.alertShow("Nenhum Ambiente", "Não existem ambientes para receberem dispositivos");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					break;
				case "Remover Disp.":
					try {
						if(listDevs.length > 0) {
							String dev = this.alertInput("Remover Dispositivo", "Qual dispositivo deseja remover?", listDevs);
							if(dev != null && dev != "") {
								dev = dev.split(",")[0].split("<")[1];
								if(this.manager.removeDevice(dev)) {
									this.alertShow("Remoção de Dispositivo", "Dispositivo removido com sucesso!");
								} else {
									this.alertShow("Erro na Remoção", "Ocorreu algum erro e o dispositivo não pôde ser removido. :(");
								}
							} else {
								return;
							}
						} else {
							this.alertShow("Nenhum Dispositivo", "Não existem dispositivos para serem removidos");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					break;
				case "Transferir Disp.":
					try {
						if(listDevs.length > 0 && listEnvs.length > 1) {
							String dev = this.alertInput("Transferir Dispositivo", "Qual dispositivo deseja transferir?", listDevs);
							if(dev != null && dev != "") {
								String excludeEnv = dev.split(",")[1].split(">")[0];
								listEnvs = this.removeFromArray(listEnvs, excludeEnv);
								dev = dev.split(",")[0].split("<")[1];
								String env = this.alertInput("Transferir Dispositivo", "Qual o ambiente de destino?", listEnvs);
								if(env != null && env != "") {
									env = env.split(",")[0].split("<")[1];
									if(this.manager.transferDevice(dev, env)) {
										this.alertShow("Transferência de Dispositivo", "Dispositivo transferido!");
									} else if(env != null && env != "") {
										this.alertShow("Erro na Transferência", "Ocorreu algum erro e na transferência. :(");
									}
								} else {
									return;
								}
							} else {
								return;
							}
						} else {
							if(listEnvs.length == 1) {
								this.alertShow("Ambientes insuficientes", "Para transferir um dispositivo crie mais ambientes");
							} else {
								this.alertShow("Nenhum Dispositivo", "Não existem dispositivos para serem transferidos");	
							}
						}
					} catch(Exception e1) {
						e1.printStackTrace();
					}
					break;
				default: break;
            }
            
            this.updateInterface();
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

	public static void main(String[] args) {
		new EnviromentsUI(760, 308);
	}


}
