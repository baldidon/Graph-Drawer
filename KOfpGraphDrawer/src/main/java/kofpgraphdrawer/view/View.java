package kofpgraphdrawer.view;

import java.awt.Point;
import kofpgraphdrawer.controller.Controller;

public class View implements IView{
    
    
    private static View view = null;
    private static MainGUI mainGUI = null;
    
    @Override
    public Point nodeToAdd() {
        return this.mainGUI.getGraphPanel().getCoordinates();
    }
    
    @Override
    public int nodesToGenerate() {
        return mainGUI.numberOfNodesToGenerateAutomatically;
    }
    
    @Override
    public Point nodeToDel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Point[] nodeToMove() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Point[] edgeToAdd() {
        return Point[] points = {mainGUI.getGraphPanel().p1, mainGUI.getGraphPanel().p2};
    }

    @Override
    public Point[] edgeToDel() {
        return Point[] points = {mainGUI.getGraphPanel().p1, mainGUI.getGraphPanel().p2};
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
        return mainGUI.getGraphPanel().getCircle().getFrameOfCircle().getWidth()/2;
    }
    
    @Override
    public Point getCenterOfGraphPanel(){
        int x = (int)Math.round(mainGUI.getGraphPanel().getWidth()/2);
        int y = (int)Math.round(mainGUI.getGraphPanel().getHeight()/2);
        
        return new Point(x,y);
    }
    
    @Override
    public void refreshGUI() {
        
        if(Controller.getInstance().getGraphNodes().isEmpty())
            mainGUI.showButtons(false);
        else
            mainGUI.showButtons(true);
        
        //sono sicuro che questi qua devono starci sicuramente
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
    
    public void openMainGUI() { 
      javax.swing.SwingUtilities.invokeLater(() -> {
          if (mainGUI == null)
              mainGUI = new MainGUI();
          mainGUI.setVisible(true);
      });
    }
    

    
    public static IView getInstance(){
        if(view == null)
            view = new View();
        return view;
    }


}
