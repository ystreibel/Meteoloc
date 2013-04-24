package com.meteoloc.services.strategy.all;

import com.meteoloc.services.facade.Facade;
import com.meteoloc.services.facade.FacadeMeteo;
import com.meteoloc.services.strategy.Strategy;
import com.meteoloc.services.utils.SearchConfiguration;

/**
 * 
 * Strategie Regarder les etoiles - elle effectue la recherche aupres de la facade des differents parametres et effectue les tests necessaire pour retourner le code de retour de la strategie
 * 0: Ok
 * 1: Peut etre
 * 2: Non ok
 * 3: Erreur
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class STASearch extends Strategy{
	
	/**
	 * Methode d'appel de la strategie
	 * @return code de retour de la strategie 0:OK 1:PE 2:NON 3:ERREUR
	 */
	public int execute() {		
		begin = System.currentTimeMillis();
		logger.info("Execution de la recherche STA");
		try {
			// Instanciation de la facade
			Facade facade = new FacadeMeteo(localisation);
			// Recuperation des parametres de recherche
			double CurrentVisibility = Double.parseDouble(facade.getCCVisibility());
			logger.info("Current visibility : "+CurrentVisibility);
				
			logger.info("Condition : "+facade.getCondition(day, partOfDay));
			int hail = facade.hasHail(day, partOfDay);
			logger.info("Hail : "+hail);
			int rain = facade.hasRain(day, partOfDay);
			logger.info("Rain : "+rain);
			int snow = facade.hasSnow(day, partOfDay);
			logger.info("Snow : "+snow);
			int tstorms = facade.hasTStorms(day, partOfDay);
			logger.info("TStorms : "+tstorms);
			int cloud = facade.hasCloud(day, partOfDay);
			logger.info("Cloud : "+cloud);
			int fog = facade.hasFog(day, partOfDay);
			logger.info("Fog : "+fog);
			
			// Recuperation des parametres de recherche	
			int sta_oui_hail= SearchConfiguration.getInstance().getParam("sta_oui_hail");
			int sta_oui_rain= SearchConfiguration.getInstance().getParam("sta_oui_rain");
			int sta_oui_snow= SearchConfiguration.getInstance().getParam("sta_oui_snow");
			int sta_oui_tstorms= SearchConfiguration.getInstance().getParam("sta_oui_tstorms");
			int sta_oui_cloud= SearchConfiguration.getInstance().getParam("sta_oui_cloud");
			int sta_oui_fog= SearchConfiguration.getInstance().getParam("sta_oui_fog");
			int sta_oui_Cvisibility= SearchConfiguration.getInstance().getParam("sta_oui_Cvisibility");

			int sta_pe_hail= SearchConfiguration.getInstance().getParam("sta_pe_hail");
			int sta_pe_snow= SearchConfiguration.getInstance().getParam("sta_pe_snow");
			int sta_pe_rain= SearchConfiguration.getInstance().getParam("sta_pe_rain");
			int sta_pe_tstorms= SearchConfiguration.getInstance().getParam("sta_pe_tstorms");
			int sta_pe_cloud= SearchConfiguration.getInstance().getParam("sta_pe_cloud");
			int sta_pe_fog= SearchConfiguration.getInstance().getParam("sta_pe_fog");
			
			// Cas OK
			if ( hail == sta_oui_hail && rain == sta_oui_rain && snow == sta_oui_snow 
					&& tstorms == sta_oui_tstorms 
					&& cloud == sta_oui_cloud 
					&& fog == sta_oui_fog 
					&& CurrentVisibility >= sta_oui_Cvisibility) result = Strategy.CODE_OK;
			// Cas PE
			else if (hail == sta_pe_hail 
					&& rain <= sta_pe_rain 
					&& snow == sta_pe_snow 
					&& tstorms == sta_pe_tstorms 
					&& cloud <=sta_pe_cloud 
					&& fog == sta_pe_fog) result = Strategy.CODE_PE;
			// Cas NON
			else result = Strategy.CODE_NON;
			
		} catch (Exception e){
			// Cas ERREUR
			result = Strategy.CODE_ERREUR;
			e.getStackTrace();
			logger.error(e.getMessage());
		}
		
		end = System.currentTimeMillis();
		
		logger.info("Resultat de la recherche STA : "+result);
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
