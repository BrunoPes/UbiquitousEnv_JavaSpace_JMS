import net.jini.core.entry.Entry;

public class EnviromentCoordinator implements Entry {
	private static final long serialVersionUID = 1L;
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