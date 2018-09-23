package com.ahmed.hr.business.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahmed.hr.business.ApplicationState;
import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.enums.ApplicationStatus;

public class ApplicationStatusChangeListener {
	private Logger logger = LoggerFactory.getLogger("ApplicationStatusChangeListener");

	public void onChange(Application application, ApplicationStatus newStatus, ApplicationStatus oldStatus) {
		logger.info("{} Status has changed before {} after {} ", application, oldStatus, newStatus);
		ApplicationState applicationState = new ApplicationState();
		if (newStatus == ApplicationStatus.APPLIED) {
			applicationState.applicationApplied(application);
		} else if (newStatus == ApplicationStatus.INVITED) {
			applicationState.applicationInvited(application);
		} else if (newStatus == ApplicationStatus.REJECTED) {
			applicationState.applicationRejected(application);
		} else if (newStatus == ApplicationStatus.HIRED) {
			applicationState.applicationHired(application);
		}
	}
}
