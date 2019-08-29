package kofpgraphdrawer.view;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import static kofpcirculardrawings.view.CircularDrawing.QUADRATO_INSCRITTA_CIRCONFERENZA;



public class NodeDrawing extends AbstractDrawing{
    /*
    la mia idea era quella di collegare node del model, con la rappresentazione grafica per mezzo di questa classe
    devo riuscire a collegare il nodo con tutta la circonferenza
    ogni nodo lo devo vedere come come un oggetto    
    */
    
    //per ottenere nodelist (?)
    protected Graph graph;
    protected Node logicalNode;
    private double x;
    private double y;
    private String label;
    protected CircularDrawing circle;
    protected double angle =0;
   
    
    //dimensione prefissata del nodo
    protected static double  DEFAULT_DIMENSION = 30.0; 
    protected GraphPanel graphPanel;
    protected Rectangle2D.Double node;
    protected boolean isSelected;
    
    
    
    //costruttore
    protected NodeDrawing(GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.isSelected = false;
        this.node = new Rectangle2D.Double();
        this.logicalNode = new Node("id","ble");
        this.circle = this.graphPanel.getCircle();
        
    }
    
    //costruttore 2, dove gli passo le coordinate
    protected NodeDrawing(double x, double y, GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.x = x;
        this.y = y;
        this.node = new Rectangle2D.Double(this.x, this.y, this.DEFAULT_DIMENSION, this.DEFAULT_DIMENSION);
        this.logicalNode = new Node("","");
        this.isSelected = false;
        this.logicalNode.setCoordinates((int)this.x,(int)this.y);
        this.circle = this.graphPanel.getCircle();
        //un numero per inizializzare l'angolo, che non sia 0 in quanto un nodo ha sempre angolo 0 e ciò mi crea problemi nel resize violento della finestra
        this.angle = Math.PI*3;
    }
    
    protected NodeDrawing(double x, double y,Node node, GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.x = x;
        this.y = y;
        this.node = new Rectangle2D.Double(this.x, this.y, this.DEFAULT_DIMENSION, this.DEFAULT_DIMENSION);
        this.logicalNode = node;
        this.isSelected = false;
        this.logicalNode.setCoordinates((int)this.x,(int)this.y);
        this.circle = this.graphPanel.getCircle();
        
        //un numero per inizializzare l'angolo, che non sia 0 in quanto un nodo ha sempre angolo 0 e ciò mi crea problemi nel resize violento della finesttra
        this.angle = Math.PI*3;
        
    }
    
    
    protected Node getLogicalNode(){
        return this.logicalNode;
    }
    
    @Override
    protected void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        
        this.setDrawing();
        
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        if(this.isSelected == false){
             g2d.setColor(Color.gray);
             g2d.fill(this.node);
             g2d.setColor(Color.black);
             g2d.draw(this.node);
             g2d.setColor(Color.white);
             g2d.drawString(this.logicalNode.getLabel(),(int)this.getNodeX()-3,(int)this.getNodeY()+3);
            
        }else{
            //questo dovrebbe avvenire quando seleziono i nodi per creare un arco!!
            g2d.setColor(Color.red);
            g2d.fill(this.node);
            g2d.setColor(Color.black);
            g2d.draw(this.node);
            g2d.setColor(Color.yellow);
            g2d.drawString(this.logicalNode.getLabel(),(int)this.getNodeX()-3,(int)this.getNodeY()+3);
            
        }
    
    }
    
    
    //metodo invocato nel mouse dragged per ricalcolare l'angolo tra centro e posizione del nodo
    public void setAngle(){
        double w = this.circle.getCircle().getCenterX();
        double h = this.circle.getCircle().getCenterY();

        //DISRANZA TRA COORDINATE CENTRO NODO E CENTRO CIRCONFERENZA
        double x = ((this.node.getX() + NodeDrawing.DEFAULT_DIMENSION/2)-w);
        double y = ((this.node.getY() + NodeDrawing.DEFAULT_DIMENSION/2)-h);
        this.angle =(Math.atan2(y, x));
    }
    
    protected void setDrawing(){
         //CENTRO DELLA CIRCONFERENZA
        double w = this.circle.getCircle().getCenterX();
        double h = this.circle.getCircle().getCenterY();
        
        
        //DISRANZA TRA COORDINATE CENTRO NODO E CENTRO CIRCONFERENZA
        double x = ((this.node.getX() + NodeDrawing.DEFAULT_DIMENSION/2)-w);
        double y = ((this.node.getY() + NodeDrawing.DEFAULT_DIMENSION/2)-h);
        //controllo se la variabile angolo sia stata inizializzata con un vero valore o meno
        if(this.angle == Math.PI*3)
           this.angle =  Math.atan2(y, x);
        
        double r = this.circle.circle.getWidth()/2;
        double coordX = Math.round(w + r* Math.cos(angle));
        double coordY = Math.round(h + r* Math.sin(angle));
        this.setRect(coordX -DEFAULT_DIMENSION/2,coordY -DEFAULT_DIMENSION/2);
    }
    
    
    //con questo metodo, posso evitare anche l'uso del rescale drawing!!!, msgari il rescale Drawing lo uso nel caso in cui il nodo non è stato inserito autonomamaente
    //potrebbe tornare utile nel caso in cui si ha l'import di un grafo già esistente, a questo metodo passo la dimensione dell'arrayList della classe grafo!! 
    public ArrayList<NodeDrawing> setNodes(int n){
        ArrayList<NodeDrawing> arrNode = new ArrayList();
        
        //only for alpha version
        double w = this.circle.circle.getCenterX();
        double h = this.circle.circle.getCenterY();
      
        for (int i = 0; i < n; i++) {
            double t = 2 * Math.PI * i / n;
            
            int x = (int) Math.round(w + (QUADRATO_INSCRITTA_CIRCONFERENZA*this.scaleFactor/2)* Math.cos(t));
            int y = (int) Math.round(h + (QUADRATO_INSCRITTA_CIRCONFERENZA*this.scaleFactor/2)* Math.sin(t));
            
            NodeDrawing newNode = new NodeDrawing(x - DEFAULT_DIMENSION/2*this.scaleFactor,
                                         y - DEFAULT_DIMENSION/2*this.scaleFactor,
                                         this.graphPanel);
            
            newNode.getLogicalNode().setCoordinates(x, y);
            
            arrNode.add(i, newNode);
            //int j = i+1;
            arrNode.get(i).getLogicalNode().setLabel(Integer.toString(i));
            arrNode.get(i).getLogicalNode().setID(Integer.toString(i));
            
            View.getInstance().addNodeToStatusArea(arrNode.get(i).getLogicalNode().toString());
       
        }
        //View.getInstance().addTextToStatusArea(arrNode.toString());
        
        return arrNode;
    }
    
    
    //gli passo una lista di nodi "logici" (ArrayList<node>) e ottengo nodi grafici!!
    public ArrayList<NodeDrawing> setNodesForLogicalNodes(ArrayList<Node> listLogicalNodes){
        int n = listLogicalNodes.size();
        ArrayList<NodeDrawing> visualNodes = new ArrayList(n);
        
        for(int i =0; i<n; i++){
            visualNodes.add(i,new NodeDrawing(0.0,0.0,listLogicalNodes.get(i),this.graphPanel));
            View.getInstance().addNodeToStatusArea(visualNodes.get(i).getLogicalNode().toString());
        }
        return visualNodes;
        
    }
    
    //only for testing, but works
    //meglio lavorare con le liste, perchè di dimensione dinamica rispetto agli array, in più c'è l'intenzione di interfacciarsi con le liste nella classe graph
    public ArrayList<EdgeDrawing> clique(ArrayList<NodeDrawing> nodes){
        int l = nodes.size();
        //dati n vertici, il numero di archi per avere un "grafo completo" sono [n(n-1)]/2
        //ogni vertice, ha n-1 collegamenti con altri archi
        ArrayList<EdgeDrawing> edges = new ArrayList();
        for(int i=0; i<l; i++){
            for(int j = i+1; j<l; j++){
                EdgeDrawing e = new EdgeDrawing(nodes.get(i), nodes.get(j), this.graphPanel);
                View.getInstance().addEdgeToStatusArea(e.getLogicalEdge().toString());
                edges.add(e);
                
            }
        }
        
        return edges;
    }
        
    
    
    public Rectangle2D.Double getFrameOfNode(){
        return this.node;
    }
    
    
    public void isSelected(boolean b){
        this.isSelected = b;
    }

    
    @Override
    protected int getDrawingWidth(){
        return (int)(this.DEFAULT_DIMENSION);
    }

    @Override
    protected int getDrawingHeight(){
        return (int)(this.DEFAULT_DIMENSION);
    }
    
    //voglio la x del centro del "nodo"
    protected double getNodeX(){
        return this.node.getX()+DEFAULT_DIMENSION/2/**this.scaleFactor*/;
    }
    
    //the same
    protected double getNodeY(){
        return this.node.getY() + DEFAULT_DIMENSION/2/**this.scaleFactor*/;
        
    }
    
    @Override
    protected void rescaleDrawing(){
        double w = this.circle.circle.getCenterX();
        double h = this.circle.circle.getCenterY();
        double rateFactor=this.scaleFactor/this.previousScaleFactor;
        double x = ((this.node.getX() + NodeDrawing.DEFAULT_DIMENSION/2)-w)*rateFactor+w;
        double y = ((this.node.getY() + NodeDrawing.DEFAULT_DIMENSION/2)-h)*rateFactor+h;
        this.setRect(x-NodeDrawing.DEFAULT_DIMENSION/2, y-NodeDrawing.DEFAULT_DIMENSION/2);
    }
    
    //AGGIUNTA AUTOMATICA, IN CUI GLI PASSO L'ARRAY DI NODI CHE CREO
    protected void rescaleDrawing(ArrayList<NodeDrawing> nodes){
        double w = this.circle.circle.getCenterX();
        double h = this.circle.circle.getCenterY();
        int n = nodes.size();
        for(int i=0; i<n; i++){
            double t = 2 * Math.PI * i / n;    
            int x = (int) Math.round(w + (QUADRATO_INSCRITTA_CIRCONFERENZA*this.scaleFactor/2)* Math.cos(t));
            int y = (int) Math.round(h + (QUADRATO_INSCRITTA_CIRCONFERENZA*this.scaleFactor/2)* Math.sin(t));
        
            nodes.get(i).setRect(x - this.DEFAULT_DIMENSION/2,
                y - this.DEFAULT_DIMENSION/2);
        }
    }
    
    protected void setRect(double d, double d0) {
        this.node.setRect(d/*this.scaleFactor/this.previousScaleFactor*/,
                d0/**this.scaleFactor/this.previousScaleFactor*/,
                NodeDrawing.DEFAULT_DIMENSION,
                NodeDrawing.DEFAULT_DIMENSION);
    }
    
}
