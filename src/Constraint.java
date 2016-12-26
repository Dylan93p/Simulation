


public abstract class Constraint {
	protected Vector2 p;
	protected Vector2 n;
	protected Vector2 r;
	protected Vector2 r2;
	mass m;
	mass m2;
	protected float l;
	protected float o;
	boolean remove = false;
	public Constraint(Vector2 p,float l,mass m){
		this.p = p;
		this.m=m;
		r=p.sub(m.r);
		this.l = l;
	}
	public Constraint(Vector2 p, mass m, mass m2){
		this.m2=m2;
		this.m=m;
		this.p=p;
	}
	public Constraint(Vector2 n, mass m) {
		this.m=m;
		this.n=n;
	
	}
	public abstract void Satisfy();
	
	
}
class LengthC extends Constraint{

	public LengthC(Vector2 p, float l, mass m) {
		super(p, l, m);
	}
	
	public void Satisfy() {

		if(p.sub(m.r.add(m.v.scale(m.dt))).Mag()>l){
			r = p.sub(m.r).normalize().scale(l);
			m.r=p.sub(r);
		}
		
	}
		
}
class PointC extends Constraint{
	protected Vector2 r,r2,p;
	mass m,m2;
	float M,l1,l2;
	public PointC(Vector2 p, mass m, mass m2){
		super(p,m,m2);
		this.m=m;
		this.m2=m2;
		this.p=p;
		r = p.sub(m.r);
		l1= r.Mag();
		r2= p.sub(m2.r);
		l2=r2.Mag();
		M=m.m+m2.m;
	}

	
	public void Satisfy() {
		p=m.r.add(r).add(m2.r.add(r2)).scale(.5f);
		
		Vector2 dC=(m.v.add(m.F.scale(m.invMass()*m.dt).add(r.Perp().scale(m.w+m.t*(1/m.I)*m.dt))).sub(m2.v.add(m2.F.scale(m2.invMass()*m2.dt).add(r2.Perp().scale(m2.w+m2.t*(1/m2.I)*m.dt)))));
		
		Vector2 K = new Vector2(new Vector2(m.invMass()+m2.invMass()+(1/m.I)*r.y*r.y+(1/m2.I)*r2.y*r2.y,(-1/m.I)*r.x*r.y-(1/m2.I)*r2.y*r2.x),
								new Vector2((-1/m.I)*r.x*r.y-(1/m2.I)*r2.y*r2.x,m.invMass()+m2.invMass()+(1/m.I)*r.x*r.x+(1/m2.I)*r2.x*r2.x));
		
		Vector2 lambda = K.inv().mult(dC);
		
		//m.applyForce(lambda.scale(-1/(m.dt)),p);
		//m2.applyForce(lambda.scale(1/(m2.dt)),p);
		
		
		r=r.rotate(lambda.Dot(m.r));
		r2=r2.rotate(-(m2.w-m.t*(1/m.I)*m2.dt)*m2.dt);
		
		
		
	}
}
class Normal extends Constraint{

	public Normal(Vector2 n, mass m) {
		super(n, m);
		
	}
	
	public void Satisfy(Vector2 F, Vector2 pt){
		m.applyForce(n.scale(F.Dot(n)));
	}

	@Override
	public void Satisfy() {
		// TODO Auto-generated method stub
		
	}
	
}
