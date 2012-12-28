package net.frcdb.config;

import java.util.List;
import net.frcdb.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global database settings
 * @author tim
 */
public class Config {
	
	public static final String VERSION = "2.1beta";
	
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	
	private static final String CONFIG_FILE;
	static {
		if (System.getProperty("os.name").startsWith("Windows")) {
			CONFIG_FILE = "C:\\frcdb\\frcdb-config.properties";
		} else {
			CONFIG_FILE = "/opt/frcdb/frcdb-config.properties";
		}
	}
	
	private static Config instance;
	
	private Database db;
	private List<ConfigurationProperty> properties;
	
	private Config() {
		db = Database.getInstance();
		
		load();
	}
	
	public void load() {
		try {
			properties = db.getConfigurationProperties();
		} catch (Exception ex) {
			logger.error("Failed loading configuration from database", ex);
		}
	}
	
	public List<ConfigurationProperty> getProperties() {
		return properties;
	}
	
	/**
	 * Retrieves the property with the given key. If no property with the key
	 * exists, null is returned.
	 * @param key the key to search for
	 * @return the property, or null
	 */
	public ConfigurationProperty getProperty(String key) {
		for (ConfigurationProperty prop : properties) {
			if (prop.getKey().equals(key)) {
				return prop;
			}
		}
		
		return null;
	}
	
	public void saveProperty(ConfigurationProperty prop) {
		db.store(prop);
	}
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		
		return instance;
	}
	
	public static String get(String key) {
		return getInstance().getProperty(key).getValue();
	}
	
	public static String get(String key, String def) {
		ConfigurationProperty prop = getInstance().getProperty(key);
		
		if (prop == null) {
			return def;
		} else {
			return prop.getValue();
		}
	}
	
	public static void set(String key, String value) {
		Config c = getInstance();
		
		ConfigurationProperty p = c.getProperty(key);
		if (p == null) {
			p = new ConfigurationProperty();
			p.setKey(key);
		}
		
		p.setValue(value);
		
		c.saveProperty(p);
	}
	
}
