package com.meteoloc.services.strategy.all;

import com.meteoloc.services.facade.Facade;
import com.meteoloc.services.facade.FacadeMeteo;
import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.utils.SearchConfiguration;

/**
 * 
 * Strategie Trajet - elle effectue la recherche aupres de la facade des differents parametres et effectue les tests necessaire pour retourner le code de retour de la strategie
 * 0: Ok
 * 1: Peut etre
 * 2: Non ok
 * 3: Erreur
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class TRJSearch extends Strategy {
	private String[] localisations;


	/**
	 * Methode d'appel de la strategie
	 * On peut spécifier plusieurs localisations separes par des ;
	 * @return code de retour de la strategie 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public int execute() {
		begin = System.currentTimeMillis();
		logger.info("Execution de la recherche TRJ");
		try {
			// Split du trajet en villes
			localisations = localisation.split(";");
			result = Strategy.CODE_OK;
			Facade facade;
			for (int i = 0; i < localisations.length; i++) {
				// Initialisation de la facade
				facade = new FacadeMeteo(localisations[i]);
				
				// Recuperation des parametres de recherche
				int tempMin = 10;
				if (!facade.getTemperatureMin(day).equals("N/A")){
					tempMin = Integer.parseInt(facade.getTemperatureMin(day));
				}
				logger.info("Temperature Min : "+tempMin);
				logger.info("Condition : "+facade.getCondition(day, partOfDay));
				int hail = facade.hasHail(day, partOfDay);
				logger.info("Hail : "+hail);
				int rain = facade.hasRain(day, partOfDay);
				logger.info("Rain : "+rain);
				int snow = facade.hasSnow(day, partOfDay);
				logger.info("Snow : "+snow);
				int fog = facade.hasFog(day, partOfDay);
				logger.info("Snow : "+snow);
				int tstorms = facade.hasTStorms(day, partOfDay);
				logger.info("TStorms : "+tstorms);
				
				// recuperation des parametres de configuration
				int trj_pe_tempMin= SearchConfiguration.getInstance().getParam("trj_pe_tempMin");
				int trj_pe_hail= SearchConfiguration.getInstance().getParam("trj_pe_hail");
				int trj_pe_rain= SearchConfiguration.getInstance().getParam("trj_pe_rain");
				int trj_pe_fog= SearchConfiguration.getInstance().getParam("trj_pe_fog");
				
				int trj_non_rain= SearchConfiguration.getInstance().getParam("trj_non_rain");
				int trj_non_tstorms= SearchConfiguration.getInstance().getParam("trj_non_tstorms");
				int trj_non_fog= SearchConfiguration.getInstance().getParam("trj_non_fog");
				int trj_non_hail= SearchConfiguration.getInstance().getParam("trj_non_hail");
				
				// Cas PE
				if (tempMin <= trj_pe_tempMin || hail == trj_pe_hail || rain == trj_pe_rain || fog == trj_pe_fog || partOfDay == "n") {
					if (result < 1) result = Strategy.CODE_PE;
				}
				// Cas NON
				if (partOfDay == "n" && rain == trj_non_rain) result = Strategy.CODE_NON;
				// Cas NON
				if ( tstorms == trj_non_tstorms || fog == trj_non_fog || hail == trj_non_hail ) {
					result = Strategy.CODE_NON;
				}
			
			}
			
		} catch (Exception e){
			// Cas ERREUR
			result = Strategy.CODE_ERREUR;
		}
		
		end = System.currentTimeMillis();
		
		logger.info("Resultat de la recherche TRJ : "+result);
		logger.info("Temps d'execution de la recherche "+(end-begin)+"ms");
		
		return result;
	}

	/**
	 * @return temps d'execution de la strategie
	 */
	public long getExecutionTime() {
		return end-begin;
	}

}
