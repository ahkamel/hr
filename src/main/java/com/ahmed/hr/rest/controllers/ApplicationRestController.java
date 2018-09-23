package com.ahmed.hr.rest.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.repositories.exceptions.ApplicationNotFoundException;
import com.ahmed.hr.services.ApplicationService;

@RestController
public class ApplicationRestController {

	private Logger logger = LoggerFactory.getLogger("ApplicationController");
	@Autowired
	private ApplicationService applicationService;

	@RequestMapping(value = "/applications/{applicationId}", method = RequestMethod.GET)
	public ResponseEntity<Application> getApplication(@PathVariable Long applicationId) {
		logger.info("Starting getApplication method with parameter {}", applicationId);
		Optional<Application> application = applicationService.get(applicationId);
		if (!application.isPresent()) {
			logger.info("Ending getApplication method");
			logger.debug("application with id {} doesn't exist", applicationId);
			throw new ApplicationNotFoundException("There is no application with id " + applicationId);
		}
		logger.info("Ending getApplication method");
		return new ResponseEntity<Application>(application.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/applications/{applicationId}", method = RequestMethod.PUT)
	public ResponseEntity<Application> updateApplicationStatus(@RequestBody Application application,
			@PathVariable Long applicationId) {
		logger.info("Starting updateApplicationStatus method with parameter {}", applicationId);
		Optional<Application> currentApplication = applicationService.get(applicationId);
		if (!currentApplication.isPresent()) {
			logger.debug("application with id {} doesn't exist", applicationId);
			logger.info("Ending getApplication method");
			throw new ApplicationNotFoundException("There is no application with id " + applicationId);
		}

		currentApplication.get().setStatus(application.getStatus());
		applicationService.save(currentApplication.get());
		logger.info("Ending updateApplicationStatus method");
		return new ResponseEntity<Application>(currentApplication.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/applications/count", method = RequestMethod.GET)
	public ResponseEntity<Long> getAllApplicationCount() {
		logger.info("Starting getAllApplicationCount method ");
		long count = applicationService.count();
		logger.info("Ending getAllApplicationCount method");
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}

}
