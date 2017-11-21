package com.ayon.api.stats.service;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ayon.api.stats.domain.TransactionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**Does every bit where  transaction
 * is involved.
 *
 * Created by AYON SANYAL on 19-11-2017
 */
@Service
public class TransactionAddingService {

	public Queue<TransactionDetail> getTransactionDetailQueue() {
		return transactionDetailQueue;
	}

	private final Queue<TransactionDetail> transactionDetailQueue;
	@Autowired
	private StatisticsCreatorService statisticsCreatorService;
	public void addTransaction(TransactionDetail transactionDetail)
	{
		this.transactionDetailQueue.add(transactionDetail);
	}
	public TransactionAddingService(StatisticsCreatorService statisticsCreatorService) {
		super();
		this.transactionDetailQueue = new  ConcurrentLinkedQueue<>();


		Thread thread = new Thread(()->
		{
			createStatisticsForTransactions();
		});
		thread.start();
		this.statisticsCreatorService=statisticsCreatorService;
	}

	/**
	 *  Traverse the queue and delegate the fetched
	 *  transactions for further processing.
	 */
	private void createStatisticsForTransactions()
	{    while(true)
	{
		Optional<TransactionDetail> optionlTransaction = Optional.ofNullable(transactionDetailQueue.poll());
		if(optionlTransaction.isPresent())
		{
			statisticsCreatorService.prepareStatistics(optionlTransaction.get());
		}
	}	
	}
	
}
