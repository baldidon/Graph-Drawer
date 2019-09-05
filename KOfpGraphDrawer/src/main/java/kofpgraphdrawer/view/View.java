package kofpgraphdrawer.view;

import java.awt.Point;
import kofpgraphdrawer.controller.Controller;

public class View implements IView{
    
    
    private static View view = null;
    private static MainGUI mainGUI = null;
    
    @Override
    public Point nodeToAdd() {
        return this.mainGUI.getGraphPanel().getPoint();
    }
    
    @Override
    public int nodesToGenerate() {
        return mainGUI.numberOfNodesToGenerateAutomatically;
    }
    
    @Override
    public Point nodeToDel() {
        return mainGUI.getGraphPanel().getPoint();
    }

    @Override
    public Point[] nodeToMove() {
       Point[] points = {mainGUI.getGraphPanel().getP1(), mainGUI.getGraphPanel().getP2()};
       return points;
    }

    @Override
    public Point[] edgeToAdd() {
        Point[] points = new Point[2];
        points[0] = mainGUI.getGraphPanel().getP1();
        points[1] = mainGUI.getGraphPanel().getP2();
        return points;
    }

    @Override
    public Point[] edgeToDel() {
        Point[] points = new Point[2];
        points[0] = mainGUI.getGraphPanel().getP1();
        points[1] = mainGUI.getGraphPanel().getP2();
        return points;
    }

    @Override
    public int getKValueForFanPlanarity() {
        return mainGUI.getKOfpValue();
    }

    @Override
    public void setError(String error) {
        mainGUI.getInfoPanel().setTextOfLogArea(error);
    }

    @Override
    public String getPath() {
        return mainGUI.filePath;
    }
    
    @Override
    public double getRadius(){
        return mainGUI.getGraphPanel().getCircle().getFrameOfCircle().getWidth()/2/MainGUI.scaleFactor;
    }
    
    @Override
    public Point getCenterOfGraphPanel(){
        int x = (int)Math.round(mainGUI.getGraphPanel().getWidth()/2);
        int y = (int)Math.round(mainGUI.getGraphPanel().getHeight()/2);
        
        return new Point(x,y);
    }
    
    @Override
    public void refreshGUI() {
        if(!Controller.getInstance().getGraphNodes().isEmpty())
            mainGUI.showButtons(true);

        //metodi per riaggiornare la GUI
        this.getInfoPanel().clearStatusArea();
        this.updateTree();
        mainGUI.getGraphPanel().revalidate();
        mainGUI.getGraphPanel().repaint();
    }
    
    @Override   
    public InfoPanel getInfoPanel(){
        return mainGUI.getInfoPanel();
    }
    
    @Override
    public GraphPanel getGraphPanel(){
        return mainGUI.getGraphPanel();
    }
    
    @Override
    public GMLFileHandler getGMLFileHandler(){
        return mainGUI.getFileHandler();
    }
    
    @Override
    public void openMainGUI() { 
      javax.swing.SwingUtilities.invokeLater(() -> {
          if (mainGUI == null)
              mainGUI = new MainGUI();
          mainGUI.setVisible(true);
      });
    }
    
    
    private void updateTree(){
        for(String toAdd : Controller.getInstance().getNodesLabels())
            mainGUI.getInfoPanel().addNodeToStatusArea(toAdd);
        for(String toAdd : Controller.getInstance().getEdgesLabels())
            mainGUI.getInfoPanel().addEdgeToStatusArea(toAdd);
    }
    

    
    public static IView getInstance(){
        if(view == null)
            view = new View();
        return view;
    }


}
