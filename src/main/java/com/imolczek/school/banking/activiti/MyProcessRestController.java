package com.imolczek.school.banking.activiti;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imolczek.school.banking.activiti.model.CreditProposition;
import com.imolczek.school.banking.activiti.model.CustomerInformation;
import com.imolczek.school.banking.activiti.model.LeadInformation;
import com.imolczek.school.banking.activiti.model.LoanSubscription;
import com.imolczek.school.banking.activiti.util.DataMapUtil;

@RestController
public class MyProcessRestController {

	private Logger LOG = Logger.getLogger(MyProcessRestController.class);
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormService formService;

	@PostMapping("/create-lead")
	public LoanSubscription startProcess(@ModelAttribute LeadInformation leadInformation) {
	   
	    ProcessInstance instance = runtimeService.startProcessInstanceByKey("creditApplicationProcess");

	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(instance.getId())
	    	      .taskDefinitionKey("createLeadTask")
	    	      .singleResult();

	    Map<String, String> leadInformationDataMap = DataMapUtil.getLeadInformationDataMap(leadInformation);
	    
		formService.submitTaskFormData(task.getId(), leadInformationDataMap);
	    
		LoanSubscription result = new LoanSubscription();
		result.setId(instance.getId());
		result.setLeadInformation(leadInformation);

		LOG.info("Created lead with new process instance: " + instance.getId());

	    return result;
	}
	
	@PostMapping("/submit-customer-information/{processInstanceId}")
	public String submitCustomerInformation(@PathVariable String processInstanceId, @ModelAttribute CustomerInformation customerInformation) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("customerInformationTask")
	    	      .singleResult();

	    Map<String, String> customerInformationDataMap = DataMapUtil.getCustomerInformationDataMap(customerInformation);
	    
		formService.submitTaskFormData(task.getId(), customerInformationDataMap);

		LOG.info("Submitted customer data for process ID: " + processInstanceId);

		return "Submitted customer data";
	}

	@PostMapping("/submit-credit-proposition/{processInstanceId}")
	public String submitCreditProposition(@PathVariable String processInstanceId, @ModelAttribute CreditProposition creditProposition) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("creditPropositionTask")
	    	      .singleResult();

	    Map<String, String> creditPropositionDataMap = DataMapUtil.getCreditPropositionDataMap(creditProposition);
	    
		formService.submitTaskFormData(task.getId(), creditPropositionDataMap);

		LOG.info("Submitted credit proposition for process ID: " + processInstanceId);

		return "Submitted credit proposition data";
	}

	@PostMapping("/approve/{processInstanceId}")
	public String approve(@PathVariable String processInstanceId) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("humanDecisionTask")
	    	      .singleResult();

	    runtimeService.setVariable(processInstanceId, "accepted", true);	    

	    taskService.complete(task.getId());

		LOG.info("Approved application for process ID: " + processInstanceId);
	    
		return "Approved credit request";
	}

	@PostMapping("/deny/{processInstanceId}")
	public String deny(@PathVariable String processInstanceId) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("humanDecisionTask")
	    	      .singleResult();

	    runtimeService.setVariable(processInstanceId, "denied", true);	    

	    taskService.complete(task.getId());

		LOG.info("Denied application for process ID: " + processInstanceId);

		return "Denied credit request";
	}
	
	@GetMapping("/list")
	public String getProcessInstances() {
		List<ProcessInstance> instancesList = runtimeService.createProcessInstanceQuery().list();
		return new Integer(instancesList.size()).toString();
	}

	@GetMapping("/status/{processInstanceId}")
	public String getProcessInstanceDetails(@PathVariable String processInstanceId) {
		
		if (runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count() == 0) {
		
			HistoricProcessInstanceQuery historyProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId);
			if (historyProcessInstanceQuery.count() == 1) {
				boolean accepted = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityId("acceptedEvent").count() > 0;
				if (accepted) {
					return "ACCEPTED";
				} else {
					return "DENIED";
				}
			} else {
				return "NON EXISTING";
			}
			
		} else {
		
			Boolean scored = (Boolean) runtimeService.getVariable(processInstanceId, "scored");
			if (scored == null) {
				return "INCOMPLETE";
			} else {
				Boolean accepted = (Boolean) runtimeService.getVariable(processInstanceId, "accepted");
				Boolean denied = (Boolean) runtimeService.getVariable(processInstanceId, "denied");
				if (accepted) {
					return "ACCEPTED";
				} else if (denied) {
					return "DENIED";
				} else {
					return "AWAITING DECISION";
				}
			}

		}
	}
	
}
