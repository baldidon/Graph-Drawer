package kofpgraphdrawer.view;
//import
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;

 


public class CircularDrawing {
    
    //creo abstractDrawing, mi tornerà utile per la rappresentazione di nodi, archi

    //abbiamo una variabile d'istanza di tipo  Rectangle.2D.Double
    protected static double QUADRATO_INSCRITTA_CIRCONFERENZA = 600.0;
    
    protected GraphPanel graphPanel;
    protected Ellipse2D.Double circle;
    

    
    public CircularDrawing(GraphPanel graphPanel){   
        this.graphPanel = graphPanel;
        this.circle = new Ellipse2D.Double(0,
                                            0,
                                            QUADRATO_INSCRITTA_CIRCONFERENZA,
                                            QUADRATO_INSCRITTA_CIRCONFERENZA);
    }

    //disegna la circonferenza
    public void draw(Graphics g) {
       Graphics2D g2d = (Graphics2D)g; //cast esplicito per lavorare con i metodi di graphics2D
       this.rescaleDrawing();
       
       //per un rendering migliore
       g2d.setRenderingHint(
           RenderingHints.KEY_ANTIALIASING,
           RenderingHints.VALUE_ANTIALIAS_ON);
       
       g2d.setColor(Color.gray);
       g2d.draw(this.circle);
       
    }
    
    public boolean circleContains(double x, double y){
        this.rescaleDrawing();
        Ellipse2D.Double circlem = new Ellipse2D.Double(this.circle.getX()+5, this.circle.getY()+5, this.circle.getWidth()-10, this.circle.getHeight()-10);
        Ellipse2D.Double circleM = new Ellipse2D.Double(this.circle.getX()-5,this.circle.getY()-5,this.circle.getWidth()+10,this.circle.getHeight()+10);
        if(!circlem.contains(x,y) && circleM.contains(x,y))
            return true;
        else{
            return false;
        }
    }
    
    //praticamente mi serve per far muovere il nodo solo sulla circonferenza
    public boolean railsForMovingNodes(double x, double y){
        this.rescaleDrawing();
        
        double q = NodeDrawing.DEFAULT_DIMENSION*Math.sqrt(2)/2;
        
        Ellipse2D.Double circlem = new Ellipse2D.Double(this.circle.getX()+5, this.circle.getY()+5, this.circle.getWidth()-(10), this.circle.getHeight()-(10));
        Ellipse2D.Double circleM = new Ellipse2D.Double(this.circle.getX()-5,this.circle.getY()-5,this.circle.getWidth()+(10),this.circle.getHeight()+(10));
        if(!circlem.contains(x,y) && circleM.contains(x,y))
            return true;
        else{
            return false;
        }

        
    }
    
    public Ellipse2D.Double getFrameOfCircle(){
        return this.circle;
    }
    
    public void rescaleDrawing(){
        double w = this.graphPanel.getWidth()/2;
        double h = this.graphPanel.getHeight()/2;
          this.circle.setFrame(w - QUADRATO_INSCRITTA_CIRCONFERENZA/2*(MainGUI.getScaleFactor()),
                                h - QUADRATO_INSCRITTA_CIRCONFERENZA/2*(MainGUI.getScaleFactor()),
                               QUADRATO_INSCRITTA_CIRCONFERENZA*(MainGUI.getScaleFactor()),
                               QUADRATO_INSCRITTA_CIRCONFERENZA*(MainGUI.getScaleFactor()));
   
          
    }
    
    protected int getDrawingWidth(){
        return (int)(this.circle.getX()+this.circle.getWidth());
    }

    protected int getDrawingHeight(){
        return (int)(this.circle.getY() + this.circle.getWidth());
    }
   
} // end class

