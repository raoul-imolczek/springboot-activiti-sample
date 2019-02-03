package com.imolczek.school.banking.activiti;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imolczek.school.banking.activiti.model.CreditProposition;
import com.imolczek.school.banking.activiti.model.CustomerInformation;
import com.imolczek.school.banking.activiti.model.LeadInformation;

@RestController
public class MyProcessRestController {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormService formService;

	@PostMapping("/create-lead")
	public String startProcess(@ModelAttribute LeadInformation leadInformation) {
	   
	    ProcessInstance instance = runtimeService.startProcessInstanceByKey("creditApplicationProcess");

	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(instance.getId())
	    	      .taskDefinitionKey("createLeadTask")
	    	      .singleResult();

	    Map<String, String> leadInformationDataMap = leadInformation.getLeadInformationDataMap();
	    
		formService.submitTaskFormData(task.getId(), leadInformationDataMap);
	    
	    return "Process started with ID: "
	    		+ instance.getId() 
	    		+ "\nNumber of currently running process instances: "
	    		+ runtimeService.createProcessInstanceQuery().count();
	}
	
	@PostMapping("/submit-customer-information/{processInstanceId}")
	public String submitCustomerInformation(@PathVariable String processInstanceId, @ModelAttribute CustomerInformation customerInformation) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("customerInformationTask")
	    	      .singleResult();

	    Map<String, String> customerInformationDataMap = customerInformation.getInformationDataMap();
	    
		formService.submitTaskFormData(task.getId(), customerInformationDataMap);
	    
		return "Submitted customer data";
	}

	@PostMapping("/submit-credit-proposition/{processInstanceId}")
	public String submitCreditProposition(@PathVariable String processInstanceId, @ModelAttribute CreditProposition creditProposition) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("creditPropositionTask")
	    	      .singleResult();

	    Map<String, String> creditPropositionDataMap = creditProposition.getCreditPropositionDataMap();
	    
		formService.submitTaskFormData(task.getId(), creditPropositionDataMap);
	    
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
	    
		return "Denied credit request";
	}
	
	@GetMapping("/list")
	public String getProcessInstances() {
		List<ProcessInstance> instancesList = runtimeService.createProcessInstanceQuery().list();
		return new Integer(instancesList.size()).toString();
	}

	@GetMapping("/consult/{processInstanceId}")
	public String getProcessInstanceDetails(@PathVariable String processInstanceId) {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
		return (String) runtimeService.getVariable(processInstanceId, "email");
	}
	
}
