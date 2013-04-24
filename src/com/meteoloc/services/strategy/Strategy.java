package com.meteoloc.services.strategy;

import org.apache.log4j.Logger;

/**
 * Classe abstraite de strategie permettant d'obtenir un socle commun a toutes les strategies sans faire de reecriture de code.
 * 
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 *
 */
public abstract class Strategy {
	/**
	 * Code de retour OK
	 */
	public static final int CODE_OK = 0;
	/**
	 * Code de retour PE
	 */
	public static final int CODE_PE = 1;
	/**
	 * Code de retour NON
	 */
	public static final int CODE_NON = 2;
	/**
	 * Code de retour ERREUR
	 */
	public static final int CODE_ERREUR = 3;
	
	public static final Logger logger = Logger.getLogger("Strategy.class");
	protected int result;
	protected String localisation;
	protected int day;
	protected String partOfDay;
	// Temps d'acces
	protected long begin;
	protected long end;
	
	/**
	 * Configuration de la strategie
	 * @param localisation localisation recherchee
	 * @param day jour de la recherche 0:Aujourd'hui 1:Demain 2:Apres-demain
	 * @param partOfDay partie de la journee de la recherche d pour jour n pour nuit 
	 */
	public void configure (String localisation, int day, String partOfDay) {
		this.localisation = localisation;
		this.day = day;
		this.partOfDay = partOfDay;
	}
	
	
	public abstract int execute();
	public abstract long getExecutionTime();
}
