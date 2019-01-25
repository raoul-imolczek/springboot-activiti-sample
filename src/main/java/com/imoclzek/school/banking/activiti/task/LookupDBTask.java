package com.imoclzek.school.banking.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class LookupDBTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String name = ((String) execution.getVariable("lastName") + " " + (String) execution.getVariable("firstName")).toUpperCase();
		
		if (name.equals("DUPONT MARCEL")) {
			execution.setVariable("risk", 1000l);
		} else if (name.equals("MARTIN FLORENCE")) {
			execution.setVariable("risk", 2000l);
		} else if (name.equals("DUPUIS SIMON")) {
			execution.setVariable("risk", 3000l);
		} else if (name.equals("ANTOINE SYLVIE")) {
			execution.setVariable("risk", 4000l);
		} else {
			execution.setVariable("risk", 0l);
		}
		
	}

}
