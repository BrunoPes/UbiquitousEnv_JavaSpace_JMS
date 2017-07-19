import java.util.Hashtable;
import java.util.Scanner;

import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JMSDeviceSender {
	private String envQueue;
	private Context context;
	private QueueSession session;
	private QueueConnection connection;
	
	public JMSDeviceSender() {
		this.envQueue = "q_amb1";
	}
	
	public JMSDeviceSender(String queueName) {
		this.envQueue = queueName;
	}
	
	public void openSession() throws Exception {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
		this.context = new InitialContext(properties);
		QueueConnectionFactory qfactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
		this.connection = qfactory.createQueueConnection();
		this.session = this.connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	public void closeSession() throws Exception {
		this.context.close();
 		this.connection.close();
	}
	
	public void updateLog(String msg){
		try {
			this.openSession();
			
			TextMessage message = this.session.createTextMessage();
	 		message.setText(msg);
	 		javax.jms.Queue dest = (javax.jms.Queue) this.context.lookup(this.envQueue);
	 		QueueSender sender = this.session.createSender(dest);
	 		sender.send(message);

	 		this.closeSession();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JMSDeviceSender send = new JMSDeviceSender();
		Scanner scanner = new Scanner(System.in);
		System.out.print("Mensagem: ");
		String message = scanner.nextLine();
		
		while(message != "\n") {
			send.updateLog(message);
			System.out.print("Mensagem: ");
			message = scanner.nextLine();
		}
		
		scanner.close();
	}
}