package kofpgraphdrawer.model;

import java.awt.Point;


/*import kofpcirculardrawings.model.Point;*/
public class Node{
        public static int ID_TO_ASSIGN=0;
    
	private Point point;
        private String id;
	private String label;
        protected double angle;

        //COSTRUTTORE 1
	public Node(int x, int y,String ID, String label){
		this.point = new Point(x, y);
                this.id = ID;
		this.label = label;
                Node.ID_TO_ASSIGN++;
                this.angle=Math.toDegrees(Math.atan2(y, x));
                if(this.angle<0)
                    this.angle+=360;
	}

	public String getLabel(){
		return this.label;
	}
    
        public String getID(){
            return this.id;
        }

	public Point getCoordinates(){
		return this.point;
	}

	public String setLabel(String label){
		this.label=label;
		return this.label;
	}

	public void setID(String ID){
        this.id = ID;
    }

	public void setCoordinates(double x, double y){
		this.point.setLocation(x, y);
                this.angle=Math.toDegrees(Math.atan2(y, x));
                if(this.angle<0)
                    this.angle+=360;
		//return this.point;
	}
        
        /*Metodo per sistemare le posizioni dei nodi aggiunti
        args: 1-added: nodo appena aggiunto
              2-listPoint: nodo all'interno della lista
        ret:  true se added va messo prima di listPoint
              false se added va messo dopo di listPoint
        */
        public boolean isBefore(Node listPoint){
            Point listPointToCheck = listPoint.getCoordinates();
            
            double thisNodeAngle=Math.atan2(this.point.getY(), this.point.getX());
            double listPointToCheckAngle=Math.atan2(listPointToCheck.getY(), listPointToCheck.getX()); 
            
            if (thisNodeAngle<0)
                thisNodeAngle+=(Math.PI*2);
            if (listPointToCheckAngle<0)
                listPointToCheckAngle+=(Math.PI*2);
            
            if(thisNodeAngle<listPointToCheckAngle)
                return true;
            else
                return false;
        }

	public String toString(){
		return "Nodo: " + this.label + "\r\n" /*+ "Coordinate: (" + this.point.getX() + ", " + this.point.getY() + ")\r\n"*/;
	}
}