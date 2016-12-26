import java.util.ArrayList;

public class BSPtree {
	Node root;
	public BSPtree(){
		root=null;
	}
	public void addRoot(Node r){
		if(root==null) root=r;
	}
	public void add(mass m){
		add(root,m);
	}
	public void add(Node n, mass m){
		if(n.lChild==null&&n.rChild==null){
			n.M.add(m);
		}
		else if(n.cut.x){
			if(m.r.x<n.cut.part){
				add(n.lChild, m);
			}else {
				add(n.rChild, m);}
		}else{
			if(m.r.y<n.cut.part){
				add(n.lChild, m);
			}else{
				add(n.rChild, m);
				}
		}
	}
	public void conTraverse(){
		
	}
}

class Node{
	Node lChild;
	Node rChild;
	Partition cut;
	ArrayList<mass> M;
	public Node(){}
	public Node(Partition p){
		lChild=null;
		rChild=null;
		cut = p;
	}
	public Node getLChild(){
		return lChild;
	}
	public Node getRChild(){
		return rChild;
	}
		
}
abstract class Partition {
	float part;
	boolean x = false;
	public Partition(float p){
		part=p;
	}
	
}class xPart extends Partition{
		public xPart(float p) {
			super(p);
			x=true;
		}
	}
	class yPart extends Partition{
		public yPart(float p) {
			super(p);
		}
		
	}
