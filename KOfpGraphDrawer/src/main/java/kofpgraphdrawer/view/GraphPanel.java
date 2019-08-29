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
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;
import kofpgraphdrawer.controller.Controller;
//import kofpcirculardrawings.model.Node;



public class GraphPanel extends JPanel implements MouseInputListener, ComponentListener {

    
    protected int width;
    protected int height;
    protected CircularDrawing circle;
    protected NodeDrawing node;
    protected ArrayList<NodeDrawing> nodes;
    protected EdgeDrawing edge;
    protected ArrayList<EdgeDrawing> edges;
    protected boolean moving;
    protected boolean removingNodesAndEdges;
    protected boolean removingOnlyEdges;
    //per creazione di archi
    protected boolean selectingNodesForCreateEdges;
    //per creazione di nodi
    protected boolean selecting;
    //per creazione di archi
    private NodeDrawing n1 = null;
    private NodeDrawing n2 = null;
    //per spostamento degli archi
    private NodeDrawing draggedNode;
    private Dimension newSize = null;    
    
    protected Point coordinatesMouse;
    
    protected GraphPanel(){
       
        super(new BorderLayout());
        this.setBackground(Color.white);
        
        this.circle = new CircularDrawing(this);
        this.node = new NodeDrawing(this);
        this.edge = new EdgeDrawing(this);
        
        this.edges = new ArrayList();
        this.nodes = new ArrayList();
        
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
        if(!edges.isEmpty()){
            for(EdgeDrawing ed : this.edges){
                ed.getFirstNode().setDrawing();
                ed.getLastNode().setDrawing();
                ed.draw(g);
            }
        }
        if(!nodes.isEmpty()){
            for(NodeDrawing no : this.nodes){
                no.draw(g);
            }
        }
    }
        
    /*
    ** METODI AVVIATI DALL'INTERAZIONE CON I BOTTONI
    */
    
    //per fare lo zoom
    protected void setScaleFactor(double scaleFactor){
        this.circle.setScaleFactor(scaleFactor); 
        this.nodes.forEach((node1) -> {    
            node1.setScaleFactor(scaleFactor);
        });
        
    }
    
    //metodo che mi serve per ottenere da MainGUI il numero di nodi
    protected void setNumberOfNodes(int n){
        View.getInstance().clearStatusArea();
        this.nodes = this.node.setNodes(n);
        
        this.nodes.forEach((no) -> {
            //graph.addNode(no.getLogicalNode());
        });
        
        this.revalidate();
        this.repaint();
        //String s = this.graph.graphToString();
        //System.out.println(s);
    }
    
    //only for testing.
    protected void setClique(){
        if(nodes.isEmpty()){
            JOptionPane.showMessageDialog(this, "ERROR \n NO NODES FOR CLIQUE!!!!","Error message", JOptionPane.ERROR_MESSAGE);
        }
        else{
                this.edges = null;
                this.edges = this.node.clique(nodes);
                this.graph.Clique();
                //String s = this.graph.graphToString();
                //System.out.println(s);
        }
        this.revalidate();
        this.repaint();
    } 
    
    //funge ma non bene
    public void setMovable(boolean b){
        this.moving = b;
    }
    
    public void setSelectionForEdges(boolean b){
        this.selectingNodesForCreateEdges = b;
            
            
    }
    public void setSelectionForNodes(boolean b){
        this.selecting = b;
    }
    
    //il primo booleano, modifica removingNodesAndEdges, il secondo booleano removingOnlyEdges
    public void setRemovable(boolean b,boolean b1){
        this.removingNodesAndEdges = b;
        this.removingOnlyEdges = b1;
    }
    public void clearWorkspace(){
        this.nodes.clear();
        this.edges.clear();
        if(this.graph!= null){
            this.graph.getEdgeList().clear();
            this.graph.getNodeList().clear();
        }
        
    }
    
    /*
        TODO
        questo modo di lavorare funziona, però non va bene passargli l'oggetto graph direttamente facendo un import. Qui servirebbe avere l'oggetto controller for view; che con un metodo statico mi permette di 
        intervenire sull'oggetto grafo.
        DOVRÀ ANDARE NEL "CONTROLLER!"
    */
    public void LoadGraph(){
        
        this.setRemovable(false,false);
        this.setSelectionForEdges(false);
        this.setSelectionForNodes(false);
        this.setMovable(false);
        
        this.clearWorkspace();
        
        Controller.getInstance().getGraphNodes();
        Controller.getInstance().getGraphEdges();
       
        this.nodes = this.node.setNodesForLogicalNodes(this.graph.getNodeList());
        this.node.rescaleDrawing(this.nodes);
        this.edges = this.edge.setEdgeDrawingForLogicalEdges(this.graph.getEdgeList(),this.nodes);
        this.repaint();
        
    }
    
    
    
    //non funziona, perchè alla creazione di ogni arco o nodo non lo riaggiungo alla lista, eo
    public void saveGraph(){
        Controller.getInstance().update("saveToFile");
    }
    
    public boolean fanPlanarityOfGraph(int k){
        if(this.graph.isFanPlanar(k) == true)
            return true;
        else{
            this.graph.getKfpEdgeList().forEach((e) -> {
                for(EdgeDrawing ed : this.edges){
                    if(ed.getLogicalEdge().toString().equals(e.toString())){
                        ed.isSelected(true);
                        System.out.println(e.toString());
                    }
                }
            });
            this.repaint();
        }
        
      
        return false;
            
    }
    
    //richiamato dal timer per decolorarsi
    public void clearSelectionOfEdges(){
        
            for(EdgeDrawing ed : this.edges){
                if(ed.isSelected==true){
                    ed.isSelected(false);
                }
            }
        this.repaint();
        
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
            this.addNode(e.getX(), e.getY());
            this.repaint();
            
        }
        //per cliccare e creare un arco, funge
        else if(this.selectingNodesForCreateEdges == true){ 
            
            //NodeDrawing[] selectedNodes = this.selectedNodesForAddEdge(e.getX(), e.getY());
            
            for(int i =0; i<this.nodes.size();i++){
                if(this.nodes.get(i).getFrameOfNode().contains(e.getX(),e.getY()) && n1 == null){
                    n1 = this.nodes.get(i);
                    View.getInstance().addTextToLogArea("node "+this.nodes.get(i).getLogicalNode().getLabel()+" selected!");
                    
                    //appena seleziono il nodo, lo coloro di rosso
                    this.nodes.get(i).isSelected(true);
                    this.repaint();
                }
                else if(this.nodes.get(i).getFrameOfNode().contains(e.getX(), e.getY()) && n1!=null && this.nodes.get(i) != n1){
                    n2 = this.nodes.get(i);
                    View.getInstance().addTextToLogArea("node "+this.nodes.get(i).getLogicalNode().getLabel()+" selected!");
                    
                    //appena seleziono il nodo, lo coloro di rosso
                    this.nodes.get(i).isSelected(true);
                    this.repaint();
                    
                }
            }
            
        if(/*selectedNodes != null*/ n1 !=null && n2!= null){
                this.addEdge(n1, n2);
                n1 = null;
                n2 = null;
            }
            this.repaint();
        }
        
        //oltre a rimuovere il nodo, elimino anche gli archi che iniziavano o finivano in quel nodo; roba orribile lo so
        else if(isPossibleToRemoveNodes()){
            this.removeNodes(e.getX(), e.getY());
        }
        
        else if(this.isPossibleToRemoveEdges()){
            this.removeEdges(e.getX(), e.getY());
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
    public void mouseDragged(MouseEvent e){
        if(this.isPossibleToMoveNodes()){
            this.moveNode(e.getX(), e.getY());
            this.revalidate();
            this.repaint();
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
        newSize = e.getComponent().getBounds().getSize();
        this.width = (int)newSize.getWidth();
        this.height = (int)newSize.getHeight();
        
        this.revalidate();
        this.repaint();       
 
    }
    
    
    
    //
    //Private Methods
    //
    
    private boolean isPossibleAddNode(double x, double y){
        if(this.circle.circleContains(x,y)==true && this.selecting == true && removingNodesAndEdges == false && removingOnlyEdges == false)
            return true;
        else
            return false;
    }
    
    private NodeDrawing addNode(double x, double y){
        
        Point mouseCoordinates = new Point(x, y);
         NodeDrawing n = new NodeDrawing(x - this.node.getDrawingWidth()/2,y - this.node.getDrawingWidth()/2,this);
            if(this.nodes.isEmpty()){
                n.getLogicalNode().setLabel(Integer.toString(this.nodes.size()));
                n.getLogicalNode().setID(Integer.toString(this.nodes.size()));
                n.getLogicalNode().setCoordinates(x - this.node.getDrawingWidth()/2,y - this.node.getDrawingWidth()/2);
                View.getInstance().addNodeToStatusArea(n.getLogicalNode().toString());
                this.nodes.add(n);
                
                //graph.addNode(n.getLogicalNode());
                Controller.getInstance().update("addNode");
            }
            else{
                int i = Integer.parseInt(this.nodes.get(this.nodes.size() -1).getLogicalNode().getID())+1;
                n.getLogicalNode().setLabel(Integer.toString(i));
                n.getLogicalNode().setID(Integer.toString(i));
                n.getLogicalNode().setCoordinates(x - this.node.getDrawingWidth()/2,y - this.node.getDrawingWidth()/2);
                View.getInstance().addNodeToStatusArea(n.getLogicalNode().toString());
                this.nodes.add(n);
                //graph.addNode(n.getLogicalNode());
                Controller.getInstance().update("addNode");
            }
        return n;
        
    }

    
    private EdgeDrawing addEdge(NodeDrawing n1, NodeDrawing n2){
        boolean toAdd = true;
        EdgeDrawing ed = null;
        if(!this.edges.isEmpty()){
            Iterator<EdgeDrawing> i = edges.iterator();
            while(i.hasNext()){
                EdgeDrawing edd = i.next();
                if((edd.getFirstNode().equals(n1) && edd.getLastNode().equals(n2)) || (edd.getFirstNode().equals(n2) && edd.getLastNode().equals(n1))){
                    View.getInstance().addTextToLogArea("just added");
                    toAdd = false;
                    break;
                }
            }
            if(toAdd==true){
                ed = new EdgeDrawing(n1,n2,this);
                this.edges.add(ed);
                View.getInstance().addTextToLogArea("nodes from "+n1.getLogicalNode().getLabel()+" to "+n2.getLogicalNode().getLabel()+" created");
                View.getInstance().addEdgeToStatusArea(ed.getLogicalEdge().toString());
                graph.addEdge(ed.getLogicalEdge());
            }
        }else{
            ed = new EdgeDrawing(n1,n2,this);
            this.edges.add(ed);
            View.getInstance().addTextToLogArea("nodes from "+n1.getLogicalNode().getLabel()+" to "+n2.getLogicalNode().getLabel()+" created");
            View.getInstance().addEdgeToStatusArea(ed.getLogicalEdge().toString());
            Controller.getInstance().update("addEdge");
        }
        //System.out.println(this.edges.size());
        for(NodeDrawing n : this.nodes)
            if(n.isSelected == true)
                n.isSelected(false);
        
        return ed;
    }
    
    private boolean isPossibleToRemoveNodes(){
        if(this.removingNodesAndEdges == true && this.removingOnlyEdges == false)
            return true;
        else
            return false;
        
    }
    
    private void removeNodes(double x, double y){
        Iterator<NodeDrawing> iit = this.nodes.iterator();
        while(iit.hasNext()){
            NodeDrawing no = iit.next();
            if(no.getFrameOfNode().contains(x,y)){
                if(!this.edges.isEmpty()){
                    Iterator<EdgeDrawing> it = edges.iterator();
                    while (it.hasNext()) {
                        EdgeDrawing o = it.next();
                        if (o.getFirstNode().equals(no) || o.getLastNode().equals(no)){
                            graph.delEdge(o.getLogicalEdge());
                            it.remove();
                        }
                    }
                    
                }
                this.graph.delNode(no.getLogicalNode());
                iit.remove();
                View.getInstance().clearStatusArea();
                for (int i=0; i<this.nodes.size(); i++)
                    View.getInstance().addNodeToStatusArea(this.nodes.get(i).getLogicalNode().toString());
                for (int i=0; i<this.edges.size(); i++)
                    View.getInstance().addEdgeToStatusArea(this.edges.get(i).getLogicalEdge().toString());
                
                    this.repaint();
                    break;
                }
                
            }
    }
    
    private boolean isPossibleToRemoveEdges(){
        if(this.removingNodesAndEdges == false && this.removingOnlyEdges == true)
            return true;
        else
            return false;
    }
    
    private void removeEdges(double x, double y){
        Rectangle2D.Double rect = new Rectangle2D.Double(x - 2,y -2 , 4, 4);
        Iterator<EdgeDrawing> it = this.edges.iterator();
        while(it.hasNext()){        
            EdgeDrawing ed = it.next();
            if (ed.getFrameOfEdge().intersects(rect)){
                graph.delEdge(ed.getLogicalEdge());
                it.remove();
                View.getInstance().clearStatusArea();
                for (int i=0; i<this.nodes.size(); i++)
                    View.getInstance().addNodeToStatusArea(this.nodes.get(i).getLogicalNode().toString());
                for (int i=0; i<this.edges.size(); i++)
                    View.getInstance().addEdgeToStatusArea(this.edges.get(i).getLogicalEdge().toString());
                this.repaint();
                System.out.println(ed.getLogicalEdge().toString());
                break;
                }
        }
        
    }
    
    private boolean isPossibleToMoveNodes(){
        if(!this.nodes.isEmpty() && this.moving == true)
            return true;
        else
            return false;
    }
    
    private void moveNode(double x, double y){
        
            for (NodeDrawing n : this.nodes){
                if(n.getFrameOfNode().contains(x,y) && this.circle.railsForMovingNodes(n.getNodeX(), n.getNodeY())){
                    draggedNode = n;
                }
            }
            draggedNode.setRect(x - NodeDrawing.DEFAULT_DIMENSION/2, y - NodeDrawing.DEFAULT_DIMENSION/2);
            draggedNode.setAngle();
    }


}
