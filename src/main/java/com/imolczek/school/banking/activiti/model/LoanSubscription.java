package com.imolczek.school.banking.activiti.model;

public class LoanSubscription {

	private String id;
	private LeadInformation leadInformation;
	private CustomerInformation customerInformation;
	private CreditProposition creditProposition;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public LeadInformation getLeadInformation() {
		return leadInformation;
	}
	
	public void setLeadInformation(LeadInformation leadInformation) {
		this.leadInformation = leadInformation;
	}
	
	public CustomerInformation getCustomerInformation() {
		return customerInformation;
	}
	
	public void setCustomerInformation(CustomerInformation customerInformation) {
		this.customerInformation = customerInformation;
	}
	
	public CreditProposition getCreditProposition() {
		return creditProposition;
	}
	
	public void setCreditProposition(CreditProposition creditProposition) {
		this.creditProposition = creditProposition;
	}
	
}
