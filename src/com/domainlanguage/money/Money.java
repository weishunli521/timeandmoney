package com.domainlanguage.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.io.Serializable;

public class Money implements Comparable, Serializable {

	private BigDecimal amount;
	private Currency currency;
	
	private static final Currency USD = Currency.getInstance("USD");
	private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
	
	/**
	 * The constructor does not complex computations and requires simple, inputs
	 * consistent with the class invariant.
	 * Other creation methods are available for convenience.
	 */
	public Money(BigDecimal amount, Currency currency) {
		if (amount.scale() != currency.getDefaultFractionDigits()) throw new IllegalArgumentException("Scale of amount does not match currency");
		this.currency = currency;
		this.amount = amount;
	}

	/**
	 * This creation method is safe to use. It will adjust scale, but will not round off the amount.
	 */
	public static Money valueOf (BigDecimal amount, Currency currency) {
		return Money.valueOf (amount, currency, BigDecimal.ROUND_UNNECESSARY);
	}

	/**
	 * For convenience, an amount can be rounded to create a Money.
	 */
	public static Money valueOf(BigDecimal rawAmount, Currency currency, int roundingMode) {
		BigDecimal amount = rawAmount.setScale(currency.getDefaultFractionDigits(), roundingMode);
		return new Money(amount, currency);
	}

	/**
	 * WARNING: Because of the indefinite precision of double, this
	 * method must round off the value.
	 */
	public static Money valueOf(double dblAmount, Currency currency) {
		return Money.valueOf(dblAmount, currency, DEFAULT_ROUNDING_MODE);
	}

	/**
	 * Because of the indefinite precision of double, this method must round off the value. 
	 * This method gives the client control of the rounding mode.
	 */
	public static Money valueOf(double dblAmount, Currency currency, int roundingMode) {
		BigDecimal rawAmount = new BigDecimal(dblAmount);
		return Money.valueOf(rawAmount, currency, roundingMode);
	}

	/**
	 * WARNING: Because of the indefinite precision of double, this
	 * method must round off the value.
	 */
	public static Money dollars(double amount) {
		return Money.valueOf(amount, USD);
	}

	/**
	 * This creation method is safe to use. It will adjust scale, but will not round off the amount.
	 */
	public static Money dollars (BigDecimal amount) {
		return Money.valueOf(amount, USD);
	}

	
	public BigDecimal amount() {
		return amount;
	}

	public Currency currency() {
		return currency;
	}

	boolean isSameCurrencyAs(Money arg) {
		return currency.equals(arg.currency);
	}

	/**
	 * This probably should be Currency responsibility.
	 * Even then, it may need to be customized for specialty apps because
	 * TODO there are other cases, where the smallest increment is not the smallest unit.
	 */
	Money minimumIncrement() {
		BigDecimal one = new BigDecimal(1);
		BigDecimal increment = one.movePointLeft(currency.getDefaultFractionDigits());
		return Money.valueOf(increment, currency);
	}
	Money incremented() {
		return this.plus(minimumIncrement());
	}
	

	public Money negated() {
		return Money.valueOf(amount.negate(), currency);
	}
	public Money abs() {
		return Money.valueOf(amount.abs(), currency);
	}
	public boolean isNegative() {
		return amount.compareTo(new BigDecimal(0)) < 0;
	}
	public boolean isPositive() {
		return amount.compareTo(new BigDecimal(0)) > 0;
	}
	public boolean isZero() {
		return this.equals(Money.valueOf(0.0, currency));
	}
	
	public Money plus(Money other) {
		if (!isSameCurrencyAs(other)) throw new IllegalArgumentException("Addition is not defined between different currencies");
		return Money.valueOf(amount.add(other.amount), currency);
	}
	public Money minus(Money other) {
		return this.plus(other.negated());
	}

	public Money dividedBy (BigDecimal divisor) {
		return dividedBy(divisor, DEFAULT_ROUNDING_MODE);
	}
	
	public Money dividedBy (BigDecimal divisor, int roundingMode) {
		BigDecimal newAmount = amount.divide(divisor, roundingMode);
		return Money.valueOf(newAmount,currency);
	}
	
	public Money dividedBy (double divisor) {
		return dividedBy(divisor, DEFAULT_ROUNDING_MODE);
	}

	/**
	 * TODO Many apps require carrying extra precision in intermediate calculations.
	 * This will require some variation on dividedBy that accepts a scale and returns...?
	 * Currently, the invariant of Money is that the scale is the currencies standard
	 * scale, but this will probably have to be suspended or elaborated in intermediate calcs.
	 */
	public Money dividedBy (double divisor, int roundingMode) {
		return dividedBy(new BigDecimal(divisor), roundingMode);
	}
	
	public BigDecimal dividedBy (Money other, int scale) {
		return amount.divide(other.amount, scale, BigDecimal.ROUND_UNNECESSARY);
	}

	public Money times(BigDecimal factor) {
		return times(factor, DEFAULT_ROUNDING_MODE);
	}
	
	/**
	 * TODO BigDecimal.multiply() has scale of "this", so what is roundingMode of that operations?
	 */
	public Money times(BigDecimal factor, int roundingMode) {
		return Money.valueOf(amount.multiply(factor), currency, roundingMode);
	}
	
	public Money times (double amount, int roundingMode) {
		return times(new BigDecimal(amount), roundingMode);
	}

	public Money times(double amount) {
		return times(new BigDecimal(amount));
	}

	public Money times(int i) {
		return times(new BigDecimal(i));
	}


	public int compareTo(Object other) {
		return compareTo((Money)other);
	}
	public int compareTo(Money other) {
		if (!isSameCurrencyAs(other)) throw new IllegalArgumentException("Compare is not defined between different currencies");
		return amount.compareTo(other.amount);
	}
	public boolean isGreaterThan(Money other) {
		return (compareTo(other) > 0);
	}
	public boolean isLessThan(Money other) {
		return (compareTo(other) < 0);
	}

	
	public boolean equals(Object other) {
		return (other instanceof Money) && equals((Money)other);
	}
	public boolean equals(Money other) {
		return currency.equals(other.currency) && (amount.equals(other.amount));
	}
	public int hashCode() {
		return amount.hashCode();
	}
	
	
	public String toString() {
		return currency.toString() + " " + amount();
	}
	
//	TODO Provide some currency-dependent formatting. Java 1.5 Currency doesn't do it.	
//	public String formatString() {
//		return currency.formatString(amount());
//	}
//	public String localString() {
//		return currency.getFormat().format(amount());
//	}
	

}