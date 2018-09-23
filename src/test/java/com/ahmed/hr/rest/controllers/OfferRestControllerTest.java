package com.ahmed.hr.rest.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

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
public class OfferRestControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Test
	public void testListAllOffers() throws Exception {
		createTestOffer("Java Developer");
		createTestOffer("Python Developer");
		createTestOffer("Senior Java Developer");

		mvc.perform(get("/offers").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
				.andExpect(jsonPath("$[0].jobTitle", is("Java Developer")))
				.andExpect(jsonPath("$[1].jobTitle", is("Python Developer")))
				.andExpect(jsonPath("$[2].jobTitle", is("Senior Java Developer")));
	}

	@Test
	public void testGetOffer() throws Exception {
		Offer offer = createTestOffer("Python Developer");

		mvc.perform(get("/offers/" + offer.getId()).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jobTitle", is("Python Developer")));
	}

	@Test
	public void testGetOfferByJobTitle() throws Exception {
		createTestOffer("Java Developer");

		mvc.perform(get("/offers/jobtitle/Java Developer").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.jobTitle", is("Java Developer")));
	}

	@Test
	public void testCreateOffer() throws Exception {
		mvc.perform(post("/offers/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"jobTitle\":\"Senior Java Developer\",\"startDate\":\"2018-09-25\"}")).andDo(print())
				.andExpect(header().exists("location")).andExpect(status().isCreated());
	}

	@Test
	public void testApplyToOffer() throws Exception {
		Offer offer = createTestOffer("Python Developer");

		mvc.perform(post("/offers/" + offer.getId() + "/apply").contentType(MediaType.APPLICATION_JSON).content(
				"{	\"offer\":{\"id\":" + offer.getId() + "},\"email\":\"b@c.com\",\"resume\":\"test ...blabla\"}"))
				.andDo(print()).andExpect(header().exists("location")).andExpect(status().isCreated());
		mvc.perform(post("/offers/" + offer.getId() + "/apply").contentType(MediaType.APPLICATION_JSON).content(
				"{	\"offer\":{\"id\":" + offer.getId() + "},\"email\":\"b@c.com\",\"resume\":\"test ...blabla\"}"))
				.andDo(print()).andExpect(header().doesNotExist("location"))

				.andExpect(status().isConflict());
	}

	@Test
	public void testListAllOfferApplications() throws Exception {
		Offer offer = createTestOffer("Java Developer");
		createTestApplication("ahmed@gmail.com", offer);
		createTestApplication("ali@gmail.com", offer);
		createTestApplication("john@gmail.com", offer);
		mvc.perform(get("/offers/" + offer.getId() + "/applications").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
				.andExpect(jsonPath("$[0].email", is("ahmed@gmail.com")))
				.andExpect(jsonPath("$[1].email", is("ali@gmail.com")))
				.andExpect(jsonPath("$[2].email", is("john@gmail.com")));
	}

	@Test
	public void testOfferApplicationsCount() throws Exception {
		Offer offer = createTestOffer("Java Developer");
		createTestApplication("ahmed@gmail.com", offer);
		createTestApplication("ali@gmail.com", offer);
		createTestApplication("john@gmail.com", offer);
		createTestApplication("garuav@gmail.com", offer);
		mvc.perform(get("/offers/" + offer.getId() + "/applications/count").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().string("4"));
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
