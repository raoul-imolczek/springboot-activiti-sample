package com.imolczek.school.banking.activiti.util;

import java.util.HashMap;
import java.util.Map;

import com.imolczek.school.banking.activiti.model.CreditProposition;
import com.imolczek.school.banking.activiti.model.CustomerInformation;
import com.imolczek.school.banking.activiti.model.LeadInformation;

public class DataMapUtil {

	public static Map<String, String> getLeadInformationDataMap(LeadInformation leadInformation) {
		Map<String, String> leadInformationDataMap = new HashMap<String, String> ();
		leadInformationDataMap.put("firstName", leadInformation.getFirstName());
		leadInformationDataMap.put("lastName", leadInformation.getLastName());
		leadInformationDataMap.put("email", leadInformation.getEmail());
		return leadInformationDataMap;
	}
	
	public static Map<String, String> getCustomerInformationDataMap(CustomerInformation customerInformation) {
		Map<String, String> customerInformationDataMap = new HashMap<String, String> ();
		customerInformationDataMap.put("income", Long.toString(customerInformation.getIncome()));
		return customerInformationDataMap;
	}

	public static Map<String, String> getCreditPropositionDataMap(CreditProposition creditProposition) {
		Map<String, String> creditPropositionDataMap = new HashMap<String, String> ();
		creditPropositionDataMap.put("loanAmount", Long.toString(creditProposition.getLoanAmount()));
		creditPropositionDataMap.put("loanDuration", Long.toString(creditProposition.getLoanDuration()));
		return creditPropositionDataMap;
	}
	
}
