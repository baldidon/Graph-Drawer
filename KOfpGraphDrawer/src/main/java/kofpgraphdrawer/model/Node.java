package kofpgraphdrawer.model;

import java.awt.Point;


/*import kofpcirculardrawings.model.Point;*/
public class Node{
        public static int ID_TO_ASSIGN=0;
    
	private Point point;
        private String id;
	private String label;
	private int edgeNumber;
	private int vectorIndex;
        
        //COSTRUTTORE 1
	public Node(int x, int y,String ID, String label){
		this.point = new Point(x, y);
                this.id = ID;
		this.label = label;
		this.edgeNumber=0;
	}
        
        //COSTRUTTORE 2
        //il punto nullo ce lo metto comunque, così posso assegnarli in futuro delle coordinates
        public Node(String ID, String label){
            this.point = new Point(0,0);
            this.id = ID;
            this.label = label;
            this.edgeNumber = 0;
        }

	public String getLabel(){
		return this.label;
	}
    
        public String getID(){
            return this.id;
        }
        
	public int getEdgeNumber(){
		return this.edgeNumber;
	}

	public Point getCoordinates(){
		return this.point;
	}

	public int getVectorIndex(){
		return this.vectorIndex;
	}

	public String setLabel(String label){
		this.label=label;
		return this.label;
	}

	public void setID(String ID){
        this.id = ID;
    }

	public int setEdgeNumber(){
		this.edgeNumber++;
		return this.edgeNumber;
	}

	public void setCoordinates(double x, double y){
		this.point.setLocation(x, y);
		//return this.point;
	}

	public int setVectorIndex(int index){
		this.vectorIndex = index;
		return this.vectorIndex;
	}

	public String toString(){
		return "Nodo: " + this.label + "\r\n" /*+ "Coordinate: (" + this.point.getX() + ", " + this.point.getY() + ")\r\n"*/;
	}
}