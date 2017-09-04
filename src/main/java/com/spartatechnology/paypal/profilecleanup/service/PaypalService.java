/**
 * Sparta Software Co.
 * 2017
 */
package com.spartatechnology.paypal.profilecleanup.service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.WebProfile;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalResource;

/**
 * Deletes all WebProfiles from Paypal.
 * 
 * @author Daniel Conde Diehl
 *
 */
@Component
public class PaypalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaypalService.class);
	
	@Value("${paypal.config.file}")
	private Resource paypalConfig;
		
	@Value("#{'${profiles-to-keep}'.split(',')}") 
	private List<String> profilesToKeep;
	
	@PostConstruct
	private void initConf() throws Exception {
		Properties props = PropertiesLoaderUtils.loadProperties(paypalConfig);
		Payment.initConfig(props);
	}

	/**
	 * Deletes all profiles that are not in the list of profilesToKeep.
	 * 
	 */
	public void deleteWebProfiles() throws Exception {
		LOGGER.info("executing delete round");
	    APIContext apiContext = new APIContext(PayPalResource.getClientID(), PayPalResource.getClientSecret(), PayPalResource.getConfigurations().get("mode"));
		
		List<WebProfile> profiles = WebProfile.getList(apiContext);
		
		AtomicBoolean itemsDeleted = new AtomicBoolean(false);
		
   		profiles.stream()
   			.filter(prof -> !profilesToKeep.contains(prof.getName()))
   			.forEach(prof -> {
   				LOGGER.info("Deleting [{}]: ID: [{}]", prof.getName(), prof.getId());
   				try {
					prof.delete(apiContext);
					itemsDeleted.set(true);
				} catch (Exception e) {
					LOGGER.error("Error deleting profile " + prof.getId(), e);
				}
   			});
   		
   		if (itemsDeleted.get()) {
   			deleteWebProfiles();
   		}
	}	
	
}
