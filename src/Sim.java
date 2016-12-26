

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;


public class Sim extends JPanel implements Runnable{
final Window w;
LinkedList<mass> M;
LinkedList<FGen> fG;
LinkedList<Constraint> C;
Ground gr;
FGen G = new Gravity();
Arbiter cons = new Arbiter();
public Sim(Window win){
	super();
	w=win;
	M=new LinkedList<mass>();
	fG=new LinkedList<FGen>();
	C=new LinkedList<Constraint>();
	gr = new Ground(5.972e24f,new Vector2(599,599));
	gr.init();
	M.add(gr);
	fG.add(G);
}


	public void run(){
		for(FGen fg:fG)
			fg.applyForce();
		for(int i=0;i<M.size();i++)
			for(int k=0;k<i;k++){
				Contact con =new Contact(M.get(i),M.get(k));
				if(con.remove<0)
					cons.add(con);
			}
		for(int i=0;i<cons.size();i++)
			cons.get(i).impulse();
		cons.generate();
		//for(mass m: M)
			//m.applyForce(m.J,m.pt);
		cons.clear();
		for(Constraint c: C)
			c.Satisfy();
		
		for(int i=0;i<M.size();i++){
			
			M.get(i).set();
			M.get(i).init();
			
		}
		
		repaint();
	}
	
	private boolean contained(Vector2 r,mass m){
		if(m.beam){
			Vector2 pos = r.sub(m.r);
			if(Math.abs(pos.Dot(m.n))>m.hfw1.Mag())
				return false;
			if(Math.abs(pos.Dot(m.n.Perp()))>m.hfw2.Mag())
				return false;
			return true;
		}else if(m.plane){
			if(r.y>m.r.y)
				return true;
		}else{
			if(r.sub(m.r).Mag()<m.size.x/2)
				return true;
			
		}return false;
	}
	public mass getM(Vector2 r){
		
		for(mass m:M)
			if(contained(r,m))
				return m;
		return null;
	}
	public void getMs(Vector2 r){
		for(int i=1;i<M.size();i++){
			if(contained(r,M.get(i))&&w.m==null){
				w.m=M.get(i);System.out.println(i);
			}
			else if(contained(r,M.get(i))&&w.m1==null&&!M.get(i).equals(w.m)){
				w.m1=M.get(i);System.out.println(i);
			}
		}
	}
	public void add(mass m){
		M.add(m);
		m.init();
		G.add(m);
	}
	public void add(Constraint c){
		C.add(c);
	}
	public void add(FGen fg){
		this.fG.add(fg);
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(1800,800);
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		g.drawLine(0, (int) (gr.r.x), this.getWidth(), (int) (gr.r.x));
		if(w.cursor!=null&&w.sel!=null&&w.getSel()!=10)
			g.drawLine((int)(w.sel.x), (int)(w.sel.y),(int) (w.cursor.x),(int) (w.cursor.y));
		
		for(mass m: M){
			if(m.beam){
				Rectangle rect = new Rectangle((int)(m.r.x-m.size.x/2), (int)(m.r.y-m.size.y/2), (int) (m.size.x),(int) (m.size.y));
				Path2D.Double p = new Path2D.Double();
				p.append(rect, false);
				AffineTransform t = new AffineTransform();
				
				t.rotate(m.o,m.r.x,m.r.y);
				
				p.transform(t);
				((Graphics2D) g).draw(p);
			}else if(!m.plane){
			g.setColor(Color.BLUE);
			g.fillOval((int)(m.r.x-m.size.x/2), (int)(m.r.y-m.size.y/2), 
					(int)(m.size.x),(int) (m.size.y));
			}

		}
		for(FGen F:fG){
			if(!F.equals(G)){
				g.drawLine((int)(F.p1.x),(int)( F.p1.y), 
						(int)(F.p2.x),(int)(F.p2.y));
			}
		}		
		for(Constraint c: C){
			g.fillOval((int)c.p.x,(int) c.p.y, 5, 5);
		}
		g.dispose();
	}
	

		
}



