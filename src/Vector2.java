


public class Vector2 {
	public float x=0,y=0;
	public Vector2 r,v;
	
	Vector2(float x_,float y_){
		x=x_;
		y=y_;
	}
	Vector2(Vector2 r_, Vector2 v_){
		r=r_;
		v=v_;
	}
	public float Mag(){
		float mag=x*x+y*y;
		return (float) ((mag<=0)?0:Math.sqrt(mag));
	}
	
	public Vector2 add(Vector2 V){
		return new Vector2(x+V.x,y+V.y);
	}
	public Vector2 inv(){
		float det=det();
		return new Vector2(new Vector2(v.y,-v.x).scale(1/det),new Vector2(-r.y,r.x).scale(1/det));
	}
	public Vector2 mult(Vector2 V){
		return new Vector2(new Vector2(r.x,v.x).Dot(V),new Vector2(r.y,v.y).Dot(V));
	}
	public float det(){
		return(r.x*v.y-v.x*r.y);
	}
	public Vector2 sub(Vector2 V){
		return new Vector2(x-V.x,y-V.y);
	}

	public Vector2 scale(float s){
		return new Vector2(x*s,y*s);
	}
	
	public float Dot(Vector2 V){
		return x*V.x + y*V.y;
	}
	
	public Vector2 normalize(){
		return (Mag()==0)?new Vector2(0,0):new Vector2(x/Mag(),y/Mag());
	}
	public Vector2 rotate(float o){
		return new Vector2((float)(x*Math.cos(o)+y*Math.sin(o)),(float)(y*Math.cos(o)-x*Math.sin(o)));
	}
	
	public Vector2 Perp(){
		return new Vector2(-y,x);
	}
	public float dist(Vector2 V){
		return this.sub(V).Mag();
	}
	public void zero(){
		x=0;
		y=0;
	}

}
