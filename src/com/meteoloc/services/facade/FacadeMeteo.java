package com.meteoloc.services.facade;

import org.apache.log4j.Logger;

import com.meteoloc.services.facade.connectors.NoResultsException;
import com.meteoloc.services.facade.connectors.local.GoogleMap;
import com.meteoloc.services.facade.connectors.meteo.Weather;

/**
 * 
 * Facade d'acces aux differents webservices de localisation et de meteo.
 * 
 *  Cette facade a pour objectif de fournir aux strategies un point d'entree centralise aux differents webservices.
 *  Elle permet aussi dans le cadre du rajout d'un nouveau webservice de le configurer dans cette classe, et de l'utiliser sans modifier les strategies.
 *  On peut aussi mettre les resultats de differents webservices en commun avec cette classe.
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class FacadeMeteo extends Facade{
	private static final Logger logger = Logger.getLogger("FacadeMeteo.class");
	private GoogleMap gmap_instance;
	private Weather wcom_instance;
	public  String localisation;

	/**
	 * Constructeur de notre facade, elle instancie les differents webservices necessaire a son fonctionnement.
	 * @param localisation
	 * @throws NoResultsException
	 */
	public FacadeMeteo(String localisation) throws NoResultsException {
		try {
			gmap_instance = new GoogleMap(localisation);
			wcom_instance = new Weather (gmap_instance.getLocalityName());
		} catch (NoResultsException e) {
			logger.error("Aucune donne pour la localisation "+localisation);
			throw e;
		}
		this.localisation = localisation;
	}


	public String getCondition(int jour, String partOfDay) {
		if (jour==0 && partOfDay.equals("d") && wcom_instance.getCondition(jour, partOfDay).equals("N/A")) return wcom_instance.getCCCondition();
		return wcom_instance.getCondition(jour, partOfDay);
	}


	public String getDirectionWindMesure(int jour, String partOfDay) {
		if (jour==0 && partOfDay.equals("d") && wcom_instance.getCondition(jour, partOfDay).equals("N/A")) return wcom_instance.getCCDirectionWindMeasure();
		return wcom_instance.getDirectionWindMesure(jour, partOfDay);
	}


	public String getDirectionWindText(int jour, String partOfDay) {
		if (jour==0 && partOfDay.equals("d") && wcom_instance.getCondition(jour, partOfDay).equals("N/A")) return wcom_instance.getCCDirectionWindText();
		return wcom_instance.getDirectionWindText(jour, partOfDay);
	}

	public long getExecutionTime() {
		return 0;
	}


	public String getGust(int jour, String partOfDay) {
		if (jour==0 && partOfDay.equals("d") && wcom_instance.getCondition(jour, partOfDay).equals("N/A")) return wcom_instance.getCCGust();
		return wcom_instance.getGust(jour, partOfDay);
	}


	public String getHumidity(int jour, String partOfDay) {
		if (jour==0 && partOfDay.equals("d") && wcom_instance.getCondition(jour, partOfDay).equals("N/A")) return wcom_instance.getCCHumidity();
		return wcom_instance.getHumidity(jour, partOfDay);
	}


	public String getPrecipitationChance(int jour, String partOfDay) {
		return wcom_instance.getPrecipitationChance(jour, partOfDay);
	}


	public String getSpeedWind(int jour, String partOfDay) {
		if (jour==0 && partOfDay.equals("d") && wcom_instance.getCondition(jour, partOfDay).equals("N/A")) return wcom_instance.getCCSpeedWind();
		return wcom_instance.getSpeedWind(jour, partOfDay);
	}


	public String getSunrise(int jour) {
		return wcom_instance.getSunrise(jour);
	}


	public String getSunset(int jour) {
		return wcom_instance.getSunset(jour);
	}


	public String getTemperatureMax(int jour) {
		return wcom_instance.getTemperatureMax(jour);
	}


	public String getTemperatureMin(int jour) {
		return wcom_instance.getTemperatureMin(jour);
	}


	public String getAccuracy() {
		return gmap_instance.getAccuracy();
	}


	public String getAddress() {
		return gmap_instance.getAddress();
	}


	public String getAdministrativeAreaName() {
		return gmap_instance.getAdministrativeAreaName();
	}


	public String getCoordinates() {
		return gmap_instance.getCoordinates();
	}


	public String getCountryName() {
		return gmap_instance.getCountryName();
	}


	public String getCountryNameCode() {
		return gmap_instance.getCountryNameCode();
	}


	public String[] getLatLonBox() {
		return gmap_instance.getLatLonBox();
	}


	public String getLocalityName() {
		return gmap_instance.getLocalityName();
	}


	public String getStatus_code() {
		return gmap_instance.getStatus_code();
	}


	public String getStatus_request() {
		return gmap_instance.getStatus_code();
	}


	public String getSubAdministrativeAreaName() {
		return gmap_instance.getSubAdministrativeAreaName();
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de neige
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasSnow (int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("snow")){
			if (condition.contains("showers") || 
					condition.contains("heavy") ) return 2;
			else if (condition.contains("drifting") || 
					condition.contains("light")|| 
					condition.contains("drizzle")  ) return 1;
			else return 2;
		}
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de pluie
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasRain (int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("rain") || condition.contains("showers")){
			if (condition.contains("showers") || 
					condition.contains("heavy") ) return 2;
			else if (condition.contains("drifting") || 
					condition.contains("light")|| 
					condition.contains("drizzle")  ) return 1;
			else return 2;
		}
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau d'orages
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasTStorms(int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("t-storms") || condition.contains("thunder")){
			if (condition.contains("showers") || 
					condition.contains("heavy") ) return 2;
			else if (condition.contains("drifting") || 
					condition.contains("light")|| 
					condition.contains("drizzle") || 
					condition.contains("scattered") ) return 1;
			else return 2;
		}
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de grele
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasHail(int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("hail") || condition.contains("ice pellets")) return 2;
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de brouillard
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasFog(int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("fog")) return 2;
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de sable
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasSand(int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("sand")) return 2;
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de nuages
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasCloud(int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("cloud") || condition.contains("overcast")){
			if (condition.contains("mostly") || 
					condition.contains("heavy") ) return 2;
			else if (condition.contains("p ") || 
					condition.contains("partly")|| 
					condition.contains("a few")  ) return 1;
			else return 2;
		}
		return 0;
	}

	/**
	 * Conditions meteo
	 * Cette méthode indique le niveau de soleil
	 * 
	 * @param jour
	 * @param partOfDay
	 * @return 0 si non, 1 si leger 2 si beaucoup
	 */
	public int hasSun(int jour, String partOfDay){ 
		String condition =  wcom_instance.getCondition(jour, partOfDay).toLowerCase();
		if (condition.contains("sun")){
			if (condition.contains("mostly") ) return 2;
			else if (condition.contains("partly")  ) return 1;
			else return 2;
		}
		return 0;
	}


	public String getCCBarPressionMeasure() {
		return wcom_instance.getCCBarPressionMeasure();
	}


	public String getCCBarPressionText() {
		return wcom_instance.getCCBarPressionText();
	}


	public String getCCCondition() {
		return wcom_instance.getCCCondition();
	}


	public String getCCDewPoint() {
		return wcom_instance.getCCDewPoint();
	}


	public String getCCDirectionWindMeasure() {
		return wcom_instance.getCCDirectionWindMeasure();
	}


	public String getCCDirectionWindText() {
		return wcom_instance.getCCDirectionWindText();
	}


	public String getCCFlick() {
		return wcom_instance.getCCFlick();
	}


	public String getCCGust() {
		return wcom_instance.getCCGust();
	}


	public String getCCHumidity() {
		return wcom_instance.getCCHumidity();
	}


	public String getCCIndexUV() {
		return wcom_instance.getCCIndexUV();
	}


	public String getCCIndextUVDescription() {
		return wcom_instance.getCCIndextUVDescription();
	}


	public String getCCSpeedWind() {
		return wcom_instance.getCCSpeedWind();
	}


	public String getCCTemperature() {
		return wcom_instance.getCCTemperature();
	}


	public String getCCVisibility() {
		return wcom_instance.getCCVisibility();
	}

}
