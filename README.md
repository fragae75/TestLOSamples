# TestLOSamples

Sample application for Datavenue Live Objects.
It simulates MQTT devices and Bussiness applications that uses Live Objects resources

6 tabbed panes : 
- Config tab : display the API Keys, by default in simulation mode => check to simulate sendings to the console
- Multi terminals tab : generate traffic of xx MQTT terminals
- OABApp tab : same as Multi terminals with other fields 
- Push Airparif tab : push time stamped values of airparif csv files
	- support Airparif CSV files with the following delimiters : ';' and ',' 
	- beware of the "model" field : if you load CSV files with different columns (ex : "PM10", "CO" versus "PM10", "PM25", "NO2"...), you must set an other model otherwise the data will not be indexed into the Elastic Search db
	- Samples
		- 20110721_20160601-PA04C_auto.csv : Paris center hour values from 2011 07 21st to 2016 06 01st
		- 20160601_20170616-PA04C_auto.csv (default value) : Paris center hour values from 2016 06 01st to 2017 06 01st
		- 20110124_20170624-OPERA_auto.csv : Paris Opera hour values from 2011 01 24th to 2017 06 24th
		/!\ change the town field (default = paris centre)
- Subscribe tab : subscribe to a route or a fifo. The fifo has to be configured in the Live Objects portal

- IFTTT tab : the idea is to trigger an event to automate an IFTTT applet
	- IFTTT.com : get an IFTTT key, create an applet with Webhooks. Through Webhook you can automate mail/SMS/Twitter/facebook notifications...
		- Live Objects : assuming you have created an API key.
			- Create a matching rule : (https://liveobjects.orange-business.com/api/v0/eventprocessing/matching-rule) ex :         "dataPredicate": {
            ">": [
                {
                    "var": "value.temperature"
                },
                20
    	        ]
	        }
			- create a firing rule binded to the matching rule (firingType = "ALWAYS") : https://liveobjects.orange-business.com/api/v0/eventprocessing/firing-rule
	- On the application
		- "Subscribe tab" : Subscribe to the route "~event/v1/data/eventprocessing/fired" to get events
		- "IFTTT tab"
		 	- Click on "Get matching rule" button to get the list of rules. Copy/paste the "enabled" matching rule Id into the "Matching Rule" field (use the \"Get Matching Rules\" button).
			- Click on "Check Firing Rules" to check whether the Firing rule is enabled
			- Fill the 2 other fields : the IFTTT key, the IFTTT event name. The URL field will be automaticaly filled
			- click on "Activate" button to create the binding between the Matching/firing rule and the IFTTT event.
	- To trigger an event : use the "OAB App" tab and publish data. In the case of a matching rule of temperature > 20, it will trigger when the published data will match.
- Result tab : what's generated & what's received on subscriptions

Important : create an account on Live Objects, generate your API key and put it into "cle.txt" and cleLora.txt. This will permit the application to acces your resources account 

