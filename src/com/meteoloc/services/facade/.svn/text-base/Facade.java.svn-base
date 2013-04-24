package com.meteoloc.services.facade;


import com.meteoloc.services.facade.connectors.local.ConnecteurLocalisation;
import com.meteoloc.services.facade.connectors.meteo.ConnecteurMeteo;

/**
 * Interface de notre Facade
 * @author Mathieu DURAND - Cedric JACQUET - Yohann STREIBEL - Ewan GICQUEL
 */
public abstract class Facade implements ConnecteurMeteo, ConnecteurLocalisation{

	public abstract int hasHail(int day, String partOfDay);

	public abstract int hasRain(int day, String partOfDay);

	public abstract int hasSnow(int day, String partOfDay);

	public abstract int hasTStorms(int day, String partOfDay);

	public abstract int hasSun(int jour, String partOfDay);

	public abstract int hasCloud(int jour, String partOfDay);

	public abstract int hasSand(int jour, String partOfDay);

	public abstract int hasFog(int jour, String partOfDay);

}
