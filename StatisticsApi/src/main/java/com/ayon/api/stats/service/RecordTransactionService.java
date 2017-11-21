package com.ayon.api.stats.service;

import com.ayon.api.stats.domain.TransactionDetail;
import com.ayon.api.stats.domain.TransactionStatistics;
/**
		* @author AYON SANYAL
		* Created by AYON SANYAL on 19-11-2017
*/
public interface RecordTransactionService {
  
	boolean validateAndRecordTransaction(TransactionDetail transactionDetail);
	TransactionStatistics getStatistics();
}
