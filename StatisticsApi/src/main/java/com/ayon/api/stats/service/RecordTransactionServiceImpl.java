package com.ayon.api.stats.service;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayon.api.stats.domain.TransactionDetail;
import com.ayon.api.stats.domain.TransactionStatistics;

import static utils.TimeConversionUtils.convertTimeStampToSeconds;
import static utils.TimeConversionUtils.findTimeElapsedInSeconds;
/**
 * @author AYON SANYAL
 * Created by AYON SANYAL on 19-11-2017
 * Operates as the main service of application.
 */
@Service
public class RecordTransactionServiceImpl implements RecordTransactionService {
	public static final int GIVEN_TIME_WINDOW = 60 * 1000;
	@Autowired
	private TransactionAddingService transactionAddingService;
	@Autowired
	private StatisticsCreatorService statisticsCreatorService;


	public RecordTransactionServiceImpl(TransactionAddingService transactionAddingService,StatisticsCreatorService statisticsCreatorService) {
		super();
		this.transactionAddingService=transactionAddingService;
		this.statisticsCreatorService=statisticsCreatorService;
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
		Runnable schedulerTask = ()-> {
			statisticsCreatorService.updatePerodicStatsics();
		};
		scheduledThreadPool.scheduleWithFixedDelay(schedulerTask,0,1000,TimeUnit.MILLISECONDS);
		// TODO Auto-generated constructor stub

	}
	/* Validate whether the transaction is older than 60 seconds or not.
	 * On successful Validation(the case when transaction is not older than 60 seconds) the result will be true.
	 * @param transactionDetail
	 * @returns boolean
	 */
	@Override
	public boolean validateAndRecordTransaction(TransactionDetail transactionDetail) {
		// TODO Auto-generated method stub
		
		boolean result= false;
		if(validateTransaction(transactionDetail))
		{   transactionAddingService.addTransaction(transactionDetail);
		result= true;
		}
		return result;
	}



	private boolean validateTransaction(TransactionDetail transactionDetail)
	{
		long timeInSeconds = convertTimeStampToSeconds(transactionDetail.getTimestamp());
	    return (findTimeElapsedInSeconds(timeInSeconds)<=GIVEN_TIME_WINDOW?true:false);

	}


	/* Brings recent data of Transaction Statistics i.e Statistics on transactions.
	 * @returns TransactionStatistics
	 */
  public TransactionStatistics getStatistics()
  {

  	return statisticsCreatorService.getStatistics();
  }
	
	
}
