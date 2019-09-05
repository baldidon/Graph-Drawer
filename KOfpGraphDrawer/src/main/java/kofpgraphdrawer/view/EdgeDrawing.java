package kofpgraphdrawer.view;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class EdgeDrawing{
    /*
    idea è quella di creare un oggetto arco che disegnerò, collegato all' oggetto arco e a quello grafo. con un metodo draw disegno tutti gli archi
    */
    
    protected GraphPanel graphPanel;
    protected Line2D.Double edge;
    protected Point p1;
    protected Point p2;
    protected boolean isSelected;
    
    //costruttore
    //gli passo due punti "visivi"
    public EdgeDrawing(Point p1, Point p2,GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.isSelected = false;
        this.p1 = p1;
        this.p2 = p2;
        
        this.edge = new Line2D.Double(this.p1.getX() + this.graphPanel.X_CENTER
                ,this.p1.getY() , this.p2.getX(), this.p2.getY());
    }

    protected void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            
            this.edge.setLine(this.p1.getX()*MainGUI.scaleFactor + this.graphPanel.X_CENTER, 
                    this.p1.getY()*MainGUI.scaleFactor + this.graphPanel.Y_CENTER, 
                    this.p2.getX()*MainGUI.scaleFactor + this.graphPanel.X_CENTER, 
                    this.p2.getY()*MainGUI.scaleFactor + this.graphPanel.Y_CENTER);
            
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            if(this.isSelected == false){
                g2d.setColor(Color.black);
                //System.out.println("sono un arco nero!");
            }
            else{
                g2d.setColor(Color.red);
                //System.out.println("sono un arco rosso");
            }
            g2d.draw(this.edge);
    }
    
    public Line2D.Double getFrameOfEdge(){
        return this.edge;
    }
    
    //primo punto dell'arco
    public Point getFirstNode(){
        return this.p1;
    }
    //ultimo punto dell'arco
    public Point getLastNode(){
        return this.p2;
    }
    
    public void isSelected(boolean b){
        this.isSelected = b;
    } 
}
