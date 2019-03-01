package com.ezuazu.mutant.dto;

import java.math.BigDecimal;

public class DnaStatReport {

	private long countMutantDna;

	private long countHumanDna;

	public long getCountMutantDna() {
		return countMutantDna;
	}

	public void setCountMutantDna(long countMutantDna) {
		this.countMutantDna = countMutantDna;
	}

	public long getCountHumanDna() {
		return countHumanDna;
	}

	public void setCountHumanDna(long countHumanDna) {
		this.countHumanDna = countHumanDna;
	}

	public BigDecimal getRatio() {
		return countHumanDna > 0 ? new BigDecimal(countMutantDna).divide(new BigDecimal(countHumanDna), 2, BigDecimal.ROUND_HALF_EVEN) : BigDecimal.ZERO;
	}

	@Override
	public String toString() {
		return "DnaStatReport [countMutantDna=" + countMutantDna + ", countHumanDna=" + countHumanDna + ", getRatio()=" + getRatio() + "]";
	}

}
