package com.imolczek.school.banking.activiti.model;

import java.util.HashMap;
import java.util.Map;

public class CreditProposition {

	private long loanAmount;
	private long loanDuration;

	public long getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(long loanAmount) {
		this.loanAmount = loanAmount;
	}

	public long getLoanDuration() {
		return loanDuration;
	}

	public void setLoanDuration(long loanDuration) {
		this.loanDuration = loanDuration;
	}

	public Map<String, String> getCreditPropositionDataMap() {
		Map<String, String> creditPropositionDataMap = new HashMap<String, String> ();
		creditPropositionDataMap.put("loanAmount", Long.toString(this.loanAmount));
		creditPropositionDataMap.put("loanDuration", Long.toString(this.loanDuration));
		return creditPropositionDataMap;
	}

}
