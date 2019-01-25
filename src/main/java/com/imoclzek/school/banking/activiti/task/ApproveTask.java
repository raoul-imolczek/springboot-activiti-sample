package com.imoclzek.school.banking.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;

public class ApproveTask implements JavaDelegate {

	private Logger LOG = Logger.getLogger(ApproveTask.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String email = (String) execution.getVariable("email");
		LOG.info("Send approval email to " + email);
		
	}

}
