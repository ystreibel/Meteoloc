package com.meteoloc.services;

import org.apache.log4j.Logger;

import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.strategy.all.BRISearch;
import com.meteoloc.services.strategy.all.BRZSearch;
import com.meteoloc.services.strategy.all.CVLSearch;
import com.meteoloc.services.strategy.all.DRYSearch;
import com.meteoloc.services.strategy.all.STASearch;
import com.meteoloc.services.strategy.all.TRJSearch;

/**
 * Point d'entrée de l'application, cette classe est reliée à Axis.
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class Meteoloc {
	private Strategy strats;
	private static final Logger logger = Logger.getLogger("Meteoloc.class");
	
	/**
	 * Méthode d'appel de notre webservice
	 * @param activity trigramme des activites , activites disponibles CVL DRY BRI TRJ BRZ STA
	 * @param localisation localisation de l'appel du webservice
	 * @param date format de date associe au webservice 0:Aujourd'hui 1:Demain 2:Apres-demain
	 * @param partOfTheDay partie de la journee d pour jour n pour nuit
	 * @return le code de retour du ws 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public String call(String activity, String localisation, String date, String partOfTheDay) {
		if(activity == null || date == null || localisation == null || partOfTheDay == null) {
			logger.error("Missing parameters");
			return "3";
		}
		if(activity.equals("") || date.equals("") || localisation.equals("")) {
			logger.error("Missing parameters");
			return "3";
		}
		if (activity.equals("CVL")){
			strats = new CVLSearch();
		}
		else if (activity.equals("DRY")){
			strats = new DRYSearch();
		}
		else if (activity.equals("BRI")){
			strats = new BRISearch();
		}
		else if (activity.equals("TRJ")){
			strats = new TRJSearch();
		}
		else if (activity.equals("BRZ")){
			strats = new BRZSearch();
		}
		else if (activity.equals("STA")){
			strats = new STASearch();
		}
		strats.configure(localisation,Integer.parseInt(date),partOfTheDay);
		int feu = strats.execute();
		return ""+feu;
	}
	
}
