package com.imolczek.school.banking;

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

@RestController
public class MyProcessRestController {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormService formService;

	@GetMapping("/start-process")
	public String startProcess() {
	   
	    ProcessInstance instance = runtimeService.startProcessInstanceByKey("myProcess");
	    return "Process started with ID: "
	    		+ instance.getId() 
	    		+ "\nNumber of currently running process instances: "
	    		+ runtimeService.createProcessInstanceQuery().count();
	}
	
	@PostMapping("/submit-customer-identity/{processInstanceId}")
	public String submitCustomerIdentity(@PathVariable String processInstanceId, @ModelAttribute CustomerIdentity customerIdentity) {
	    Task task = taskService.createTaskQuery()
	    	      .processInstanceId(processInstanceId)
	    	      .taskDefinitionKey("customerIdentity")
	    	      .singleResult();

	    Map<String, String> customerIdentityDataMap = customerIdentity.getIdentityDataMap();
	    
		formService.submitTaskFormData(task.getId(), customerIdentityDataMap);
	    
		return "Submitted data: " + customerIdentityDataMap.get("firstName") + " " + customerIdentityDataMap.get("lastName");
	}
	
}
