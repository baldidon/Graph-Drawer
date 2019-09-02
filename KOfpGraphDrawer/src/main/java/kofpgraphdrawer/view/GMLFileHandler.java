package kofpgraphdrawer.view;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GMLFileHandler extends FileDialog{
    
    protected int PREFERRED_DIMENSION = 400;
    
    private final String FILE_IMPORTED = "graph imported successfully!";
    private final String FILE_SAVED = "file saved succesfully!";
    
    protected BufferedReader importedGraphFile = null;
    private Path path = null;
    protected MainGUI mainGUI;
    

    public GMLFileHandler(MainGUI mainGUI,String mode, int purposeOfThisWindow){
        /*purpose of this window mi serve per indicare che, quello che voglio aprire sia una finestra di caricamento file o di salvataggio file*/
        super(mainGUI,mode,purposeOfThisWindow);
        this.mainGUI = mainGUI;
        this.setFilenameFilter((File dir1, String name1) -> name1.endsWith(".gml"));
        this.setPreferredSize(new java.awt.Dimension(PREFERRED_DIMENSION,PREFERRED_DIMENSION));
        this.pack();
        this.setVisible(true);
    }
    
    

    
    public String getPathFile(){
        return this.getDirectory()+ File.separator + this.getFile();
    }



    public BufferedReader getOpenedFile(){
       try{
           path = Paths.get(this.getPathFile());
           importedGraphFile = Files.newBufferedReader(path);
           //mainGUI.getInfoPanel().setTextOfLogArea(FILE_IMPORTED);
       }
       catch (IOException ioe) {
           //View.getInstance().addTextToLogArea("error! no graph loaded!");

       }
       catch(NullPointerException | NoSuchFieldError npe){
           mainGUI.getInfoPanel().setTextOfLogArea("error! no graph loaded!");

       }

       return importedGraphFile;
    }


}