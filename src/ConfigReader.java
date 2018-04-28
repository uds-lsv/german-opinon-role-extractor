import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * ConfigReader object contains all configuration information
 *
 */
public class ConfigReader{
	Properties prop = new Properties( System.getProperties() );
	String filename;
	
	/**
	 * If no config file name is given ConfigReader object takes information from standard file
	 */
	public ConfigReader(){
		this.filename = "data/config.txt";
		File file = new File(this.filename);
		if (file.exists()==false){
			createDefault();
		}
	}
	
	/**
	 * Reads in config information from {@link #filename}
	 * @param filename
	 */
	public ConfigReader(String filename){
		this.filename = filename;
		File file = new File(filename);
		if (file.exists()==false){
			System.out.println("Configfile not found. Reading default settings...");
			Writer writer = null;
			try
			{
			  writer = new FileWriter(filename);

			  setDefaultProperties(writer);

			}
			catch ( IOException e )
			{
			  e.printStackTrace();
			}
			finally
			{
			  try { writer.close(); } catch ( Exception e ) { }
			}
		}
	}

	/**
	 * @return {@link #prop} for the given configuration
	 */
	public Properties readConfig(){
		Reader reader = null;
		try
		{
			reader = new FileReader(filename);
			prop.load( reader );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			try { reader.close(); } catch ( Exception e ) { }
		}
		return prop;
	}
	
	/**
	 * 
	 */
	public void createDefault(){
		Writer writer = null;

		try
		{
		  writer = new FileWriter( "data/config.txt" );


			setDefaultProperties(writer);

		}
		catch ( IOException e )
		{
		  e.printStackTrace();
		}
		finally
		{
		  try { writer.close(); } catch ( Exception e ) { }
		}
	}

	private void setDefaultProperties(Writer writer) throws IOException {
		prop = new Properties( System.getProperties() );

		for (ConfigKeys ck: ConfigKeys.values()) {
			prop.setProperty(ck.getKey(), ck.getDefaultValue());
		}

		prop.store( writer, null );
	}
}