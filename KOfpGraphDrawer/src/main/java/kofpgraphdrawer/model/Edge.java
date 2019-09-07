package kofpgraphdrawer.model;
import java.awt.Point;
import java.awt.geom.Line2D;
        
public class Edge{
	private Node n1;
	private Node n2;
	private String label;
	private int vectorIndex;
        
        //se intendi per edgeNumber, il numero di archi interagenti; nella creazione di un arco, invoco anche il metodo setEdgeNumber().
	public Edge(Node n1, Node n2){
		this.n1 = n1;
                //n1.setEdgeNumber();
                
		this.n2 = n2;
                //n2.setEdgeNumber();
                
                this.label = "Edge from " + this.n1.getID() + " to " + this.n2.getID();
	}

	public int getVectorIndex(){
		return this.vectorIndex;
	}

	public int setVectorIndex(int index){
		this.vectorIndex = index;
		return this.vectorIndex;
	}

	public String getLabel(){
		return this.label;
	}

	/*public String setLabel(){
		return this.setLabel("Edge from " + this.n1.getLabel() + " to " + this.n2.getLabel());
	}*/

	public String setLabel(String s){
		this.label = s;
		return this.label;
	}

	public Node getNode1(){
		return this.n1;
	}
        
        public Point getCoordinatesNode1(){
            return this.n1.getCoordinates();
        }

	public Node setNode1(Node n){
		this.n1 = n;
		return this.n1;
	}

	public Node getNode2(){
		return this.n2;
	}
        
        public Point getCoordinatesNode2(){
            return this.n2.getCoordinates();
        }

	public Node setNode2(Node n){
		this.n2 = n;
		return this.n2;
	}
        
        public boolean cross(Edge e){
                
                Line2D thisLine = new Line2D.Double(this.getNode1().getCoordinates().getX(), this.getNode1().getCoordinates().getY(), this.getNode2().getCoordinates().getX(), this.getNode2().getCoordinates().getY());
		Line2D eLine = new Line2D.Double(e.getNode1().getCoordinates().getX(), e.getNode1().getCoordinates().getY(), e.getNode2().getCoordinates().getX(), e.getNode2().getCoordinates().getY());

		return thisLine.intersectsLine(eLine);
	}

	public String toString(){
		return "Edge from " + this.n1.getID() + " to " + this.n2.getID() + "\r\n";
	}
        
        protected boolean contains(Node n){
            if(n.equals(this.n1) || n.equals(this.n2))
                return true;
            else
                return false;
        }
}