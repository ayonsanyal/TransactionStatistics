package com.ayon.api.stats.service;

import com.ayon.api.stats.domain.TransactionDetail;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by AYON SANYAL on 19-11-2017.
 */
public class RecordTransactionServiceTest {
    StatisticsCreatorService statisticsCreatorServiceMock =mock(StatisticsCreatorService.class);
    TransactionAddingService transactionAddingServiceMock =mock(TransactionAddingService.class);
    @Test
    public void validateAndRecordTransactionSuccessCaseTest()
    {
        long timeStamp1= System.currentTimeMillis() - 40 * 1000;
        double amount= 4000.00;
        TransactionDetail transactionDetail = new TransactionDetail(amount,timeStamp1);
        RecordTransactionService recordTransactionService = new RecordTransactionServiceImpl(transactionAddingServiceMock,statisticsCreatorServiceMock);

        recordTransactionService.validateAndRecordTransaction(transactionDetail);
        assertEquals("For the transaction not older than 60 seconds,the validation should be successful",true, recordTransactionService.validateAndRecordTransaction(transactionDetail));

    }
    @Test
    public void validateAndRecordTransactionFailureCaseTest() throws InterruptedException
    {
        long timeStamp1= System.currentTimeMillis() - 40 * 1000;
        long timeStamp2 = timeStamp1 + 15*1000;
        double amount= 4000.00;
        Thread.sleep(60*1000);
        TransactionDetail transactionDetail = new TransactionDetail(amount,timeStamp2);
        RecordTransactionService recordTransactionService = new RecordTransactionServiceImpl(transactionAddingServiceMock,statisticsCreatorServiceMock);

        recordTransactionService.validateAndRecordTransaction(transactionDetail);
        assertEquals("For the transaction older than 60 seconds,the validation should not be successful",false, recordTransactionService.validateAndRecordTransaction(transactionDetail));

    }

}
