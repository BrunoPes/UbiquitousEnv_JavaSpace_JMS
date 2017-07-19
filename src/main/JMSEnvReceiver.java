import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.jms.*;
import javax.naming.*;

import org.exolab.jms.administration.*;

public class JMSEnvReceiver extends Thread {
	private EnviromentsUI ui;
	private String envQueue;
	private Context context;
	private QueueSession session;
	private QueueConnection connection;
	
	public JMSEnvReceiver(EnviromentsUI ui) {
		this.ui = ui;
		this.envQueue = "default";
	}
	
	public JMSEnvReceiver(String queueName, EnviromentsUI ui) {
		try {
			this.ui = ui;
			this.envQueue = queueName;
			this.checkAndCreateQueue(this.envQueue);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getEnvName() {
		return this.envQueue.split("q_")[1].trim();
	}
	
	@SuppressWarnings("rawtypes")
	private void checkAndCreateQueue(String queueToAdd) throws Exception {
		String url = "tcp://localhost:3035/";
	    JmsAdminServerIfc admin = AdminConnectionFactory.create(url);
	    
		Vector destinations = admin.getAllDestinations();
		Iterator iterator = destinations.iterator();
		while(iterator.hasNext()) {
			Destination destination = (Destination) iterator.next();
			if (destination instanceof Queue) {
				Queue queue = (Queue) destination;
				if(queue.getQueueName().equals(queueToAdd)) {
					System.out.println("Queue existente!");
					return;
				}
			}
		}
		
		if(!admin.addDestination(queueToAdd, true)) {
	        System.err.println("Failed to create topic " + queueToAdd);
	    } else {
	    	System.out.println("Queue " + queueToAdd + " foi criada!");
	    }
	}
	
	private void openSession() throws Exception {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
		this.context = new InitialContext(properties);
		QueueConnectionFactory qfactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
		this.connection = qfactory.createQueueConnection();
		this.session = this.connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		this.connection.start();
	}
	
	public void closeSession() throws Exception {
		this.context.close();
 		this.connection.close();
	}
	
	private void getUpdates() {
		try {
			this.openSession();
	
	 		javax.jms.Queue dest = (javax.jms.Queue) this.context.lookup(this.envQueue);
	 		QueueReceiver receiver = session.createReceiver(dest);
	 		TextMessage msg = null;
	 		while(true) {
	 			msg = (TextMessage) receiver.receive();
                System.out.println("Mensagem Recebida: " + msg.getText());
                this.ui.updateLogEnvs(this.getEnvName(), msg.getText());
	 		}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.getUpdates();
	}
	
	public static void main(String args[]) {
//		JMSEnvReceiver envRec = new JMSEnvReceiver();
//		envRec.start();
	}
}