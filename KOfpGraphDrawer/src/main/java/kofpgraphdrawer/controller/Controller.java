package kofpgraphdrawer.controller;

import java.awt.Point;
import java.util.ArrayList;
import kofpgraphdrawer.view.View;

public class Controller implements IController{
    
    private static Controller controller = null;
    
    private String filePath;
    private Point[] points;
    
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
                for(int i=0; i<View.getInstance().nodesToGenerate(); i++){
                    Model.getInstance.add
                }
            
            case "delNode":
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
                points = View.getInstance.edgeToAdd();
                if(points.length==2){
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
                points = View.getInstance.edgeToAdd();
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
                boolean risKFP;
                risKFP = Model.getInstance().isFanPlanar(View.getInstance().getKValueForFanPlanarity());
                View.getInstance().isFanPlanar(risKFP);
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
                result = Model.getInstance.clearGraph();
            default :
                View.getInstance().setError("Function not found!");
                result=false;
                break;
        }
        View.getInstance().refreshGUI();
        return result;
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
    
    @Override
    public boolean clearGraph(){
        return Model.getInstance().clearGraph();
    }
    
    
    public static IController getInstance(){
        if(controller == null)
            controller = new Controller();
        return controller;
    }
    
    public static void main(String[] args){
        View.getInstance().openMainGUI();
    }
    
    private Point generatePoint(int index, int length){
        
    }
    
}
