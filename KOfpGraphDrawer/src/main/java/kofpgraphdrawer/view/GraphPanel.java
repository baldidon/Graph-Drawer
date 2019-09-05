package kofpgraphdrawer.view;


//IMPORT
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import kofpgraphdrawer.controller.Controller;



public class GraphPanel extends JPanel implements MouseInputListener, ComponentListener {

    protected int width;
    protected int height;
    
    protected int X_CENTER = width/2;
    protected int Y_CENTER = height/2;
    
    protected CircularDrawing circle;
    protected NodeDrawing node;
    protected EdgeDrawing edge;
    
    protected boolean moving;
    protected boolean removingNodesAndEdges;
    protected boolean removingOnlyEdges;
    
    //per creazione di archi
    protected boolean selectingNodesForCreateEdges;
    //per creazione di nodi
    protected boolean insertNodeMode;
    
    /*Punti ausiliari;
    p: mouse clicked
    p1, p2: creazione archi
    */
    private Point point = null;
    private Point p1 = null;
    private Point p2 = null;
    
    //per spostamento degli archi
    private NodeDrawing draggedNode;
    private Dimension newSize = null;    
            
    protected GraphPanel(){
       
        super(new BorderLayout());
        this.setBackground(Color.white);
        
        this.circle = new CircularDrawing(this);
        
        this.removingNodesAndEdges = false;
        this.removingOnlyEdges = false;
        this.moving = false;
        this.selectingNodesForCreateEdges = false;
        //per ottenere le info sulla dimensione in tempo reale
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        
        this.setPreferredSize(getPreferredSize());  
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        circle.draw(g);  
        
        //Disegno gli archi
        for(Point[] points : Controller.getInstance().getGraphEdges()){
            if(points.length==2){
                edge = new EdgeDrawing(points[0], points[1], this);
                edge.draw(g);
            }
        }
        
        //Colora gli archi critici
        if(Controller.getInstance().getColourCritalEdges()){
            for(Point[] points : Controller.getInstance().getGraphEdges()){
                if(points.length==2){
                    edge = new EdgeDrawing(points[0], points[1], this);
                    edge.isSelected(true);
                    edge.draw(g);
                }
            }
            
        }
        
        //Disegno i nodi
        for(Point point : Controller.getInstance().getGraphNodes()){
            node = new NodeDrawing(point, this);
            node.draw(g);
        }
    }
        
    /*
    ** METODI AVVIATI DALL'INTERAZIONE CON I BOTTONI
    */
    
    /*
    //per fare lo zoom
    protected void setScaleFactor(double scaleFactor){
        this.circle.setScaleFactor(scaleFactor); 
        this.nodes.forEach((node1) -> {    
            node1.setScaleFactor(scaleFactor);
        });
        
    }*/
    
    //funge ma non bene
    public void setMovable(boolean b){
        this.moving = b;
    }
    
    public void setSelectionForEdges(boolean b){
        this.selectingNodesForCreateEdges = b;         
    }
    
    public void setSelectionForNodes(boolean b){
        this.insertNodeMode = b;
    }
    
    //il primo booleano, modifica removingNodesAndEdges, il secondo booleano removingOnlyEdges
    public void setRemovable(boolean b,boolean b1){
        this.removingNodesAndEdges = b;
        this.removingOnlyEdges = b1;
    }
    
  
    //per far comparire le barre serve fare così
    @Override
    public Dimension getPreferredSize(){
        
        return new Dimension(this.circle.getDrawingWidth()+20,
                            this.circle.getDrawingHeight()+20);
    }
    
    @Override
    public int getWidth(){
            return this.width;
    }
    
    @Override
    public int getHeight(){
            return this.height;
    }
    
    public CircularDrawing getCircle(){
        return this.circle;
    }
    
    //
    //MOUSE LISTENER METHODS
    //
    
    @Override
    public void mouseClicked(MouseEvent e){
        
        if(this.isPossibleAddNode(e.getX(), e.getY())){
            point = new Point(e.getX()-X_CENTER, e.getY()-Y_CENTER);
            Controller.getInstance().update("addNode");
        }
        //per cliccare e creare un arco, funge
        else if(this.selectingNodesForCreateEdges == true){ 
            
            for (Point auxPoint : Controller.getInstance().getGraphNodes()){
                node = new NodeDrawing(auxPoint, this);
                if (node.getFrameOfNode().contains(e.getX(), e.getY()) && p1 == null){
                    p1 = auxPoint;
                    int index = Controller.getInstance().getGraphNodes().indexOf(auxPoint);
                    View.getInstance().getInfoPanel().setTextOfLogArea("Node " + Controller.getInstance().getNodesLabels().get(index) +" selected!");
                }
                else if(node.getFrameOfNode().contains(e.getX(), e.getY()) && p1!=null && auxPoint != p1){
                    p2=auxPoint;
                    int index = Controller.getInstance().getGraphNodes().indexOf(auxPoint);
                    View.getInstance().getInfoPanel().setTextOfLogArea("Node " + Controller.getInstance().getNodesLabels().get(index) +" selected!");
                }
            }
            
            if(p1 !=null && p2!= null){
                Controller.getInstance().update("addEdge");
                p1 = null;
                p2 = null;
            }
        }
        
        //oltre a rimuovere il nodo, elimino anche gli archi che iniziavano o finivano in quel nodo; roba orribile lo so
        else if(this.isPossibleToRemoveNodes()){
            for (Point auxPoint : Controller.getInstance().getGraphNodes()){
                
                node = new NodeDrawing(auxPoint, this);
         
                if (node.getFrameOfNode().contains(e.getX() - X_CENTER, e.getY() - Y_CENTER)){
                    point = auxPoint;
                    Controller.getInstance().update("delNode");
                }
            }
        }
      
        else if(this.isPossibleToRemoveEdges()){
            Rectangle2D.Double rect = new Rectangle2D.Double(e.getX()-2, e.getY()-2, 4, 4);
            for (Point[] points : Controller.getInstance().getGraphEdges()){
                if(points.length==2){
                    edge = new EdgeDrawing(points[0], points[1], this);
                    if(edge.getFrameOfEdge().intersects(rect)){
                        p1 = points[0];
                        p2 = points[1];
                        Controller.getInstance().update("delEdge");
                    }
                }
            }
        }
    }

    
    
    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override 
    public void mouseReleased(MouseEvent e){}
    
    //PER IL MOVE
    @Override
    //DA FARE BENE LUNEDI
    public void mouseDragged(MouseEvent e){
        boolean found = false;
        if(this.isPossibleToMoveNodes()){
            double x=0;
            double y=0;
            for(Point auxPoint : Controller.getInstance().getGraphNodes()){
                node = new NodeDrawing(auxPoint, this);
                
                //posizione del mouse relative e scalate
                x = (e.getX() - X_CENTER)/MainGUI.scaleFactor;
                y = (e.getY() - Y_CENTER)/MainGUI.scaleFactor;
                
                if(node.getFrameOfNode().contains(x, y)){
                   found = true;
                   p1 = auxPoint;
                   draggedNode = new NodeDrawing(new Point((int)x, (int)y), this);
                   break;
                }
            }
            if(found){
                draggedNode.getFrameOfNode().setRect(x - NodeDrawing.DEFAULT_DIMENSION/2, 
                    y - NodeDrawing.DEFAULT_DIMENSION/2, NodeDrawing.DEFAULT_DIMENSION, NodeDrawing.DEFAULT_DIMENSION);
                double angle = Math.atan2(draggedNode.getNodeY(), draggedNode.getNodeX());
                p2 = new Point((int) (View.getInstance().getRadius() * Math.cos(angle)), 
                        (int)(View.getInstance().getRadius() * Math.sin(angle)));
                System.out.println("Point1: (" + p1.getX() + ", " + p1.getY() + ")");
                System.out.println("Point2: (" + p2.getX() + ", " + p2.getY() + ")");
                Controller.getInstance().update("setNode");
                View.getInstance().refreshGUI();
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e){}
    
    //
    //componentListener Methods
    //
    
    @Override
    public void componentHidden(ComponentEvent e) {}
    
    @Override
    public void componentMoved(ComponentEvent e) {}
    
    @Override
    public void componentShown(ComponentEvent e) {
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void componentResized(ComponentEvent e) {

        //per ottenere la dim in tempo reale del pannello
        this.newSize = e.getComponent().getBounds().getSize();
        this.width = (int)newSize.getWidth();
        this.height = (int)newSize.getHeight();
        
        this.X_CENTER = width/2;
        this.Y_CENTER = height/2;
        
        View.getInstance().refreshGUI();
    }
    
    
    
    //
    //Private Methods
    //
    
    private boolean isPossibleAddNode(double x, double y){
        if(this.circle.circleContains(x,y) && this.insertNodeMode && !removingNodesAndEdges && !removingOnlyEdges)
            return true;
        else
            return false;
    }

    
    private boolean isPossibleToRemoveNodes(){
        if(this.removingNodesAndEdges == true && this.removingOnlyEdges == false && !Controller.getInstance().getGraphNodes().isEmpty())
            return true;
        else
            return false;
        
    }
    
    private boolean isPossibleToRemoveEdges(){
        if(this.removingNodesAndEdges == false && this.removingOnlyEdges == true)
            return true;
        else
            return false;
    }

    private boolean isPossibleToMoveNodes(){
        if(!Controller.getInstance().getGraphNodes().isEmpty() && this.moving == true)
            return true;
        else
            return false;
    }
    
    protected Point getPoint(){
        return this.point;
    }
    
    protected Point getP1(){
        return this.p1;
    }

    protected Point getP2(){
        return this.p2;
    }
}
