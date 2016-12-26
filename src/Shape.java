

public abstract class Shape extends mass{
	public Shape(float m, Vector2 r, float size) {
		super(m, r, size);
		this.size=new Vector2(size,size);
	}

	public Shape(Vector2 p1, Vector2 p2) {
		super(p1,p2);
	}
}

class Circle extends Shape{
	Circle(float m, Vector2 r, float size) {
		super(m, r, size);
		
	}
	public void set(){
		if(active){
		v=v.add(F.scale(invMass()).scale(dt));
		r=r.add(v.scale(dt));
		
		w += t*(1/I)*dt;
		o += w*dt;
		}
		J= new Vector2(0,0);
		
	}
	
}
class Beam extends Shape{
	public Beam(Vector2 p1,Vector2 p2, boolean active) {
		super(p1,p2);
		size=new Vector2(m,20);
		I=(float) ((Math.min(size.x, size.y)*Math.pow(Math.max(size.x, size.y),3))/30);
		beam=true;
		hfw1 = n.scale(size.y/2);
		hfw2 = n.Perp().scale(size.x/2);
	}
	public void set(){
		r=r.add(v.scale(dt));
		v=v.add(F.scale(invMass()).scale(dt));
		
		o += w*dt;
		w += t*(1/I)*dt;
		n = new Vector2((float)Math.sin(o),(float)-Math.cos(o));
		hfw1 = n.scale(size.y/2);
		hfw2 = n.Perp().scale(size.x/2);
		J= new Vector2(0,0);
	}
}