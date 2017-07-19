import net.jini.core.entry.Entry;

public class Device implements Entry {
	private static final long serialVersionUID = 1L;
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

	public Device(String dev, String env) {
		this.deviceName = dev;
		this.enviromentName = env;
	}

	public int getNumber() {
		return Integer.valueOf(this.deviceName.split("disp")[1]);
	}
}