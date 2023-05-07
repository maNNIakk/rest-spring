package br.com.restspring.math;

public class SimpleMath {

	
	public Double sum(Double numberOne,Double numberTwo) {
		return numberOne + (numberTwo);
	}
	
	public Double subtraction(Double numberOne,Double numberTwo) {
		return numberOne - numberTwo;
	}
	
	public Double multiply(Double numberOne ,Double numberTwo) {
		return numberOne * numberTwo;
	}
	
	public Double division(Double numberOne,Double numberTwo) {
		return numberOne / numberTwo;
	}
	
	public Double average(Double numberOne,Double numberTwo){
		return (numberOne + numberTwo)/2;
	}
	
	public Double squareR(Double numberOne) {
		return Math.sqrt(numberOne);
	}
}
