package com.ayon.api.stats.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ayon.api.stats.domain.TransactionStatistics;
import com.ayon.api.stats.service.StatisticsCreatorService;
import com.ayon.api.stats.service.TransactionAddingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ayon.api.stats.domain.TransactionDetail;
import com.ayon.api.stats.service.RecordTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsApiController.class)
@ComponentScan("com.ayon.api.stats")
public class StatisticsApiControllerTest {

	@Autowired
    private MockMvc mockMvc;
	@MockBean
	private RecordTransactionService recordTransactionService;
	@MockBean
	private StatisticsCreatorService statisticsCreatorServiceMock;
	@MockBean
	private TransactionAddingService transactionAddingServiceMock;
	@Test
	public void successfulValidationShouldReturnTrue() throws Exception
	{    TransactionDetail transactionDetail = new TransactionDetail(12.3,1478192204000L);
		when(recordTransactionService.validateAndRecordTransaction(transactionDetail)).thenReturn(true);	
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transactionDetail))).andExpect(status().is(201));
		
	}
	
	@Test
	public void unsuccessfulValidationShouldReturnFalse() throws Exception
	{    TransactionDetail transactionDetails = new TransactionDetail(12.3,1478192204010L);
		when(recordTransactionService.validateAndRecordTransaction(transactionDetails)).thenReturn(false);	
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transactionDetails))).andExpect(status().is(204));
		
	}

	@Test
	public void getStatisticsTest() throws Exception
	{


		when(recordTransactionService.getStatistics()).thenReturn(new TransactionStatistics(9000,500,5000,1800,5));

		this.mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON)).andExpect((status().isOk())).
				andDo(MockMvcResultHandlers.print());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
}
	
}
