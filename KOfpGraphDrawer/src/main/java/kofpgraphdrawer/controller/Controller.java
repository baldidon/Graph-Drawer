package kofpgraphdrawer.controller;

import java.awt.Point;
import java.util.ArrayList;
import kofpgraphdrawer.model.Model;
import kofpgraphdrawer.view.View;

public class Controller implements IController{
    
    private static Controller controller = null;
    
    private String filePath;
    private Point[] points;
    private boolean colourCriticalEdges;
    
    @Override
    public boolean update(String command){
        boolean result = true;
        switch(command)
        {
            case "addNode":
                if(!Model.getInstance().addNode(View.getInstance().nodeToAdd())){
                    View.getInstance().setError("Error in adding node");
                    result=false;
                }
                break;
                
            case "addMultipleNodes":
                int numberOfNodes = View.getInstance().nodesToGenerate();
                for(int i=0; i<numberOfNodes; i++){
                    Model.getInstance().addNode(this.generatePoint(i, numberOfNodes));
                }
                break;
                
            case "delNode":
                //TO-DO elimina anche gli archi del nodo
                if(!Model.getInstance().delNode(View.getInstance().nodeToDel())){
                    View.getInstance().setError("Error in removing node");
                    result=false;
                }
                break;
            
            case "setNode":
                points = View.getInstance().nodeToMove();
                if(points.length == 2){
                    if(!Model.getInstance().setNode(points[0],points[1])){
                        View.getInstance().setError("Error in set Node");
                        result=false;
                    }
                }
                else{
                   View.getInstance().setError("Error in moving node");
                   result=false;
                }
                break;
            
            case "addEdge":
                points = View.getInstance().edgeToAdd();
                if(points.length==2){
                    //TO-DO controlla nel model se c'è un arco uguale (stessi punti)
                    if(!Model.getInstance().addEdge(points[0], points[1])){
                        View.getInstance().setError("Error in add edge");
                        result=false;
                    }
                }
                else{
                    View.getInstance().setError("Error in adding edge");
                    result=false;
                }
                break;
                
            case "delEdge":
                points = View.getInstance().edgeToDel();
                if(points.length==2){
                    if(!Model.getInstance().delEdge(points[0], points[1])){
                        View.getInstance().setError("Error in del edge");
                        result=false;
                    }
                }
                else{
                    View.getInstance().setError("Error in removing edge");
                    result=false;
                }
                break;
                
            case "doClique":
                if(!Model.getInstance().doClique()){
                    View.getInstance().setError("Error in doing clique");
                    result=false;
                }
                break;
            
            case "isFanPlanar":
                result = Model.getInstance().isFanPlanar(View.getInstance().getKValueForFanPlanarity());
                if(!result)
                    colourCriticalEdges=true;
                //View.getInstance().isFanPlanar(risKFP);
                break;
                
            case "saveToFile":
                filePath = View.getInstance().getPath();
                if(filePath==null || filePath.equals("")){
                    View.getInstance().setError("Error in the file handler");
                    result=false;
                }
                else{
                    Model.getInstance().saveToFile(filePath);
                    result=false;
                }
                break;
                
            case "loadFromFile":
                filePath = View.getInstance().getPath();
                if(filePath==null || filePath.equals("")){
                    View.getInstance().setError("Error in the file handler");
                    result=false;
                }
                else{
                    Model.getInstance().loadFromFile(filePath);
                    result=false;
                }
                break;
                
            case "clearGraph":
                result = Model.getInstance().clearGraph();
                break;
            
            default :
                View.getInstance().setError("Function not found!");
                result=false;
                break;
        }
        View.getInstance().refreshGUI();
        return result;
    }
    
    public boolean getColourCritalEdges(){
        return colourCriticalEdges;
    }
    
    public void setColourCriticalEdges(boolean b){
        this.colourCriticalEdges=b;
    }

    @Override
    public ArrayList<Point> getGraphNodes() {
        return Model.getInstance().getNodes();
    }

    @Override
    public ArrayList<Point[]> getGraphEdges() {
        return Model.getInstance().getEdges();
    }

    @Override
    public ArrayList<String> getNodesLabels() {
        return Model.getInstance().getNodesLabels();
    }

    @Override
    public ArrayList<String> getEdgesLabels() {
        return Model.getInstance().getEdgeLabels();
    }
    
    private Point generatePoint(int index, int length){
        
        double t = 2 * Math.PI * index / length;
        int x = (int) Math.round(View.getInstance().getRadius()* Math.cos(t));
        int y = (int) Math.round(View.getInstance().getRadius()* Math.sin(t));
            
        return new Point(x, y);
    }
    
    
    public static void main(String[] args){
        View.getInstance().openMainGUI();
    }
        
    public static IController getInstance(){
        if(controller == null)
            controller = new Controller();
        return controller;
    }
    
}
