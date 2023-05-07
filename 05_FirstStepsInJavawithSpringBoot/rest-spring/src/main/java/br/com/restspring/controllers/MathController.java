package br.com.restspring.controllers;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.restspring.exceptions.UnsupportedMathOperationException;
import br.com.restspring.math.SimpleMath;
import br.com.restspring.converters.NumberConverter;


@RestController
public class MathController {
	
	private final AtomicLong counter = new AtomicLong();
	private SimpleMath math = new SimpleMath();
	
	@RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
	public Double sum(@PathVariable(value = "numberOne")String numberOne,
					  @PathVariable(value = "numberTwo")String numberTwo) 
	throws Exception {
		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
  			throw new UnsupportedMathOperationException("Please set a numeric value for the sum!");
		}
		return math.sum(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
	}
	
	@RequestMapping(value = "/subtraction/{numberOne}/{numberTwo}", method=RequestMethod.GET)
	public Double subtraction(@PathVariable(value = "numberOne")String numberOne,
					  @PathVariable(value = "numberTwo")String numberTwo) 
	throws Exception {
		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value for the subtraction!");
		}
		return math.subtraction(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
	}
	
	@RequestMapping(value = "/multiply/{numberOne}/{numberTwo}", method=RequestMethod.GET)
	public Double multiply(@PathVariable(value ="numberOne")String numberOne,
						   @PathVariable(value = "numberTwo")String numberTwo)
	throws Exception {
		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value for the multiplication");
		}
		return math.multiply(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
		
	}
	
	@RequestMapping(value = "/division/{numberOne}/{numberTwo}", method=RequestMethod.GET)
	public Double division(@PathVariable(value ="numberOne")String numberOne,
						   @PathVariable(value = "numberTwo")String numberTwo)
	throws Exception {
		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value for the division");
		}
		return math.division(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
		
	}
	
	@RequestMapping(value = "/average/{numberOne}/{numberTwo}", method=RequestMethod.GET)
	public Double average(@PathVariable(value ="numberOne")String numberOne,
						   @PathVariable(value = "numberTwo")String numberTwo)
	throws Exception {
		if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value for the average");
		}
		return math.average(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
		
	}
	
	@RequestMapping(value = "/squareR/{numberOne}", method=RequestMethod.GET)
	public Double squareR(@PathVariable(value ="numberOne")String numberOne)
	throws Exception {
		if(!NumberConverter.isNumeric(numberOne)) {
			throw new UnsupportedMathOperationException("Please set a numeric value for the square root");
		}
		return math.squareR(NumberConverter.convertToDouble(numberOne));
		
	}
}
