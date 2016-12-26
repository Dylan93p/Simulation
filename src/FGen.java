

import java.util.ArrayList;
import java.util.List;



public abstract class FGen {
	protected Vector2 r;
	protected Vector2 p1,p2;
	mass m;
	mass m2;
	protected float l;
	protected float damping;
	protected float k;
	public FGen(){}
	public FGen(mass m,Vector2 p1,Vector2 p2){
		this.m=m;
		this.p1=p1;
		this.p2=p2;
	}
	public FGen(mass m, mass m2, Vector2 p1, Vector2 p2){
		this.m=m;
		this.m2=m2;

		this.p1=p1;
		this.p2=p2;
		l = (float) p1.dist(p2);
		r = p1.sub(p2).normalize();
	}
	public FGen(mass m,Vector2 p1,float l){
		this.m=m;
		this.p1=p1;
		this.l=l;
	}
	public abstract void add(mass m);
	public abstract void applyForce();
	
}
class Gravity extends FGen{
	List<mass> M;
	Vector2 G;
	public Gravity(){
		M = new ArrayList<mass>();
		G = new Vector2(0,9.8f);
	}
	public void add(mass m){
		M.add(m);
	}
	@Override
	public void applyForce() {
		for(mass m:M)
			m.applyForce(G.scale(m.m),m.r);
	}
}
class Tension extends FGen{
	public Tension(mass m, Vector2 p1, Vector2 p2){
		super(m,p1,p2);
		l = p2.sub(m.r).Mag();
		r=p2.sub(m.r).normalize();
	}
	public void applyForce() {
		if(p1.sub(m.r.add(m.v.scale(m.dt))).Mag()>l){
		r = p1.sub(m.r).normalize();
		m.v=m.v.sub(r.scale(m.v.Dot(r)));
		m.applyForce(r.scale((m.getMass()*m.v.Dot(m.v))/l+r.scale(-1).Dot(m.F)));
		}
	}
	@Override
	public void add(mass m) {
		// TODO Auto-generated method stub
		
	}
	
}

class Spring extends FGen{
	Vector2 r1,r2;
	public Spring(mass m, mass m2, Vector2 p1, Vector2 p2){
		super(m,m2,p1,p2);
		
		r1 = m.r.sub(p1);
		r2 = m2.r.sub(p2);
		k=75;
		damping = 50;
		
	}

	public void applyForce() {
		p1=m.r.sub(r1);p2=m2.r.sub(r2);
		r1=r1.rotate(-m.w*m.dt);r2=r2.rotate(-m2.w*m2.dt);
		r = p1.sub(p2).normalize();
		m.applyForce(r.scale(-k*(p1.sub(p2).Mag()-l)).add(r.scale(m.v.sub(m2.v).Dot(r)*-damping)),p1);
		m2.applyForce(r.scale(k*(p2.sub(p1).Mag()-l)).add(r.scale(m.v.sub(m2.v).Dot(r)*damping)),p2);
		
	}

	@Override
	public void add(mass m) {
		// TODO Auto-generated method stub
		
	}
}
