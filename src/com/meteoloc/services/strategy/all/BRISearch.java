package com.meteoloc.services.strategy.all;

import com.meteoloc.services.facade.Facade;
import com.meteoloc.services.facade.FacadeMeteo;
import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.utils.SearchConfiguration;


/**
 * 
 * Strategie Bricolage - elle effectue la recherche aupres de la facade des differents parametres et effectue les tests necessaire pour retourner le code de retour de la strategie
 * 0: Ok
 * 1: Peut etre
 * 2: Non ok
 * 3: Erreur
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class BRISearch extends Strategy{

	/**
	 * Methode d'appel de la strategie
	 * @return code de retour de la strategie 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public int execute() {

		begin = System.currentTimeMillis();
		logger.info("Execution de la recherche BRI");
		
		try {
			// Creation de la facade
			Facade facade = new FacadeMeteo(localisation);
			int tempMax=0;
			int tempMin=0;
			if (!facade.getTemperatureMax(day).equals("N/A")){
				tempMax = Integer.parseInt(facade.getTemperatureMax(day));
			}
			if (!facade.getTemperatureMin(day).equals("N/A")){
				tempMin = Integer.parseInt(facade.getTemperatureMin(day));
			}
			logger.info(facade.getCondition(day, partOfDay));
			logger.info("Temperature Min : "+facade.getTemperatureMin(day));
			logger.info("Temperature Max : "+facade.getTemperatureMax(day));
			logger.info("Condition : "+facade.getCondition(day, partOfDay));
			int hail = facade.hasHail(day, partOfDay);
			logger.info("Hail : "+hail);
			int rain = facade.hasRain(day, partOfDay);
			logger.info("Rain : "+rain);
			int snow = facade.hasSnow(day, partOfDay);
			logger.info("Snow : "+snow);
			int tstorms = facade.hasTStorms(day, partOfDay);
			logger.info("TStorms : "+tstorms);
			
			
			// Recuperation des parametres de recherche
			int bri_oui_tempMin =  SearchConfiguration.getInstance().getParam("bri_oui_tempMin");
			int bri_oui_tempMax =  SearchConfiguration.getInstance().getParam("bri_oui_tempMax");
			int bri_oui_hail =  SearchConfiguration.getInstance().getParam("bri_oui_hail");
			int bri_oui_rain =  SearchConfiguration.getInstance().getParam("bri_oui_rain");
			int bri_oui_snow =  SearchConfiguration.getInstance().getParam("bri_oui_snow");
			int bri_oui_tstorms =  SearchConfiguration.getInstance().getParam("bri_oui_tstorms");
			
			int bri_pe_tempMin =  SearchConfiguration.getInstance().getParam("bri_pe_tempMin");
			int bri_pe_tempMax =  SearchConfiguration.getInstance().getParam("bri_pe_tempMax");
			int bri_pe_hail =  SearchConfiguration.getInstance().getParam("bri_pe_hail");
			int bri_pe_rain =  SearchConfiguration.getInstance().getParam("bri_pe_rain");
			int bri_pe_snow =  SearchConfiguration.getInstance().getParam("bri_pe_snow");
			int bri_pe_tstorms =  SearchConfiguration.getInstance().getParam("bri_pe_tstorms");
			
			// Cas OK		
			if (tempMin >= bri_oui_tempMin && tempMax <= bri_oui_tempMax && 
					hail == bri_oui_hail && 
					rain == bri_oui_rain &&
					snow == bri_oui_snow &&
					tstorms == bri_oui_tstorms) result = Strategy.CODE_OK;
			// Cas PE
			else if (tempMin > bri_pe_tempMin && tempMax <= bri_pe_tempMax &&
					hail <= bri_pe_hail && 
					rain <= bri_pe_rain &&
					snow <= bri_pe_snow &&
					tstorms <= bri_pe_tstorms) result = Strategy.CODE_PE;
			// Cas NON
			else result = Strategy.CODE_NON;
			
		} catch (Exception e){
			// Cas ERREUR
			result = Strategy.CODE_ERREUR;
		}
		
		end = System.currentTimeMillis();
		
		logger.info("Resultat de la recherche BRI : "+result);
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
