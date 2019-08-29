package kofpgraphdrawer.view;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;



public class NodeDrawing /*extends AbstractDrawing*/{
    /*
    la mia idea era quella di collegare node del model, con la rappresentazione grafica per mezzo di questa classe
    devo riuscire a collegare il nodo con tutta la circonferenza
    ogni nodo lo devo vedere come come un oggetto    
    */
    
    private double x;
    private double y;
    private String label;
    protected double angle =0;
   
    
    //dimensione prefissata del nodo
    protected static double  DEFAULT_DIMENSION = 30.0; 
    protected GraphPanel graphPanel;
    protected Rectangle2D.Double node;
    protected boolean isSelected;
    
    
    
    //costruttore da rimuovere
    protected NodeDrawing(GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.isSelected = false;
        this.node = new Rectangle2D.Double();
    }
    
    //costruttore , dove gli passo le coordinate del centro del nodo relative rispetto al centro del pannello
    protected NodeDrawing(Point p, GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.x = p.getX() + this.graphPanel.getWidth()/2;
        this.y = p.getY() + this.graphPanel.getHeight()/2;
        
        this.node = new Rectangle2D.Double(this.x - NodeDrawing.DEFAULT_DIMENSION/2, this.y - NodeDrawing.DEFAULT_DIMENSION/2, NodeDrawing.DEFAULT_DIMENSION, NodeDrawing.DEFAULT_DIMENSION);
        
        this.isSelected = false;
        //un numero per inizializzare l'angolo, che non sia 0 in quanto un nodo ha sempre angolo 0 e ciò mi crea problemi nel resize violento della finestra
        //this.angle = Math.PI*3;
    }
    
    
    protected void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        
        //this.setDrawing();
        
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
            //g2d.drawString(this.logicalNode.getLabel(),(int)this.getNodeX()-3,(int)this.getNodeY()+3);
            
        }else{
            //questo dovrebbe avvenire quando seleziono i nodi per creare un arco!!
            g2d.setColor(Color.red);
            g2d.fill(this.node);
            g2d.setColor(Color.black);
            g2d.draw(this.node);
            g2d.setColor(Color.yellow);
            //g2d.drawString(this.logicalNode.getLabel(),(int)this.getNodeX()-3,(int)this.getNodeY()+3);
            
        }
    
    }
    
    public void isSelected(boolean b){
        this.isSelected = b;
    }
    
    //voglio la x del centro del "nodo"
    protected double getNodeX(){
        return this.node.getX()+DEFAULT_DIMENSION/2/**this.scaleFactor*/;
    }
    
    //the same
    protected double getNodeY(){
        return this.node.getY() + DEFAULT_DIMENSION/2/**this.scaleFactor*/;
        
    }
    
    /*@Override
    protected void rescaleDrawing(){
        double w = this.graphPanel.getCircle().getFrameOfCircle().getCenterX();
        double h = this.graphPanel.getCircle().getFrameOfCircle().getCenterY();
        double rateFactor=this.scaleFactor/this.previousScaleFactor;
        double x = ((this.node.getX() + NodeDrawing.DEFAULT_DIMENSION/2)-w)*rateFactor+w;
        double y = ((this.node.getY() + NodeDrawing.DEFAULT_DIMENSION/2)-h)*rateFactor+h;
        this.setRect(x-NodeDrawing.DEFAULT_DIMENSION/2, y-NodeDrawing.DEFAULT_DIMENSION/2);
    }*/
    
    
    private void setRect(double d, double d0) {
        this.node.setRect(d/*this.scaleFactor/this.previousScaleFactor*/,
                d0/**this.scaleFactor/this.previousScaleFactor*/,
                NodeDrawing.DEFAULT_DIMENSION,
                NodeDrawing.DEFAULT_DIMENSION);
    }
    
}
