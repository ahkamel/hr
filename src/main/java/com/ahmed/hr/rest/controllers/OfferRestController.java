package com.ahmed.hr.rest.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.enums.ApplicationStatus;
import com.ahmed.hr.persistence.repositories.exceptions.OfferNotFoundException;
import com.ahmed.hr.services.ApplicationService;
import com.ahmed.hr.services.OfferService;

@RestController
public class OfferRestController {

	private Logger logger = LoggerFactory.getLogger("OfferController");
	@Autowired
	private OfferService offerService;
	@Autowired
	private ApplicationService applicationService;

	@RequestMapping(value = "/offers", method = RequestMethod.GET)
	public ResponseEntity<List<Offer>> listAllOffers() {
		logger.info("Starting listAllOffers method");
		List<Offer> offers = offerService.findAll();
		if (offers.isEmpty()) {
			return new ResponseEntity<List<Offer>>(HttpStatus.NO_CONTENT);
		}
		logger.info("Ending listAllOffers method");
		return new ResponseEntity<List<Offer>>(offers, HttpStatus.OK);
	}

	@RequestMapping(value = "/offers/{offerId}", method = RequestMethod.GET)
	public ResponseEntity<Offer> getOffer(@PathVariable Long offerId) {
		logger.info("Starting getOffer method with parameter {}", offerId);
		Optional<Offer> offer = offerService.findById(offerId);
		if (!offer.isPresent()) {
			logger.info("Ending getOffer method");
			logger.debug("offer with id {} doesn't exist", offerId);
			throw new OfferNotFoundException("There is no offer with id " + offerId);
		}
		logger.info("Ending getOffer method");
		return new ResponseEntity<Offer>(offer.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/offers/jobtitle/{jobTitle}", method = RequestMethod.GET)
	public ResponseEntity<Offer> getOfferByJobTitle(@PathVariable String jobTitle) {
		logger.info("Starting getOfferByJobTitle method with parameter {}", jobTitle);
		Optional<Offer> offer = offerService.findByJobTitle(jobTitle);
		if (!offer.isPresent()) {
			logger.info("Ending getOfferByJobTitle method");
			logger.debug("offer with jobTitle {} doesn't exist", jobTitle);
			throw new OfferNotFoundException("There is no offer with job title " + jobTitle);
		}
		logger.info("Ending getOfferByJobTitle method");
		return new ResponseEntity<Offer>(offer.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/offers", method = RequestMethod.POST)
	public ResponseEntity<Void> createOffer(@RequestBody @Valid Offer offer, UriComponentsBuilder ucBuilder) {
		logger.info("Starting createOffer method with parameter {}", offer);
		if (offerService.isOfferExist(offer)) {
			logger.debug("An Offer with job title {} already exist", offer.getJobTitle());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		Offer savedOffer = offerService.save(offer);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/offers/{offerId}").buildAndExpand(savedOffer.getId()).toUri());
		logger.info("Ending createOffer method");
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/offers/{offerId}/apply", method = RequestMethod.POST)
	public ResponseEntity<Void> applyToOffer(@RequestBody @Valid Application application, @PathVariable Long offerId,
			UriComponentsBuilder ucBuilder) {
		logger.info("Starting applyToOffer method with parameter {}", application);
		if (!offerService.findById(offerId).isPresent()) {
			logger.debug("Applying for offer not exist {}", offerId);
			throw new OfferNotFoundException("There is no offer with id " + offerId);
		}
		Offer offer = offerService.findById(offerId).get();

		Optional<Application> oldApplication = applicationService.findByOfferAndEmail(offer, application.getEmail());
		if (oldApplication.isPresent()) {
			logger.debug("Applying for offer already applied for offer Id {}", offer.getId());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);

		}
		application.setOffer(offer);
		application.setStatus(ApplicationStatus.APPLIED);
		Application savedApplication = applicationService.save(application);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/applications/{applicationId}").buildAndExpand(savedApplication.getId()).toUri());
		logger.info("Ending applyToOffer method");
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/offers/{offerId}/applications", method = RequestMethod.GET)
	public ResponseEntity<List<Application>> listAllOfferApplications(@PathVariable Long offerId) {
		logger.info("Starting listAllOfferApplications method");
		Optional<Offer> offer = offerService.findById(offerId);
		if (!offer.isPresent()) {
			logger.debug("Trying to get applicaitons for offer not exist {}", offerId);
			throw new OfferNotFoundException("There is no offer with id " + offerId);
		}
		List<Application> applications = applicationService.findByOffer(offer.get());
		if (applications.isEmpty()) {
			return new ResponseEntity<List<Application>>(HttpStatus.NO_CONTENT);
		}
		logger.info("Ending listAllOfferApplications method");
		return new ResponseEntity<List<Application>>(applications, HttpStatus.OK);
	}

	@RequestMapping(value = "/offers/{offerId}/applications/count", method = RequestMethod.GET)
	public ResponseEntity<Long> countAllOfferApplications(@PathVariable Long offerId) {
		logger.info("Starting countAllOfferApplications method");
		Optional<Offer> offer = offerService.findById(offerId);
		if (!offer.isPresent()) {
			logger.debug("Trying to get applicaitons count for offer not exist {}", offerId);
			throw new OfferNotFoundException("There is no offer with id " + offerId);
		}
		Long count = applicationService.countByOffer(offer.get());
		logger.info("Ending countAllOfferApplications method");
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}

}
