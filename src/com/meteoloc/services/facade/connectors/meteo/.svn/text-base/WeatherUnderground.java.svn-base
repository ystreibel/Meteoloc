package com.meteoloc.services.facade.connectors.meteo;

import java.net.URL;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * 
 * Classe d'acces au webservice de localisation de weatherUnderground, nous utilisons l'API de geocoding et de meteo de weatherUnderground .
 * Cette api permet de structurer une adresse passee en parametre pour trouver la station meteo correspondante, de la standardiser puis de rechercher la meteo pour la station meteo trouvee.
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 *
 */
public class WeatherUnderground implements ConnecteurMeteo{
	private int[] lowTemp;
	private int[] highTemp;
	private String[] condition;

	public final String base_url = "http://api.wunderground.com/auto/wui/geo/ForecastXML/index.xml?query=";
	private static final Logger logger = Logger.getLogger("WeatherUnderground.class");
	// Temps d'acces
	private long begin;
	private long end;

	/**
	 * Constructeur du connecteur Meteo WeatherUndeground,
	 * 		Ce webservice permet de recuperer sur les 3 prochains jours:
	 * 			- la temperature minimum.
	 * 			- la temperature maximum.
	 * 			- les conditions meteo du jour.
	 * Dans ce constructeur l'appel au ws est effectue, l'appel est traite et les variables sont stoques.
	 * @param localisation localisation de l'appel du ws
	 */
	public WeatherUnderground(String localisation) {
		begin = System.currentTimeMillis();
		lowTemp = new int[3];
		highTemp = new int[3];
		condition = new String[3];
		try {
			URL url = new URL(base_url+localisation);
			// Creation du parser
			SAXBuilder sxb = new SAXBuilder();
			// Parsing de l'url
			Document document = sxb.build(url);
			Element root = document.getRootElement();
			root.getChild("simpleforecast");
			// Parcours du flux rss
			for (Iterator<Element> iterator = root.getChild("simpleforecast").getChildren().iterator(); iterator.hasNext();) {
				Element forecastday = (Element) iterator.next();
				int currentPeriod = Integer.parseInt(forecastday.getChildText("period"));
				if (currentPeriod <= 3) {
					highTemp[currentPeriod-1] = Integer.parseInt(forecastday.getChild("high").getChildText("celsius"));
					lowTemp[currentPeriod-1] = Integer.parseInt(forecastday.getChild("low").getChildText("celsius"));
					condition[currentPeriod-1] = forecastday.getChildText("conditions");
				}
					
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		end = System.currentTimeMillis();
	}
	
	
	/**
	 * Methode permettant de recuperer la temperature minimale du jour day
	 * @param day est un jour compris entre 0 et 2 representant les jour e partir d'aujourd'hui. Aujourd'hui est represente par 0, demain par 1, apres-demain par 2.
	 * @return la temperature en e Celcius
	 */
	public String getTemperatureMin(int day) {
		if (day >= 0 && day <= 2) return ""+lowTemp[day];
		return "N/A";
	}
	
	/**
	 * Methode permettant de recuperer la temperature maximale du jour day.
	 * @param day est un jour compris entre 0 et 2 representant les jour e partir d'aujourd'hui. Aujourd'hui est represente par 0, demain par 1, apres-demain par 2.
	 * @return la temperature en e Celcius.
	 */
	public String getTemperatureMax(int day) {
		if (day >= 0 && day <= 2) return ""+highTemp[day];
		return "N/A";
	}
	
	/**
	 * Methode permettant de recuperer la temperature maximale du jour day.
	 * @param day est un jour compris entre 0 et 2 representant les jour e partir d'aujourd'hui. Aujourd'hui est represente par 0, demain par 1, apres-demain par 2.
	 * @return La condition meteo du jour.
	 * différentes conditions meteo possibles
	 * Chance of Flurries
	 * Chance of Rain
	 * Chance of Freezing Rain
	 * Chance of Sleet
	 * Chance of Snow
	 * Chance of Thunderstorms
	 * Chance of a Thunderstorm
	 * Clear
	 * Cloudy
	 * Flurries
	 * Fog
	 * Haze
	 * Mostly Cloudy
	 * Mostly Sunny
	 * Partly Cloudy
	 * Partly Sunny
	 * Freezing Rain
	 * Rain
	 * Sleet
	 * Snow
	 * Sunny
	 * Thunderstorms
	 * Thunderstorm
	 * Unknown
	 * Overcast
	 * Scattered Clouds
	 */
	public String getCondition(int day,String partOfDay) {
		if (day >= 0 && day <= 2) return condition[day];
		return "N/A";
	}
	
	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.meteo.ConnecteurMeteo#getExecutionTime()
	 */
	public long getExecutionTime() {
		return end-begin;
	}


	@Override
	public String getDirectionWindMesure(int jour, String partOfDay) {
		return "N/A";
	}


	@Override
	public String getDirectionWindText(int jour, String partOfDay) {
		return "N/A";
	}


	@Override
	public String getGust(int jour, String partOfDay) {
		return "N/A";
	}


	@Override
	public String getHumidity(int jour, String partOfDay) {
		return "N/A";
	}


	@Override
	public String getPrecipitationChance(int jour, String partOfDay) {
		return "N/A";
	}


	@Override
	public String getSpeedWind(int jour, String partOfDay) {
		return "N/A";
	}


	@Override
	public String getSunrise(int jour) {
		return "N/A";
	}


	@Override
	public String getSunset(int jour) {
		return "N/A";
	}

	@Override
	public String getCCBarPressionMeasure() {
		return "N/A";
	}

	@Override
	public String getCCBarPressionText() {
		return "N/A";
	}

	@Override
	public String getCCCondition() {
		return "N/A";
	}

	@Override
	public String getCCDewPoint() {
		return "N/A";
	}

	@Override
	public String getCCDirectionWindMeasure() {
		return "N/A";
	}

	@Override
	public String getCCDirectionWindText() {
		return "N/A";
	}

	@Override
	public String getCCFlick() {
		return "N/A";
	}

	@Override
	public String getCCGust() {
		return "N/A";
	}

	@Override
	public String getCCHumidity() {
		return "N/A";
	}

	@Override
	public String getCCIndexUV() {
		return "N/A";
	}

	@Override
	public String getCCIndextUVDescription() {
		return "N/A";
	}

	@Override
	public String getCCSpeedWind() {
		return "N/A";
	}

	@Override
	public String getCCTemperature() {
		return "N/A";
	}

	@Override
	public String getCCVisibility() {
		return "N/A";
	}


}