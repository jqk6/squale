/*
 * Created on Mar 12, 2004
 */
package com.airfrance.jraf.helper;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;

import com.airfrance.jraf.bootstrap.locator.*;
import com.airfrance.jraf.bootstrap.locator.ProviderLocator;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.provider.IProvider;
import com.airfrance.jraf.spi.provider.IProviderConstants;

/**
 * <p>Title : PersistenceHelper.java</p>
 * <p>Description : Helper pour acceder un provider de persistance</p>
 * <p>Copyright : Copyright (c) 2004</p>
 * <p>Company : AIRFRANCE</p>
 */
public class PersistenceHelper {

	/**
	 * Retourne un provider de persistance.
	 * @return provider de persistance
	 */
	public final static IPersistenceProvider getPersistenceProvider() {
		IPersistenceProvider persistenceProvider =
			(IPersistenceProvider) ProviderLocator.getProvider(
				IProviderConstants.PERSISTENCE_PROVIDER_KEY);
		return persistenceProvider;
	}

	/**
	 * Retourne un provider de persistance.
	 * @param id nom du provider a retourner
	 * @return provider de persistance
	 */
	public final static IPersistenceProvider getPersistenceProvider(String id) {
		IPersistenceProvider persistenceProvider =
			(IPersistenceProvider) ProviderLocator.getProvider(id);
		return persistenceProvider;

	}
}