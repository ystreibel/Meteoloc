package com.meteoloc.services.facade.connectors.meteo;

/**
 * Facade d'acces aux webservices de meteo
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 *
 */
public interface ConnecteurMeteo {


	public abstract String getCCTemperature();

	public abstract String getCCFlick();

	public abstract String getCCCondition();

	public abstract String getCCHumidity();

	public abstract String getCCVisibility();

	public abstract String getCCDewPoint();

	public abstract String getCCBarPressionMeasure();

	public abstract String getCCBarPressionText();

	public abstract String getCCSpeedWind();

	public abstract String getCCGust();

	public abstract String getCCDirectionWindMeasure();

	public abstract String getCCDirectionWindText();

	public abstract String getCCIndexUV();

	public abstract String getCCIndextUVDescription();

	public abstract String getTemperatureMax(int jour);

	public abstract String getTemperatureMin(int jour);

	public abstract String getSunrise(int jour);

	public abstract String getSunset(int jour);

	public abstract String getGust(int jour, String partOfDay);

	public abstract String getSpeedWind(int jour, String partOfDay);

	public abstract String getDirectionWindMesure(int jour, String partOfDay);

	public abstract String getDirectionWindText(int jour, String partOfDay);

	public abstract String getCondition(int jour, String partOfDay);

	public abstract String getPrecipitationChance(int jour, String partOfDay);

	public abstract String getHumidity(int jour, String partOfDay);

	public abstract long getExecutionTime();

}