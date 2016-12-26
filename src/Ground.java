

public class Ground extends mass{
	public Ground(float m, Vector2 r) {
		super(m, r);

		plane=true;
		n = new Vector2(0,-1);
		hfw1=new Vector2(0,0);
		hfw2=new Vector2(0,0);
	}

	@Override
	
	public void set(){
		J = new Vector2(0,0);Jt=new Vector2(0,0);
	}
	public void init(){
		v=new Vector2(0,0);
		w=0;
		o=0;
		F = new Vector2(0,0);
		t = 0;
		J = new Vector2(0,0);
	}
	
	public void impulse(Vector2 J,Vector2 Jt){init();}

	
	
}
