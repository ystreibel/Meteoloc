package com.meteoloc.services.strategy.all;

import com.meteoloc.services.facade.Facade;
import com.meteoloc.services.facade.FacadeMeteo;
import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.utils.SearchConfiguration;

/**
 * 
 * Strategie Sechage de linge - elle effectue la recherche aupres de la facade des differents parametres et effectue les tests necessaire pour retourner le code de retour de la strategie
 * 0: Ok
 * 1: Peut etre
 * 2: Non ok
 * 3: Erreur
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class DRYSearch extends Strategy {

	/**
	 * Methode d'appel de la strategie
	 * @return code de retour de la strategie 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public int execute() {
		begin = System.currentTimeMillis();
		logger.info("Execution de la recherche DRY");
		try {
			// Instanciation de la facade
			Facade facade = new FacadeMeteo(localisation);
			// Recuperation des donnees du service
			int windmesure = 0;
			if (!facade.getSpeedWind(day, partOfDay).equals("N/A")){
				windmesure = Integer.parseInt(facade.getSpeedWind(day, partOfDay));
			}
			logger.info("Wind mesure : "+windmesure);
			int tempMin = 10; // tout le temps bonne si N/A
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

			// Recuperation des parametres de recherche
			int dry_oui_tempMin1 =  SearchConfiguration.getInstance().getParam("dry_oui_tempMin1");
			int dry_oui_wind1Max= SearchConfiguration.getInstance().getParam("dry_oui_wind1Max");
			int dry_oui_tempMin2= SearchConfiguration.getInstance().getParam("dry_oui_tempMin2");
			int dry_oui_wind2Min= SearchConfiguration.getInstance().getParam("dry_oui_wind2Min");
			int dry_oui_wind2Max= SearchConfiguration.getInstance().getParam("dry_oui_wind2Max");
			int dry_oui_hail= SearchConfiguration.getInstance().getParam("dry_oui_hail");
			int dry_oui_rain= SearchConfiguration.getInstance().getParam("dry_oui_rain");
			int dry_oui_snow= SearchConfiguration.getInstance().getParam("dry_oui_snow");
			int dry_oui_tstorms= SearchConfiguration.getInstance().getParam("dry_oui_tstorms");

			int dry_pe_tempMin1= SearchConfiguration.getInstance().getParam("dry_pe_tempMin1");
			int dry_pe_windMin1= SearchConfiguration.getInstance().getParam("dry_pe_windMin1");
			int dry_pe_windMax1= SearchConfiguration.getInstance().getParam("dry_pe_windMax1");
			int dry_pe_hail= SearchConfiguration.getInstance().getParam("dry_pe_hail");
			int dry_pe_snow= SearchConfiguration.getInstance().getParam("dry_pe_snow");
			int dry_pe_rain= SearchConfiguration.getInstance().getParam("dry_pe_rain");
			int dry_pe_tstorms= SearchConfiguration.getInstance().getParam("dry_pe_tstorms");
			// Cas OK
			if (((tempMin >= dry_oui_tempMin1 && windmesure <= dry_oui_wind1Max) ||
					(tempMin >= dry_oui_tempMin2 && windmesure >= dry_oui_wind2Min && windmesure <= dry_oui_wind2Max)) && 
					hail == dry_oui_hail && 
					rain == dry_oui_rain &&
					snow == dry_oui_snow &&
					tstorms == dry_oui_tstorms)
				result = Strategy.CODE_OK;
			// Cas PE
			else if (tempMin >= dry_pe_tempMin1 && windmesure >= dry_pe_windMin1 && windmesure <= dry_pe_windMax1 &&
					hail <= dry_pe_hail &&
					rain <= dry_pe_snow &&
					snow <= dry_pe_rain &&
					tstorms <= dry_pe_tstorms) result = Strategy.CODE_PE;
			// Cas NON
			else result = Strategy.CODE_NON;

		} catch (Exception e){
			// Cas ERREUR
			result = Strategy.CODE_ERREUR;
		}

		end = System.currentTimeMillis();

		logger.info("Resultat de la recherche DRY : "+result);
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