# TestLOSamples

Sample application for Datavenue Live Objects
It simulates MQTT devices and Bussiness applications that uses Live Objects resources

6 tabbed panes : 
- Config
- Multi terminals : generate traffic of xx MQTT terminals
- OABApp : same as Multi terminals with other fields 
- Push Airparif : push time stamped values of airparif csv files
	- support Airparif CSV files with the following delimiters : ';' and ',' 
	- beware of the "model" field : if you load CSV files with different columns (ex : "PM10", "CO" versus "PM10", "PM25", "NO2"...), you must set an other model otherwise the data will not be indexed into the Elastic Search db
	- Samples
		- 20110721_20160601-PA04C_auto.csv : Paris center hour values from 2011 07 21st to 2016 06 01st
		- 20160601_20170616-PA04C_auto.csv (default value) : Paris center hour values from 2016 06 01st to 2017 06 01st
		- 20110124_20170624-OPERA_auto.csv : Paris Opera hour values from 2011 01 24th to 2017 06 24th
		/!\ change the town field (default = paris centre)
- Subscribe : subscribe to a route or a fifo. The fifo has to be configured in the Live Objects portal
- result : what's generated & what's received on subscriptions

Important : create an account on Live Objects, generate your API key and put it into "cle.txt" and cleLora.txt. This will permit the application to acces your resources account 

