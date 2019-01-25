package com.imoclzek.school.banking.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CalculateScoreTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		long riskAmount = (long) execution.getVariable("risk");
		long loanAmount = (long) execution.getVariable("loanAmount");
		long yearlyIncome = (long) execution.getVariable("income");

		execution.setVariable("accepted", false);
		execution.setVariable("denied", false);

		if (riskAmount + loanAmount < yearlyIncome / 10) {
			execution.setVariable("accepted", true);
		} else if (riskAmount + loanAmount > yearlyIncome / 5) {
			execution.setVariable("denied", true);
		}
		
	}

}
