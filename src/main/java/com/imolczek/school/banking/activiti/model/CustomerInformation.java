package com.imolczek.school.banking.activiti.model;

import java.util.HashMap;
import java.util.Map;

public class CustomerInformation {

	private long income;
	
	public long getIncome() {
		return income;
	}

	public void setIncome(long income) {
		this.income = income;
	}

	public Map<String, String> getInformationDataMap() {
		Map<String, String> customerInformationDataMap = new HashMap<String, String> ();
		customerInformationDataMap.put("income", Long.toString(this.income));
		return customerInformationDataMap;
	}

}
