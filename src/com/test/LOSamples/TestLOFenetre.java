package com.test.LOSamples;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JTabbedPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.test.LOSamples.TestLOSamples.QueueTypes;



public class TestLOFenetre extends JFrame {
	private JTabbedPane onglet = new JTabbedPane();
	public JTextArea textPane = new JTextArea();
	private JScrollPane scroll = new JScrollPane(textPane);
	private JPanel panOutput = new JPanel();
	private JPanel panConfig = new JPanel();
	private JPanel panMultiTerminal = new JPanel();
	private JPanel panOABApp = new JPanel();
	private JPanel panSubscribe = new JPanel();
	// Config
	private static JCheckBox jckSimulation = new JCheckBox("Simulation");
	private JLabel jlbKey = new JLabel("Key : ");
	private static JTextField jtfKey = new JTextField();
	private JLabel jlbServer = new JLabel("Server : ");
	private static JTextField jtfServer = new JTextField();
	// Multi Devices
	private JLabel jlbStreamID = new JLabel("StreamID : ");
	private static JTextField jtfStreamID = new JTextField();
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
	private JLabel jlbTitleFifo = new JLabel("Subscribe Fifo : ");
	private static JTextField jtffifoName = new JTextField();
	public static JTextArea textPaneSubscribe = new JTextArea();
	private JScrollPane scrollSubscribe = new JScrollPane(textPaneSubscribe);
	private JRadioButton jrbPubSub = new JRadioButton("Pubsub");
	private JRadioButton jrbFifo = new JRadioButton("Fifo");
	private JRadioButton jrbRouter = new JRadioButton("Router");
	private ButtonGroup rbGroupSubscribe = new ButtonGroup();
	public JButton boutonSubscribe =  new JButton("Subscribe");


	/*
	 * 
	 * 
	 * Mettre � jour les donn�es de configuration
	 * 
	 * 
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
	    TestLOSamples.bPublish = !jckSimulation.isSelected();
		TestLOSamples.sAPIKey = jtfKey.getText();
		
		/*
		 * Multi terminals
		 * 
		 */
		TestLOSamples.sStreamID = jtfStreamID.getText();
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
		TestLOSamples.sFifoName = jtffifoName.getText();

		return bGood;
	}
	
	
	/*
	 * 
	 * La fenetre
	 * 
	 */
	public TestLOFenetre(){
		this.setTitle("Generateur traffic Live Objects");
		this.setSize(500, 500);
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
	    jckSimulation.addActionListener(new CheckSimulationActionListener());
	    jckSimulation.setSelected(!TestLOSamples.bPublish); 
	    jpCBSimul.add(jckSimulation);
	    		
	    // API Key
	    JPanel jpKey = new JPanel();
	    jpKey.setLayout(new BoxLayout(jpKey, BoxLayout.LINE_AXIS));
		jpKey.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpKey.add(jlbKey);
	    jtfKey.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfKey.getMinimumSize().height));
	    jpKey.add(jtfKey);
	    jtfKey.setText(TestLOSamples.sAPIKey);

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
	    // Stream ID
	    JPanel jpStreamID = new JPanel();
	    jpStreamID.setLayout(new BoxLayout(jpStreamID, BoxLayout.LINE_AXIS));
		jpStreamID.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpStreamID.add(jlbStreamID);
	    jtfStreamID.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfStreamID.getMinimumSize().height));
	    jpStreamID.add(jtfStreamID);
	    jtfStreamID.setText(TestLOSamples.sStreamID);

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
	    

	    // Saisie de la Fifo
	    JPanel jplFifo = new JPanel();
	    jplFifo.setLayout(new BoxLayout(jplFifo, BoxLayout.LINE_AXIS));
	    jplFifo.add(Box.createRigidArea(new Dimension(30, 0)));
	    jplFifo.add(jlbTitleFifo);
	    jtffifoName.setMaximumSize(new Dimension(100, jtffifoName.getMinimumSize().height));
	    jplFifo.add(jtffifoName);
	    jtffifoName.setText(String.valueOf(TestLOSamples.sFifoName));

	    // radio bouton
	    JPanel jpRBType = new JPanel();
	    jrbPubSub.setSelected(true);
	    jrbPubSub.addActionListener(new radioTypeActionListener());
	    jrbFifo.addActionListener(new radioTypeActionListener());
	    jrbRouter.addActionListener(new radioTypeActionListener());
	    rbGroupSubscribe.add(jrbPubSub);
	    rbGroupSubscribe.add(jrbFifo);
	    rbGroupSubscribe.add(jrbRouter);
	    jpRBType.setLayout(new BoxLayout(jpRBType, BoxLayout.LINE_AXIS));
	    jpRBType.add(jrbPubSub);
	    jpRBType.add(jrbFifo);
	    jpRBType.add(jrbRouter);
	    
	    
	    // Ajout du bouton d'action
	    JPanel jpButtonSubscribe = new JPanel();
	    boutonSubscribe.addActionListener(new BoutonListenerSubscribe()); 
	    jpButtonSubscribe.setLayout(new BoxLayout(jpButtonSubscribe, BoxLayout.LINE_AXIS));
	    jpButtonSubscribe.add(boutonSubscribe);

	    
	    /*
	     * 
	     * Construction de la fenetre
	     * 
	     */
	    //Panneau R�sultat
	    panOutput.setLayout(new BoxLayout(panOutput, BoxLayout.PAGE_AXIS));
	    panOutput.add(scroll);
	    
	    //Panneau Config
	    panConfig.setLayout(new BoxLayout(panConfig, BoxLayout.PAGE_AXIS));
	    panConfig.add(Box.createRigidArea(new Dimension(0, 20)));
	    panConfig.add(jpCBSimul);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpKey);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpServer);
	    
	    // Panneau Multi Terminals
	    panMultiTerminal.setLayout(new BoxLayout(panMultiTerminal, BoxLayout.PAGE_AXIS));
	    panMultiTerminal.add(Box.createRigidArea(new Dimension(0, 5)));
	    panMultiTerminal.add(jpStreamID);
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
	    panSubscribe.add(jplFifo);
	    panSubscribe.add(Box.createRigidArea(new Dimension(0, 5)));
	    panSubscribe.add(jpButtonSubscribe);
	    panSubscribe.add(scrollSubscribe);
	    
	    // Ajout des onglets 
	    onglet.add("Result", panOutput);
	    onglet.add("Configuration", panConfig);
	    onglet.add("Multi Terminals", panMultiTerminal);
	    onglet.add("OAB App", panOABApp);
	    onglet.add("Subscribe", panSubscribe);
	    //On passe ensuite les onglets au content pane
		this.getContentPane().add(onglet, BorderLayout.CENTER);

	    this.setContentPane(onglet);
	    this.setVisible(true);
	}	

	
	/*
	 * Checkbox Simulation
	 */
	class CheckSimulationActionListener implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	      System.out.println("source : " + ((JCheckBox)e.getSource()).getText() + " - �tat : " + ((JCheckBox)e.getSource()).isSelected());
	      TestLOSamples.bPublish = jckSimulation.isSelected();
	    }
	}
	/*
	 * Radio type de Queue
	 */
	class radioTypeActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		      System.out.println("RB Type Queue (pubsub/Fifo/Router) : " + jrbPubSub.isSelected() + "/" + jrbFifo.isSelected() + "/" + jrbRouter.isSelected());
		      if (jrbPubSub.isSelected()) TestLOSamples.queueType = QueueTypes.PUBSUB;
		      if (jrbFifo.isSelected()) TestLOSamples.queueType = QueueTypes.FIFO;
		      if (jrbRouter.isSelected()) TestLOSamples.queueType = QueueTypes.ROUTER;
		}
	}

	/*
	 * Simule un groupe de devices fictifs
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
    			   									TestLOSamples.lNbDataPerDevice, 
    			   									TestLOSamples.lTempoEnvoi, 
    			   									TestLOSamples.bPublish, 
    			   									TestLOSamples.fenetreTestLOSamples.textPane));
    	   t[i].start();

 	       System.out.println("Thread : " + i);
       }
	}
	
	/*
	 * Simule l'application de d�mo OAB
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
    			   									TestLOSamples.fenetreTestLOSamples.textPane));
    	   t[i].start();

 	       System.out.println("Thread : " + i);
       }
	   
	}
	
	public static void doSubscribeElements()
	{
		Thread t;
		RunConsumeQueue consumeQueue = new RunConsumeQueue(TestLOSamples.sFifoName, TestLOSamples.queueType, TestLOSamples.fenetreTestLOSamples.textPane, textPaneSubscribe);

		t = new Thread(consumeQueue);
		t.start();
        System.out.println("Thread : consume Queue");
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
}
