package com.ayon.api.stats.service;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ayon.api.stats.domain.TransactionDetail;
import com.ayon.api.stats.domain.TransactionStatistics;
import org.springframework.stereotype.Service;

import static utils.TimeConversionUtils.convertTimeStampToSeconds;
import static utils.TimeConversionUtils.findTimeElapsedInSeconds;
import static utils.TimeConversionUtils.divide;

/**
 *
 *   This class is responsible for initiation,preparation,
 *   population of statistics based on transactions.
 *   Created by AYON SANYAL on 19-11-2017
 */
@Service
public class StatisticsCreatorService {
	public static final int GIVEN_TIME_WINDOW = 60 * 1000;
	private  ConcurrentNavigableMap<Long, TransactionStatistics> perodicStatsMap;
	public ConcurrentNavigableMap<Long, TransactionStatistics> getPerodicStatsMap() {
		if(this.perodicStatsMap==null)
		{
			this.perodicStatsMap = new ConcurrentSkipListMap<>();
		}
		return this.perodicStatsMap;

	}









	private TransactionStatistics actualStatistics;
	private TransactionStatistics updatableStatistics;
	private final Object actualStatisticsLock;


	/** Responsible for creation of statistical data.
	 * @param transactionDetail
	 */
	public void prepareStatistics(TransactionDetail transactionDetail) {


        long elapsedTime = convertTimeStampToSeconds(transactionDetail.getTimestamp());
		TransactionStatistics perodicStats = new TransactionStatistics();
		populateStatistics(perodicStats,transactionDetail);
		getPerodicStatsMap().put(elapsedTime, perodicStats);
		synchronized (actualStatisticsLock) {
			populateStatistics(updatableStatistics,transactionDetail);
			prepareFinalStatistics();
		}

	}

	public StatisticsCreatorService() {
		super();

        //Actual statistics which will be usec further
		actualStatistics = new TransactionStatistics();
		//A copy of actual which can be updated.
		updatableStatistics = new TransactionStatistics();
		actualStatisticsLock = new Object();
		// TODO Auto-generated constructor stub
	}


	/** Populates the data
	 * of actual statistics based on time stamp of
	 * transactions.
	 *
	 * @param transactionStatistics
	 * @param transactionDetail
	 */
	private void populateStatistics(TransactionStatistics transactionStatistics, TransactionDetail transactionDetail) {
		double amount = transactionDetail.getAmount();

		transactionStatistics.setSum(updatableStatistics.getSum() + amount);
		transactionStatistics.setCount(updatableStatistics.getCount() + 1);

		{
			transactionStatistics
					.setAvg(divide(transactionStatistics.getSum(), transactionStatistics.getCount()));
		}

		if (amount > updatableStatistics.getMax()) {
			transactionStatistics.setMax(amount);
		}
		else{
			transactionStatistics.setMax(updatableStatistics.getMax());
		}
		if(transactionStatistics.getCount()==1)
		{
			transactionStatistics.setMin(amount);
		}
		else if (updatableStatistics.getCount() == 1 || amount < updatableStatistics.getMin()) {
			transactionStatistics.setMin(amount);
		}
		else{
			transactionStatistics.setMin(updatableStatistics.getMin());
		}

		
	}


	/**
	 *  Creates actual statistics from its dummy counterpart
	 *  and keeps both the objects in sync.
	 */
	private void prepareFinalStatistics() {
		TransactionStatistics tempValue = this.actualStatistics;
		this.actualStatistics = this.updatableStatistics;
		tempValue.updateStatistics(this.updatableStatistics);
		this.updatableStatistics = tempValue;
	}


	public TransactionStatistics getStatistics() {
		return this.actualStatistics;
	}

	/**
	 * Checks for transactions older
	 * than 60 seconds and if found,
	 * remove them and then updates the statistical data accordingly.
	 */
	public void updatePerodicStatsics() {

		Entry<Long, TransactionStatistics> firstEntry = getPerodicStatsMap().firstEntry();
		if(firstEntry!=null){
			Long minPerodicTimestamp = firstEntry.getKey();
			Long elapsedTime = findTimeElapsedInSeconds(minPerodicTimestamp);
			if (elapsedTime > GIVEN_TIME_WINDOW) {
				removeFromPerodicStats(minPerodicTimestamp);
			}
		}
}
	
	private void removeFromPerodicStats(long removeTimeStamp) {
		TransactionStatistics perodicStats = perodicStatsMap.remove(removeTimeStamp);

		if(perodicStats!=null){
			updatableStatistics.setSum(updatableStatistics.getSum() - perodicStats.getSum());
			updatableStatistics.setCount(updatableStatistics.getCount() - perodicStats.getCount());
			updatableStatistics.setAvg(divide(updatableStatistics.getSum(), updatableStatistics.getCount()));
			// we need to find the max and min. value from all periodic statistics in case that the removed statistics was the min or max value.
			if (updatableStatistics.getMin() == perodicStats.getMin() || updatableStatistics.getMax() == perodicStats.getMax()) {
				double minValue = perodicStatsMap.isEmpty() ? 0d : Double.MAX_VALUE;
				double maxValue = 0;
				perodicStatsMap.values().forEach(data -> {
					if (data.getMin() < minValue) {
						updatableStatistics.setMin(data.getMin());
					}
					if (data.getMax() > maxValue) {
						updatableStatistics.setMax(data.getMax());
					}
				});
				
				}
				
				
				
				
				
			}
		prepareFinalStatistics();
}
	
	
}
