
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class Arbiter implements Iterable{
	ArrayList<Contact> cons;
	public Arbiter(){
		cons = new ArrayList<Contact>();
	}
	public void add(Contact con){
		cons.add(con);
	}
	public void clear(){
		cons=new ArrayList<Contact>();
	}
	public void generate(){
		for(Contact con: cons){
			con.generate();
			con.m1.J=new Vector2(0,0);
			con.m2.J=new Vector2(0,0);
		}
	}
	public Contact get(int i){
		return cons.get(i);
	}
	public int size(){
		return cons.size();
	}
@Override
public Iterator iterator() {
	return cons.iterator();
}
}
