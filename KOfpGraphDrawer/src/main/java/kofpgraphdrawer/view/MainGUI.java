package kofpgraphdrawer.view;
//IMPORTS
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;
import java.io.File; 

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import kofpgraphdrawer.controller.Controller;




public class MainGUI extends JFrame implements ActionListener{

    /*
    provo ad implementare lo zoom della circonferenza
    
    
    version 0.03
    avevo pensato ad un'interfaccia grafica divisa in due parti
        la prima parte con il disegno e tutti i bottoni in alto ( tale parte più grande dell'altra) la chiamo "Graph panel"
        la seconda con info su numero di vertici inizializzati e numero di archi( e possibili fan) "info panel"
    */
    

    //--------
    //COSTANTI
    //--------
    private final static double[] SCALE_DRAW = { 0.20,0.40, 0.60 ,0.80, 1.0, 1.25, 1.50,1.60/*, 1.75, 2.00, 4.0, 8.00, 16.00, 24.00, 32.00, 64.00 */};
    private final static int DEFAULT_SCALE_INDEX = 4;
    
    private final static Object[] INSERT_NODE_OPTIONS = {"yes", "manually", "abort"};
    
    
    private final String SAVE_FILE = "SAVE";
    private final String LOAD_FILE = "LOAD";
    private final String DO_CLIQUE = "CLIQUE";
    private final String INSERT_NODE = "INSERT NODES";
    private final String INSERT_EDGE = "INSERT EDGES";
    private final String MOVE = "MOVE ";
    private final String REMOVE_NODES = "REMOVE NODES";
    private final String REMOVE_EDGES = "REMOVE EDGES";
    private final String ZOOM_IN = "ZOOM IN";
    private final String ZOOM_OUT = "ZOOM OUT";
    private final String CLEAR = "CLEAR";
    private final String FAN_PLANAR = "KFP";
    protected static final int PREFERRED_WIDTH = 1000;
    protected static final int PREFERRED_HEIGHT = 900;  


    //--------
    //VARIABILI D'ISTANZA
    //--------
    private GraphPanel graphPanel;
    private InfoPanel infoPanel;

    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JButton saveButton;
    private JButton loadButton;
    private JButton doCliqueButton;
    private JToggleButton insertEdgeButton;
    private JToggleButton insertNodeButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JToggleButton removeNodesButton;
    private JToggleButton removeEdgesButton;
    private JToggleButton moveButton;
    private JButton clearButton;
    private JButton fanPlanarButton;
    private GMLFileHandler fileHandler;
    private JToolBar toolBar;
    
    public static String filePath;
    public int numberOfNodesToGenerateAutomatically;
   

    //private int numberOfClicksForInsertNodeButton =0;
    private int SCALE_INDEX ;

    
    protected static MainGUI mainGui = null;
    
    public MainGUI(){
        super("Circular drawings editor");
        this.createNewGUI();
    }

    private void createNewGUI(){

        Container contPane = this.getContentPane();
        contPane.setLayout(new BorderLayout());
        this.infoPanel = new InfoPanel();
        this.setGraphPanel();
        this.SCALE_INDEX = DEFAULT_SCALE_INDEX;
        
        contPane.add(mainPanel,BorderLayout.CENTER);
        contPane.add(infoPanel,BorderLayout.EAST);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(PREFERRED_WIDTH,PREFERRED_HEIGHT));
        this.setMinimumSize(new Dimension(PREFERRED_WIDTH,PREFERRED_HEIGHT));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        try {
            this.setIconImage(ImageIO.read(new File("."+File.separator+"icon.png")));
        } catch (IOException ioe){
            System.out.println("Errore logo Finestra");
        }
        
        this.pack();
        
    }

    /*inizializzo il panello costituito da: toolbar contenente i bottoni e graphPanel"*/
    private void setGraphPanel(){
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());

        this.graphPanel = new GraphPanel();
        this.scrollPane = new JScrollPane(this.graphPanel);
        this.setToolbar();
        this.mainPanel.add(toolBar, BorderLayout.NORTH);
        this.mainPanel.add(scrollPane, BorderLayout.CENTER);

    }
   //alcuni bottoni non sono subito visibili, bensì compariranno dopo alcune azioni
    private void setToolbar(){
        
        this.toolBar = new JToolBar("options for circular drawings");
        this.toolBar.setFloatable(false);
        
        this.saveButton = new JButton(SAVE_FILE);
        this.saveButton.addActionListener(this);  
        this.toolBar.add(saveButton);
        this.saveButton.setVisible(false);
        
        this.loadButton = new JButton(LOAD_FILE);
        this.loadButton.addActionListener(this);
        this.toolBar.add(loadButton);
        
        //deve diventare un jtoggle
        this.insertNodeButton = new JToggleButton(INSERT_NODE,false);
        this.insertNodeButton.addActionListener(this);
        this.toolBar.add(insertNodeButton);
        
        this.insertEdgeButton = new JToggleButton(INSERT_EDGE,false);
        this.insertEdgeButton.addActionListener(this);
        this.toolBar.add(insertEdgeButton);
        this.insertEdgeButton.setVisible(false);
        
        this.zoomInButton = new JButton(ZOOM_IN);
        this.zoomInButton.addActionListener(this);
        this.toolBar.add(zoomInButton);
        this.zoomInButton.setVisible(false);
        
        this.zoomOutButton = new JButton(ZOOM_OUT);
        this.zoomOutButton.addActionListener(this);
        this.toolBar.add(zoomOutButton);
        this.zoomOutButton.setVisible(false);
     

        this.doCliqueButton = new JButton(DO_CLIQUE);
        this.doCliqueButton.addActionListener(this);
        this.toolBar.add(doCliqueButton);
        this.doCliqueButton.setVisible(false);
        
        this.removeNodesButton = new JToggleButton(REMOVE_NODES,false);
        this.removeNodesButton.addActionListener(this);
        this.toolBar.add(removeNodesButton);
        this.removeNodesButton.setVisible(false);
        
        this.removeEdgesButton = new JToggleButton(REMOVE_EDGES,false);
        this.removeEdgesButton.addActionListener(this);
        this.toolBar.add(removeEdgesButton);
        this.removeEdgesButton.setVisible(false);
        
        this.moveButton = new JToggleButton(MOVE,false);
        this.moveButton.addActionListener(this);
        this.toolBar.add(moveButton);
        this.moveButton.setVisible(false);
        
        this.clearButton = new JButton(CLEAR);
        this.clearButton.addActionListener(this);
        this.toolBar.add(clearButton);
        this.clearButton.setVisible(false);
        
        this.fanPlanarButton = new JButton(FAN_PLANAR);
        this.fanPlanarButton.addActionListener(this);
        this.toolBar.add(fanPlanarButton);
        this.fanPlanarButton.setVisible(false);
   
    }
    
    protected InfoPanel getInfoPanel(){
        return this.infoPanel;
    }
    
    protected GraphPanel getGraphPanel(){
        return this.graphPanel;
    }
    
    /*
    protected GMLFileHandler getFileHandler(){
        return this.fileHandler;
    }*/
    
    private void zoomIn(){
          if((this.SCALE_INDEX+1)< SCALE_DRAW.length){
               this.zoomOutButton.setEnabled(true);
               this.SCALE_INDEX++;
               this.graphPanel.setScaleFactor(SCALE_DRAW[this.SCALE_INDEX]);
               
          }
          else{
               this.infoPanel.setTextOfLogArea("zoom in max reached");
               this.zoomInButton.setEnabled(false);
          }
    }
    
    private void zoomOut(){
        if(this.SCALE_INDEX >0){
               this.zoomInButton.setEnabled(true);
               this.SCALE_INDEX--;
               this.graphPanel.setScaleFactor(SCALE_DRAW[this.SCALE_INDEX]);
                  
        
        }
        else{
               this.infoPanel.setTextOfLogArea("zoom out max reached");
               this.zoomOutButton.setEnabled(false);
        }
    }
    
 

    //potrei rifarlo con una sintassi switch-case
    @Override
    public void actionPerformed(ActionEvent event){//DEVO GESTIRE DUE AZIONI CON LO STESSO ACTION PERFORMED
				
        if(event.getSource() == saveButton){
            this.infoPanel.setTextOfLogArea("saving file..");
            fileHandler = new GMLFileHandler(this,SAVE_FILE,FileDialog.SAVE);
            filePath = fileHandler.getPathFile();
            Controller.getInstance().update("saveToFile");           
        }
        
        else if(event.getSource() == loadButton){
            this.infoPanel.setTextOfLogArea("loading file..");
            fileHandler = new GMLFileHandler(this,LOAD_FILE,FileDialog.LOAD);
            filePath = fileHandler.getPathFile();
            Controller.getInstance().clearGraph();
            if(Controller.getInstance().update("loadFromFile")){
                
                /*this.removeNodesButton.setVisible(true);
                this.removeEdgesButton.setVisible(true);
                this.doCliqueButton.setVisible(true);
                this.moveButton.setVisible(true);
                this.clearButton.setVisible(true);
                this.fanPlanarButton.setVisible(true);
                this.zoomInButton.setVisible(true);
                this.insertEdgeButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.zoomOutButton.setVisible(true);
                */
                this.infoPanel.setTextOfLogArea("File imported succesfully!");
                //da mettere nel refreshGUI()
                //View.getInstance().
                //this.graphPanel.repaint();
            }
            else
                this.infoPanel.setTextOfLogArea("Error in importing file!");
        }
        
        
        else if(event.getSource() == insertNodeButton){
            if(insertNodeButton.isSelected() && Controller.getInstance().getGraphNodes().isEmpty()){
                int n = JOptionPane.showOptionDialog(this,"Would you add nodes automatically?","Question",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,INSERT_NODE_OPTIONS,INSERT_NODE_OPTIONS[2]);
                switch (n) {
                    case 0:
                        try{
                            this.infoPanel.setTextOfLogArea("'auto insert node' mode enabled");    
                            //this.graphPanel.setNumberOfNodes(
                            this.numberOfNodesToGenerateAutomatically = Integer.valueOf(JOptionPane.showInputDialog(this, "number of nodes to add"));
                            
                            if(this.numberOfNodesToGenerateAutomatically<1){
                                JOptionPane.showMessageDialog(this,"ERROR \nthe number must be at least 1!","ERROR",JOptionPane.ERROR_MESSAGE);
                                this.infoPanel.setTextOfLogArea("error in runtime \nno nodes added to graph");
                            }    
                            else{
                                Controller.getInstance().update("addMultipleNodes");
                                this.saveButton.setVisible(true);
                                this.removeNodesButton.setVisible(true);
                                this.removeEdgesButton.setVisible(true);
                                this.doCliqueButton.setVisible(true);
                                this.moveButton.setVisible(true);
                                this.clearButton.setVisible(true);
                                this.fanPlanarButton.setVisible(true);
                                this.insertEdgeButton.setVisible(true);
                                this.zoomInButton.setVisible(true);
                                this.zoomOutButton.setVisible(true);
                            }
                        }
                        catch(NumberFormatException nfe){
                            this.infoPanel.setTextOfLogArea("error in runtime \nno nodes added to graph");
                        }   //per ora utile, ma da cambiare appena possibile
                        this.insertNodeButton.doClick();
                        break;
                        
                    case 1:
                        
                        this.infoPanel.setTextOfLogArea("'manual insert node' mode enabled");
                        this.graphPanel.setSelectionForNodes(true);
                        this.graphPanel.setRemovable(false,false);
                        this.graphPanel.setSelectionForEdges(false);
                        this.insertEdgeButton.setEnabled(false);
                        this.removeNodesButton.setEnabled(false);
                        this.removeEdgesButton.setEnabled(false);
                        this.moveButton.setEnabled(false);
                        Controller.getInstance().update("addNode");
                        break;
                    case 2:
                        this.insertNodeButton.doClick();
                        break;
                    default:
                        break;
                }
            }
            else if(this.insertNodeButton.isSelected()){
                this.infoPanel.setTextOfLogArea("'insert node' mode enabled");    
                this.graphPanel.setSelectionForNodes(true);
                this.graphPanel.setRemovable(false,false);
                this.graphPanel.setSelectionForEdges(false);
                this.removeNodesButton.setVisible(true);
                this.removeEdgesButton.setVisible(true);
                this.doCliqueButton.setVisible(true);
                this.moveButton.setVisible(true);
                this.clearButton.setVisible(true);
                this.fanPlanarButton.setVisible(true);
                this.insertEdgeButton.setVisible(true);
                this.zoomInButton.setEnabled(true);
                this.zoomOutButton.setEnabled(true);
                this.insertEdgeButton.setEnabled(false);
                this.removeNodesButton.setEnabled(false);
                this.removeEdgesButton.setEnabled(false);
                this.moveButton.setEnabled(false);
            }
            else{
                this.infoPanel.setTextOfLogArea("'insert node' disabled");
                this.graphPanel.setSelectionForNodes(false);
                this.removeNodesButton.setVisible(true);
                this.removeEdgesButton.setVisible(true);
                this.insertEdgeButton.setVisible(true);
                this.doCliqueButton.setVisible(true);
                this.moveButton.setVisible(true);
                this.clearButton.setVisible(true);
                this.fanPlanarButton.setVisible(true);
                
                this.insertEdgeButton.setEnabled(true);
                this.removeNodesButton.setEnabled(true);
                this.removeEdgesButton.setEnabled(true);
                this.moveButton.setEnabled(true);
            }
        }
        
        else if(event.getSource() == insertEdgeButton){
            if(this.insertEdgeButton.isSelected() == true){
                doCliqueButton.setEnabled(false);
                this.infoPanel.setTextOfLogArea("'insert edge' mode enabled");
                this.graphPanel.setSelectionForEdges(true);
                this.graphPanel.setSelectionForNodes(false);
                this.graphPanel.setRemovable(false,false);
                this.insertNodeButton.setEnabled(false);
                this.removeNodesButton.setEnabled(false);
                this.removeEdgesButton.setEnabled(false);
                this.moveButton.setEnabled(false);
            }else{
                doCliqueButton.setEnabled(true);
                this.infoPanel.setTextOfLogArea("'insert edge' mode disabled");
                this.graphPanel.setSelectionForEdges(false);
                this.insertNodeButton.setEnabled(true);
                this.removeNodesButton.setEnabled(true);
                this.removeEdgesButton.setEnabled(true);
                this.moveButton.setEnabled(true);
            }
           
            
        }
        else if(event.getSource() == zoomInButton){
            zoomIn();
            this.infoPanel.setTextOfLogArea("zoom in! Scale factor: " + SCALE_DRAW[SCALE_INDEX]);
            this.graphPanel.revalidate();
            this.graphPanel.repaint();

        }
        else if(event.getSource() == zoomOutButton){
            zoomOut();
            this.infoPanel.setTextOfLogArea("zoom out! Scale factor: " +SCALE_DRAW[SCALE_INDEX]);
            this.graphPanel.revalidate();
            this.graphPanel.repaint();
            
        }
        else if(event.getSource() == doCliqueButton){
            this.infoPanel.setTextOfLogArea("clique!");
            this.graphPanel.setClique();
            
        }
        else if(event.getSource() == removeNodesButton){
            if(this.removeNodesButton.isSelected() == true){
                this.infoPanel.setTextOfLogArea("'remove node' mode enabled");
                this.graphPanel.setMovable(false);
                this.insertEdgeButton.setEnabled(false);
                this.insertNodeButton.setEnabled(false);
                this.moveButton.setEnabled(false);
                this.doCliqueButton.setEnabled(false);   
                if(this.graphPanel.nodes.isEmpty())
                    JOptionPane.showMessageDialog(this, "ERROR \n NO NODES TO REMOVE!!!!","Error message", JOptionPane.ERROR_MESSAGE);
                else
                    this.graphPanel.setRemovable(true,false);           
            }
            else{
                this.infoPanel.setTextOfLogArea("'remove node' mode disabled");
                this.graphPanel.setRemovable(false,false);
                this.moveButton.setEnabled(true);
                this.insertEdgeButton.setEnabled(true);
                this.insertNodeButton.setEnabled(true);
                this.doCliqueButton.setEnabled(true);
            }
            
        }
        else if(event.getSource() == removeEdgesButton){
            if(this.removeEdgesButton.isSelected() == true){    
                this.infoPanel.setTextOfLogArea("'remove edges' mode enabled");
                this.graphPanel.setMovable(false);
                this.insertEdgeButton.setEnabled(false);
                this.insertNodeButton.setEnabled(false);
                this.moveButton.setEnabled(false);
                this.doCliqueButton.setEnabled(false);
            
                if(this.graphPanel.edges.isEmpty())
                    JOptionPane.showMessageDialog(this, "ERROR \n NO EDGES TO REMOVE!!!!","Error message", JOptionPane.ERROR_MESSAGE);
                else
                    this.graphPanel.setRemovable(false, true);
            }
            else{
                this.infoPanel.setTextOfLogArea("'remove edges' mode disabled");
                this.graphPanel.setRemovable(false,false);
                this.moveButton.setEnabled(true);
                this.insertEdgeButton.setEnabled(true);
                this.insertNodeButton.setEnabled(true);
                this.doCliqueButton.setEnabled(true);
                this.graphPanel.setSelectionForEdges(false);
            }
            
        }
        else if(event.getSource() == moveButton){
            if(this.moveButton.isSelected() == true && !this.graphPanel.nodes.isEmpty()){
                this.infoPanel.setTextOfLogArea("free mode enabled");
                this.graphPanel.setMovable(true);
                this.graphPanel.setRemovable(false,false);
                this.removeNodesButton.setEnabled(false);
                this.removeEdgesButton.setEnabled(false);
            }
            else if(this.moveButton.isSelected() == false){
                this.infoPanel.setTextOfLogArea("free mode disabled");
                this.graphPanel.setMovable(false);
                this.removeNodesButton.setEnabled(true);
                this.removeEdgesButton.setEnabled(true);
            }
            else if(this.graphPanel.nodes.isEmpty()){
                JOptionPane.showMessageDialog(this, "ERROR \n NO NODES TO MOVE!!!!","Error message", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(event.getSource() == clearButton){
            if(JOptionPane.showConfirmDialog(this,"Are you sure to clear the workspace?\n","CAUTION", JOptionPane.YES_NO_OPTION)==0){
                View.getInstance().clearStatusArea();
                View.getInstance().clearLogArea();
                this.graphPanel.clearWorkspace();
                //numberOfClicksForInsertNodeButton = 0;
                this.graphPanel.setMovable(false);
                this.graphPanel.setRemovable(false,false);
                while(SCALE_INDEX != DEFAULT_SCALE_INDEX){
                    if(SCALE_INDEX<DEFAULT_SCALE_INDEX)
                        zoomIn();
                    else
                        zoomOut();
                }
                
                this.graphPanel.repaint();
            }
        }
        else if(event.getSource() == fanPlanarButton){
            try{
                int k = Integer.valueOf(JOptionPane.showInputDialog(this, "Insert k parameter"));
                boolean ris = this.graphPanel.fanPlanarityOfGraph(k);
                this.infoPanel.setTextOfLogArea("The graph is " + k + " fan-planar:" + ris);
                
                
                //dopo 5 secondi, mi decolora gli archi selezionati che non mi danno la fan-planarità
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        View.getInstance().getGraphPanel().clearSelectionOfEdges();
                    }
                };
                timer.schedule(task, 5000);
            }
            catch(NumberFormatException nfe){
                this.infoPanel.setTextOfLogArea("error in runtime \nFor k parameter, insert a number\n");
            }
        }
    }

    
    
    //mostra i bottoni a seconda della situazione dei nodi
    protected void showButtons(boolean b){
        this.saveButton.setVisible(b);
        this.removeNodesButton.setVisible(b);
        this.removeEdgesButton.setVisible(b);
        this.doCliqueButton.setVisible(b);
        this.moveButton.setVisible(b);
        this.clearButton.setVisible(b);
        this.fanPlanarButton.setVisible(b);
        this.insertEdgeButton.setVisible(b);
        this.zoomInButton.setVisible(b);
        this.zoomOutButton.setVisible(b);
    }
    /*
    //per ottenere l'istanza del mainGUI
    public static MainGUI getInstance(){
			
	if(mainGui != null){ //controllo se maingui è stato già istanziato

            return mainGui;
	}
	else{
            mainGui = new MainGUI();
            return mainGui;
        }
    }*/
        


}