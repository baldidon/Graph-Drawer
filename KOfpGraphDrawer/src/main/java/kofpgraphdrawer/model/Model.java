package kofpgraphdrawer.model;

import java.awt.Point;
import java.util.ArrayList;
import kofpgraphdrawer.controller.Controller;

public class Model implements IModel {
    
    private Graph graph = null;
    private static Model model = null;

    @Override
    public boolean addNode(Point point) {
        if(graph == null)
            graph = new Graph();
        
        String ID = Integer.toString(Node.ID_TO_ASSIGN);
        boolean result = graph.addNode(new Node(point.x, point.y, ID, ID));
                
        if(result) 
            graph.checkNodesOrder();
       
        return result;
    }

    @Override
    public boolean delNode(Point point) {
        if(graph == null)
            graph = new Graph();
        
        boolean ris = false; 
        //forse va usato l'iteratore, però prima proviamo così!
        for(Node n : graph.getNodeList()){
            if(n.getCoordinates().equals(point)){
                ris = graph.delNode(n);
                break;
            }
        }
        
        return ris;
    }

    @Override
    public boolean setNode(Point point1, Point point2) {
        if(graph == null)
            graph = new Graph();
        
        boolean ris = false;
        for(Node n : graph.getNodeList()) {
            if(n.getCoordinates().equals(point1)){
                ris = true;
                n.setCoordinates(point2.x, point2.y);
                break;
            }
        }
        
        graph.checkNodesOrder();
        
        return ris;
    }

    @Override
    public ArrayList<Point> getNodes() {
        if(graph == null)
            graph = new Graph();
        
        ArrayList<Point> coordOfNodes = new ArrayList<>();
        for(Node n : graph.getNodeList()){
            coordOfNodes.add(n.getCoordinates());
        }
        
        return coordOfNodes;
    }

    @Override
    public ArrayList<String> getNodesLabels(){
        if(graph == null)
            graph = new Graph();
        
        ArrayList<String> labelOfNodes = new ArrayList<>();
        //non so se ci devo mettere id o label
        for(Node n : graph.getNodeList()){
            labelOfNodes.add(n.getID());
        }
        
        return labelOfNodes;
    }

    @Override
    public boolean addEdge(Point point1, Point point2) {
        if(graph == null)
            graph = new Graph();
        
        boolean notFoundSameEdge = true;
        
        boolean foundPoint1 = false;
        int index1 =0;
        
        boolean foundPoint2 = false;
        int index2 = 0;
        
        //Controllo se l'arco esiste già
        for(Edge e : graph.getEdgeList()){
            if(e.getNode1().getCoordinates().equals(point1) && e.getNode2().getCoordinates().equals(point2)
                    || e.getNode1().getCoordinates().equals(point2) && e.getNode2().getCoordinates().equals(point1)){
                notFoundSameEdge = false;
                break;
            }
        }
        
        //Cerco i 2 nodi per creare l'arco (se esistono)
        for(int i=0; i<graph.getNodeList().size(); i++){
            if(graph.getNodeList().get(i).getCoordinates().equals(point1)){
                foundPoint1 = true;
                index1 = i;
            }
            else if(graph.getNodeList().get(i).getCoordinates().equals(point2)){
                foundPoint2 = true;
                index2 = i;
            }
            
        }
        
        boolean result=false;
        
        if(notFoundSameEdge && foundPoint1 && foundPoint2){
            result = graph.addEdge(new Edge(graph.getNodeList().get(index1),graph.getNodeList().get(index2)));
        }
        return result;
    }

    @Override
    public boolean delEdge(Point point1, Point point2) {
        if(graph == null)
            graph = new Graph();
        
        boolean ris = false;
        for(Edge e : graph.getEdgeList()){
            if(e.getNode1().getCoordinates().equals(point1) && e.getNode2().getCoordinates().equals(point2)
                    || e.getNode1().getCoordinates().equals(point2) && e.getNode2().getCoordinates().equals(point1)){
                ris = graph.delEdge(e);
                break;
            }
        }
        
        return ris;
    }

    @Override
    public ArrayList<Point[]> getEdges() {
        if(graph == null)
            graph = new Graph();
        
        ArrayList<Point[]> coordsOfEdges = new ArrayList<>();
        for(Edge e : graph.getEdgeList()){
            Point[] points = new Point[2];
            points[0] = e.getCoordinatesNode1();
            points[1] = e.getCoordinatesNode2();
            coordsOfEdges.add(points);
        }
        
        return coordsOfEdges;
    }

    @Override
    public ArrayList<String> getEdgeLabels() {
        if(graph == null)
            graph = new Graph();
        
        ArrayList<String> labelsOfEdges = new ArrayList<>();
        for(Edge e : graph.getEdgeList()){
            labelsOfEdges.add(e.getLabel());
        }
        
        return labelsOfEdges;
    }

    @Override
    public boolean doClique() {
        if(graph == null)
            graph = new Graph();
       
        return graph.Clique();
    }

    @Override
    public boolean isFanPlanar(int k) {
        if(graph == null)
            graph = new Graph();
        
        return graph.isFanPlanar(k);
    }

    @Override
    public boolean saveToFile(String filePath) {
        if(graph == null)
            graph = new Graph();
        
        return graph.saveToFile(filePath);
    }

    @Override
    public boolean loadFromFile(String filePath) {
       boolean result=true;
       result=graph.nodeFromFile();
       int length = graph.getNodeList().size();
       for(int i =0; i<graph.getNodeList().size(); i++){
           Node n = graph.getNodeList().get(i);
           Point auxPoint = Controller.getInstance().generatePoint(i,length);
           n.setCoordinates(auxPoint.x,auxPoint.y);
       }
       if(result)
        result=graph.edgeFromFile();
       return result;
    }

    @Override
    public boolean clearGraph(){
        if(this.graph == null)
            this.graph = new Graph();
        
        Node.ID_TO_ASSIGN=0;
        this.graph.getEdgeList().clear();
        this.graph.getNodeList().clear();
        
        return this.graph.getEdgeList().isEmpty() && this.graph.getNodeList().isEmpty();
    }
    
    
    @Override
    public ArrayList<Point[]> getKFPEdges() {
        if(graph == null)
            graph = new Graph();
        
        ArrayList<Point[]> coordsOfEdges = new ArrayList<>();
        for(Edge e : graph.getKfpEdgeList()){
            Point[] points = new Point[2];
            points[0] = e.getCoordinatesNode1();
            points[1] = e.getCoordinatesNode2();
            coordsOfEdges.add(points);
        }
        
        return coordsOfEdges;
    }
    public static IModel getInstance(){
        if(model == null)
            return model = new Model();
        else 
            return model;
    }
    
}
