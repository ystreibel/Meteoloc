package com.meteoloc.services.strategy.all;

import com.meteoloc.services.facade.Facade;
import com.meteoloc.services.facade.FacadeMeteo;
import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.utils.SearchConfiguration;

/**
 *
 * Strategie Cerf-Volant - elle effectue la recherche aupres de la facade des differents parametres et effectue les tests necessaire pour retourner le code de retour de la strategie
 * 0: Ok
 * 1: Peut etre
 * 2: Non ok
 * 3: Erreur
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class CVLSearch extends Strategy {

	/**
	 * Methode d'appel de la strategie
	 * @return code de retour de la strategie 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public int execute() {		
		begin = System.currentTimeMillis();
		logger.info("Execution de la recherche CVL");
		try {
			// Instanciation de la facade
			Facade facade = new FacadeMeteo(localisation);
			// Recuperation des variables de recherche
			int windmesure = 7;
			if (!facade.getSpeedWind(day, partOfDay).equals("N/A")){
				windmesure = Integer.parseInt(facade.getSpeedWind(day, partOfDay));
			}
			logger.info("Wind mesure : "+windmesure);
			logger.info("Condition : "+facade.getCondition(day, partOfDay));
			int hail = facade.hasHail(day, partOfDay);
			logger.info("Hail : "+hail);
			int rain = facade.hasRain(day, partOfDay);
			logger.info("Rain : "+rain);
			int snow = facade.hasSnow(day, partOfDay);
			logger.info("Snow : "+snow);
			int tstorms = facade.hasTStorms(day, partOfDay);
			logger.info("TStorms : "+tstorms);
			

			// Recuperation des parametres de recherche du fichier de configuration de strategies
			int cvl_oui_windMin =  SearchConfiguration.getInstance().getParam("cvl_oui_windMin");
			int cvl_oui_windMax= SearchConfiguration.getInstance().getParam("cvl_oui_windMax");
			int cvl_oui_hail= SearchConfiguration.getInstance().getParam("cvl_oui_hail");
			int cvl_oui_rain= SearchConfiguration.getInstance().getParam("cvl_oui_rain");
			int cvl_oui_snow= SearchConfiguration.getInstance().getParam("cvl_oui_snow");
			int cvl_oui_tstorms= SearchConfiguration.getInstance().getParam("cvl_oui_tstorms");

			int cvl_pe_windMin= SearchConfiguration.getInstance().getParam("cvl_pe_windMin");
			int cvl_pe_windMax= SearchConfiguration.getInstance().getParam("cvl_pe_windMax");
			int cvl_pe_hail= SearchConfiguration.getInstance().getParam("cvl_pe_hail");
			int cvl_pe_rain= SearchConfiguration.getInstance().getParam("cvl_pe_rain");
			int cvl_pe_snow= SearchConfiguration.getInstance().getParam("cvl_pe_snow");
			int cvl_pe_tstorms= SearchConfiguration.getInstance().getParam("cvl_pe_tstorms");
			
			// Cas OK
			if ( windmesure >= cvl_oui_windMin &&
					windmesure <= cvl_oui_windMax &&
					hail == cvl_oui_hail &&
					rain == cvl_oui_rain &&
					snow == cvl_oui_snow &&
					tstorms == cvl_oui_tstorms) result = Strategy.CODE_OK;
			// Cas PE
			else if (windmesure >= cvl_pe_windMin &&
					windmesure <= cvl_pe_windMax &&
					hail <= cvl_pe_hail &&
					rain <= cvl_pe_rain &&
					snow == cvl_pe_snow &&
					tstorms == cvl_pe_tstorms) result = Strategy.CODE_PE;
			// Cas NON
			else result = Strategy.CODE_NON;
			
		} catch (Exception e){
			// Cas ERREUR
			result = Strategy.CODE_ERREUR;
		}
		
		end = System.currentTimeMillis();
		
		logger.info("Resultat de la recherche CVL : "+result);
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
