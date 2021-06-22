import java.io.*; 
import java.util.*; 

// Tree node
class Node {
    public int id;
    Node boss;
    Node right;
    Node left;
    Vector <Node> children = new Vector<Node>();
    int lvl;
    int height;
    public Node(int id1){
        id = id1;
        boss = null;
        lvl = 1;
        height = 0;
    }
    public int hgt (Node k){
      if(k == null){
          return -1;
      } 
      return k.height;
  }
    public void updateheight(Node a){
        if(a == null){
            return;
        }
        Node p = new Node(-1);
        Node b = a.right;
        Node c = a.left;
        int b1 = p.hgt(b);
        int c1 = p.hgt(c);
        if(b1 > c1){
            a.height = b1 + 1;
        } else{
          a.height = c1 + 1;
        }
    }
    public int heightdiff(Node a){
        if(a == null){
            return 0;
        }
        Node b = a.right;
        Node c = a.left;
        int b1 = hgt(b);
        int c1 = hgt(c);
        int m = c1 - b1;
        return m;
    }  
}
  class AVLTree{
    Node root;
    public Node RightRot(Node y){
        Node k = new Node(-1);
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        k.updateheight(y);
        k.updateheight(x);
        return x;
    }
    public Node LeftRot(Node x){
        Node k = new Node(-1);
        Node y = x.right;
        //System.out.println("We're in rotation");
        //System.out.println(y.id);
        Node T2 = y.left;
        //System.out.println(y.id);
        y.left = x;
        x.right = T2;
        k.updateheight(x);
        k.updateheight(y);
        //System.out.println(y.id + " "+ y.height);
        return y;
    }
    public Node insert(Node n, int id){
        Node i = new Node(-1);
        if(n == null){
            Node p = new Node(id);
            return p;
        }
        else if(n.id > id){
            Node k = insert(n.left, id);
            n.left = k;
        }
        else if(n.id < id){
            Node k = insert(n.right, id);
            n.right = k;
        }
        else{
            System.out.println("Duplicate ods not allowed");
            return n;
        }
        i.updateheight(n);
        int diff = i.heightdiff(n);
        if(diff <= -2){
            if(id < n.right.id){
                //System.out.println("Hi you're less than and less than");
                //System.out.println("You're here -2 and less than");
                Node j = RightRot(n.right);
                n.right = j;
                i.updateheight(n.right);
                Node k = LeftRot(n);
                i.updateheight(n);
                return k;
            }
            if(id > n.right.id){
                //System.out.println("You're here -2 and greater than");
                //System.out.println(n.id + " " + n.height);
                //System.out.println("Hi you're less than and greater than");
                Node j = LeftRot(n);
                //System.out.println(n.id + " " + n.height);
                i.updateheight(n);
                return j;
            }
        }
        if(diff >= 2){
            if(id < n.left.id){
                //System.out.println("You're here 2 and less than");
                //System.out.println("Hi you're greater than and less than");
                Node k = RightRot(n);
                i.updateheight(n);
                return k;
            }
            if(id > n.left.id){
                //System.out.println("You're here 2 and greater than");
                //System.out.println("Hi you're greater than and greater than");
                Node j = LeftRot(n.left);
                n.left = j;
                i.updateheight(n.left);
                Node k = RightRot(n);
                i.updateheight(n);
                return k;
            }
        }
        return n;
    }
    public Node searchwithid(int id){
        Node nod = root;
        while(nod!= null){
            if(nod.id > id){
                nod = nod.left;
            } else if(nod.id < id){
                nod = nod.right;
            } else{
                return nod;
            }
        }
        return nod;
    }
    public Node replacenode(Node k){
        Node nod = k;
        if(nod == null){
            return nod;
        }
        while(nod.left!= null){
            nod = nod.left; 
        }
        return nod;
    }
    public Node delNode(Node a, int id){
        if(a == null){
            return a;
        }
        if(id < a.id){
            Node j = delNode(a.left, id);
            a.left = j;
        }
        if(id > a.id){
            Node j = delNode(a.right, id);
            a.right = j;
        }
        if(id == a.id){
            if((a.left == null) ||(a.right == null)){
                Node k = new Node(-1);
                if((a.left == null)&&(a.right == null)){
                    a = null;
                    return a;
                }
                if(a.left == null){
                    k = a.right;
                } else{
                    k = a.left;
                }
                a = k;

            } else{
                Node k = new Node(-1);
                k = replacenode(a.right);
                //System.out.println("Deleted ID " + k.id);
                Node temp = a.boss;
                if(temp != null)
                {
                    temp.children.remove(a);}
                a.id = k.id;
                //System.out.println("Deleted ID " + a.id);
                a.children = k.children;
                a.lvl = k.lvl;
                a.boss = k.boss;
                if(k.boss != null){
                    k.boss.children.add(a);}
                //System.out.println("Deleted ID " + a.boss.id);
                Node j = delNode(a.right, k.id);
                a.right = j;
            }
        }
        Node p = new Node(-1);
        p.updateheight(a);
        int diff = p.heightdiff(a);
        if(diff < -1){
            int y = p.heightdiff(a.right);
            if(y > 0){
                Node j = RightRot(a.right);
                a.right = j;
                Node k = LeftRot(a);
                return k;
            } else{
                Node k = LeftRot(a);
                return k;
            }
        } 
        if(diff > 1){
            int y = p.hgt(a.left);
            if(y < 0){
                Node j = LeftRot(a.left);
                a.left = j;
                Node k = RightRot(a);
                return k;
            } else{
                Node k = RightRot(a);
                return k;
            }
        }
        return a;
    }
  }

public class OrgHierarchy implements OrgHierarchyInterface {
Node root = null;
Node o = new Node(-1);
AVLTree tree = new AVLTree();
// no. of employees
public int totalemployees = 0;
int maxlevel = 0;

public boolean isEmpty(){
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");	
	 if(tree.root == null){
		 totalemployees = 0;
		 return true;
	 }
	 return false;
} 

public int size(){
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	return totalemployees;
}

public int level(int id) throws IllegalIDException, EmptyTreeException{
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	if(isEmpty()){
		throw new EmptyTreeException("Tree is Empty");
	}
	Node p = tree.searchwithid(id);
	if(p == null){
		throw new IllegalIDException("No such ID exists");
	}
	return p.lvl;
} 

public void hireOwner(int id) throws NotEmptyException{
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	if(!isEmpty()){
		throw new NotEmptyException("Owner already exists");
	} 
	tree.root = tree.insert(tree.root, id);
	Node k = tree.root;
	root = k;
	totalemployees += 1;
	k.lvl = 1;
}

public void hireEmployee(int id, int bossid) throws IllegalIDException, EmptyTreeException{
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	if(isEmpty()){
		throw new EmptyTreeException("Tree is Empty");
	}
	Node k = tree.searchwithid(bossid);
	if(k == null){
		throw new IllegalIDException("No such Boss ID exists");
	}
	tree.root = tree.insert(tree.root, id);
	Node q = tree.searchwithid(id);
	q.boss = k;
	k.children.add(q);
	totalemployees += 1;
	q.lvl = k.lvl + 1;
} 

public void fireEmployee(int id) throws IllegalIDException,EmptyTreeException{
	//your implementation

 	//throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	 //simply insert and increase the totalemployees by 1
	 if(isEmpty()){
		throw new EmptyTreeException("Tree is Empty");
	 } 
	 Node m = tree.searchwithid(id);
	 if((m == null) || (m.boss == null)){
		throw new IllegalIDException("No such ID exists/can't remove owner");
	 }
	 tree.root = tree.delNode(tree.root, id);
	 Node e = m.boss;
	 e.children.remove(m);
	 m.boss = null;
	 totalemployees -= 1;
}
public void fireEmployee(int id, int manageid) throws IllegalIDException,EmptyTreeException{
	if(isEmpty()){
		throw new EmptyTreeException("Tree is Empty");
	 } 
     //System.out.println("Hi you're at fireemployee with manageid");
	 Node m = tree.searchwithid(id);
	 if((m == null)||(m.boss == null)){
		throw new IllegalIDException("No such ID exists/Can't remove boss");
	 }
	 Node n = tree.searchwithid(manageid);
     
     //System.out.println(n.id);
	 Iterator<Node> f = (m.children).iterator();
	 while(f.hasNext()){
         Node q = f.next();
         //System.out.println(q.id);
		 n.children.add(q);
         q.boss = n;
	 }
	 tree.root = tree.delNode(tree.root, id);
     //Node k = tree.searchwithid(10);
     //System.out.println(k.boss.id);
     //System.out.println(boss(10));
     //System.out.println(boss(10));
	 Node e = m.boss;
     //System.out.println(boss(10));
     if(e != null){
         e.children.remove(m);}
     //System.out.println(boss(10));
	 m = null;
	 totalemployees -= 1;
} 

public int boss(int id) throws IllegalIDException,EmptyTreeException{
	//your implementation
	 //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	 if(isEmpty()){
		 throw new EmptyTreeException("The Tree is Empty");
	 }
	 Node m = tree.searchwithid(id);
	 if (m == null){
		 throw new IllegalIDException("No such employee");
	 }
	 Node b = m.boss;
	 if(b == null){
		 return -1;
	 }
	 return b.id;
}

public int lowestCommonBoss(int id1, int id2) throws IllegalIDException,EmptyTreeException{
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	//Compare their levels, if same, ask them to move up until same boss occurred
	//if not, ask them the one with larger level to come up, until the their level is same and then move up
	if(isEmpty()){
		throw new EmptyTreeException("The Tree is Empty");
	}
    //System.out.println("Hi! You're at lcb");
	Node a = tree.searchwithid(id1);
    //System.out.println(a.boss.lvl);
	Node b = tree.searchwithid(id2);
    //System.out.println(b.lvl);
	if((a == null) || (b == null)||(a.boss == null)||(b.boss == null)){
		throw new IllegalIDException("No such employee/owner doesn't have a boss");
	}
	int a1 = a.lvl;
	int b1 = b.lvl;
	if(a1 == b1){
		Node a2 = a.boss;
		Node b2 = b.boss;
		while((a2 != null)&&(b2 != null)){
			if(a2.id == b2.id){
				return a2.id;
			}else{
				a2 = a2.boss;
				b2 = b2.boss;
			}
		}
	}
	else if(a1 < b1){
		Node a2 = a;
		Node b2 = b;
		int in = b1;
		while((in != a1)&&(b2 != null)){
			b2 = b2.boss;
			in -= 1;
		}
		while((a2 != null)&&(b2 != null)){
			if(a2.id == b2.id){
				return a2.id;
			}else{
				a2 = a2.boss;
				b2 = b2.boss;
			}
		}
	}else{
		Node a2 = a;
		Node b2 = b;
		int in = a1;
		while((in != b1)&&(a2 != null)){
			a2 = a2.boss;
			in -= 1;
		}
		while((a2 != null)&&(b2 != null)){
			if(a2.id == b2.id){
				return a2.id;
			}else{
				a2 = a2.boss;
				b2 = b2.boss;
			}
		}
	}
    return root.id;
}

public String toString(int id) throws IllegalIDException, EmptyTreeException{
    if(isEmpty()){
        throw new EmptyTreeException("The Tree is Empty");
    }
	String s = "";
    Node newroot = tree.searchwithid(id);
    if(newroot == null){
		throw new IllegalIDException("No such ID exists");
	}
    Vector<Node> v = new Vector<Node>(); 
    v.add(newroot);
    int count = 1;
    int n = v.size();
    while(n > 0){
        Vector<Integer> k = new Vector<Integer>();
        count = v.size();
        while(count > 0){
            Node p = v.get(0);
            Vector<Node> l = p.children;
            if(l.size() != 0){
                Iterator<Node> m = l.iterator();
                while(m.hasNext()){
                    v.add(m.next());
                }
            }
            k.add(p.id);
            v.remove(p);
            count -= 1;
        }
        if(k.size() != 0){
            Collections.sort(k);
            Iterator <Integer> g = k.iterator();
            while(g.hasNext()){
                int h = g.next();
                String i = String.valueOf(h);
                if(g.hasNext()){
                    i = i + " ";
                    s = s + i;
                } else{
                    if(v.size() != 0){
                        i = i + ",";
                        s = s + i;
                    }
                    else{
                        s = s + i;
                    }
                }
            }
        }
        n = v.size();
    }
    return s;
}
}
