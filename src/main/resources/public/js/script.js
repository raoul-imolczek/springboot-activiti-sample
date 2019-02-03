var app = new Vue({
    
    el: '#app',

    data: {
    	id: "",
        firstName: "",
        lastName: "",
        email: "",
        income: "",
        loanAmount: "",
        loanDuration: "",
        customerInformationSubmitted: false,
        creditPropositionSubmitted: false,
        status: ""
    },

    methods: {
        createLead: function(event) {
        	$.post('http://localhost:8080/create-lead', {
        		firstName: this.firstName,
        		lastName: this.lastName,
        		email: this.email
        	}, function(data, status) {
            	this.id = data.id;
            }.bind(this));
        },
        submitCustomerInformation: function(event) {
        	$.post('http://localhost:8080/submit-customer-information/' + this.id, {
        		income: this.income
        	}, function(data, status) {
        		this.customerInformationSubmitted = true;
        		this.updateStatus();
            }.bind(this));
        },
        submitCreditProposition: function(event) {
        	$.post('http://localhost:8080/submit-credit-proposition/' + this.id, {
        		loanAmount: this.loanAmount,
        		loanDuration: this.loanDuration
        	}, function(data, status) {
        		this.creditPropositionSubmitted = true;
        		this.updateStatus();
            }.bind(this));
        },
        updateStatus: function(event) {
        	$.get('http://localhost:8080/status/' + this.id, function(data, status) {
        		this.status = data;
            }.bind(this));
        }        
    }

})