package kofpgraphdrawer.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class Model implements IModel {
    
    private Graph graph = null;
    private static Model model = null;

    @Override
    public boolean addNode(Point point) {
        if(graph == null)
            graph = new Graph();
        
        boolean ris = true;
        String lastID = null;
        if(graph.getNodeList().isEmpty())
            lastID = "0";
        else{
            int ID =Integer.parseInt(graph.getNodeList().get(graph.getNodeList().size()-1).getID())+1;
            lastID = Integer.toString(ID);
        }
            
        for(Node n : graph.getNodeList()){
            if(n.getCoordinates().equals(point))
                ris = false;
        }
        if(ris == true)
            graph.addNode(new Node(point.x,point.y,lastID,lastID));
        
        return ris;
    }

    @Override
    public boolean delNode(Point point) {
        if(graph == null)
            graph = new Graph();
        
        boolean ris = false; 
        //forse va usato l'iteratore, però prima proviamo così!
        for(Node n : graph.getNodeList()){
            if(n.getCoordinates().equals(point)){
                ris = true;
                graph.delNode(n);
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
        
        for(Edge e : graph.getEdgeList()){
            if(e.getNode1().getCoordinates().equals(point1) && e.getNode2().getCoordinates().equals(point2)){
                notFoundSameEdge = false;
                break;
            }
        }
        
        for(Node n : graph.getNodeList()){
            if(n.getCoordinates().equals(point1)){
                foundPoint1 = true;
                index1 = graph.getNodeList().indexOf(n);
            }
            else if(n.getCoordinates().equals(point2)){
                foundPoint2 = true;
                index2 = graph.getEdgeList().indexOf(n);
            }
            
        }
        
        if(notFoundSameEdge == true && foundPoint1 == true && foundPoint2 == true){
            graph.addEdge(new Edge(graph.getNodeList().get(index1),graph.getNodeList().get(index2)));
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean delEdge(Point point1, Point point2) {
        if(graph == null)
            graph = new Graph();
        
        boolean ris = false;
        for(Edge e : graph.getEdgeList()){
            if(e.getNode1().getCoordinates().equals(point1) && e.getNode2().getCoordinates().equals(point2)){
                ris = true;
                graph.delEdge(e);
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
       graph.nodeFromFile();
       graph.edgeFromFile();
       return true;
    }

    @Override
    public boolean clearGraph(){
        if(graph == null)
            graph = new Graph();
        
        graph.getEdgeList().clear();
        graph.getNodeList().clear();
        
        return graph.getEdgeList().isEmpty() && graph.getNodeList().isEmpty();
    }
    
    public static IModel getInstance(){
        if(model == null)
            return model = new Model();
        else 
            return model;
    }
    
    
}
