package com.meteoloc.services.facade.connectors.local;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.meteoloc.services.facade.connectors.NoResultsException;
import com.meteoloc.services.utils.ServiceConfiguration;

/**
 * Classe d'acces au webservice de localisation de google, nous utilisons l'API de geocoder de google.
 * Cette api permet de structurer une adresse passee en parametre, de la standardiser.
 *
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public class GoogleMap implements ConnecteurLocalisation {
	
	public final String base_url = "http://maps.google.com/maps/geo?";
	private static final Logger logger = Logger.getLogger("GoogleMap.class");

	private String status_code ="";
	private String status_request="";
	private String address="";
	private String accuracy="";
	private String countryNameCode="";
	private String countryName="";
	private String administrativeAreaName="";
	private String subAdministrativeAreaName="";
	private String localityName="";
	private String[] latLonBox = new String[4];
	private String coordinates="";
	// Temps d'acces
	private long begin;
	private long end;
	

	/**
	 * Initialise la recherche Google Map
	 * @param localisation : localisation recherchee
	 * @throws NoResultsException : exception lancee si la recherche ne retourne pas de resultat
	 */
	public GoogleMap(String localisation) throws NoResultsException {
		begin = System.currentTimeMillis();
		try {
			// Mise en place des parametres pour la recherche Google Map
			String q = "q="+localisation.replaceAll(" ", "+");
			String key_param = ServiceConfiguration.getInstance().getProperty("gmap_key");
			String key = "key="+key_param;
			String sensor_param = ServiceConfiguration.getInstance().getProperty("gmap_sensor");
			String sensor = "sensor="+sensor_param;
			String output_param = "xml";
			String output = "output="+output_param;
			// Autres params facultatifs
			//		ll	Latitude, longitude. Ex : "ll=40,4789,-1,1248"	N
			//		spn	Trou? : Latitude, longitude. Ex : "ll=40,4789,-1,1248"	N
			//		gl	Country Code ccTLD (2 caracteres)	N
			String arguments = q+"&"+key+"&"+sensor+"&"+output;
			URL url = new URL(base_url+arguments);
			logger.info("Url de d'appel : "+base_url+arguments);
			// Creation du parser
			SAXBuilder sxb = new SAXBuilder();
			Namespace nameSpace_ge = Namespace.getNamespace("http://earth.google.com/kml/2.0");
			Namespace nameSpace_ad = Namespace.getNamespace("urn:oasis:names:tc:ciq:xsdschema:xAL:2.0");
			
			// Parsing de l'url
			Document document = sxb.build(url);
			// Recuperation des elements
			Element mainElement = document.getRootElement().getChild("Response",nameSpace_ge);
			Element status = mainElement.getChild("Status",nameSpace_ge);
	
			status_code = status.getChildText("code",nameSpace_ge);
			status_request = status.getChildText("request",nameSpace_ge);

			Element placemark = mainElement.getChild("Placemark",nameSpace_ge);
			address = placemark.getChildText("address",nameSpace_ge);
			
			Element adresseDetails = placemark.getChild("AddressDetails",nameSpace_ad);
		
			accuracy = adresseDetails.getAttributeValue("Accuracy");

			Element country = adresseDetails.getChild("Country",nameSpace_ad);

			countryNameCode = country.getChildText("CountryNameCode",nameSpace_ad);
			countryName = country.getChildText("CountryName",nameSpace_ad);

			Element administrativeArea = country.getChild("AdministrativeArea",nameSpace_ad);

			administrativeAreaName = administrativeArea.getChildText("AdministrativeAreaName",nameSpace_ad);

			Element subAdministrativeArea = administrativeArea.getChild("SubAdministrativeArea",nameSpace_ad);
			if (subAdministrativeArea !=null){
				subAdministrativeAreaName = subAdministrativeArea.getChildText("SubAdministrativeAreaName",nameSpace_ad);
				Element locality = subAdministrativeArea.getChild("Locality",nameSpace_ad);
				if (locality == null){
					localityName = localisation;
				}
				else localityName = locality.getChildText("LocalityName",nameSpace_ad);
			}
			else {
				Element locality = administrativeArea.getChild("Locality",nameSpace_ad);
				if (locality == null){
					localityName = localisation;
				}
				else localityName = locality.getChildText("LocalityName",nameSpace_ad);
			}
			logger.info("LocalityName : "+localityName);
			Element latLonBox_element = placemark.getChild("ExtendedData",nameSpace_ge).getChild("LatLonBox",nameSpace_ge);
			latLonBox = new String [4];
			latLonBox[0]= latLonBox_element.getAttributeValue("north");
			latLonBox[1]= latLonBox_element.getAttributeValue("south");
			latLonBox[2]= latLonBox_element.getAttributeValue("east");
			latLonBox[3]= latLonBox_element.getAttributeValue("west");
			Element point = placemark.getChild("Point",nameSpace_ge);
			coordinates = point.getChildText("coordinates",nameSpace_ge);
			
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (JDOMException e) {
			logger.error(e.getMessage());
		}catch (NullPointerException e){
			logger.error("Aucune donnee trouvee pour la localisation "+localisation);
			throw new NoResultsException(e.getMessage());
		}
		end = System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getStatus_code()
	 */
	public String getStatus_code() {
		return status_code;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getStatus_request()
	 */
	public String getStatus_request() {
		return status_request;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getAddress()
	 */
	public String getAddress() {
		return address;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getAccuracy()
	 */
	public String getAccuracy() {
		return accuracy;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getCountryNameCode()
	 */
	public String getCountryNameCode() {
		return countryNameCode;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getCountryName()
	 */
	public String getCountryName() {
		return countryName;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getAdministrativeAreaName()
	 */
	public String getAdministrativeAreaName() {
		return administrativeAreaName;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getSubAdministrativeAreaName()
	 */
	public String getSubAdministrativeAreaName() {
		return subAdministrativeAreaName;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getLocalityName()
	 */
	public String getLocalityName() {
		return localityName;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getLatLonBox()
	 */
	public String[] getLatLonBox() {
		return latLonBox;
	}

	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation2#getCoordinates()
	 */
	public String getCoordinates() {
		return coordinates;
	}
	
	public String toString(){
		String result = "";
		result+="status_code : "+status_code+"\n";
		result+="status_request : "+status_request+"\n";
		result+="address : "+address+"\n";
		result+="accuracy : "+accuracy+"\n";
		result+="countryNameCode : "+countryNameCode+"\n";
		result+="countryName : "+countryName+"\n";
		result+="administrativeAreaName : "+administrativeAreaName+"\n";
		result+="subAdministrativeAreaName : "+subAdministrativeAreaName+"\n";
		result+="localityName : "+localityName+"\n";
		result+="latLonBox : N"+latLonBox[0]+" S"+latLonBox[1]+" E"+latLonBox[2]+" W"+latLonBox[3]+"\n";
		result+="coordinates : "+coordinates;
		return result;
	}

	
	/* (non-Javadoc)
	 * @see com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation#getExecutionTime()
	 */
	public long getExecutionTime() {
		return end-begin;
	}
}
