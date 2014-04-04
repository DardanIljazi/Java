import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;

import javax.swing.JFrame;

class MoveMouseListener implements MouseListener, MouseMotionListener {
	Frame target;
	Point start_drag;
	Point start_loc;
	Point start_target_loc;
	Dimension start_siz;
	public int handler_size = 10;
	public int double_click_interval_ms = 500;
	public long click_time;

	public MoveMouseListener(Frame frame) { //CONSTRUCTOR
		this.target = frame;
	}

	public static JFrame getFrame(Container target) {
		if (target instanceof JFrame) return (JFrame) target;
		return getFrame(target.getParent());
	}

	public Point getScreenLocation(MouseEvent e) {
		Point cursor = e.getPoint();
		Point target_location = this.target.getLocationOnScreen();
		return new Point((int) (target_location.getX() + cursor.getX()), (int) (target_location.getY() + cursor.getY()));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point current = this.getScreenLocation(e);
		Point offset = new Point((int) current.getX() - (int) start_drag.getX(), (int) current.getY() - (int) start_drag.getY());
		JFrame frame = MoveMouseListener.getFrame(target);
		Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));
		Point target_location = this.target.getLocationOnScreen();

		//Taille de l'écran de l'user 
		Rectangle screen_size = target.getGraphicsConfiguration().getBounds();

		//Ici je verifie que ca depasse pas l'écran
		if(new_location.x<0)new_location.x=0;
		if(new_location.y<0)new_location.y=0;
		if(new_location.x>screen_size.width-target.getWidth())new_location.x=screen_size.width-target.getWidth();
		if(new_location.y>screen_size.height-target.getHeight())new_location.y=screen_size.height-target.getHeight();

		//Resize et drags
		if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR))
			frame.setLocation(new_location);

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR))
			frame.setSize((int) (current.getX()-target_location.getX()), frame.getHeight());

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR))
			frame.setSize(frame.getWidth(), (int) (current.getY()-target_location.getY()));

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR))
			frame.setSize((int) (current.getX()-target_location.getX()), (int) (current.getY()-target_location.getY()));

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)){
			frame.setSize((int) (current.getX()-target_location.getX()), (int) (start_siz.height+start_target_loc.getY()-new_location.getY()));
			frame.setLocation((int) target_location.getX(), (int) new_location.getY());
		}

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)){
			frame.setSize((int) (start_siz.width+start_target_loc.getX()-new_location.getX()), (int) (start_siz.height+start_target_loc.getY()-new_location.getY()));
			frame.setLocation((int) new_location.getX(), (int) new_location.getY());
		}

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)){
			frame.setSize((int) (start_siz.width+start_target_loc.getX()-new_location.getX()), (int) (current.getY()-target_location.getY()));
			frame.setLocation((int) new_location.getX(), (int) target_location.getY());
		}

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)){
			frame.setSize((int) (start_siz.width+start_target_loc.getX()-new_location.getX()), frame.getHeight());
			frame.setLocation((int) new_location.getX(), (int) target_location.getY());
		}

		else if(this.target.getCursor() == Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)){
			frame.setSize(frame.getWidth(), (int) (start_siz.height+start_target_loc.getY()-new_location.getY()));
			frame.setLocation((int) target_location.getX(), (int) new_location.getY());
		}

		frame.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Calendar time = Calendar.getInstance();
		long time_ms = time.getTimeInMillis();
		//Si déjà cliqué il y a moins de Xms alors c'est un double click
		if((int)(time_ms - click_time)<double_click_interval_ms){ 
			this.target.screenshot();
		}else click_time = time_ms;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.start_drag = this.getScreenLocation(e);
		this.start_siz = this.target.getSize();
		this.start_target_loc = this.target.getLocationOnScreen();
		this.start_loc = MoveMouseListener.getFrame(this.target).getLocation();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point f = this.target.getLocationOnScreen();
		Point m = this.getScreenLocation(e);
		Point m_relative = new Point((int)( m.getX()-f.getX()),(int) (m.getY()-f.getY()));
		//On redéfini le cursor en fonction de la position de la souris sur la frame
		if(m_relative.x>=0 && m_relative.x<handler_size){
			if(m_relative.y>=0 && m_relative.y<handler_size)
				this.target.setCursor (Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			else if(m_relative.y>=this.target.getHeight()-handler_size && m_relative.y<this.target.getHeight())
				this.target.setCursor (Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			else 
				this.target.setCursor (Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
		}
		else if(m_relative.x>=this.target.getWidth()-handler_size && m_relative.x<this.target.getWidth()){
			if(m_relative.y>=0 && m_relative.y<handler_size)
				this.target.setCursor (Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			else if(m_relative.y>=this.target.getHeight()-handler_size && m_relative.y<this.target.getHeight())
				this.target.setCursor (Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			else 
				this.target.setCursor (Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		}
		else if(m_relative.y>handler_size && m_relative.y<this.target.getHeight()-handler_size) 
			this.target.setCursor (Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		else if(m_relative.y<10) 
			this.target.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
		else 
			this.target.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}