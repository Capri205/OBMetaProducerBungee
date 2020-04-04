package net.obmc.OBMetaProducerBungee;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class OBMetaProducer extends Plugin {
	
	private String metafile;
	PrintWriter tracker = null;
	Configuration config = null;
	public static String program = "OBMetaProducerBungee";
	
	@Override
	public void onEnable() {

		getLogger().info("[" + program + "] Plugin loaded");
		
		LoadConfig();
		
		Initialize();

		Register();

		getLogger().info("[" + program + "] Plugin Version " + this.getDescription().getVersion() + " activated");
	
	}

	@Override
	public void onDisable() {
		
		//close file
		tracker.close();
		getLogger().info("[" + program + "] Plugin unloaded");
	}


	/**
	 * Load default config - create if not there
	 */
	public void LoadConfig() {
		if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Initialize() {
		// open our tracker - create if it doesn't exist
		this.metafile = config.getString("MetaFile.Filename");
		File file = new File(this.metafile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// open the meta file for writing
		File mfile = new File(metafile);
		FileWriter writer;
		try {
			writer = new FileWriter(mfile, true);
			tracker = new PrintWriter(writer);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void Register() {
		// register our event handler
		getProxy().getPluginManager().registerListener(this, new OBMetaEvent(tracker));
	}
}