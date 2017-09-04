/**
 * Sparta Software Co.
 * 2017
 */
package com.spartatechnology.paypal.profilecleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spartatechnology.paypal.profilecleanup.service.PaypalService;

/**
 * Main class for SPring boot app.
 * 
 * @author Daniel Conde Diehl - Sparta Technology
 *
 * History:
 *  Sep 4, 2017 - Daniel Conde Diehl
 */
@SpringBootApplication
public class PaypalWebprofileCleanupApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaypalWebprofileCleanupApplication.class);
	
	@Autowired
	private PaypalService paypalService;
	
	/* (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
   		LOGGER.info("Begin deleting profiles");
		paypalService.deleteWebProfiles();
   		LOGGER.info("Completed deleting profiles");
	}
	
	/**
	 * Main methods
	 * 
	 * @param args command-line args
	 */
	public static void main(String[] args) {
	    SpringApplication app = new SpringApplication(PaypalWebprofileCleanupApplication.class);
	    app.setBannerMode(Banner.Mode.OFF);
	    app.run(args);
	}
}
