package kofpgraphdrawer.view;

import java.awt.Point;

public interface IView {
    
    /*
        METODI PER GESTIRE I NODI
    */
    
    //per aggiungere nodo
    public Point nodeToAdd();
    
    public int nodesToGenerate();
    
    //per rimuovere nodo
    public Point nodeToDel();
    
    /*
    def: sposta il nodo
    ret: point[0] nodo da spostare
         point[1] nuove coordinate nodo
    */
    public Point[] nodeToMove();
    
    /*
        METODI PER GESTIRE ARCHI
    */
    
    //per aggiungere arco
    public Point[] edgeToAdd();
    
    //per rimuovere arco
    public Point[] edgeToDel();
    
    //aggiornare il disegno nella GUI ad ogni interazione dell'utentea
    public void refreshGUI();
    
    public int getKValueForFanPlanarity();
    
    public void setError(String error);
    
    public String getPath();
    
    //oltre al raggio, devo ottenere il centro di graphPanel
    public double getRadius();
    
    public Point getCenterOfGraphPanel();
    
    public InfoPanel getInfoPanel();
    
    public GraphPanel getGraphPanel();
    
    public GMLFileHandler getGMLFileHandler();
    
    public void openMainGUI();
}
