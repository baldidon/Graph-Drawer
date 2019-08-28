package kofpgraphdrawer.view;

import java.awt.Point;

public class View implements IView{
    
    private static View view = null;
    private static MainGUI mainGUI = null;
    
    @Override
    public Point nodeToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point nodeToDel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point[] nodeToMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point[] edgeToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point[] edgeToDel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshGUI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getKValueForFanPlanarity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void isFanPlanar(boolean result) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setError(String error) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPath() {
        return mainGUI.filePath;
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
