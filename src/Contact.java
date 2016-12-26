import java.util.Iterator;




public class Contact{
	
	public mass m1;
	public mass m2;
	public double dist;
	public Vector2 n;
	public Vector2 pt;
	public Vector2 newJ;
	public float J;
	public float Fr;
	float relNv;
	float remove;
	float temp;
	Contact(mass m1, mass m2){
		if(m1.beam&&m2.beam)
			Con((Beam)m1,(Beam)m2);
		else if(m2.plane)
			Con(m1,(Ground)m2);
		else if(m1.beam)
			Con(m2,(Beam)m1);
		else if(m2.beam)
			Con(m1,(Beam)m2);
		else
			Con(m1,m2);
	}
	void Con(mass m1, mass m2){
		this.m1=m1;
		this.m2=m2;
		n = m1.r.sub(m2.r).normalize();
		pt = m1.r.sub(n.scale(m1.size.x/2));
		relNv = m2.v.sub(m1.v).Dot(n);
		dist = (m2.r.dist(m1.r)-(m2.size.x/2+m1.size.x/2));
		m1.maxHalfW(n);
		
		remove = (float) (-relNv+dist*10f);
		if(remove<=0){
			J = (float)Math.max(((1.2f*(m2.v.add(pt.sub(m2.r).Perp().scale(m2.w)).sub(m1.v.add(pt.sub(m1.r).Perp().scale(m1.w)))).Dot(n))
					/n.Dot(n.scale(m1.invMass()+m2.invMass())))
					+Math.pow(pt.sub(m1.r).Perp().Dot(n),2)/(m1.I)
					+Math.pow(pt.sub(m2.r).Perp().Dot(n),2)/(m2.I),0)/m1.dt;
			//m1.impulse(n.scale(J));
			//m2.impulse(n.scale(-J));
		}
		
		
	}
	void Con(mass m1, Beam b1){
		this.m1=m1;
		m2=b1;
		remove = 1;
		if(sAxis(b1,m1)){
			J = (float)Math.max((1.2f*(m2.v.add(pt.sub(m2.r).Perp().scale(m2.w)).sub(m1.v.add(pt.sub(m1.r).Perp().scale(m1.w)))).Dot(n))
					/(n.Dot(n.scale(m1.invMass()+m2.invMass()))
					+Math.pow(pt.sub(m1.r).Perp().Dot(n),2)/(m1.I)
					+Math.pow(pt.sub(m2.r).Perp().Dot(n),2)/(m2.I)),0)/m1.dt;
			remove=-1;
			}
			//m1.impulse(n.scale(J));
			//m2.impulse(n.scale(-J));
		
	}
	void Con(mass m1, Ground g){
		this.m1 = m1;
		m2 = g;
		//g.init();
		m1.maxHalfW(g.n);
		n = g.n;
		pt = m1.r.sub(n.scale(m1.size.x/2));
		g.J=m1.J.scale(-1);
		dist = (m1.r.dist(new Vector2(m1.r.x,g.r.x)))-m1.size.x/2;
		if(m1.beam)
			dist =(sAxis((Beam)m1,g));
		relNv = g.v.sub(m1.v.add(pt.sub(m1.r).scale(m1.w))).Dot(n);
		
		
		
		remove = (float) (-relNv+dist*10);
		if(remove<=0){
			J = (float)Math.max((1.2f*(m2.v.add(pt.sub(m2.r).Perp().scale(m2.w)).sub(m1.v.add(pt.sub(m1.r).Perp().scale(m1.w)))).Dot(n))
					/(n.Dot(n.scale(m1.invMass()+m2.invMass()))
					+Math.pow(pt.sub(m1.r).Perp().Dot(n),2)/(m1.I)
					+Math.pow(pt.sub(m2.r).Perp().Dot(n),2)/(m2.I)),0)/m1.dt;
			//m1.impulse(n.scale(-J));
			//m1.constraints.add(new Normal(n,m1));
			//m2.constraints.add(new Normal(n,m2));
		}
	}
	void Con(Beam b1, Beam b2){
		m1=b1;
		m2=b2;
		n = m2.r.sub(m1.r).normalize();
		if(sAxis(b1,b2)){
			J = (float)Math.max(((1.2f*(m2.v.add(pt.sub(m2.r).Perp().scale(m2.w)).sub(m1.v.add(pt.sub(m1.r).Perp().scale(m1.w)))).Dot(n))
					/n.Dot(n.scale(m1.invMass()+m2.invMass()))
					+Math.pow(pt.sub(m1.r).Perp().Dot(n),2)/(m1.I)
					+Math.pow(pt.sub(m2.r).Perp().Dot(n),2)/(m2.I)),0)/m1.dt;
			remove=-1;
			}
	}
	
	public void impulse(){
		temp=Math.min(m2.J.add(m1.J).Dot(n),0);
		//J = Math.min((-temp-J),0)-temp;
		m1.impulse(n.scale(Math.max(m1.J.Dot(n)+J,0)-Math.min(m1.F.Dot(n), 0)), pt);
		m2.impulse(n.scale(Math.min(m2.J.Dot(n)-J,0)-Math.max(m2.F.Dot(n), 0)),pt);
		m1.pt=pt;
		m2.pt=pt;
		//m1.init();
		//m2.init();
		
	}
	public void generate(){
		
		
		m1.applyForce(m1.J,pt);
		m2.applyForce(m2.J,pt);
		m1.J=new Vector2(0,0);
		m1.J=new Vector2(0,0);
		//m1.w+=(pt.sub(m1.r).Perp().Dot(n.scale(m1.J.Dot(n)))/m1.I);
		//m2.w+=(pt.sub(m2.r).Perp().Dot(n.scale(m2.J.Dot(n)))/m2.I);
	}

	public boolean sAxis(Beam b1, mass m2){
		b1.maxHalfW(b1.r.sub(m2.r).normalize());
		Vector2 pos = (b1.r).sub(m2.r);
		
		float dP1 = Math.abs(pos.Dot(b1.n));
		float p1 = Math.abs(b1.maxHalfW(b1.n)+m2.size.x/2)+Math.abs(m2.v.Dot(b1.n)*m2.dt);
		if(dP1-p1>0)
			return false;
		float dP2 = Math.abs(pos.Dot(b1.n.Perp()));
		float p2 =  Math.abs(b1.maxHalfW(b1.n.Perp())+m2.size.x/2)+Math.abs(m2.v.Dot(b1.n.Perp())*m2.dt);
		if(dP2-p2>0)
			return false;
		//b1.maxHalfW(pos.normalize());
		//float dP3 = Math.abs(pos.Dot(m2.r.sub(b1.r.add(b1.hfw1.add(b1.hfw2))).normalize()));
		//float p3 = Math.abs(b1.maxHalfW(pos.normalize())+m2.size.x/2)+Math.abs(m2.v.Dot(pos.normalize())*m2.dt);
		
		//if(dP3-p3>0)
			//return false;
		
		if(dP1-p1<dP2-p2)
			
			n=(b1.n.Perp().Dot(pos)<b1.n.Perp().scale(-1).Dot(pos)?b1.n.Perp():b1.n.Perp().scale(-1));
		else 
			n=(b1.n.Dot(pos)<b1.n.scale(-1).Dot(pos)?b1.n:b1.n.scale(-1));
		pt=m2.r.add(m2.v.scale(m2.dt)).sub(n.scale(m2.size.x));
		
		return true;
	}
	public float sAxis(Beam b1, Ground g){
		b1.n= new Vector2((float)Math.sin(b1.o+b1.w*b1.dt),(float)-Math.cos(b1.o+b1.w*b1.dt));
		
		Vector2 pos = b1.r.sub(new Vector2(b1.r.x,g.r.y));
		//b1.maxHalfW(g.n);
		float dP1 = Math.abs(pos.Dot(g.n));
		float P1 = b1.maxHalfW(g.n);
		
		pt=b1.r.sub(b1.hfw1.add(b1.hfw2));
			
		return dP1-P1;
	}
	public boolean sAxis(Beam b1, Beam b2){
		b1.n= new Vector2((float)Math.sin(b1.o+b1.w*b1.dt),(float)-Math.cos(b1.o+b1.w*b1.dt));
		b2.n= new Vector2((float)Math.sin(b2.o+b2.w*b2.dt),(float)-Math.cos(b2.o+b2.w*b2.dt));
		float pn1,pn2;
		Vector2 n1 = b1.n;
		Vector2 n2 = b2.n;
		Vector2 pos = (b2.r.add(b2.v.scale(b2.dt))).sub(b1.r.add(b1.v.scale(b1.dt)));
		float dP1 = Math.abs(pos.Dot(n1));
		
		float P1 = Math.abs(b1.maxHalfW(n1))+Math.abs(b2.maxHalfW(n1));
		if(dP1-P1>0)
			return false;
		float dP2 = Math.abs(pos.Dot(n1.Perp()));
		float P2 =  Math.abs(b1.maxHalfW(n1.Perp()))+Math.abs(b2.maxHalfW(n1.Perp()));
		if(dP2-P2>0)
			return false;
		float dP3 = Math.abs(pos.Dot(n2));
		float P3 =  Math.abs(b1.maxHalfW(n2))+Math.abs(b2.maxHalfW(n2));
		if(dP3-P3>0)
			return false;
		float dP4 = Math.abs(pos.Dot(n2.Perp()));
		float P4 =  Math.abs(b1.maxHalfW(n2.Perp()))+Math.abs(b2.maxHalfW(n2.Perp()));;
		if(dP4-P4>0)
			return false;
		if(dP1-P1> dP2-P2){
			pn1=dP1-P1;
			n1=(n1.Dot(pos)<n1.scale(-1).Dot(pos)?n1:n1.scale(-1));
		}else{
			pn1=dP2-P2;
			n1=(n1.Perp().Dot(pos)<n1.Perp().scale(-1).Dot(pos)?n1.Perp():n1.Perp().scale(-1));
		}
		if(dP3-P3>dP4-P4){
			pn2=dP3-P3;
			n2=(n2.Dot(pos)<n2.scale(-1).Dot(pos)?n2:n2.scale(-1));
		}
		else{
			pn2=dP4-P4;
			n2=(n2.Perp().Dot(pos)<n2.Perp().scale(-1).Dot(pos)?n2.Perp():n2.Perp().scale(-1));
		}
		if(Math.abs(pn1)<Math.abs(pn2)){
			n=n1;m2.maxHalfW(n);
			pt=m2.r.add(m2.hfw1.add(m2.hfw2));
		}else{
			n=n2;m1.maxHalfW(n);
			pt=m1.r.sub(m1.hfw1.add(m1.hfw2));
		}
		if(Math.abs(pn1)-Math.abs(pn2)<.5){
			
		}
		
		return true;
		
		
		
	}
}

