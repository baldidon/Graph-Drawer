package kofpgraphdrawer.model;

import java.awt.Point;
import java.util.ArrayList;

public interface IModel {
    
    public boolean addNode(Point point);
    
    public boolean delNode(Point point);
    
    /*
    def: sposta il nodo
    arg1: nodo da spostare
    arg2: nuove coordinate nodo
    */
    public boolean setNode(Point point1,Point point2);
    
    public ArrayList<Point> getNodes();
    
    public ArrayList<String> getNodesLabels();
    
    public boolean addEdge(Point point1,Point point2);
    
    public boolean delEdge(Point point1,Point point2); 
    
    public ArrayList<Point[]> getEdges();
    
    public ArrayList<String> getEdgeLabels();
    
    public ArrayList<Point[]> getKFPEdges();
    
    public boolean doClique();
    
    public boolean isFanPlanar(int k);

    public boolean saveToFile(String filePath);
    
    public boolean loadFromFile(String filePath);

    public boolean clearGraph();
}
