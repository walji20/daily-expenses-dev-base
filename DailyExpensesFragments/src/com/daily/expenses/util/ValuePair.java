package com.daily.expenses.util;

public class ValuePair {
	private long Value1;
	private long Value2;
	
	public ValuePair(long value1, long value2) {
		Value1 = value1;
		Value2 = value2;
	}
	
	/**
	 * @return the value1
	 */
	public long getValue1() {
		return Value1;
	}
	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(long value1) {
		Value1 = value1;
	}
	/**
	 * @return the value2
	 */
	public long getValue2() {
		return Value2;
	}
	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(long value2) {
		Value2 = value2;
	}
}
