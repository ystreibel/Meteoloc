package com.meteoloc.services.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Classe d'acces aux proprietes de configuration des webservices tels que les cles d'acces aux services
 * Cette classe est un singleton, on y accede donc en apellant la methode getInstance()
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class ServiceConfiguration extends Properties{

	private static ServiceConfiguration _instance;
	private static final Logger logger = Logger.getLogger("ServiceConfiguration.class");
	
	/**
	 * @return instance de ServiceConfiguration
	 */
	public synchronized static ServiceConfiguration getInstance(){
		if (_instance == null) _instance = new ServiceConfiguration();
		return _instance;
	}

	/**
	 * Constructeur prive
	 */
	private ServiceConfiguration() {
		super();
		try {
			this.load(this.getClass().getClassLoader().getResourceAsStream("service.properties"));
		} catch (FileNotFoundException e) {
			logger.error("Aucun fichier de configuration trouve");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
