package connection;

public class URL {

	private String data;
	private String host;
	private String port;
	private String databaseName;

	public String getData() {
		return data;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setData(String data) {
		this.data = data;
	}

	// Read!:
	// https://www.codejava.net/ides/eclipse/how-to-pass-arguments-when-running-a-java-program-in-eclipse
	// https://www.cs.colostate.edu/helpdocs/eclipseCommLineArgs.html

}
