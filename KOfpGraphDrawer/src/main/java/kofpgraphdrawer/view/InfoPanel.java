package kofpgraphdrawer.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class InfoPanel extends JPanel{

    /*divido questo pannello in due sotto pannelli, uno contenente log, altro info sul grafo creato(vertex arc)

    /*QUESTA PORZIONE ANDRà A POPOLARE L'INFOPANEL*/
    private final JTextArea logArea;
    private final JLabel logLabel;
    private final JLabel statusLabel;
    private JPanel logPanel;
    private JPanel statusPanel;
    private JScrollPane scrollLogArea;
    private JScrollPane scrollStatusArea;
    /*to-do*/
    private final Font font;
    private static InfoPanel infoPanel = null;
    
    //Variabili per albero, nodi e archi
    private JTree tree;
    //Creo la cartella root (principale)
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Graph");
    //Creo le cartelle "Nodes" e "Edges", saranno queste a contenere tutti gli archi e tutti i nodi
    DefaultMutableTreeNode nodeList;
    DefaultMutableTreeNode edgeList;
    
    
    protected InfoPanel(){
        super(new GridLayout(0,1));;

        this.statusLabel = new JLabel("STATUS");
        this.logLabel = new JLabel("LOG");
        this.font= new Font("verdana",Font.PLAIN,14);

        this.logArea = new JTextArea();
        this.logArea.setFont(font);
        this.logArea.setText("new session started");
        
        this.logArea.setRows(30);
        this.logArea.setColumns(20);
  
        this.setLogPanel();
        this.setStatusPanel();
    }  

    private void setLogPanel(){
        this.logPanel = new JPanel(new BorderLayout());

        this.scrollLogArea = new JScrollPane(logArea);
        this.logPanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,0));
        this.logPanel.add(logLabel,BorderLayout.NORTH);
        this.logPanel.add(scrollLogArea,BorderLayout.CENTER);
        this.add(logPanel);
        
    }

    private void setStatusPanel(){
        
        //Creo e aggiungo al root
        this.nodeList = new DefaultMutableTreeNode("Nodes");
        this.edgeList = new DefaultMutableTreeNode("Edges");
        this.root.add(this.nodeList);
        this.root.add(this.edgeList);
         
        //Creo l'albero con le due cartelle, il setVisible nasconde la cartella root ("Graph")
        this.tree = new JTree(this.root);
        this.tree.setRootVisible(true);
        
        //Creo scrollpane 
        this.scrollStatusArea = new JScrollPane(tree);
        this.scrollStatusArea.setBorder(BorderFactory.createEmptyBorder(0, 5, 0,0));
                
        //Aggiungo il pannello al Frame
        this.add(scrollStatusArea);
    }
    
    protected void clearStatusArea(){
        this.nodeList.removeAllChildren();
        this.edgeList.removeAllChildren();
        this.tree.updateUI();
    }
    
    protected void clearLogArea(){
        this.logArea.setText("new session started");
    }

    public void setTextOfLogArea(String s){
        
        this.logArea.append("\n" + s);
        /*
        tramite controller for model, devo far comunicare le info dell'aggiunta e rimozione di vertici e nodi        
        */
    }
    
    protected void addNodeToStatusArea(String nodeString){
        this.nodeList.add(new DefaultMutableTreeNode(nodeString));
        this.tree.expandRow(0);
        this.tree.updateUI();
    }
    
    protected void addEdgeToStatusArea(String edgeString){
        this.edgeList.add(new DefaultMutableTreeNode(edgeString));
        
        //Aggiungo anche l'arco anche sotto ogni nodo
        String idNode;
        String startNode = edgeString.substring(edgeString.indexOf(" ", 8)+1, edgeString.indexOf(" ", 10)).trim();
        String endNode = edgeString.substring(edgeString.lastIndexOf(" ")+1).trim();
        for (int i=0; i<this.nodeList.getChildCount(); i++){
            idNode = this.nodeList.getChildAt(i).toString();
            idNode = idNode.substring(idNode.indexOf(" ")+1).trim();
            DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode)this.nodeList.getChildAt(i);
            if (idNode.equals(startNode) || idNode.equals(endNode))
                tempNode.add(new DefaultMutableTreeNode(edgeString));
        }
        this.tree.updateUI();
    }
    
    protected void clearInfoPanel(){
        this.clearStatusArea();
        this.clearLogArea();
        
    }
    /*
    public static InfoPanel getInstance(){
			
	if(infoPanel != null){ //controllo se config è stato già istanziato
            return infoPanel;
	}
	else{
            infoPanel = new InfoPanel();
            return infoPanel;
        }
    }*/
    
    
    
    

}