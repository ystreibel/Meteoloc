package com.meteoloc.services.strategy.all;

import com.meteoloc.services.facade.Facade;
import com.meteoloc.services.facade.FacadeMeteo;
import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.utils.SearchConfiguration;


/**
 * Strategie Bronzage - elle effectue la recherche aupres de la facade des differents parametres et effectue les tests necessaire pour retourner le code de retour de la strategie
 * 0: Ok
 * 1: Peut etre
 * 2: Non ok
 * 3: Erreur
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class BRZSearch extends Strategy {

	/**
	 * Methode d'appel de la strategie
	 * @return code de retour de la strategie 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public int execute() {
		begin = System.currentTimeMillis();
		logger.info("Execution de la recherche BRZ");
		try {
			// Instanciation de la facade
			Facade facade = new FacadeMeteo(localisation);
			// Recuperation des differentes variables
			int windmesure = 0;
			if (!facade.getSpeedWind(day, partOfDay).equals("N/A")){
				windmesure = Integer.parseInt(facade.getSpeedWind(day, partOfDay));
			}
			logger.info("Wind mesure : "+windmesure);
			int tempMin = 0;
			if (!facade.getTemperatureMin(day).equals("N/A")){
				tempMin = Integer.parseInt(facade.getTemperatureMin(day));
			}
			
			logger.info("Temperature Min : "+facade.getTemperatureMin(day));
			logger.info("Condition : "+facade.getCondition(day, partOfDay));
			int hail = facade.hasHail(day, partOfDay);
			logger.info("Hail : "+hail);
			int rain = facade.hasRain(day, partOfDay);
			logger.info("Rain : "+rain);
			int snow = facade.hasSnow(day, partOfDay);
			logger.info("Snow : "+snow);
			int tstorms = facade.hasTStorms(day, partOfDay);
			logger.info("TStorms : "+tstorms);
			int sun = facade.hasSun(day, partOfDay);
			logger.info("Sun : "+sun);
			int uv = -1;
			
			if (!facade.getCCIndexUV().equals("N/A")){
				if (day == 0) uv = Integer.parseInt(facade.getCCIndexUV());
			}
			logger.info("uv : "+uv);
			
			// Recuperation des differents parametres pour configurer la recherche par fichier de configuration
			int brz_oui_tempMin =  SearchConfiguration.getInstance().getParam("brz_oui_tempMin");
			int brz_oui_windMax =  SearchConfiguration.getInstance().getParam("brz_oui_windMax");
			int brz_oui_hail =  SearchConfiguration.getInstance().getParam("brz_oui_hail");
			int brz_oui_rain =  SearchConfiguration.getInstance().getParam("brz_oui_rain");
			int brz_oui_snow =  SearchConfiguration.getInstance().getParam("brz_oui_snow");
			int brz_oui_tstorms =  SearchConfiguration.getInstance().getParam("brz_oui_tstorms");
			int brz_oui_maxUv =  SearchConfiguration.getInstance().getParam("brz_oui_maxUv");
			
			int brz_pe_tempMin =  SearchConfiguration.getInstance().getParam("brz_pe_tempMin");
			int brz_pe_windMin =  SearchConfiguration.getInstance().getParam("brz_pe_windMin");
			int brz_pe_windMax =  SearchConfiguration.getInstance().getParam("brz_pe_windMax");
			int brz_pe_hail =  SearchConfiguration.getInstance().getParam("brz_pe_hail");
			int brz_pe_rain =  SearchConfiguration.getInstance().getParam("brz_pe_rain");
			int brz_pe_snow =  SearchConfiguration.getInstance().getParam("brz_pe_snow");
			int brz_pe_tstorms =  SearchConfiguration.getInstance().getParam("brz_pe_tstorms");
			int brz_pe_maxUv =  SearchConfiguration.getInstance().getParam("brz_pe_maxUv");
			
			// Cas OK
			if (tempMin >= brz_oui_tempMin && windmesure <= brz_oui_windMax && 
					hail == brz_oui_hail && 
					rain == brz_oui_rain &&
					snow == brz_oui_snow &&
					tstorms == brz_oui_tstorms
					&& uv <= brz_oui_maxUv)
				result = Strategy.CODE_OK;
			// Cas PE
			else if (tempMin >= brz_pe_tempMin && windmesure >= brz_pe_windMin && windmesure <= brz_pe_windMax &&
					hail <= brz_pe_hail && 
					rain <= brz_pe_rain &&
					snow <= brz_pe_snow &&
					tstorms <= brz_pe_tstorms &&
					uv <= brz_pe_maxUv) result = Strategy.CODE_PE;
			// Cas NON
			else result = Strategy.CODE_NON;
			
		} catch (Exception e){
			// Cas ERREUR
			result = Strategy.CODE_ERREUR;
		}
		
		end = System.currentTimeMillis();
		
		logger.info("Resultat de la recherche BRZ : "+result);
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