package com.ayon.api.stats.service;

import com.ayon.api.stats.domain.TransactionDetail;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by AYON SANYAL on 19-11-2017.
 */
public class TransactionAddingServiceTest {




    @Test
    public void addTransactionTest() throws InterruptedException
    {

        double amount= 4000.00;
        StatisticsCreatorService statisticsCreatorServiceMock =mock(StatisticsCreatorService.class);
        TransactionDetail transactionDetail = new TransactionDetail(amount,System.currentTimeMillis());
        TransactionAddingService transactionAddingService = new TransactionAddingService(statisticsCreatorServiceMock);

        transactionAddingService.addTransaction(transactionDetail);
        assertEquals("1 transaction should be added to transaction detail queue",1,transactionAddingService.getTransactionDetailQueue().size());
        Thread.sleep(5000);
        verify(statisticsCreatorServiceMock,times(1)).prepareStatistics(transactionDetail);

    }





}
