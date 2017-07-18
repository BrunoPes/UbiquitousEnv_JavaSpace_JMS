import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class EnviromentsUI extends JFrame implements MouseListener, KeyListener {
	private ArrayList<String> envTexts;
	private ArrayList<String> devTexts;
	private JButton[] envBtns = {
		new JButton("Novo Amb."),
		new JButton("Remover Amb.")
	};
	private JButton[] devBtns = {
		new JButton("Criar Disp."),
		new JButton("Remover Disp."),
		new JButton("Transferir Disp.")
	};
	private JTextArea enviroments;
	private JTextArea devices;

	public EnviromentsUI(int width, int height) {
		super("JavaSpace - Ambiente Ubíquo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, width, height);
        this.getContentPane().setLayout(null);
        this.setResizable(false);


		JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(new Dimension((width/2), 100));
        scrollPane.setMaximumSize(new Dimension((width/2), 100));
        // scrollPane.setLocation(10, 10);
        // scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel logLabel = new JLabel("Ambientes");
        logLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        logLabel.setBackground(Color.BLACK);
        logLabel.setForeground(Color.BLUE);
		this.enviroments = new JTextArea();
		this.enviroments.setEditable(false);
		this.enviroments.setText("asd");
		this.enviroments.setLineWrap(true);
		// this.enviroments.setMaximumSize(new Dimension((width/2)-10,100));
		this.enviroments.setBackground(Color.RED);
		((DefaultCaret)this.enviroments.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(this.enviroments);
		scrollPane.setColumnHeaderView(logLabel);

		JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setMaximumSize(new Dimension(293, 200));
        // scrollPane2.setLocation(10, 10);
        scrollPane2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel logLabel2 = new JLabel("Dispositivos");
        logLabel2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        logLabel2.setBackground(Color.BLACK);
        logLabel2.setForeground(Color.BLUE);
		this.devices = new JTextArea();
		this.devices.setEditable(false);
		this.devices.setText("");
		this.devices.setLineWrap(true);
		this.devices.setMaximumSize(new Dimension(293, 180));
		((DefaultCaret)this.devices.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane2.setViewportView(this.devices);
		scrollPane2.setColumnHeaderView(logLabel2);

		JPanel envPanel = new JPanel();
        envPanel.setLayout(new BoxLayout(envPanel, BoxLayout.Y_AXIS));
        envPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        envPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        envPanel.setBackground(Color.WHITE);
        envPanel.setBounds(0,0,(width/2),height);
		JPanel envBtnsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
		envBtnsPanel.setBackground(Color.WHITE);
		envBtnsPanel.setMaximumSize(new Dimension((width/2), 50));
		for(JButton button : this.envBtns) {
			// button.setMaximumSize(new Dimension(30, 25));
			envBtnsPanel.add(button);
		}
		envPanel.add(envBtnsPanel);
		envPanel.add(scrollPane, BorderLayout.CENTER);

		this.getContentPane().add(envPanel);
		// this.getContentPane().add(this.devices);
		this.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == 10) { //Enter
        }
	}

	public void mousePressed(MouseEvent e) {
        if(e.getSource() instanceof JButton) {
        	JButton button = (JButton)e.getSource();
            switch(button.getText()) {
				case "Novo Amb.": break;
				default: break;
            }
        }
    }

	public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

	public static void main(String[] args) {
		new EnviromentsUI(600, 400);
	}
}
