package com.test.LOSamples;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JTabbedPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class TestLOFenetre extends JFrame {
	private JTabbedPane onglet = new JTabbedPane();
	public JTextArea textPane = new JTextArea();
	private JScrollPane scroll = new JScrollPane(textPane);
	private JPanel panFenetre = new JPanel();
	private JPanel panConfig = new JPanel();
	private JLabel jlbKey = new JLabel("Key : ");
	private static JTextField jtfKey = new JTextField();
	private JLabel jlbStreamID = new JLabel("StreamID : ");
	private static JTextField jtfStreamID = new JTextField();
	private JLabel jlbServer = new JLabel("Server : ");
	private static JTextField jtfServer = new JTextField();
	private JLabel jlbDeviceUrnPrefix = new JLabel("Device URN prefix : ");
	private static JTextField jtfDeviceUrnPrefix = new JTextField();
	private JLabel jlbNbDevices = new JLabel("Nb Devices to simulate : ");
	private static JTextField jtfNbDevices = new JTextField();
	private JLabel jlbNbDataPerDevice = new JLabel("Nb Data per Device : ");
	private static JTextField jtfNbDataPerDevice = new JTextField();
    public JButton boutonPubTerminaux =  new JButton("Publish Terminaux");
    public JButton boutonPubOABApp =  new JButton("Publish OAB App");

	private JCheckBox jckSimulation = new JCheckBox("Simulation");

	/*
	 * Mettre � jour les donn�es de configuration
	 */
	static void GatherConfigValues()
	{
//		TestLOSamples.sGetDataLinkBase = jtfReqBase.getText();
		TestLOSamples.sAPIKey = jtfKey.getText();
		TestLOSamples.sStreamID = jtfStreamID.getText();
	}
	
	public TestLOFenetre(){
		this.setTitle("Tests HTTP Live Objects");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

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

	    // Stream ID
	    JPanel jpStreamID = new JPanel();
	    jpStreamID.setLayout(new BoxLayout(jpStreamID, BoxLayout.LINE_AXIS));
		jpStreamID.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpStreamID.add(jlbStreamID);
	    jtfStreamID.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfStreamID.getMinimumSize().height));
	    jpStreamID.add(jtfStreamID);
	    jtfStreamID.setText(TestLOSamples.sStreamID);

	    // Server
	    JPanel jpServer = new JPanel();
	    jpServer.setLayout(new BoxLayout(jpServer, BoxLayout.LINE_AXIS));
	    jpServer.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpServer.add(jlbServer);
	    jtfServer.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfServer.getMinimumSize().height));
	    jpServer.add(jtfServer);
	    jtfServer.setText(TestLOSamples.sServer);

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
	    jtfNbDevices.setText(String.valueOf(TestLOSamples.iNbDevices));

	    // Nb Data Per device
	    JPanel jpNbDataPerDevice = new JPanel();
	    jpNbDataPerDevice.setLayout(new BoxLayout(jpNbDataPerDevice, BoxLayout.LINE_AXIS));
	    jpNbDataPerDevice.add(Box.createRigidArea(new Dimension(30, 0)));
	    jpNbDataPerDevice.add(jlbNbDataPerDevice);
	    jtfNbDataPerDevice.setMaximumSize(new Dimension(Integer.MAX_VALUE, jtfNbDataPerDevice.getMinimumSize().height));
	    jpNbDataPerDevice.add(jtfNbDataPerDevice);
	    jtfNbDataPerDevice.setText(String.valueOf(TestLOSamples.lNbDataPerDevice));

	    
	    JPanel jpButtons = new JPanel();
	    boutonPubTerminaux.addActionListener(new BoutonListenerPubTerminaux()); 
	    boutonPubOABApp.addActionListener(new BoutonListenerPubOABApp()); 
	    jpButtons.setLayout(new BoxLayout(jpButtons, BoxLayout.LINE_AXIS));
	    jpButtons.add(boutonPubTerminaux);
	    jpButtons.add(boutonPubOABApp);


	    //Panneau R�sultat
	    panFenetre.setLayout(new BoxLayout(panFenetre, BoxLayout.PAGE_AXIS));
	    panFenetre.add(scroll);
	    panFenetre.add(jpButtons);
	    
	    //Panneau Config
	    panConfig.setLayout(new BoxLayout(panConfig, BoxLayout.PAGE_AXIS));
	    panConfig.add(Box.createRigidArea(new Dimension(0, 10)));
	    panConfig.add(jpCBSimul);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpKey);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpStreamID);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpServer);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpDeviceUrnPrefix);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpNbDevices);
	    panConfig.add(Box.createRigidArea(new Dimension(0, 5)));
	    panConfig.add(jpNbDataPerDevice);

	    // Ajout des onglets 
	    onglet.add("Resultat", panFenetre);
	    onglet.add("Configuration", panConfig);
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
	 * Simule un groupe de devices fictifs
	 */
	public static void SimuleDevices()
	{
       Random rand = new Random();
       int i;
       Thread t[] = new Thread[TestLOSamples.NB_DEVICES];
       String sURNDevice;

        
       // Cr�ation de NB_DEVICES
       for (i=0; i<TestLOSamples.NB_DEVICES; i++){
    	   sURNDevice = String.format("%S%05d", TestLOSamples.DEVICE_URN_PREFIX, i);
    	   t[i] = new Thread(new RunGenerateTrafic(sURNDevice, TestLOSamples.NB_ECHANTILLONS, TestLOSamples.TEMPO_ENVOIS, TestLOSamples.bPublish, TestLOSamples.fenetreTestLOSamples.textPane));
    	   t[i].start();

 	       System.out.println("Thread : " + i);
       }
	}
	
	
	/*
	 * Bouton lancement publication Terminaux
	 */
	class BoutonListenerPubTerminaux implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GatherConfigValues();
			SimuleDevices();
	    }
	  }
	
	/*
	 * Bouton lancement publication donn�es App OAB
	 */
	class BoutonListenerPubOABApp implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			GatherConfigValues();
	    	// Simuler l'appli Android OAB
			TestLOSamples.SimuleOABApp();
	    }
	  }
}
