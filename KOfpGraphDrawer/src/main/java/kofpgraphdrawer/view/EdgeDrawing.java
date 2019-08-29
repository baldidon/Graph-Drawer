package kofpgraphdrawer.view;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import kofpcirculardrawings.model.*;



public class EdgeDrawing  extends AbstractDrawing{
    /*
    idea è quella di creare un oggetto arco che disegnerò, collegato all' oggetto arco e a quello grafo. con un metodo draw disegno tutti gli archi
    */
    
    
    public Graph graph;
    protected GraphPanel graphPanel;
    public Edge logicalEdge;
    public Line2D.Double edge;
    public NodeDrawing n1;
    public NodeDrawing n2;
    protected boolean isSelected;
    
    //costruttore
    //gli passo due punti "visivi"
    public EdgeDrawing(NodeDrawing n1, NodeDrawing n2,GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.isSelected = false;
        this.n1 = n1;
        this.n2 = n2;
        //instanzio l'oggetto "edge" logico
        this.logicalEdge = new Edge(this.n1.getLogicalNode(), this.n2.getLogicalNode());
        this.edge = new Line2D.Double(this.n1.getNodeX(),this.n1.getNodeY() , this.n2.getNodeX(), this.n2.getNodeY());
    }
    //costruttore
    //gli passo due punti "logici", forse non necessario
    
    public EdgeDrawing(GraphPanel graphPanel){
        this.graphPanel = graphPanel;
        this.edge = new Line2D.Double();
        
    }

    @Override
    protected void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
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
            
            this.rescaleDrawing();
            g2d.draw(this.edge);
            
    }
    
    public Edge getLogicalEdge(){
        return this.logicalEdge;
    }
    
    public Line2D.Double getFrameOfEdge(){
        return this.edge;
    }
    
    protected ArrayList<EdgeDrawing> setEdgeDrawingForLogicalEdges(ArrayList<Edge> listLogicalEdges, ArrayList<NodeDrawing> visualNodes){
        int l = listLogicalEdges.size();
        int ll = visualNodes.size();
        ArrayList<EdgeDrawing> visualEdges = new ArrayList(l);
        
        //scorro lista archi logici
        for(int i=0; i<l; i++){
            NodeDrawing nn1 = null;
            NodeDrawing nn2 = null;
            String n1 = listLogicalEdges.get(i).getNode1().getLabel();
            String n2 = listLogicalEdges.get(i).getNode2().getLabel();
            
            
            for(int j =0;j<ll; j++){
                if(visualNodes.get(j).getLogicalNode().getLabel().equals(n1)){
                    nn1 = visualNodes.get(j);
                }
                else if(visualNodes.get(j).getLogicalNode().getLabel().equals(n2)){
                    nn2 = visualNodes.get(j);
                }
                   
            }
            if(nn1 != null && nn2 != null){
                EdgeDrawing e = new EdgeDrawing(nn1, nn2, this.graphPanel);
                View.getInstance().addEdgeToStatusArea(e.getLogicalEdge().toString());
                visualEdges.add(e);
            }
                
        }
        
        return visualEdges;
    }

    
    //primo nodo dell'arco
    public NodeDrawing getFirstNode(){
        return this.n1;
    }
    //ultimo nodo dell'arco
    public NodeDrawing getLastNode(){
        return this.n2;
    }
    
    
    public void isSelected(boolean b){
        this.isSelected = b;
    } 

    @Override
    protected int getDrawingWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int getDrawingHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void rescaleDrawing() {
        this.edge.setLine(this.scaleFactor*this.n1.getNodeX(), this.scaleFactor*this.n1.getNodeY(),
                this.scaleFactor*this.n2.getNodeX(), this.scaleFactor*this.n2.getNodeY());
    }
    
    
    
}
