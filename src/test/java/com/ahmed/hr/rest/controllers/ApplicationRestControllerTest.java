package com.ahmed.hr.rest.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ahmed.hr.HrApplication;
import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.enums.ApplicationStatus;
import com.ahmed.hr.persistence.repositories.ApplicationRepository;
import com.ahmed.hr.persistence.repositories.OfferRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = HrApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ApplicationRestControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Test
	public void testGetApplicatoin() throws Exception {
		Offer offer = createTestOffer("Java Developer");
		Application application1 = createTestApplication("ahmed@gmail.com", offer);

		mvc.perform(get("/applications/" + application1.getId()).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.email", is("ahmed@gmail.com"))).andExpect(jsonPath("$.resume", is("CV..")));
	}

	@Test
	public void testUpdateApplicationStatus() throws Exception {
		Offer offer = createTestOffer("Java Developer");
		Application application1 = createTestApplication("ahmed@gmail.com", offer);

		mvc.perform(put("/applications/" + application1.getId()).contentType(MediaType.APPLICATION_JSON)
				.content("{\"status\":\"HIRED\"}")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is("HIRED")));
	}

	@Test
	public void testApplicationsCount() throws Exception {
		Offer offer = createTestOffer("Java Developer");
		createTestApplication("ahmed@gmail.com", offer);
		createTestApplication("ali@gmail.com", offer);
		mvc.perform(get("/applications/count").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().string("2"));
	}

	private Offer createTestOffer(String jobTitle) {
		Offer offer = new Offer();
		offer.setJobTitle(jobTitle);
		offer.setStartDate(Calendar.getInstance());
		return offerRepository.saveAndFlush(offer);
	}

	private Application createTestApplication(String email, Offer offer) {
		Application application = new Application();
		application.setEmail(email);
		application.setOffer(offer);
		application.setResume("CV..");
		application.setStatus(ApplicationStatus.APPLIED);
		return applicationRepository.saveAndFlush(application);
	}

	@After
	public void resetDb() {
		applicationRepository.deleteAll();
		offerRepository.deleteAll();
	}

}
