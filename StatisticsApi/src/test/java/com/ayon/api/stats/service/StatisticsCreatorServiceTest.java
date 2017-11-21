package com.ayon.api.stats.service;

import org.junit.Before;
import org.junit.Test;

import com.ayon.api.stats.domain.TransactionDetail;
import com.ayon.api.stats.domain.TransactionStatistics;
import static org.junit.Assert.assertEquals;

/**
 * Created by AYON SANYAL on 19-11-2017
 */
public class StatisticsCreatorServiceTest {

	private StatisticsCreatorService statisticsCreatorService;
	@Before
	public void runBeforeTestMethod() {
		statisticsCreatorService = new StatisticsCreatorService();
		}
	
	@Test
	public void testPrepareStatistics()
	{
		double amount= 4000.00;
		statisticsCreatorService.prepareStatistics(new TransactionDetail(amount,System.currentTimeMillis()));
		assertEquals("Transaction Processed for 1 transactions with value of sum,average,max,min same as amount and count is 1", new TransactionStatistics(amount,
				amount,amount,amount,1),statisticsCreatorService.getStatistics());
	}
	
	
	@Test
	public void testupdatePerodicStatsics() throws InterruptedException
	{
		
		long timeStamp1= System.currentTimeMillis() - 40 * 1000;
		long timeStamp2 = timeStamp1 +10 * 1000;
        long timeStamp3 = timeStamp2 + 10 * 1000;
        
        statisticsCreatorService.prepareStatistics(new TransactionDetail(5000,timeStamp1));
        statisticsCreatorService.prepareStatistics(new TransactionDetail(2000,timeStamp1) );
        statisticsCreatorService.prepareStatistics(new TransactionDetail(700,timeStamp2) );
        statisticsCreatorService.prepareStatistics(new TransactionDetail(800,timeStamp2) );
        statisticsCreatorService.prepareStatistics(new TransactionDetail(500,timeStamp3) );
        assertEquals("Transaction statistics should be",
				new TransactionStatistics(9000,500,5000,1800,5),statisticsCreatorService.getStatistics());
        statisticsCreatorService.updatePerodicStatsics();
        assertEquals("No Transactions should be removed ", 5, statisticsCreatorService.getStatistics().getCount());
        Thread.sleep(30*1000);
        statisticsCreatorService.updatePerodicStatsics();
         assertEquals(" 2 Transactions  should be removed", 3,statisticsCreatorService.getStatistics().getCount());
        
	}
}
