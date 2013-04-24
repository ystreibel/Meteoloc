package com.meteoloc.services.facade.connectors.meteo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.meteoloc.services.facade.connectors.NoResultsException;
import com.meteoloc.services.utils.ServiceConfiguration;

/**
 * Classe d'acces au webservice de localisation de weather.com, nous utilisons l'API de geocoding et de meteo de weather.com .
 * Cette api permet de structurer une adresse passee en parametre pour trouver la station meteo correspondante, de la standardiser puis de rechercher la meteo pour la station meteo trouvee.
 * 
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 *
 */
public class Weather implements ConnecteurMeteo{
	/**
	 * Url permettant d'interroger le webservice meteoloc afin de recuperer les donnée meteo 
	 */
	public final String weather_base_url = "http://xoap.weather.com/weather/local/";
	/**
	 * Url de geocoding, permettant de nous donner le code correspondant a une ville donnee
	 */
	public final String search_base_url = "http://xoap.weather.com/search/search?where=";
	/**
	 * Definition du logger de la classe pour le log4j
	 */
	private static final Logger logger = Logger.getLogger("Weather.class");
	/**
	 * Code de la ville
	 */
	private String cityCode;
	/**
	 * Tableau des jours
	 */
	private Element[] jours;
	/**
	 * Les conditions courantes
	 */
	private Element cc;
	/**
	 *  Temps d'acces
	 */
	private long begin;
	private long end;
	
	/**
	 * Constructor
	 * @param localisation
	 * @throws NoResultsException
	 */
	public Weather(String localisation) throws NoResultsException{
		begin = System.currentTimeMillis();
		try {		
			//Recuperation du code correspondant a la ville
			URL search_url = new URL(search_base_url+localisation);

			// Creation du parser
			SAXBuilder sxba = new SAXBuilder();
			
			// Parsing de l'url de recherche du code de la ville
			Document document = sxba.build(search_url);
			
			//Variable permettant de creer la requete de recuperation des donnees meteo
			String currentConditions = "cc=*";
			String day = "dayf="+3;
			String par = "par="+ServiceConfiguration.getInstance().getProperty("wcom_partnerID");
			String key = "key="+ServiceConfiguration.getInstance().getProperty("wcom_licenseKey");
			String unit ="unit="+ServiceConfiguration.getInstance().getProperty("wcom_unit");
			
			
			cityCode = document.getRootElement().getChild("loc").getAttribute("id").getValue();
			logger.info(weather_base_url+cityCode+"?"+currentConditions+"&"+day+"&"+par+"&"+key+"&"+unit);
			jours = new Element[3];
		
			URL weather_url = new URL(weather_base_url+cityCode+"?"+currentConditions+"&"+day+"&"+par+"&"+key+"&"+unit);
			// Creation du parser
			SAXBuilder sxbb = new SAXBuilder();
			// Parsing de l'url concernant la meteo
			Document documentb;
			documentb = sxbb.build(weather_url);
			List<Element> items = documentb.getRootElement().getChild("dayf").getChildren();
			for (Element element : items) {
				if(element.getName().contains("day")){
					this.jours[Integer.parseInt(element.getAttributeValue("d"))] = element;
				}
			}
			cc = documentb.getRootElement().getChild("cc");

		}catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}catch (NullPointerException e) {
			if (cityCode == null) logger.error("Il n'y a pas de code correspondant à la ville "+localisation);
			else if (cc == null) logger.error("Localisation non trouvee");
			throw new NoResultsException(e.getMessage());
		}
		end = System.currentTimeMillis();
	}

	/**
	 * Methode de recuperation des conditions meteo courantes
	 * @return Element : Conditions meteo courantes 
	 */
	private Element getCc() {
		return cc;
	}

	/**
	 * Methode donnant la temperature courante
	 * @return String : La temperature courante
	 */
	public String getCCTemperature(){
		String ret="N/A";
		try{
			ret = this.getCc().getChildText("tmp");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de temperature courante");
		}
		return ret;
		
	}
	
	/**
	 * Methode de recuperation de la temperature courante ressentie
	 * @return String : La temperature courante ressentie
	 */
	public String getCCFlick(){
		String ret="N/A";
		try{
			ret = this.getCc().getChildText("flik");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de temperature ressentie courante");
		}
		return ret;
	}
	
	/**
	 * Methode de recuperation de la description de la meteo courante (exemple : cloudy, rain ...)
	 * @return String : La description de la meteo courante
	 */
	public String getCCCondition(){
		String ret="N/A";
		try{
			ret = this.getCc().getChildText("t");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de condition textuelle courante");
		}
		return ret;
	}
	
	/**
	 * Methode permettant de recuperer l'humidite courante
	 * @return String : L'humidite courante
	 */
	public String getCCHumidity(){
		String ret="N/A";
		try{
			ret = this.getCc().getChildText("hmid");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas d'humidite courante");
		}
		return ret;
	}
	
	/**
	 * Methode donnant la visibilite courante
	 * @return String : La visibilite courante
	 */
	public String getCCVisibility(){
		String ret="N/A";
		try{
			ret = this.getCc().getChildText("vis");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de visibilite courante");
		}
		return ret;
	}
	
	/**
	 * Methode permettant de recuperer le point de rosee courant
	 * @return String : Le point de rosee courant
	 */
	public String getCCDewPoint(){
		String ret="N/A";
		try{
			ret = this.getCc().getChildText("dewp");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de point de rosee courante");
		}
		return ret;
	}
	
	/**
	 * Methode de recuperation de la pression atmospherique courante en bar
	 * @return String : La pression atmospherique courante en bar
	 */
	public String getCCBarPressionMeasure(){
		String ret="N/A";
		try{
			ret = this.getCc().getChild("bar").getChildText("r");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de pression courante");
		}
		return ret;
	}
	
	/**
	 * Methode permettant de recuperer la description de la pression atmospherique courante
	 * @return String : La description de la pression atmospherique courante
	 */
	public String getCCBarPressionText(){
		String ret="N/A";
		try{
			ret = this.getCc().getChild("bar").getChildText("d");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de description de pression courante");
		}
		return ret;
    }
	
    /**
     * Methode qui permet de recuperer la vitesse du vent courante en km/h
     * @return String : La vitesse de vent courante
     */
    public String getCCSpeedWind(){
		String ret="N/A";
		try{
			ret = this.getCc().getChild("wind").getChildText("s");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de mesure du vent courante");
		}
		return ret;
    }
    
    /**
     * Methode de recuperation des rafales de vent corantes
     * @return String : Les rafales de vents courantes
     */
    public String getCCGust(){
    	String ret="N/A";
		try{
			ret = this.getCc().getChild("wind").getChildText("gust");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de rafales de vent courantes");
		}
		return ret;
    }
    
    /**
     * Methode donnant la direction du vent courante en degres
     * @return String : La direction du vent courante en degres
     */
    public String getCCDirectionWindMeasure(){
    	String ret="N/A";
		try{
			ret = this.getCc().getChild("wind").getChildText("d");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de direction en degres du sens du vent courant");
		}
		return ret;
    }
    
    /**
     * Methode recuperant la description de la direction du vent courante en texte
     * @return String : La description du vent courante en texte
     */
    public String getCCDirectionWindText(){
    	String ret="N/A";
		try{
			ret = this.getCc().getChild("wind").getChildText("t");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de direction en text du sens du vent courant");
		}
		return ret;
    }
    
    /**
     * Methode de recuperation de l'index UV courant
     * @return String : L'index UV courant
     */
    public String getCCIndexUV(){
    	String ret="N/A";
		try{
			ret = this.getCc().getChild("uv").getChildText("i");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas d'index UV courant");
		}
		return ret;
    }
    
    /**
     * Methode permettant de recuperer la description de l'index UV courant
     * @return String : La description de l'index UV courant
     */
    public String getCCIndextUVDescription(){
    	String ret="N/A";
		try{
			ret = this.getCc().getChild("uv").getChildText("t");
		}catch (NullPointerException e) {
			logger.error("Il n'y a pas de description de l'index UV courant");
		}
		return ret;
    }
	
	/**
	 * Methode permettant de recuperer les donnees meteo d'un jour precis
	 * @param i : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @return Element : Le jour souhaite
	 */
	public Element getJour(int i){
		
		Element ret = new Element("tmp");
		try {
			ret = jours[i];
		} catch (NullPointerException e) {
			logger.error("Le tableau des jours est vide");
		}
		return ret;
	}
	/**
	 * Methode de recuperation de la temperature max au jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain) 
	 * @return String : La temperature max au jour souhaite
	 */
	public String getTemperatureMax(int jour){
		String ret = this.getJour(jour).getChildText("hi");
		if(ret == null){
			ret = "N/A";
			logger.error("Il n'y a pas de temperature max pour ce jour");
		}
		return ret;
	}
	/**
	 * Methode de recuperation de la temperature minimum au jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @return String : La temperature min du jour souhaite
	 */
	public String getTemperatureMin(int jour){
		String ret = this.getJour(jour).getChildText("low");
		if(ret == null){
			ret = "N/A";
			logger.error("Il n'y a pas de temperature min pour ce jour");
		}
		return ret;
	}
	
	/**
	 * Methode permettant de recuperer l'heure du lever du soleil au jour souhaite 
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @return String : L'heure du lever du soleil au jour souhaite
	 */
	public String getSunrise(int jour){
		return this.getJour(jour).getChildText("sunr");
	}
	
	/**
	 * Methode permettant de recuperer l'heure du coucher du soleil au jour souhaite 
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @return String : L'heure du coucher du soleil au jour souhaite
	 */
	public String getSunset(int jour){
		return this.getJour(jour).getChildText("suns");
	}
	/**
	 * Methode permettant de recuperer les rafales de vent pour un jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain) 
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : Les rafales de vent pour un jour souhaite
	 */
	public String getGust(int jour, String partOfDay){
		String ret="";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
				ret = courant.getChild("wind").getChildText("gust");
			}
		}
		return ret;
	}
	/**
	 * Methode permettant de recuperer la vitesse du vent pour un jour donne
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : La vitesse du vent pour un jour donne
	 */
	public String getSpeedWind(int jour, String partOfDay){
		String ret="N/A";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
					ret = courant.getChild("wind").getChildText("s");
			}
		}
		return ret;
	}
	

	/**
	 * Methode donnant la direction du vent en degre au jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : La direction du vent en degre au jour souhaite
	 */
	public String getDirectionWindMesure(int jour, String partOfDay){
		String ret="N/A";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
				ret = courant.getChild("wind").getChildText("d");
			}
		}
		return ret;
	}
	

	/**
	 * Methode permettant de recuperer la direction du vent en texte du jour souhaite 
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : La direction du vent en texte du jour souhaite
	 */
	public String getDirectionWindText(int jour, String partOfDay){
		String ret="N/A";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
				ret = courant.getChild("wind").getChildText("t");
			}
		}
		return ret;
	}

	/**
	 * Methode de recuperation de la description de la meteo (exemple : cloudy, rain ...) au jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : La description de la meteo au jour souhaite
	 */
	public String getCondition(int jour, String partOfDay){
		String ret="N/A";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
				ret = courant.getChildText("t");
			}
		}
		return ret;
	}

	/**
	 * Methode permettant de recuperer les chances de precipitation pour le jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : Les chances de precipitation pour le jour souhaite
	 */
	public String getPrecipitationChance(int jour, String partOfDay){
		String ret="N/A";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
					ret = courant.getChildText("ppcp");
			}
		}
		return ret;
	}
	

	/**
	 * Methode permettant de recuperer le taux d'humidity au jour souhaite
	 * @param jour : Le jour (0 equivaut a aujourd'hui, 1 equivaut a demain, 2 equivaut a apres demain)
	 * @param partOfDay : La partie de la journee souhaite (d pour matin et n pour apres-midi)
	 * @return String : Le taux d'humidity au jour souhaite
	 */
	public String getHumidity(int jour, String partOfDay){
		String ret="N/A";
		Iterator i = this.getJour(jour).getChildren("part").iterator();
		while(i.hasNext()){
			Element courant = (Element)i.next();
			if(courant.getAttributeValue("p").equals(partOfDay)){
				ret = courant.getChildText("hmid");
			}
		}
		return ret;
	}

	/**
	 * Methode donnant le temps d'execution a l'appel de cette api 
	 * @return Long : Temps d'execution
	 */
	public long getExecutionTime() {
		return end-begin;
	}	
}