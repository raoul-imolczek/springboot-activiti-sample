package com.imolczek.school.banking;

import java.util.HashMap;
import java.util.Map;

public class CustomerIdentity {

	private String firstName;
	private String lastName;
	
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

	public Map<String, String> getIdentityDataMap() {
		Map<String, String> identityDataMap = new HashMap<String, String> ();
		identityDataMap.put("firstName", this.firstName);
		identityDataMap.put("lastName", this.lastName);
		return identityDataMap;
	}
		
}
