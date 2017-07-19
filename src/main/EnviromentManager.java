import java.util.*;

import net.jini.space.JavaSpace;

public class EnviromentManager {
	private JavaSpace space;
	private HashMap<String, JMSDeviceSender> senders = new HashMap<String, JMSDeviceSender>();
	private HashMap<String, JMSEnvReceiver> rcvrs = new HashMap<String, JMSEnvReceiver>();
	private EnviromentsUI ui;

	public static int envId = 0;
	public static int deviceId = 0;

	public EnviromentManager(EnviromentsUI ui) {
		System.out.println("Procurando pelo servico JavaSpace...");
		this.ui = ui;
		Lookup finder = new Lookup(JavaSpace.class);
		this.space = (JavaSpace) finder.getService();
		if(this.space != null) System.out.println("Servico encontrado");
		else System.out.println("Servico não encontrado");
	
		this.clearJavaSpace();
	}
	
	public EnviromentManager(JavaSpace space, EnviromentsUI ui) {
		this.ui = ui;
		this.space = space;
		this.clearJavaSpace();
	}
	
	public void clearJavaSpace() {
		try {
			Device dev = (Device)this.space.takeIfExists(new Device(), null, Long.MAX_VALUE);
			EnviromentCoordinator env = (EnviromentCoordinator)this.space.takeIfExists(new EnviromentCoordinator(), null, Long.MAX_VALUE);
			
			while(env != null)
				env = (EnviromentCoordinator)this.space.takeIfExists(new EnviromentCoordinator(), null, Long.MAX_VALUE);
			
			while(dev != null)
				dev = (Device)this.space.takeIfExists(new Device(), null, Long.MAX_VALUE);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public EnviromentCoordinator getEnviroment(String envName) throws Exception {
		EnviromentCoordinator env = new EnviromentCoordinator(envName);
		return (EnviromentCoordinator)this.space.readIfExists(env, null, Long.MAX_VALUE);
	}
	
	public ArrayList<EnviromentCoordinator> getAllEnviroments() throws Exception {
		ArrayList<EnviromentCoordinator> envs = new ArrayList<EnviromentCoordinator>();
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.takeIfExists(new EnviromentCoordinator(), null, Long.MAX_VALUE);
		
		while(env != null) {
			envs.add(env);
			env = (EnviromentCoordinator)this.space.takeIfExists(new EnviromentCoordinator(), null, Long.MAX_VALUE);
		}
		
		for(EnviromentCoordinator envAux : envs)
			this.space.write(envAux, null, Long.MAX_VALUE);
		
		return envs;
	}

	public boolean createEnviroment(String envName) throws Exception {
		EnviromentCoordinator env = new EnviromentCoordinator(envName, 0);
		this.space.write(env, null, Long.MAX_VALUE);
		
		if(this.space.readIfExists(env, null, Long.MAX_VALUE) != null){
			this.senders.put(envName, new JMSDeviceSender("q_"+envName));
			this.rcvrs.put(envName, new JMSEnvReceiver("q_"+envName, this.ui));
			this.rcvrs.get(envName).start();
		}
			
		return (this.space.readIfExists(env, null, Long.MAX_VALUE) != null);
	}

	public boolean removeEnviroment(String envName) throws Exception {
		EnviromentCoordinator envTemp = new EnviromentCoordinator(envName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.takeIfExists(envTemp, null, Long.MAX_VALUE);

		for(int i=0; i<env.totalDevices; i++) {
			Device device = new Device(envName, true);
			this.space.takeIfExists(device, null, Long.MAX_VALUE);
		}
		
		if(this.space.readIfExists(envTemp, null, Long.MAX_VALUE) == null) {
			this.senders.remove(envName);
			this.rcvrs.get(envName).interrupt();
			this.rcvrs.remove(envName);
		}
		
		return this.space.readIfExists(envTemp, null, Long.MAX_VALUE) == null;
	}

	public ArrayList<Device> getAllDevices() throws Exception {
		ArrayList<Device> devices = new ArrayList<Device>();
		Device dev = (Device)this.space.takeIfExists(new Device(), null, Long.MAX_VALUE);
		
		while(dev != null) {
			devices.add(dev);
			dev = (Device)this.space.takeIfExists(new Device(), null, Long.MAX_VALUE);
		}
		
		for(Device device : devices)
			this.space.write(device, null, Long.MAX_VALUE);
		
		return devices;
	}
	
	public ArrayList<Device> getAllDevicesInEnv(String envName) throws Exception {
		Device devTemp;
		ArrayList<Device> devices = new ArrayList<Device>();
		EnviromentCoordinator envTemp = new EnviromentCoordinator(envName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.readIfExists(envTemp, null, Long.MAX_VALUE);
		if(env != null && env.totalDevices > 0) {
			for(int i=0; i<env.totalDevices; i++) {
				devTemp = new Device(envName, true);
				Device device = (Device)this.space.takeIfExists(devTemp, null, Long.MAX_VALUE);
				if(device != null && devices.indexOf(device) < 0) {
					devices.add(device);
				}
			}
			for(Device device : devices)
				this.space.write(device, null, Long.MAX_VALUE);
			
			return devices;
		} else {
			System.out.println("Ambiente Inexistente");
			return null;
		}
	}

	public Device getDevice(String deviceName) throws Exception {
		Device temp = new Device(deviceName, false);
		Device device = (Device)this.space.readIfExists(temp, null, Long.MAX_VALUE);
		if(device != null) {
			return device;
		} else {
			System.out.println("Device inexistente");
			return null;
		}
	}

	public boolean insertDevice(String deviceName, String envName) throws Exception {
		Device device = new Device(deviceName, envName);
		EnviromentCoordinator temp = new EnviromentCoordinator(envName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.takeIfExists(temp, null, Long.MAX_VALUE);
		
		env.totalDevices++;
		this.space.write(env, null, Long.MAX_VALUE);
		this.space.write(device, null, Long.MAX_VALUE);

		if(this.space.readIfExists(device, null, Long.MAX_VALUE) != null) {
			this.senders.get(envName).updateLog(deviceName + " entrou no " + envName);
		}
		
		return this.space.readIfExists(device, null, Long.MAX_VALUE) != null;
	}

	public boolean removeDevice(String deviceName) throws Exception {
		Device device = (Device)this.space.takeIfExists(new Device(deviceName, false), null, Long.MAX_VALUE);
		if(device != null) {
			EnviromentCoordinator envTemp = new EnviromentCoordinator(device.enviromentName);
			EnviromentCoordinator env = (EnviromentCoordinator)this.space.takeIfExists(envTemp, null, Long.MAX_VALUE);
			env.totalDevices--;
			this.space.write(env, null, Long.MAX_VALUE);
			
			if(this.space.readIfExists(device, null, Long.MAX_VALUE) == null) {
				this.senders.get(env.enviromentName).updateLog(deviceName + " saiu do " + env.enviromentName);
			}
			return this.space.readIfExists(device, null, Long.MAX_VALUE) == null;
		} else {
			return true;
		}
	}

	public boolean transferDevice(String deviceName, String newEnv) throws Exception {
		this.removeDevice(deviceName);
		this.insertDevice(deviceName, newEnv);
		
		Device transferredDev = new Device(deviceName, newEnv);
		return this.space.readIfExists(transferredDev, null, Long.MAX_VALUE) != null;
	}

	public boolean deviceExists(String deviceName) throws Exception {
		Device temp = new Device(deviceName, false);
		Device device = (Device)this.space.readIfExists(temp, null, Long.MAX_VALUE);
		return device != null;
	}
}