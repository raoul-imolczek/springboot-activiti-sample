package com.imoclzek.school.banking.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;

public class DenyTask implements JavaDelegate {

	private Logger LOG = Logger.getLogger(DenyTask.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String email = (String) execution.getVariable("email");
		LOG.info("Send denial email to " + email);
		
	}

}
