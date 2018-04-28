import java.io.IOException;
import java.util.Properties;

/**
 * @author Erik Hahn
 *
 * Retrieves configuration data similarily to {@link java.util.Properties} but provides some protection
 * against configuration and programming errors.
 */
class SafeProperties {
	final Properties properties;

	/**
	 *
	 * @param properties Properties object to wrap
	 */
	SafeProperties(Properties properties) {
		this.properties = properties;
	}

	private String getProperty(String key) throws IOException {
		final String result = properties.getProperty(key);
		if (result == null) {
			throw new IOException("The variable " + key + " is not specified in the configuration file.");
		} else {
			return result;
		}
	}

	/**
	 * @param key A {@link ConfigKeys} object
	 * @return The value configured for the given configuration key
	 * @throws IOException if no value is present in the configuration file for the given key
	 */
	public String getProperty(ConfigKeys key) throws IOException {
		return getProperty(key.toString());
	}

	/**
	 *
	 * @param key A {@link ConfigKeys} object
	 * @return true if <code>getProperty(key)=="True"</code>, false otherwise
	 * @throws IOException if no value is present in the configuration file for the given key or if the value is
	 * not "True" or "False"
	 */
	public boolean getPropertyBool(ConfigKeys key) throws IOException {
		final String rawValue = getProperty(key);
		if (rawValue.equals("True")) {
			return true;
		} else if (rawValue.equals("False")) {
			return false;
		} else {
			throw new IOException("The variable " + key + " must be 'True' or 'False'");
		}
	}
}
