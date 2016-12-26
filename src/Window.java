import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;



@SuppressWarnings("serial")
public class Window extends JFrame {
	GMenu panel2;
	Sim s;
	float dt=.1f;
	Timer timer;
	KeyboardFocusManager manager;
	Vector2 sel=null;
	Vector2 cursor=null;
	LinkedList<Shape> enemies;
	mass m;
	mass m1;
	public static void main(String[]args){
		Window w = new Window();
		w.setDefaultCloseOperation(EXIT_ON_CLOSE);
		w.setSize(1800,1000);
		w.setVisible(true);
	}
	
	public Window(){
		
		super("SUPER AWESOME");

		manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new Dispatcher());
		enemies = new LinkedList<Shape>();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy=0;
		s=new Sim(this);
		s.setBackground(Color.white);
		add(s,c);
		panel2=new GMenu();
		c.fill=GridBagConstraints.VERTICAL;
		c.gridy=1;
		add(panel2,c);
		TimeClass tc = new TimeClass(dt);
		timer = new Timer(10,tc);
		addMouseWheelListener(
				new MouseWheelListener(){
					public void mouseWheelMoved(MouseWheelEvent e) {
						//zoom(new Vector2(e.getX(),e.getY()),e.getUnitsToScroll());
					}
					
				});
		s.addMouseListener(
				new MouseListener(){
					public void mouseClicked(MouseEvent e){
						
						sel = new Vector2(e.getX(),e.getY());
						switch(getSel()){
						case 1:{
							if(m==null){
								s.add(new Circle(10,sel,30));
								repaint();
								}
								sel=null;
								cursor=null;
								break;
							}
						case 2:{
							m=m1=null;
							s.getMs(sel);
							if(m!=null&&m1!=null){
								s.add(new PointC(sel,m,m1));
								//m.constraints.add(tmp);
								//m1.constraints.add(tmp);
								//System.out.println("yuuup");
							}
							repaint();
							sel=cursor=null;m=m1=null;
							break;
						}
						}
					}
					public void mousePressed(MouseEvent e){
					sel = new Vector2(e.getX(),e.getY());
					
						m=s.getM(sel);
						
					}
					public void mouseReleased(MouseEvent e){
						Vector2 r = new Vector2(e.getX(),e.getY());
						
						switch(getSel()){
						
						case 1:{
							if(sel!=null&&cursor!=null&&m==null){
								if(r.dist(sel)>10){
									s.add(new Circle((float)sel.dist(cursor),sel.add(cursor).scale(.5f),(float)sel.dist(cursor)));
									repaint();}
									sel=null;cursor=null;
									}
							break;}
						case 2:{
							if(s.getM(r)!=null&&r.dist(sel)>10&&cursor!=null){
								s.add(new Spring(m,s.getM(r),sel,r));
							}else
							if(sel!=null&&m!=null&&r.dist(sel)>10){
								s.add(new Tension(m,sel,r));
								s.add(new LengthC(r, r.sub(m.r).Mag(),m));
							}
							sel=null;cursor=null;m=null;
						break;
						}
						case 11:{
							if(sel!=null&&cursor!=null){
								if(r.dist(sel)>10){
								s.add(new Beam(r,sel,true));
								repaint();}
								sel=null;cursor=null;
								}
							break;
						}}
						if(m!=null)
							m.active=true;
						if(getSel()!=2)
							m=null;
					}
					public void mouseEntered(MouseEvent e) {
						
					}
					public void mouseExited(MouseEvent arg0) {
						
					}
				}
				);
		s.addMouseMotionListener(
				new MouseMotionListener(){
					public void mouseDragged(MouseEvent e){
						switch(getSel()){
						case 1:{
							if(m==null){
								cursor=new Vector2(e.getX(),e.getY());
								repaint();
							}break;
						}
						case 2:{
							if(sel!=null){
								cursor=new Vector2(e.getX(),e.getY());
								repaint();
							}break;
						}
						case 11:{
							if(m==null){
								cursor=new Vector2(e.getX(),e.getY());
								repaint();
							}break;
						}
						}if(m!=null&&getSel()!=2){
							m.active=false;
							m.r=new Vector2(e.getX(),e.getY());
							m.v=new Vector2(0,0);
							m.w=0;
							m.J=new Vector2(0,0);
							m.init();
						}
					}
					public void mouseMoved(MouseEvent e){}
				}
				
				);
	}
	public int getSel(){
		return panel2.sel;
	}
	public int getOp(){
		return panel2.op;
	}
	private class Dispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
 
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            	
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
            }
            return false;
        }
    }	
class TimeClass implements ActionListener{
	float dt;
	public TimeClass(float counter){
		this.dt = counter;
	}
	public void actionPerformed(ActionEvent e) {
		s.run();
		s.repaint();
			//image(s);
	}	
	
}
	
class GMenu extends JTabbedPane{
	OptionPanel oP = new OptionPanel();
	OptionPanel oP2 = new OptionPanel();
	JPanel buildings = new JPanel();
	JPanel units = new JPanel();
	Button RigidBody = new Button("RB");
	Button beam = new Button("Beam");
	Button circle = new Button("Circle");
	Button constraint = new Button("Constraint");
	Button start = new Button("Start");
	int sel=0;
	int op=0;
	public GMenu(){
		super();
		addTab("Units",units);
		units.add(start);
		units.add(oP);
		units.add(RigidBody);
		units.add(constraint);
		//add(start);
		
		setBackground(Color.DARK_GRAY);
		start.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						timer.start();
						
					}
				});
		beam.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						
					}
				});
		
		
		RigidBody.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						oP.set(new Button[]{beam,circle});
						beam.addActionListener(
								new ActionListener(){
					public void actionPerformed(ActionEvent e){
						sel = 11;
					
					}});
						circle.addActionListener(
								new ActionListener(){
					public void actionPerformed(ActionEvent e){
						sel = 1;
					
					}});
					}});
		constraint.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						sel = 2;
					}
				});
		
		
	}
	public Dimension getPreferredSize(){
		return new Dimension(1000,200);
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
class OptionPanel extends JPanel{
	
	OptionPanel(){
		super();
		setBackground(Color.WHITE);
	}
	public void set(Button[] B){
		this.removeAll();
		for(int i=0;i<B.length;i++)
			add(B[i]);
		this.revalidate();
	}
	public Dimension getPreferredSize(){
		return new Dimension(500,200);
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
}
}
class Button extends JButton{
	String name;
	Button(String name){
		super();
		this.name=name;
		enableInputMethods(true);
		
	}
	public Dimension getPreferredSize(){
		return new Dimension(40,40);
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillRect(0, 0, 40, 40);
		g.setColor(Color.BLACK);
		g.drawString(name.substring(0,2), 10, 15);
	}

}
}
