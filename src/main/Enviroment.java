import net.jini.core.entry.Entry;
import java.util.ArrayList;

public class EnviromentManager {
	private JavaSpace space;
	private ArrayList<Device> devices;

	public Static int envId = 0;
	public Static int deviceId = 0;

	public EnviromentManager(JavaSpace space) {
		this.space = space;
	}

	public EnviromentCoordinator getEnviroment(String envName) {
		EnviromentCoordinator env = new EnviromentCoordinator(envName);
		return (EnviromentCoordinator)this.space.read(env, null, Long.MAX_VALUE);
	}

	public void createEnviroment(String envName) {
		EnviromentCoordinator env = new EnviromentCoordinator(envName, 0);
		this.space.write(env, null, Long.MAX_VALUE);
	}

	public void removeEnviroment(String envName) {
		EnviromentCoordinator envTemp = new EnviromentCoordinator(envName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.take(envTemp, null, Long.MAX_VALUE);

		for(int i=0; i<env.totalDevices; i++) {
			Device device = new Device(envName);
			this.space.take(device, null, Long.MAX_VALUE);
		}
	}

	public ArrayList<Device> getAllDevices(String envName) {
		Device devTemp;
		EnviromentCoordinator envTemp = new EnviromentCoordinator(envName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.read(envTemp, null, Long.MAX_VALUE);
		if(env != null) {
			devices.clear();
			for(int i=0; i<env.totalDevices; i++) {
				devTemp = new Device(envName);
				Device device = (Device)this.space.read(devTemp, null, Long.MAX_VALUE);
				if(device != null && this.devices.indexOf(device) >= 0) {
					this.devices.add(device);
				}
			}
			return this.devices;
		} else {
			System.out.println("Ambiente Inexistente");
			return null;
		}
	}

	public Device getDevice(String deviceName) {
		Device temp = new Device(envName, ("disp"+i));
		Device device = (Device)this.space.read(temp, null, Long.MAX_VALUE);
		if(device != null) {
			return device;
		} else {
			System.out.println("Device inexistente");
			return null;
		}
	}

	public void insertDevice(String deviceName, String envName) {
		Device device = new Device(deviceName, envName);
		EnviromentCoordinator temp = new EnviromentCoordinator(envName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.take(temp, null, Long.MAX_VALUE);
		
		env.totalDevices++;
		this.space.write(env, null, Long.MAX_VALUE);
		this.space.write(device, null, Long.MAX_VALUE);
	}

	public void removeDevice(String deviceName) {
		Device device = new Device(deviceName);
		EnviromentCoordinator envTemp = new EnviromentCoordinator(device.enviromentName);
		EnviromentCoordinator env = (EnviromentCoordinator)this.space.take(envTemp, null, Long.MAX_VALUE);
		this.space.take(device, null, Long.MAX_VALUE);
		
		env.totalDevices--;
		this.space.write(env, null, Long.MAX_VALUE);
	}

	public void transferDevice(String deviceName, String newEnv) {
		this.removeDevice(deviceName);
		this.insertDevice(deviceName, newEnv);
	}

	public boolean deviceExists(String deviceName) {
		Device temp = new Device(deviceName);
		Device device = (Device)this.space.read(temp, null, Long.MAX_VALUE);

		return (device != null);
	}
}

class EnviromentCoordinator implements Entry {
	public String enviromentName;
	public Integer totalDevices;

	public EnviromentCoordinator() {}

	public EnviromentCoordinator(String envName) {
		this.enviromentName = envName;
	}

	public EnviromentCoordinator(String envName, Integer devices) {
		this.enviromentName = envName;
		this.totalDevices = devices;
	}
}

class Device implements Entry {
	public String deviceName;
	public String enviromentName;

	public Device() {}
	
	public Device(String name, Boolean isEnvName) {
		if(isEnvName) {
			this.enviromentName = name;
		} else {
			this.deviceName = name;
		}
	}

	public Device(String env, String dev) {
		this.enviromentName = env;
		this.deviceName = dev;
	}

	public int getNumber() {
		return Integer.valueOf(this.deviceName.split("disp")[1]);
	}
}