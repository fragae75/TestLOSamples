/**
 *
 * Build the GUI/launch the threads
 * 
 */


package com.test.LOSamples;
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import javax.swing.JTabbedPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.test.LOSamples.TestLOSamples.QueueTypes;

/**
 *  
 * @author Franck
 * 
 * Build the GUI/launch the threads
 * 6 tabbed panes : 
 * - Config
 * - Multi terminals : generate traffic of xx MQTT terminals
 * - OABApp : same as Multi terminals with other values 
 * - Push Airparif : push time stamped values of airparif csv files
 * - Subscribe : subscribe to a route or a fifo
 * - result : what's generated & what's received on subscriptions
 *
 */
public class TestLOFenetre extends JFrame {
	private JTabbedPane onglet = new JTabbedPane();
	private JLabel jlbSend = new JLabel("Send");
	public JTextArea textPaneSend = new JTextArea();
	private JScrollPane scrollSend = new JScrollPane(textPaneSend);
	private JLabel jlbReceive = new JLabel("Receive");
	public JTextArea textPaneReceive = new JTextArea();
	private JScrollPane scrollReceive = new JScrollPane(textPaneReceive);
	private JPanel panOutput = new JPanel();
	private JPanel panConfig = new JPanel();
	private JPanel panMultiTerminal = new JPanel();
	private JPanel panOABApp = new JPanel();
	private JPanel panSubscribe = new JPanel();
	private JPanel panPushData = new JPanel();
	private JPanel panIFTTT = new JPanel();
	private JPanel panConvertUDPToMQTT = new JPanel();
	// Config
	private static JCheckBox jcbSimulation = new JCheckBox("Simulation (just logs)");
	private JLabel jlbKey = new JLabel("Key : ");
	private static JTextField jtfKey = new JTextField();
	private JLabel jlbLoraKey = new JLabel("Lora Key : ");
	private static JTextField jtfLoraKey = new JTextField();
	private JLabel jlbServer = new JLabel("Server : ");
	private static JTextField jtfServer = new JTextField();
	// Multi Devices
	private static JCheckBox jcbDeviceMode = new JCheckBox("Device Mode");
	private JLabel jlbStreamID = new JLabel("StreamID : ");
	private static JTextField jtfStreamID = new JTextField();
	private JLabel jlbTopic = new JLabel("Topic : ");
	private static JTextField jtfTopic = new JTextField();
	private JLabel jlbDeviceUrnPrefix = new JLabel("Device URN prefix : ");
	private static JTextField jtfDeviceUrnPrefix = new JTextField();
	private JLabel jlbNbDevices = new JLabel("Nb Devices to simulate : ");
	private static JTextField jtfNbDevices = new JTextField();
	private JLabel jlbNbDataPerDevice = new JLabel("Nb Data per Device : ");
	private static JTextField jtfNbDataPerDevice = new JTextField();
	private JLabel jlblTempoEnvoi = new JLabel("Tempo B/W 2 messages/device : ");
	private static JTextField jtflTempoEnvoi = new JTextField();
	public JButton boutonPubTerminaux =  new JButton("Publish Terminaux");
	// OAB App
	private JLabel jlbStreamIDOAB = new JLabel("StreamID : ");
	private static JTextField jtfStreamIDOAB = new JTextField();
	private JLabel jlbNbDevicesOAB = new JLabel("Nb Devices to simulate : ");
	private static JTextField jtfNbDevicesOAB = new JTextField();
	private JLabel jlbNbDataPerDeviceOAB = new JLabel("Nb Data per Device : ");
	private static JTextField jtfNbDataPerDeviceOAB = new JTextField();
	private JLabel jlblTempoEnvoiOAB = new JLabel("Tempo B/W 2 messages/device : ");
	private static JTextField jtflTempoEnvoiOAB = new JTextField();
    public JButton boutonPubOABApp =  new JButton("Publish OAB App");
    // Subscribe
	private JLabel jlbTitleFifo = new JLabel("Subscribe to : ");
	private static JTextField jtfQueueName = new JTextField();
	public static JTextArea textPaneSubscribe = new JTextArea();
	private JScrollPane scrollSubscribe = new JScrollPane(textPaneSubscribe);
	private JRadioButton jrbPubSub = new JRadioButton("Pubsub");
	private JRadioButton jrbFifo = new JRadioButton("Fifo");
	private JRadioButton jrbRouter = new JRadioButton("Router");
	private JRadioButton jrbLoraRouter = new JRadioButton("Lora Router");
	private JRadioButton jrbLoraFifo = new JRadioButton("Lora Fifo");
	private ButtonGroup rbGroupSubscribe = new ButtonGroup();
	public JButton boutonSubscribe =  new JButton("Subscribe");
	private JComboBox jcbRouters = new JComboBox(TestLOSamples.LISTE_ROUTERS);
	// Push AirParif values
	private static JCheckBox jcbDeviceModePush = new JCheckBox("Device Mode");
	private JLabel jlbStreamIDPush = new JLabel("StreamID : ");
	private static JTextField jtfStreamIDPush = new JTextField();
	private JLabel jlbTopicPush = new JLabel("Topic : ");
	private static JTextField jtfTopicPush = new JTextField();
	private JLabel jlbDeviceUrnPush = new JLabel("Device URN : ");
	private static JTextField jtfDeviceUrnPush = new JTextField();
	private JLabel jlbCSVFileToOpen = new JLabel("CSV File to open : ");
	private static JTextField jtfCSVFile = new JTextField(TestLOSamples.sCSVFilePush);
	private JLabel jlbPushPeriod = new JLabel("Push value period (ms) : ");
	private static JTextField jtfPushPeriodValue = new JTextField(Long.toString(TestLOSamples.lTempoPush));
	private JLabel jlbTown = new JLabel("Town : ");
	private static JTextField jtfTown = new JTextField(TestLOSamples.sTown);
	private JLabel jlbDataModelPush = new JLabel("Data Model : ");
	private static JTextField jtfDataModelPush = new JTextField(TestLOSamples.sDataModelPush);
	private JLabel jlbDataTagPush = new JLabel("Tag : ");
	private static JTextField jtfDataTagPush = new JTextField(TestLOSamples.sDataTagPush);
	public JButton boutonPushStart =  new JButton("Push !");
	public JButton boutonPushStop =  new JButton("Stop");
	public static JButton boutonPushPause =  new JButton("Pause");
	// IFTTT
	private JLabel jlbIFTTTMatchingRule = new JLabel("Matching Rule : ");
	private static JTextField jtfIFTTTMatchingRule = new JTextField();
	private JLabel jlbIFTTTKey = new JLabel("Key : ");
	private static JTextField jtfIFTTTKey = new JTextField();
	private JLabel jlbIFTTTEvent = new JLabel("Event : ");
	private static JTextField jtfIFTTTEvent = new JTextField();
	private JLabel jlbIFTTTURL = new JLabel("URL : ");
	private static JTextField jtfIFTTTURL = new JTextField();
	public static JTextArea textPaneIFTTT = new JTextArea();
	private JScrollPane scrollIFTTT = new JScrollPane(textPaneIFTTT);
	public JButton boutonIFTTTActivate =  new JButton("Activate");
	public JButton boutonIFTTTMatchingRules =  new JButton("Get Matching Rules");
	public JButton boutonIFTTTCheckFiringRules =  new JButton("Check Firing Rules");
//	private JList<String> jlMatchingRules = new JList<>();
	// UDPToMQTT
	private JLabel jlbListenPortUDP = new JLabel("Listening Port : ");
	private static JTextField jtfListenPortUDP = new JTextField();
	public JButton boutonUDPToMQTTStart =  new JButton("Start");
	private JLabel jlbDataModelUDPToMQTT = new JLabel("Data Model : ");
	private JLabel jlbStreamIDUDPToMQTT = new JLabel("StreamID : ");
	private static JTextField jtfStreamIDUDPToMQTT = new JTextField();
	private static JTextField jtfDataModelUDPToMQTT = new JTextField(TestLOSamples.sDataModelUDPToMQTT);
	private JLabel jlbDataTagUDPToMQTT = new JLabel("Tag : ");
	private static JTextField jtfDataTagUDPToMQTT = new JTextField(TestLOSamples.sDataTagUDPToMQTT);
	private JLabel jlbDeviceUrnUDPToMQTT = new JLabel("Device URN : ");
	private static JTextField jtfDeviceUrnUDPToMQTT = new JTextField();
	 

	

	/**
	 * Updates all the values from the GUI
	 * @return
	 */
	static boolean gatherConfigValues()
	{
		int i;
		long l;
		boolean bGood = true;
		
		/*
		 * Config
		 */
		//		TestLOSamples.sGetDataLinkBase = jtfReqBase.getText();
	    TestLOSamples.bPublish = !jcbSimulation.isSelected();
		TestLOSamples.sAPIKey = jtfKey.getText();
		TestLOSamples.sAPILoraKey = jtfLoraKey.getText();
		
		/*
		 * Multi terminals
		 * 
		 */
		TestLOSamples.sStreamID = jtfStreamID.getText();
		TestLOSamples.sDeviceTopic = jtfTopic.getText();
		TestLOSamples.sDeviceUrnPrefix = jtfDeviceUrnPrefix.getText();

		// Nb Devices
		try {
		    i = Integer.parseInt(jtfNbDevices.getText());
		    if (i>0 && i<TestLOSamples.NB_MAX_DEVICES)
		    	TestLOSamples.nbDevices = 	i;
		    else{
			    jtfNbDevices.setText(String.valueOf(TestLOSamples.nbDevices));
		    	bGood = false;
		    }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		    jtfNbDevices.setText(String.valueOf(TestLOSamples.nbDevices));
	    	bGood = false;
		}

		// Nb Data per device
		try {
		    l = Long.parseLong(jtfNbDataPerDevice.getText());
		    if (l>0)
		    	TestLOSamples.lNbDataPerDevice = l;		
		    else{
		    	jtfNbDataPerDevice.setText(String.valueOf(TestLOSamples.lNbDataPerDevice));
		    	bGood = false;
		    }
		} catch (NumberFormatException e) {
			e.printStackTrace();
	    	jtfNbDataPerDevice.setText(String.valueOf(TestLOSamples.lNbDataPerDevice));
	    	bGood = false;
		}
		
		// Tempo Envoi
		try {
		    l = Long.parseLong(jtflTempoEnvoi.getText());
			if (l>0)
				TestLOSamples.lTempoEnvoi = l;
		    else{
				jtflTempoEnvoi.setText(String.valueOf(TestLOSamples.lTempoEnvoi));
		    	bGood = false;
		    }

		} catch (NumberFormatException e) {
			e.printStackTrace();
			jtflTempoEnvoi.setText(String.valueOf(TestLOSamples.lTempoEnvoi));
	    	bGood = false;
		}		

		/*
		 * App OAB
		 */
		TestLOSamples.sStreamIDOAB = jtfStreamIDOAB.getText();
		// Nb Devices
		try {
		    i = Integer.parseInt(jtfNbDevicesOAB.getText());
		    if (i>0 && i<TestLOSamples.NB_MAX_DEVICES)
		    	TestLOSamples.nbDevicesOAB = 	i;
		    else{
			    jtfNbDevicesOAB.setText(String.valueOf(TestLOSamples.nbDevicesOAB));
		    	bGood = false;
		    }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		    jtfNbDevicesOAB.setText(String.valueOf(TestLOSamples.nbDevicesOAB));
	    	bGood = false;
		}
		// Nb Data per device
		try {
		    l = Long.parseLong(jtfNbDataPerDeviceOAB.getText());
		    if (l>0)
		    	TestLOSamples.lNbDataPerDeviceOAB = l;		
		    else{
		    	jtfNbDataPerDeviceOAB.setText(String.valueOf(TestLOSamples.lNbDataPerDeviceOAB));
		    	bGood = false;
		    }
		} catch (NumberFormatException e) {
			e.printStackTrace();
	    	jtfNbDataPerDeviceOAB.setText(String.valueOf(TestLOSamples.lNbDataPerDeviceOAB));
	    	bGood = false;
		}
		
		// Tempo Envoi
		try {
		    l = Long.parseLong(jtflTempoEnvoiOAB.getText());
			if (l>0)
				TestLOSamples.lTempoEnvoiOAB = l;
		    else{
				jtflTempoEnvoiOAB.setText(String.valueOf(TestLOSamples.lTempoEnvoiOAB));
		    	bGood = false;
		    }

		} catch (NumberFormatException e) {
			e.printStackTrace();
			jtflTempoEnvoiOAB.setText(String.valueOf(TestLOSamples.lTempoEnvoiOAB));
	    	bGood = false;
		}		

		/*
		 * Subscribe
		 */
		TestLOSamples.sQueueName = jtfQueueName.getText();

	
		/*
		 * Push values
		 * 
		 */
		TestLOSamples.sStreamIDPush = jtfStreamIDPush.getText();
		TestLOSamples.sDeviceTopicPush = jtfTopicPush.getText();
		TestLOSamples.sDeviceUrnPush = jtfDeviceUrnPush.getText();
		TestLOSamples.sCSVFilePush = jtfCSVFile.getText();
		// Tempo Envoi : min of PUSH_MIN_PERIOD_VALUE
		try {
		    l = Long.parseLong(jtfPushPeriodValue.getText());
			if (l > TestLOSamples.PUSH_MIN_PERIOD_VALUE)
				TestLOSamples.lTempoPush = l;
		    else{
		    	TestLOSamples.lTempoPush = TestLOSamples.PUSH_MIN_PERIOD_VALUE;
		    	jtfPushPeriodValue.setText(String.valueOf(TestLOSamples.lTempoPush));
		    	bGood = false;
		    }

		} catch (NumberFormatException e) {
			e.printStackTrace();
			jtfPushPeriodValue.setText(String.valueOf(TestLOSamples.PUSH_MIN_PERIOD_VALUE));
	    	bGood = false;
		}
		TestLOSamples.sTown = jtfTown.getText();
		TestLOSamples.sDataModelPush = jtfDataModelPush.getText();
		TestLOSamples.sDataTagPush = jtfDataTagPush.getText();
		
		/*
		 * IFTTT
		 * 
		 */
		TestLOSamples.sIFTTTDefaultMatchingRule = jtfIFTTTMatchingRule.getText();
		TestLOSamples.sIFTTTKey = jtfIFTTTKey.getText();
		TestLOSamples.sIFTTTEvent = jtfIFTTTEvent.getText();
		TestLOSamples.sIFTTTURL = TestLOSamples.sIFTTTURL1 + TestLOSamples.sIFTTTEvent + TestLOSamples.sIFTTTURL2 + TestLOSamples.sIFTTTKey;
		jtfIFTTTURL.setText(TestLOSamples.sIFTTTURL);

		/*
		 * UDP To MQTT
		 * 
		 */
	    l = Long.parseLong(jtfListenPortUDP.getText());
	    if (l > 0 && l < 65535)
	    {
	    	TestLOSamples.iUDPPort = (short)l;
	    }
	    else 
	    {
	    	bGood = false;
	    	jtfListenPortUDP.setText(Integer.toString(TestLOSamples.iUDPPort));
	    }
		TestLOSamples.sStreamIDUDPToMQTT = jtfStreamIDUDPToMQTT.getText();
		TestLOSamples.sDeviceUrnUDPToMQTT = jtfDeviceUrnUDPToMQTT.getText();
		TestLOSamples.sDataModelUDPToMQTT = jtfDataModelUDPToMQTT.getText();
		TestLOSamples.sDataTagUDPToMQTT = jtfDataTagUDPToMQTT.getText();
	    
		return bGood;
	}
	
	
	/**
	 * build the window elements
	 */
	public TestLOFenetre(){
		this.setTitle("Live Objects trafic generator & feature testing");
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		/*
		 * 
		 * Panel Config
		 * 
		 * 
		 */
	    // Checkbox Simulation
	    JPanel jpCBSimul = new JPanel();
	    jpCBSimul.setLayout(new BoxLayout(jpCBSimul, BoxLayout.LINE_AXIS));
	    jcbSimulation.addActionListener(new CheckSimulationActionListener());
	    jcbSimulation.setSelected(!TestLOSamples.bPublish); 
	    jpCBSimul.add(jcbSimulation);
	    		
	    // API Key
	    JPanel jpKey = new JPanel();
	    jpKey.setLayout(new BoxLayout(jpKey, BoxLayout.LINE_AXIS));
		jpKey.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpKey.add(jlbKey);
	    jtfKey.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfKey.getMinimumSize().height));
	    jpKey.add(jtfKey);
	    jtfKey.setText(TestLOSamples.sAPIKey);

	    // API Lora Key
	    JPanel jpLoraKey = new JPanel();
	    jpLoraKey.setLayout(new BoxLayout(jpLoraKey, BoxLayout.LINE_AXIS));
		jpLoraKey.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpLoraKey.add(jlbLoraKey);
	    jtfLoraKey.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfLoraKey.getMinimumSize().height));
	    jpLoraKey.add(jtfLoraKey);
	    jtfLoraKey.setText(TestLOSamples.sAPILoraKey);

	    // Server
	    JPanel jpServer = new JPanel();
	    jpServer.setLayout(new BoxLayout(jpServer, BoxLayout.LINE_AXIS));
	    jpServer.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpServer.add(jlbServer);
	    jtfServer.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfServer.getMinimumSize().height));
	    jpServer.add(jtfServer);
	    jtfServer.setText(TestLOSamples.sServer);

		/*
		 * 
		 * Panel MultiTerminals
		 * 
		 * 
		 */
	    // Device mode
	    jcbDeviceMode.addActionListener(new CheckDeviceModeActionListener());
	    jcbDeviceMode.setSelected(TestLOSamples.bDeviceMode); 
	    
	    // Stream ID
	    JPanel jpStreamID = new JPanel();
	    jpStreamID.setLayout(new BoxLayout(jpStreamID, BoxLayout.LINE_AXIS));
		jpStreamID.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpStreamID.add(jlbStreamID);
	    jtfStreamID.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfStreamID.getMinimumSize().height));
	    jpStreamID.add(jtfStreamID);
	    jtfStreamID.setText(TestLOSamples.sStreamID);

	    // Topic 
	    JPanel jpTopic = new JPanel();
	    jpTopic.setLayout(new BoxLayout(jpTopic, BoxLayout.LINE_AXIS));
		jpTopic.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpTopic.add(jlbTopic);
	    jtfTopic.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfTopic.getMinimumSize().height));
	    jpTopic.add(jtfTopic);
	    jtfTopic.setText(TestLOSamples.DEFAULT_DEVICE_TOPIC);

	    // Device Urn Prefix
	    JPanel jpDeviceUrnPrefix = new JPanel();
	    jpDeviceUrnPrefix.setLayout(new BoxLayout(jpDeviceUrnPrefix, BoxLayout.LINE_AXIS));
	    jpDeviceUrnPrefix.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDeviceUrnPrefix.add(jlbDeviceUrnPrefix);
	    jtfDeviceUrnPrefix.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDeviceUrnPrefix.getMinimumSize().height));
	    jpDeviceUrnPrefix.add(jtfDeviceUrnPrefix);
	    jtfDeviceUrnPrefix.setText(TestLOSamples.sDeviceUrnPrefix);

	    // NbDevices
	    JPanel jpNbDevices = new JPanel();
	    jpNbDevices.setLayout(new BoxLayout(jpNbDevices, BoxLayout.LINE_AXIS));
	    jpNbDevices.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpNbDevices.add(jlbNbDevices);
	    jtfNbDevices.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfNbDevices.getMinimumSize().height));
	    jpNbDevices.add(jtfNbDevices);
	    jtfNbDevices.setText(String.valueOf(TestLOSamples.nbDevices));

	    // Nb Data Per device
	    JPanel jpNbDataPerDevice = new JPanel();
	    jpNbDataPerDevice.setLayout(new BoxLayout(jpNbDataPerDevice, BoxLayout.LINE_AXIS));
	    jpNbDataPerDevice.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpNbDataPerDevice.add(jlbNbDataPerDevice);
	    jtfNbDataPerDevice.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfNbDataPerDevice.getMinimumSize().height));
	    jpNbDataPerDevice.add(jtfNbDataPerDevice);
	    jtfNbDataPerDevice.setText(String.valueOf(TestLOSamples.lNbDataPerDevice));

	    // Tempo entre 2 envois
	    JPanel jplTempoEnvoi = new JPanel();
	    jplTempoEnvoi.setLayout(new BoxLayout(jplTempoEnvoi, BoxLayout.LINE_AXIS));
	    jplTempoEnvoi.add(Box.createRigidArea(new Dimension(30, 0)));
	    jplTempoEnvoi.add(jlblTempoEnvoi);
	    jtflTempoEnvoi.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtflTempoEnvoi.getMinimumSize().height));
	    jplTempoEnvoi.add(jtflTempoEnvoi);
	    jtflTempoEnvoi.setText(String.valueOf(TestLOSamples.lTempoEnvoi));

	    
	    // Ajout du bouton d'action
	    JPanel jpButtonTerm = new JPanel();
	    boutonPubTerminaux.addActionListener(new BoutonListenerPubTerminaux()); 
	    jpButtonTerm.setLayout(new BoxLayout(jpButtonTerm, BoxLayout.LINE_AXIS));
	    jpButtonTerm.add(boutonPubTerminaux);

	    /*
	     * 
	     * Panel OAB App
	     * 
	     */
	    // Stream ID
	    JPanel jpStreamIDOAB = new JPanel();
	    jpStreamIDOAB.setLayout(new BoxLayout(jpStreamIDOAB, BoxLayout.LINE_AXIS));
		jpStreamIDOAB.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpStreamIDOAB.add(jlbStreamIDOAB);
	    jtfStreamIDOAB.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfStreamIDOAB.getMinimumSize().height));
	    jpStreamIDOAB.add(jtfStreamIDOAB);
	    jtfStreamIDOAB.setText(TestLOSamples.sStreamIDOAB);

	    // NbDevices
	    JPanel jpNbDevicesOAB = new JPanel();
	    jpNbDevicesOAB.setLayout(new BoxLayout(jpNbDevicesOAB, BoxLayout.LINE_AXIS));
	    jpNbDevicesOAB.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpNbDevicesOAB.add(jlbNbDevicesOAB);
	    jtfNbDevicesOAB.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfNbDevicesOAB.getMinimumSize().height));
	    jpNbDevicesOAB.add(jtfNbDevicesOAB);
	    jtfNbDevicesOAB.setText(String.valueOf(TestLOSamples.nbDevicesOAB));

	    // Nb Data Per device
	    JPanel jpNbDataPerDeviceOAB = new JPanel();
	    jpNbDataPerDeviceOAB.setLayout(new BoxLayout(jpNbDataPerDeviceOAB, BoxLayout.LINE_AXIS));
	    jpNbDataPerDeviceOAB.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpNbDataPerDeviceOAB.add(jlbNbDataPerDeviceOAB);
	    jtfNbDataPerDeviceOAB.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfNbDataPerDeviceOAB.getMinimumSize().height));
	    jpNbDataPerDeviceOAB.add(jtfNbDataPerDeviceOAB);
	    jtfNbDataPerDeviceOAB.setText(String.valueOf(TestLOSamples.lNbDataPerDeviceOAB));

	    // Tempo entre 2 envois
	    JPanel jplTempoEnvoiOAB = new JPanel();
	    jplTempoEnvoiOAB.setLayout(new BoxLayout(jplTempoEnvoiOAB, BoxLayout.LINE_AXIS));
	    jplTempoEnvoiOAB.add(Box.createRigidArea(new Dimension(30, 0)));
	    jplTempoEnvoiOAB.add(jlblTempoEnvoiOAB);
	    jtflTempoEnvoiOAB.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtflTempoEnvoiOAB.getMinimumSize().height));
	    jplTempoEnvoiOAB.add(jtflTempoEnvoiOAB);
	    jtflTempoEnvoiOAB.setText(String.valueOf(TestLOSamples.lTempoEnvoiOAB));

	    // Ajout du bouton d'action
	    JPanel jpButtonOAB = new JPanel();
	    boutonPubOABApp.addActionListener(new BoutonListenerPubOABApp()); 
	    jpButtonOAB.setLayout(new BoxLayout(jpButtonOAB, BoxLayout.LINE_AXIS));
	    jpButtonOAB.add(boutonPubOABApp);
	    
	    
	    /*
	     * 
	     * Panel Subscribe
	     * 
	     */
	    // Saisie de la route/Fifo
	    JPanel jplFifo = new JPanel();
	    jplFifo.setLayout(new BoxLayout(jplFifo, BoxLayout.LINE_AXIS));
	    jplFifo.add(Box.createRigidArea(new Dimension(30, 0)));
	    jplFifo.add(jlbTitleFifo);
	    jtfQueueName.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfQueueName.getMinimumSize().height));
	    jplFifo.add(jtfQueueName);
	    jtfQueueName.setText(String.valueOf(TestLOSamples.sQueueName));

	    // combo routes
	    JPanel jpcbRouters = new JPanel();
	    jcbRouters.addActionListener(new ActionListenerCBRouters());
	    jpcbRouters.setLayout(new BoxLayout(jpcbRouters, BoxLayout.LINE_AXIS));
	    jcbRouters.setMaximumSize(jcbRouters.getPreferredSize());
	    jpcbRouters.add(jcbRouters);
	    
	    // radio bouton
	    JPanel jpRBType = new JPanel();
	    jrbPubSub.addActionListener(new radioTypeActionListener());
	    jrbFifo.addActionListener(new radioTypeActionListener());
	    jrbFifo.setSelected(true);
	    jrbRouter.addActionListener(new radioTypeActionListener());
	    jrbLoraRouter.addActionListener(new radioTypeActionListener());
	    jrbLoraFifo.addActionListener(new radioTypeActionListener());
	    rbGroupSubscribe.add(jrbPubSub);
	    rbGroupSubscribe.add(jrbFifo);
	    rbGroupSubscribe.add(jrbRouter);
	    rbGroupSubscribe.add(jrbLoraRouter);
	    rbGroupSubscribe.add(jrbLoraFifo);
	    jpRBType.setLayout(new BoxLayout(jpRBType, BoxLayout.LINE_AXIS));
	    jpRBType.add(jrbPubSub);
	    jpRBType.add(jrbFifo);
	    jpRBType.add(jrbRouter);
	    jpRBType.add(jrbLoraRouter);
	    jpRBType.add(jrbLoraFifo);
	    
	    // Ajout du bouton d'action
	    JPanel jpButtonSubscribe = new JPanel();
	    boutonSubscribe.addActionListener(new BoutonListenerSubscribe()); 
	    jpButtonSubscribe.setLayout(new BoxLayout(jpButtonSubscribe, BoxLayout.LINE_AXIS));
	    jpButtonSubscribe.add(boutonSubscribe);

	    /*
	     * 
	     * Panel Push Data Airparif
	     * 
	     * 
	     */
	    // Device mode
	    jcbDeviceModePush.addActionListener(new CheckDeviceModePushActionListener());
	    jcbDeviceModePush.setSelected(TestLOSamples.bDeviceModePush); 

	    // Stream ID
	    JPanel jpStreamIDPush = new JPanel();
	    jpStreamIDPush.setLayout(new BoxLayout(jpStreamIDPush, BoxLayout.LINE_AXIS));
		jpStreamIDPush.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpStreamIDPush.add(jlbStreamIDPush);
	    jtfStreamIDPush.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfStreamIDPush.getMinimumSize().height));
	    jpStreamIDPush.add(jtfStreamIDPush);
	    jtfStreamIDPush.setText(TestLOSamples.sStreamIDPush);

	    // Topic 
	    JPanel jpTopicPush = new JPanel();
	    jpTopicPush.setLayout(new BoxLayout(jpTopicPush, BoxLayout.LINE_AXIS));
		jpTopicPush.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpTopicPush.add(jlbTopicPush);
	    jtfTopicPush.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfTopicPush.getMinimumSize().height));
	    jpTopicPush.add(jtfTopicPush);
	    jtfTopicPush.setText(TestLOSamples.DEFAULT_DEVICE_TOPIC_PUSH);

	    // Device Urn Prefix
	    JPanel jpDeviceUrnPrefixPush = new JPanel();
	    jpDeviceUrnPrefixPush.setLayout(new BoxLayout(jpDeviceUrnPrefixPush, BoxLayout.LINE_AXIS));
	    jpDeviceUrnPrefixPush.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDeviceUrnPrefixPush.add(jlbDeviceUrnPush);
	    jtfDeviceUrnPush.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDeviceUrnPush.getMinimumSize().height));
	    jpDeviceUrnPrefixPush.add(jtfDeviceUrnPush);
	    jtfDeviceUrnPush.setText(TestLOSamples.sDeviceUrnPush);

	    
	    JPanel jpCSVFile = new JPanel();
	    jpCSVFile.setLayout(new BoxLayout(jpCSVFile, BoxLayout.LINE_AXIS));
	    jpCSVFile.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpCSVFile.add(jlbCSVFileToOpen);
	    jpCSVFile.add(jtfCSVFile);
	    jtfCSVFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfCSVFile.getMinimumSize().height));
	    
	    JPanel jpPushPeriod = new JPanel();
	    jpPushPeriod.setLayout(new BoxLayout(jpPushPeriod, BoxLayout.LINE_AXIS));
	    jpPushPeriod.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpPushPeriod.add(jlbPushPeriod);
	    jpPushPeriod.add(jtfPushPeriodValue);
	    jtfPushPeriodValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfPushPeriodValue.getMinimumSize().height));

	    JPanel jpButtonPush = new JPanel();
	    boutonPushStart.addActionListener(new BoutonListenerStartPush()); 
	    boutonPushStop.addActionListener(new BoutonListenerStopPush()); 
	    boutonPushPause.addActionListener(new BoutonListenerPausePush()); 
	    jpButtonPush.setLayout(new BoxLayout(jpButtonPush, BoxLayout.LINE_AXIS));
	    jpButtonPush.add(boutonPushStart);
	    jpButtonPush.add(Box.createRigidArea(new Dimension(30, 30)));
	    jpButtonPush.add(boutonPushStop);
	    jpButtonPush.add(Box.createRigidArea(new Dimension(30, 30)));
	    jpButtonPush.add(boutonPushPause);
	    
	    JPanel jpTown = new JPanel();
	    jpTown.setLayout(new BoxLayout(jpTown, BoxLayout.LINE_AXIS));
	    jpTown.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpTown.add(jlbTown);
	    jpTown.add(jtfTown);
	    jtfTown.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfTown.getMinimumSize().height));
	    
	    JPanel jpDataModelPush = new JPanel();
	    jpDataModelPush.setLayout(new BoxLayout(jpDataModelPush, BoxLayout.LINE_AXIS));
	    jpDataModelPush.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDataModelPush.add(jlbDataModelPush);
	    jpDataModelPush.add(jtfDataModelPush);
	    jtfDataModelPush.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDataModelPush.getMinimumSize().height));

	    JPanel jpDataTagPush = new JPanel();
	    jpDataTagPush.setLayout(new BoxLayout(jpDataTagPush, BoxLayout.LINE_AXIS));
	    jpDataTagPush.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDataTagPush.add(jlbDataTagPush);
	    jpDataTagPush.add(jtfDataTagPush);
	    jtfDataTagPush.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDataTagPush.getMinimumSize().height));
	    
	    /*
	     * 
	     * Panel IFTTT
	     * 
	     * 
	     */
	    JPanel jpIFTTTMatchingRule = new JPanel();
	    jpIFTTTMatchingRule.setLayout(new BoxLayout(jpIFTTTMatchingRule, BoxLayout.LINE_AXIS));
	    jpIFTTTMatchingRule.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpIFTTTMatchingRule.add(jlbIFTTTMatchingRule);
	    jpIFTTTMatchingRule.add(jtfIFTTTMatchingRule);
	    jtfIFTTTMatchingRule.setText(TestLOSamples.sIFTTTDefaultMatchingRule);
	    jtfIFTTTMatchingRule.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfIFTTTMatchingRule.getMinimumSize().height));
	    JPanel jpIFTTTKey = new JPanel();
	    jtfIFTTTKey.addFocusListener(new FocusListener(){
	        public void focusGained(FocusEvent e){
	        }
	        public void focusLost(FocusEvent e) {
	          gatherConfigValues();
	        }
	    });
	    jpIFTTTKey.setLayout(new BoxLayout(jpIFTTTKey, BoxLayout.LINE_AXIS));
	    jpIFTTTKey.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpIFTTTKey.add(jlbIFTTTKey);
	    jpIFTTTKey.add(jtfIFTTTKey);
	    jtfIFTTTKey.setText(TestLOSamples.sIFTTTKey);
	    jtfIFTTTKey.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfIFTTTKey.getMinimumSize().height));
	    JPanel jpIFTTTEvent = new JPanel();
	    jtfIFTTTEvent.addFocusListener(new FocusListener(){
	        public void focusGained(FocusEvent e){
	        }
	        public void focusLost(FocusEvent e) {
	          gatherConfigValues();
	        }
	    });
	    jpIFTTTEvent.setLayout(new BoxLayout(jpIFTTTEvent, BoxLayout.LINE_AXIS));
	    jpIFTTTEvent.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpIFTTTEvent.add(jlbIFTTTEvent);
	    jpIFTTTEvent.add(jtfIFTTTEvent);
	    jtfIFTTTEvent.setText(TestLOSamples.sIFTTTEvent);
	    jtfIFTTTEvent.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfIFTTTEvent.getMinimumSize().height));
	    JPanel jpIFTTTURL = new JPanel();
	    jpIFTTTURL.setLayout(new BoxLayout(jpIFTTTURL, BoxLayout.LINE_AXIS));
	    jpIFTTTURL.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpIFTTTURL.add(jlbIFTTTURL);
	    jpIFTTTURL.add(jtfIFTTTURL);
	    jtfIFTTTURL.setText(TestLOSamples.sIFTTTURL);
	    jtfIFTTTURL.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfIFTTTURL.getMinimumSize().height));
	    JPanel jpMatchingRulesList = new JPanel();
	    jpMatchingRulesList.setLayout(new BoxLayout(jpMatchingRulesList, BoxLayout.LINE_AXIS));
	    jpMatchingRulesList.add(Box.createRigidArea(new Dimension(30, 0)));
	    // Ajout des boutons d'action
	    JPanel jpIFTTTButtonActivate = new JPanel();
	    boutonIFTTTActivate.addActionListener(new BoutonListenerIFTTTActivate()); 
	    boutonIFTTTMatchingRules.addActionListener(new BoutonListenerMatchingRules()); 
	    boutonIFTTTCheckFiringRules.addActionListener(new BoutonListenerCheckFiringRules()); 
	    jpIFTTTButtonActivate.setLayout(new BoxLayout(jpIFTTTButtonActivate, BoxLayout.LINE_AXIS));
	    jpIFTTTButtonActivate.add(boutonIFTTTActivate);
	    jpIFTTTButtonActivate.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpIFTTTButtonActivate.add(boutonIFTTTMatchingRules);
	    jpIFTTTButtonActivate.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpIFTTTButtonActivate.add(boutonIFTTTCheckFiringRules);
	    
	    
	    /*
	     * 
	     * Panel ConvertUDP_MQTT
	     * 
	     * 
	     */
	    // UDP Port
	    JPanel jpUDPPort = new JPanel();
	    jpUDPPort.setLayout(new BoxLayout(jpUDPPort, BoxLayout.LINE_AXIS));
	    jpUDPPort.add(jlbListenPortUDP);
		jpUDPPort.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpUDPPort.add(jtfListenPortUDP);
	    jtfListenPortUDP.setMaximumSize(new Dimension(100, jtfListenPortUDP.getMinimumSize().height));
	    jtfListenPortUDP.setText(Integer.toString(TestLOSamples.iUDPPort));
	    boutonUDPToMQTTStart.addActionListener(new BoutonListenerUDPToMQTTStart()); 

	    // Stream ID
	    JPanel jpStreamIDUDPToMQTT = new JPanel();
	    jpStreamIDUDPToMQTT.setLayout(new BoxLayout(jpStreamIDUDPToMQTT, BoxLayout.LINE_AXIS));
	    jpStreamIDUDPToMQTT.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpStreamIDUDPToMQTT.add(jlbStreamIDUDPToMQTT);
	    jpStreamIDUDPToMQTT.setMaximumSize(new Dimension(Integer.MAX_VALUE, jpStreamIDUDPToMQTT.getMinimumSize().height));
	    jpStreamIDUDPToMQTT.add(jtfStreamIDUDPToMQTT);
	    jtfStreamIDUDPToMQTT.setText(TestLOSamples.sStreamIDUDPToMQTT);

	    // Device Urn Prefix
	    JPanel jpDeviceUrnUDPToMQTT = new JPanel();
	    jpDeviceUrnUDPToMQTT.setLayout(new BoxLayout(jpDeviceUrnUDPToMQTT, BoxLayout.LINE_AXIS));
	    jpDeviceUrnUDPToMQTT.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDeviceUrnUDPToMQTT.add(jlbDeviceUrnUDPToMQTT);
	    jtfDeviceUrnUDPToMQTT.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDeviceUrnUDPToMQTT.getMinimumSize().height));
	    jpDeviceUrnUDPToMQTT.add(jtfDeviceUrnUDPToMQTT);
	    jtfDeviceUrnUDPToMQTT.setText(TestLOSamples.sDeviceUrnUDPToMQTT);

	    // Data model
	    JPanel jpDataModelUDPToMQTT = new JPanel();
	    jpDataModelUDPToMQTT.setLayout(new BoxLayout(jpDataModelUDPToMQTT, BoxLayout.LINE_AXIS));
	    jpDataModelUDPToMQTT.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDataModelUDPToMQTT.add(jlbDataModelUDPToMQTT);
	    jpDataModelUDPToMQTT.add(jtfDataModelUDPToMQTT);
	    jtfDataModelUDPToMQTT.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDataModelUDPToMQTT.getMinimumSize().height));

	    // Tag
	    JPanel jpDataTagUDPToMQTT = new JPanel();
	    jpDataTagUDPToMQTT.setLayout(new BoxLayout(jpDataTagUDPToMQTT, BoxLayout.LINE_AXIS));
	    jpDataTagUDPToMQTT.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpDataTagUDPToMQTT.add(jlbDataTagUDPToMQTT);
	    jpDataTagUDPToMQTT.add(jtfDataTagUDPToMQTT);
	    jtfDataTagUDPToMQTT.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfDataTagUDPToMQTT.getMinimumSize().height));
	    
	    /*
	     * 
	     * Construction de la fenetre
	     * 
	     */
	    //Panneau R�sultat
	    panOutput.setLayout(new BoxLayout(panOutput, BoxLayout.PAGE_AXIS));
	    panOutput.add(Box.createRigidArea(new Dimension(0, 5)));
	    panOutput.add(jlbSend, BorderLayout.WEST);
	    panOutput.add(scrollSend);
	    panOutput.add(Box.createRigidArea(new Dimension(0, 5)));
	    panOutput.add(jlbReceive, BorderLayout.WEST);
	    panOutput.add(scrollReceive);
	    //Panneau Config
	    panConfig.setLayout(new BoxLayout(panConfig, BoxLayout.PAGE_AXIS));
	    panConfig.add(Box.createRigidArea(new Dimension(0, 20)));
	    panConfig.add(jpCBSimul);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpKey);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpLoraKey);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpServer);
	    
	    // Panneau Multi Terminals
	    panMultiTerminal.setLayout(new BoxLayout(panMultiTerminal, BoxLayout.PAGE_AXIS));
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jcbDeviceMode, BorderLayout.LINE_START);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jpStreamID);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jpTopic);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jpDeviceUrnPrefix);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jpNbDevices);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jpNbDataPerDevice);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jplTempoEnvoi);
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 20)));
	    panMultiTerminal.add(jpButtonTerm);

	    // Panneau OAB App
	    panOABApp.setLayout(new BoxLayout(panOABApp, BoxLayout.PAGE_AXIS));
	    panOABApp.add(Box.createRigidArea(new Dimension(0, 5)));
	    panOABApp.add(jpStreamIDOAB);
	    panOABApp.add(Box.createRigidArea(new Dimension(0, 5)));
	    panOABApp.add(jpNbDevicesOAB);
	    panOABApp.add(Box.createRigidArea(new Dimension(0, 5)));
	    panOABApp.add(jpNbDataPerDeviceOAB);
	    panOABApp.add(Box.createRigidArea(new Dimension(0, 5)));
	    panOABApp.add(jplTempoEnvoiOAB);
	    panOABApp.add(Box.createRigidArea(new Dimension(0, 20)));
	    panOABApp.add(jpButtonOAB);

	    // Panneau Subscribe
	    panSubscribe.setLayout(new BoxLayout(panSubscribe, BoxLayout.PAGE_AXIS));
	    panSubscribe.add(Box.createRigidArea(new Dimension(0, 20)));
	    panSubscribe.add(jpRBType);
	    panSubscribe.add(Box.createRigidArea(new Dimension(0, 5)));
	    panSubscribe.add(jpcbRouters);
	    panSubscribe.add(Box.createRigidArea(new Dimension(0, 5)));
	    panSubscribe.add(jplFifo);
	    panSubscribe.add(Box.createRigidArea(new Dimension(0, 5)));
	    panSubscribe.add(scrollSubscribe);
	    panSubscribe.add(Box.createRigidArea(new Dimension(0, 5)));
	    panSubscribe.add(jpButtonSubscribe);
	    
	    // Paneau Push Data
	    panPushData.setLayout(new BoxLayout(panPushData, BoxLayout.PAGE_AXIS));
	    panPushData.add(Box.createRigidArea(new Dimension(0, 20)));
	    panPushData.add(jcbDeviceModePush, BorderLayout.LINE_START);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpStreamIDPush);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpTopicPush);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpDeviceUrnPrefixPush);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpCSVFile);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpPushPeriod);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpTown);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpDataModelPush);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpDataTagPush);
	    panPushData.add(Box.createRigidArea(new Dimension(0, 5)));
	    panPushData.add(jpButtonPush);

	    // Panneau IFTTT
	    panIFTTT.setLayout(new BoxLayout(panIFTTT, BoxLayout.PAGE_AXIS));
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 20)));
	    panIFTTT.add(jpIFTTTMatchingRule);
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panIFTTT.add(jpIFTTTKey);
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panIFTTT.add(jpIFTTTEvent);
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panIFTTT.add(jpIFTTTURL);
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panIFTTT.add(scrollIFTTT);
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panIFTTT.add(jpMatchingRulesList);
	    panIFTTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panIFTTT.add(jpIFTTTButtonActivate);

	    textPaneIFTTT.append("1) Subscribe to the route \"~event/v1/data/eventprocessing/fired\" to get events\n"
	    		+ "2) Copy/paste an \"enabled\" matching rule Id into the \"Matching Rule\" field (use the \"Get Matching Rules\" button).\n"
	    		+ "3) A firing rule has to be enabled with this Matching rule : check it.\n"
	    		+ "4) Get an IFTTT key and paste it into the \"Key\" field\n"
	    		+ "5) Create an Event with Webhooks (that can automate Twitter or an email) and paste it into the \"Event\" field \n"
	    		+ "6) To trigger an event : use the \"OAB App\" tab and publish data. In the case of a matching rule of temperature > 20, it will trigger when the published data will match.\n"
	    		);

	    // Panneau UDP To MQTT
	    panConvertUDPToMQTT.setLayout(new BoxLayout(panConvertUDPToMQTT, BoxLayout.PAGE_AXIS));
	    panConvertUDPToMQTT.add(Box.createRigidArea(new Dimension(0, 20)));
	    panConvertUDPToMQTT.add(jpUDPPort);
	    panConvertUDPToMQTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConvertUDPToMQTT.add(jpDeviceUrnUDPToMQTT);
	    panConvertUDPToMQTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConvertUDPToMQTT.add(jpStreamIDUDPToMQTT);
	    panConvertUDPToMQTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConvertUDPToMQTT.add(jpDataModelUDPToMQTT);
	    panConvertUDPToMQTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConvertUDPToMQTT.add(jpDataTagUDPToMQTT);
	    panConvertUDPToMQTT.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConvertUDPToMQTT.add(boutonUDPToMQTTStart);
	    
	    
	    // Ajout des onglets 
	    onglet.add("Configuration", panConfig);
	    onglet.add("Multi Terminals", panMultiTerminal);
	    onglet.add("OAB App", panOABApp);
	    onglet.add("Push Air Parif", panPushData);
	    onglet.add("Subscribe", panSubscribe);
	    onglet.add("IFTTT", panIFTTT);
	    onglet.add("Convert UDP To MQTT", panConvertUDPToMQTT);
	    onglet.add("Result", panOutput);
	    
	    //On passe ensuite les onglets au content pane
		this.getContentPane().add(onglet, BorderLayout.CENTER);

	    this.setContentPane(onglet);
	    this.setVisible(true);
	}	

	

	/**
	 * 
	 * Simule un groupe de devices fictifs
	 * 
	 */
	public static void simuleDevices()
	{
       int i;
       Thread t[] = new Thread[TestLOSamples.NB_MAX_DEVICES];
       String sURNDevice;

        
       // Cr�ation de NB_DEVICES
       for (i=0; i<TestLOSamples.nbDevices; i++){
    	   sURNDevice = String.format("%S%05d", TestLOSamples.DEVICE_URN_PREFIX, i);
    	   t[i] = new Thread(new RunGenerateTrafic(	sURNDevice, 
    			   									TestLOSamples.sDeviceTopic,
    			   									TestLOSamples.lNbDataPerDevice, 
    			   									TestLOSamples.lTempoEnvoi, 
    			   									TestLOSamples.bDeviceMode,
    			   									TestLOSamples.bPublish, 
    			   									TestLOSamples.fenetreTestLOSamples.textPaneSend));
    	   t[i].start();

 	       System.out.println("Thread : " + i);
       }
	}
	
	/*
	 * 
	 * Simule l'application de d�mo OAB
	 * 
	 * 
	 */
	public static void simuleOABApp()
	{

       Random rand = new Random();
       int i;
       Thread t[] = new Thread[TestLOSamples.NB_MAX_DEVICES];
       String sURNDevice;

        
       // Cr�ation de NB_DEVICES
       for (i=0; i<TestLOSamples.nbDevicesOAB; i++){
    	   sURNDevice = String.format("%S%05d", TestLOSamples.DEVICE_URN_PREFIX_OAB, i);
    	   t[i] = new Thread(new RunOABAppTraffic(	sURNDevice, 
    			   									TestLOSamples.lNbDataPerDeviceOAB, 
    			   									TestLOSamples.lTempoEnvoiOAB, 
    			   									TestLOSamples.bPublish, 
    			   									TestLOSamples.sStreamIDOAB,
    			   									TestLOSamples.sDataModelOAB,
    			   									TestLOSamples.sDataTagOAB,
    			   									TestLOSamples.fenetreTestLOSamples.textPaneSend));
    	   t[i].start();

 	       System.out.println("Thread : " + i);
       }
	   
	}
	/*
	 * 
	 * doSubscribeElements()
	 * 
	 */
	public static void doSubscribeElements()
	{
		Thread t;
		RunConsumeQueue consumeQueue = new RunConsumeQueue(
				TestLOSamples.sQueueName, 
				TestLOSamples.queueType, 
				TestLOSamples.fenetreTestLOSamples.textPaneReceive, 
				textPaneSubscribe);

		t = new Thread(consumeQueue);
		t.start();
        System.out.println("Thread : consume Queue");
	}

	/*
	 * 
	 * doPushValues()
	 * launch Airparif thread for pushing values
	 * 
	 */
	public static void doPushValues()
	{
		Thread t;
		RunPushValues pushValues = new RunPushValues(TestLOSamples.sCSVFilePush, 
													TestLOSamples.lTempoPush, 
													TestLOSamples.sStreamIDPush,
													TestLOSamples.sDeviceTopicPush,
													TestLOSamples.sDeviceUrnPush,
    			   									TestLOSamples.bDeviceModePush,
    			   									TestLOSamples.bPublish, 
    			   									TestLOSamples.sTown,
													TestLOSamples.fenetreTestLOSamples.textPaneReceive,
													boutonPushPause);

		t = new Thread(pushValues);
		t.start();
        System.out.println("Thread : PushValues");
	}
	
	/*
	 * 
	 * doPushIFTTT()
	 * Launch threads that push IFTTT events related to Live Objects events
	 * 
	 */
/*	public static void doPushIFTTT()
	{
		Thread t;
		RunPushIFTTT pushIFTTT = new RunPushIFTTT(
				TestLOSamples.sIFTTTURL,
				TestLOSamples.fenetreTestLOSamples.textPaneSend,
				TestLOSamples.fenetreTestLOSamples.textPaneReceive,
				textPaneIFTTT);

		t = new Thread(pushIFTTT);
		t.start();
        System.out.println("Thread : pushIFTTT");
	}
*/
		
	/*
	 * Checkbox Simulation
	 */
	class CheckSimulationActionListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	      System.out.println("source : " + ((JCheckBox)e.getSource()).getText() + " - �tat : " + ((JCheckBox)e.getSource()).isSelected());
	      TestLOSamples.bPublish = jcbSimulation.isSelected();
	    }
	}
	/*
	 * Checkbox Device Mode
	 */
	class CheckDeviceModeActionListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	      System.out.println("Device Mode : " + ((JCheckBox)e.getSource()).getText() + " - �tat : " + ((JCheckBox)e.getSource()).isSelected());
	      TestLOSamples.bDeviceMode = jcbDeviceMode.isSelected();
	    }
	}
	class CheckDeviceModePushActionListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	      System.out.println("Device Mode Push : " + ((JCheckBox)e.getSource()).getText() + " - �tat : " + ((JCheckBox)e.getSource()).isSelected());
	      TestLOSamples.bDeviceModePush = jcbDeviceModePush.isSelected();
	    }
	}
	/*
	 * Radio type de Queue
	 */
	class radioTypeActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		      System.out.println("RB Type Queue (pubsub/Fifo/Router) : " + jrbPubSub.isSelected() + "/" + jrbFifo.isSelected() + "/" + jrbRouter.isSelected());
		      if (jrbPubSub.isSelected()) 
		      {
		    	  TestLOSamples.queueType = QueueTypes.PUBSUB;
		    	  TestLOSamples.sQueueName = TestLOSamples.DEFAULT_PUBSUB;
		      }
		      if (jrbFifo.isSelected()) 
		      {
		    	  TestLOSamples.queueType = QueueTypes.FIFO;
		    	  TestLOSamples.sQueueName = TestLOSamples.DEFAULT_FIFO;
		      }
		      if (jrbRouter.isSelected()) 
		      {
		    	  TestLOSamples.queueType = QueueTypes.ROUTER;
		    	  jtfQueueName.setText(String.valueOf(jcbRouters.getSelectedItem()));
		    	  TestLOSamples.sQueueName = String.valueOf(jcbRouters.getSelectedItem());
		      }
		      if (jrbLoraRouter.isSelected()) 
		      {
		    	  TestLOSamples.queueType = QueueTypes.LORA_ROUTER;
		    	  TestLOSamples.sQueueName = TestLOSamples.DEFAULT_LORA_ROUTER;
		      }
		      if (jrbLoraFifo.isSelected()) 
		      {
		    	  TestLOSamples.queueType = QueueTypes.LORA_FIFO;
		    	  TestLOSamples.sQueueName = TestLOSamples.DEFAULT_LORA_FIFO;
		      }
	    	  jtfQueueName.setText(TestLOSamples.sQueueName);
		}
	}

	/*
	 * Combo Routers
	 * 
	 */
	class ActionListenerCBRouters implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
		    System.out.println("ActionListener : action sur " + jcbRouters.getSelectedItem());
		    jtfQueueName.setText(String.valueOf(jcbRouters.getSelectedItem()));
//		    jrbRouter.setSelected(true);
		    jrbRouter.doClick();
		}
	}

	/*
	 * Bouton lancement publication Terminaux
	 */
	class BoutonListenerPubTerminaux implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (gatherConfigValues())
				simuleDevices();
	    }
	  }
	
	/*
	 * Bouton lancement publication donn�es App OAB
	 */
	class BoutonListenerPubOABApp implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
	    	// Simuler l'appli Android OAB
			if (gatherConfigValues())
				simuleOABApp();
	    }
	  }

	/*
	 * Bouton souscription
	 */
	class BoutonListenerSubscribe implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
	    	// Simuler l'appli Android OAB
			if (gatherConfigValues())
				doSubscribeElements();
	    }
	  }

	/*
	 * Bouton push
	 */
	class BoutonListenerStartPush implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
	    	if (TestLOSamples.bPushPause){
				TestLOSamples.bPushPause = false;
				boutonPushPause.setText("Pause");
				gatherConfigValues();
	    	}
	    	else{
				// Start pushing Airparif CSV value file
				TestLOSamples.bPushStop = false;
				TestLOSamples.bPushPause = false;
				if (gatherConfigValues())
					doPushValues();
	    	}
	    }
	  }

	class BoutonListenerStopPush implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
	    	if (TestLOSamples.bPushPause){
				TestLOSamples.bPushPause = false;
				boutonPushPause.setText("Pause");
	    	}

	    	// Stop pushing Airparif CSV value file
			gatherConfigValues();
			TestLOSamples.bPushStop = true;
	    }
	  }
	
	class BoutonListenerPausePush implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
	    	// Pause on pushing Airparif CSV value file
			gatherConfigValues();
			TestLOSamples.bPushPause = true;
	    }
	  }

	class BoutonListenerIFTTTActivate implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			gatherConfigValues();
			
			// Add to the Matching rule list
			TestLOSamples.lIFTTTEvents.add(TestLOSamples.sIFTTTDefaultMatchingRule);
            System.out.println("Event activation on MatchingRule : " + TestLOSamples.sIFTTTDefaultMatchingRule);
    		textPaneIFTTT.setCaretPosition(textPaneIFTTT.getDocument().getLength());
    		textPaneIFTTT.append("Event activation on MatchingRule : " + TestLOSamples.sIFTTTDefaultMatchingRule + "\n");

	    }
	}

	class BoutonListenerMatchingRules implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			Thread t;
			RunGetMatchingRules matchingRules = new RunGetMatchingRules(TestLOSamples.fenetreTestLOSamples.textPaneIFTTT);

			t = new Thread(matchingRules);
			t.start();
	        System.out.println("Thread : RunGetMatchingRules");
	    }
	}

	class BoutonListenerCheckFiringRules implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			Thread t;
			
			gatherConfigValues();
			
			RunCheckFiringRules checkFiringRules = new RunCheckFiringRules(TestLOSamples.sIFTTTDefaultMatchingRule, TestLOSamples.fenetreTestLOSamples.textPaneIFTTT);
			
			t = new Thread(checkFiringRules);
			t.start();
	        System.out.println("Thread : RunCheckFiringRules");
	    }
	}

	/*
	 * 
	 * Bouton UDP To MQTT
	 * 
	 */
	class BoutonListenerUDPToMQTTStart implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			Boolean bRetour = gatherConfigValues();
			Thread t;
			
			if (!TestLOSamples.bUDPStart)
			{
				RunUDPToMQTT UDPToMQTT = new RunUDPToMQTT(	
//						TestLOSamples.bPublish,
						TestLOSamples.bDeviceModeUDPToMQTT,
						TestLOSamples.sDeviceUrnUDPToMQTT,
						TestLOSamples.sStreamIDUDPToMQTT,
						TestLOSamples.sTopicUDPToMQTT,
						TestLOSamples.iUDPPort,
						TestLOSamples.sDataModelUDPToMQTT,
						TestLOSamples.sDataTagUDPToMQTT,
						TestLOSamples.fenetreTestLOSamples.textPaneSend,
						TestLOSamples.fenetreTestLOSamples.textPaneReceive);
		
				
				t = new Thread(UDPToMQTT);
				t.start();
				
		        System.out.println("Thread : RunUDPToMQTT");
		        boutonUDPToMQTTStart.setText("Pause");
		        TestLOSamples.bUDPStart = true;
		        TestLOSamples.bUDPPause = false;
			}
			else
			{
		        // Pause => Resume
				if (TestLOSamples.bUDPPause)
		        {
			        TestLOSamples.bUDPPause = false;
			        boutonUDPToMQTTStart.setText("Pause");
		        }else 
		        // Activity => Pause
		        {
			        TestLOSamples.bUDPPause = true;
			        boutonUDPToMQTTStart.setText("Resume");
		        }
				
			}
	    }
	}

	
}


