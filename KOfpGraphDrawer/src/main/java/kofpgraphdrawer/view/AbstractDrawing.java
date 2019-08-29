package kofpgraphdrawer.view;
//classe astratta che mi serve per l'incapsulamento del disegno
//aggiungiamoci i dati per scaling
import java.awt.Graphics;

public abstract class AbstractDrawing {

	protected double scaleFactor = 1.0;
	protected double previousScaleFactor =1.0;
        public boolean isMovable = false;


	public void setScaleFactor(double scaleFactor){

		this.previousScaleFactor = this.scaleFactor;
		this.scaleFactor = scaleFactor;
		this.rescaleDrawing();
	}
    protected abstract void draw(Graphics g);

    protected abstract int getDrawingWidth();

    protected abstract int getDrawingHeight();

    protected abstract void rescaleDrawing();//dipende dal disegno specifico

} // end class