package com.meteoloc.services.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Classe d'acces aux proprietes de configurations des strategies
 * Cette classe est un singleton, on y accede donc en apellant la methode getInstance()
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class SearchConfiguration extends Properties{

	private static SearchConfiguration _instance;
	private static final Logger logger = Logger.getLogger("SearchConfiguration.class");

	/**
	 * 
	 * @return instance de SearchConfiguration
	 */
	public synchronized static SearchConfiguration getInstance(){
		if (_instance == null) _instance = new SearchConfiguration();
		return _instance;
	}

	/**
	 * Constructeur prive
	 */
	private SearchConfiguration() {
		super();
		try {
			this.load(this.getClass().getClassLoader().getResourceAsStream("search.properties"));
		} catch (FileNotFoundException e) {
			logger.error("Aucun fichier de configuration trouve");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * methode d'acces aux proprietes des strategie
	 * @param key String recherche
	 * @return valeur du parametre
	 */
	public int getParam(Object key) {
		int result = 0;
		try{
			result = Integer.parseInt((String) super.get(key));
		}catch(NumberFormatException e){
			logger.error("Le parametre "+key+" ne retourne pas d'entier");
		}
		return  result;
	}
}
