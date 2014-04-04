import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.*;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public float decal = 1.0f; //Décalage des pointillés
	public Main main;
	
    public Frame(Main m) {  //CONSTRUCTOR
    	this.main = m;
        setUndecorated(true);
        setSize(300,200);
        setAlwaysOnTop(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.1f));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MoveMouseListener mml = new MoveMouseListener(this);
        this.addMouseListener(mml);
        this.addMouseMotionListener(mml);
    }
    
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		// Overlay noir
		g2d.setColor(new Color(0f,0f,0f,.1f));
		g2d.fillRect(2, 2, this.getWidth()-4, this.getHeight()-4);
		//Lignes pointillés
		float dash1[] = {10.0f};
		decal+=.3f;
		if(decal>20f) decal=1f;
		BasicStroke dashed = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, decal, dash1, decal);
		g2d.setStroke(dashed);
		g2d.setColor(new Color(.5f,.5f,.5f));
		g2d.drawRect(2, 2, this.getWidth()-5, this.getHeight()-5);
		//Petits carrés aux coins
		g2d.setColor(new Color(.2f,.2f,.2f));
		g2d.fillRect(0,0,5,5);
		g2d.fillRect(this.getWidth()-5,0,5,5);
		g2d.fillRect(0,this.getHeight()-5,5,5);
		g2d.fillRect(this.getWidth()-5,this.getHeight()-5,5,5);
		//Texte du milieu
		String s = "Double clique pour capturer";
	    g2d.setColor(Color.gray);
	    Font f = new Font(null, Font.PLAIN, 18);
	    g2d.setFont(f);
		FontMetrics fontmet = g2d.getFontMetrics(f);
		int font_size = fontmet.stringWidth(s);
		int x = (int)((this.getWidth()-font_size)/2); int y = this.getHeight()/2+4;
		//Bordures du texte
		g.drawString(s, x-1, y+1);
		g.drawString(s, x-1, y-1);
		g.drawString(s, x+1, y+1);
		g.drawString(s, x+1, y-1);
	    g2d.setColor(Color.white);
	    g2d.drawString(s,x,y); 
	    //Boucle pour animer les pointillés
        try{
            Thread.sleep(10);
        } catch (Exception exc){}
        this.repaint();
	}
	public void screenshot() {
		Point loc = this.getLocationOnScreen();
		Dimension siz = this.getSize();
		main.takescreen(loc.x,loc.y,siz.width,siz.height);
	}
}