package com.ayon.api.stats.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ayon.api.stats.domain.TransactionDetail;
import com.ayon.api.stats.domain.TransactionStatistics;
import com.ayon.api.stats.service.RecordTransactionService;




/**
 * @author AYON SANYAL
 * 
 * Representation of api end points
 * and the result it offers.
 * Output of the api is represented by json for all the corner cases(success as well as error) .
 * Created by AYON SANYAL on 19-11-2017
 */
@RestController
public class StatisticsApiController implements HandlerExceptionResolver {
	
	public StatisticsApiController(RecordTransactionService recordTransactionService) {
		this.recordTransactionService = recordTransactionService;
	}
	@Autowired
	private final RecordTransactionService recordTransactionService;
	
	
	/** Records the transaction details for the
	 * transaction that just happened.
	 * @param transactionDetail
	 * @return
	 * Created by AYON SANYAL on 19-11-2017
	 */
	@RequestMapping(method=RequestMethod.POST,value="/transactions",consumes =  "application/json")
	
	ResponseEntity<Void> recordTransaction(@RequestBody TransactionDetail transactionDetail)
	{ 
		boolean result= recordTransactionService.validateAndRecordTransaction(transactionDetail);
		 
	if(result)
		{
			return new ResponseEntity(HttpStatus.CREATED);
			
		}
	return new ResponseEntity(HttpStatus.NO_CONTENT);
	}


	/** End Point to recieve statistics on transactions.
	 * @return TransactionStatistics
	 */
	@RequestMapping(method=RequestMethod.GET,value="/statistics",produces = MediaType.APPLICATION_JSON_VALUE)
	TransactionStatistics getStatistics()
	{

		return recordTransactionService.getStatistics();
    }


	/** Exception Handling
	 * @param req
	 * @param resp
	 * @param handler
	 * @param ex
	 * @return
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler,
			Exception ex) {
		 	resp.reset();
	        resp.setCharacterEncoding("UTF-8");
	        resp.setContentType("text/json");
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        ModelAndView model = new ModelAndView(new MappingJackson2JsonView());
            model.addObject("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addObject("message", ex.getMessage());
            return model;
}


}
