package com.ahmed.hr.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahmed.hr.persistence.entities.Application;

public class ApplicationState {
	private Logger logger = LoggerFactory.getLogger("ApplicationState");

	public void applicationApplied(Application application) {
		logger.info("Starting application applied process with {}  ", application);
	}

	public void applicationInvited(Application application) {
		logger.info("Starting application invited process with {}  ", application);

	}

	public void applicationRejected(Application application) {
		logger.info("Starting application rejected process with {}  ", application);

	}

	public void applicationHired(Application application) {
		logger.info("Starting application hired process with {}  ", application);

	}

}
