
**twig-template-core** is a library for template based NLG. All you have to do is define your templates in mustache format along with the parameter details and pass the input in json format. Library will use the parameter details fo fill the templates and will return filled template.

**Check the following example**

Sample template description file:
```
{
  "filename" : "test-templates.json",
  "description" : "Test templates.",
  "allMappings": [
  {
    "templateID": "test_result_template",
    "description": "",
    "footNoteId":[""],
    "templateStrings":["Here is {{entity}}'s {{metric}} {{#if time-phrase}} in {{time-phrase}} {{else}}   {{#if from}} from  {{from}} {{/if}} {{#if till}} to {{till}} {{/if}} {{/if}}.",
    	"{{entity}}'s {{metric}} {{#if time-phrase}}{{time-phrase}} {{else}}  {{#if from}} from  {{from}} {{/if}} {{#if till}} to {{till}} {{/if}} {{/if}}."],
    "templateParameters": [
      {
        "name": "entity",
        "type":"single-value",
         "required": true,
        "default_value": ""
      },
      {
        "name": "metric",
        "type":"single-value",
        "required": true,
        "default_value": ""
      },
      {
        "name": "time-phrase",
        "type":"single-value",
        "required": false,
        "default_value": ""
      },
      {
        "name": "from",
        "type":"single-value",
        "required": false,
        "default_value": ""
      },
      {
        "name": "till",
        "type":"single-value",
        "required": false,
        "default_value": ""
      }
    ],
    "headTemplates":["I know you asked about {{entity}}."],
    "headTemplateParameters": [
      {
        "name": "entity",
        "type":"single-value",
        "required": true,
        "default_value": ""
      }
    ],
    "footTemplates":[],
    "footTemplateParameters":[]
  },
  {
    "templateID": "test-discounts_template",
    "description": "",
    "footNoteId":[""],
    "templateStrings":["You have to pay {{payment}}. But you are eligible for the following discounts: {{#discounts}}\n {{@index_1}})For {{type}} you will get {{value}} discount{{/discounts}}."],
    "templateParameters": [
      {
        "name": "discounts",
        "type":"multi-map",
        "required": true,
        "default_value": ""
      },
      {
        "name":"payment",
        "type":"single-value",
        "required":true,
        "default_value":""
      }
    ],
    "headTemplates":[""],
    "headTemplateParameters": [
    ],
     "footTemplates":["You are eligible for the above discounts because {{reason}}."],
    "footTemplateParameters": [
      {
        "name": "reason",
        "type":"single-value",
        "required": true,
        "default_value": ""
      }
    ]
  }
]
}
```
Sample input json: Loop is specified in mustache format {{#discounts}}. Handlebars @each construct is not tested, but it might work.


Input:
```
  {
  	"templateID": "test-discounts_template",
  	"templateParameters": {
  		"discounts": [{
  			"type": "discount type 1",
  			"value": "5"
  		}, {
  			"type": "discount type 2",
  			"value": "25 %"
  		}],
  		"payment": "500$"
  	}
  }
  ```
  Output:
```
You have to pay 500$. But you are eligible for the following discounts: 
 1)For discount type 1 you will get 5 discount
 2)For discount type 2 you will get 25 % discount.
  ```
  
  Sample input 2: Contains if else condition
  
  Input:
  ```
  {
"templateID":"test_result_template",
"templateParameters":{
	"entity":"Citi bank",
	"metric":"revenue",
	"time-phrase":"last six month"
},
"headTemplateParameters":{
	"entity":"Citi bank"
}
}
```
 Output:
 ```
 I know you asked about Citi bank.Here is Citi bank's revenue  in last six month .
```

Note:
 default values are not yet supported
  
