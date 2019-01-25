package com.imolczek.school.banking.activiti.model;

import java.util.HashMap;
import java.util.Map;

public class LeadInformation {

	private String firstName;
	private String lastName;
	private String email;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> getLeadInformationDataMap() {
		Map<String, String> leadInformationDataMap = new HashMap<String, String> ();
		leadInformationDataMap.put("firstName", this.firstName);
		leadInformationDataMap.put("lastName", this.lastName);
		leadInformationDataMap.put("email", this.email);
		return leadInformationDataMap;
	}

}
