package kofpgraphdrawer.controller;

import java.awt.Point;
import java.util.ArrayList;

public interface IController {
    
    //metodo per richiamare il controller e aggiornare il model
    //ritorna la risposta del model (successo o insuccesso)
    public boolean update(String command);
    
    //lista di nodi
    public ArrayList<Point> getGraphNodes();
    
    //lista di archi(punto 1, punto 2)
    public ArrayList<Point[]> getGraphEdges();
    
    public ArrayList<String> getNodesLabels();
    
    public ArrayList<String> getEdgesLabels();
    
    public boolean getColourCritalEdges();
    
    public void setColourCriticalEdges(boolean b);
    
    public ArrayList<Point[]> getKFPGraphEdges();

    public Point generatePoint(int index, int length);
}
