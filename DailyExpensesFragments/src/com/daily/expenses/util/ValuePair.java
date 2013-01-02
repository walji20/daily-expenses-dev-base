package com.daily.expenses.util;

public class ValuePair {
	private String Value1;
	private Double Value2;
	
	public ValuePair(String value1, Double value2) {
		Value1 = value1;
		Value2 = value2;
	}
	
	/**
	 * @return the value1
	 */
	public String getValue1() {
		return Value1;
	}
	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		Value1 = value1;
	}
	/**
	 * @return the value2
	 */
	public Double getValue2() {
		return Value2;
	}
	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(Double value2) {
		Value2 = value2;
	}
}
