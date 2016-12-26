

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public abstract class mass {
	public float m;
	public Vector2 size;
	public Vector2 F;
	public Vector2 r;
	public Vector2 v = new Vector2(0,0);
	public Vector2 n;
	public Vector2 hfw1;
	public Vector2 hfw2;
	public boolean beam = false;
	public boolean plane = false;
	public boolean active = true;
	public Vector2 J = new Vector2(0,0);
	public Vector2 Jt = new Vector2(0,0);
	public float I;
	public float o;
	public float t;
	public float w;
	public float dt=.1f;
	public float fr = 2f;
	public Vector2 pt;
	public List<Constraint> constraints = new LinkedList<Constraint>();
	public mass(float m, Vector2 r, float size){
		this.m = m;
		this.size=new Vector2(size,size);
		this.r=r;
		I=(float) ((size*Math.pow(size,3))/12);
		pt=r;
	}
	public mass(float m, Vector2 r){
		this.m = m;		
		this.r=r;
		I=1E10f;
		pt=r;
	}

	public mass(Vector2 p1, Vector2 p2){
		o=(float) Math.asin(p1.sub(p2).normalize().Perp().Dot(new Vector2(-p1.sub(p2).x,0).normalize()));
		n = new Vector2((float)Math.sin(o),(float)-Math.cos(o));
		m = (float)p1.dist(p2);
		r=p1.add(p2).scale(.5f);
		n = new Vector2((float)Math.sin(o),(float)-Math.cos(o));

		pt=r;
	}
	public float maxHalfW(Vector2 pN){
		if(beam){
			if(hfw1.Dot(pN)<hfw1.scale(-1).Dot(pN))
				hfw1=hfw1.scale(-1);
			if(hfw2.Dot(pN)<hfw2.scale(-1).Dot(pN))
				hfw2=hfw2.scale(-1);
					
		return Math.abs(hfw1.Dot(pN))+Math.abs(hfw2.Dot(pN));
		}
		else
		return (size.x/2);
	}
	public void init(){
		F = new Vector2(0,0);
		t = 0;
	}
	public void impulse(Vector2 J) {
		this.J = J.add(this.J);
	}
	public void impulse(Vector2 J,Vector2 Jt){
		this.J = J.add(this.J);
		this.Jt = Jt.add(this.Jt);
	}
	public void applyForce(Vector2 F,Vector2 point){
		this.F=this.F.add(F);
		t += (point.sub(r)).Perp().Dot(F);
	}
	public void applyForce(Vector2 F){
		this.F=this.F.add(F);
	}
	public void set(){
		if(active){
		r=r.add(v.scale(dt));
		v=v.add(F.scale(invMass()).scale(dt));
	
		w += t*(1/I)*dt;
		o += w*dt;
		n = new Vector2((float)Math.sin(o),(float)-Math.cos(o));
		hfw1 = n.scale(size.y/2);
		hfw2 = n.Perp().scale(size.x/2);
		
	}
	J = new Vector2(0,0);Jt = new Vector2(0,0);}

	public float getMass(){
		return m;
	}
	
	public float invMass(){
		return (m!=0) ? 1/m:0;
	}
	
	
}
