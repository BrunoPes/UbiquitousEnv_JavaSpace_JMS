import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class EnviromentUI extends JFrame implements MouseListener, KeyListener {
	private ArrayList<String> envs;
	private ArrayList<String> devs;
	private JButton createEnv;
	private JButton createDev;
	private JTextArea enviroments;
	private JTextArea devices;

	public EnviromentUI() {
		super("JavaSpace - Ambiente Ubíquo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, width, height);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

		this.enviroments = new JTextArea();
		this.devices = new JTextArea();
        this.enviroments.setEditable(false);
		this.devices.setEditable(false);
        this.enviroments.setText("");
		this.devices.setText("");
        this.enviroments.setLineWrap(true);
		this.devices.setLineWrap(true);
        this.enviroments.setMaximumSize(new Dimension(293, 180));
		this.devices.setMaximumSize(new Dimension(293, 180));
        ((DefaultCaret)this.enviroments.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		((DefaultCaret)this.devices.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		// this.getContentPane().add(this.enviroments);
		// this.getContentPane().add(this.devices);
		// this.setVisible(true);
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

            }
        }
    }

	public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}