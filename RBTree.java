package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

enum Color { RED, BLACK }

class Node
{
    public int key;
    public Node left;
    public Node right;
    public Node parent;
    public Color color;
    public Node successor, predecessor;

    public Node(int key, Color nodeColor, Node left, Node right) {
        this.key = key;
        this.color = nodeColor;
        this.left = left;
        this.right = right;
        if (left  != null)  left.parent = this;
        if (right != null) right.parent = this;
        this.parent = null;
        this.successor = null;
        this.predecessor = null;
    }
    public Node grandparent() {
        return parent.parent;
    }
    public Node sibling() {
        if (this == parent.left)
            return parent.right;
        else
            return parent.left;
    }
    public Node uncle() {
        return parent.sibling();
    }
}

public class RBTree
{
    public static final boolean VERIFY_RBTREE = true;
    private static final int INDENT_STEP = 4;

    public Node minimum;
    public Node maximum;
    public Node root;

    public RBTree() {
        root = null;
        minimum = null;
        maximum = null;
    }

    private static Color nodeColor(Node n) {
        return n == null ? Color.BLACK : n.color;
    }

    private Node successor (Node x) {
        if (x.right != null) {
            return minimumNode(x.right);
        }
        else {
            Node p = x.parent;
            while (p != null && x == p.right) {
                x = p;
                p = p.parent;
            }
            return p;
        }
    }

    private Node predecessor (Node x) {
        if (x.left != null) {
            return maximumNode(x.left);
        }
        else {
            Node p = x.parent;
            while (p != null && x == p.left) {
                x = p;
                p = p.parent;
            }
            return p;
        }
    }

    private Node lookupNode(int key) {
        Node n = root;
        while (n != null) {
            if (key == n.key) {
                return n;
            } else if (key < n.key) {
                n = n.left;
            } else {
                n = n.right;
            }
        }
        return n;
    }
    public Node lookup(int key) {
        Node n = lookupNode(key);
        return n;
    }
    private void rotateLeft(Node n) {
        Node r = n.right;
        replaceNode(n, r);
        n.right = r.left;
        if (r.left != null) {
            r.left.parent = n;
        }
        r.left = n;
        n.parent = r;
    }
    private void rotateRight(Node n) {
        Node l = n.left;
        replaceNode(n, l);
        n.left = l.right;
        if (l.right != null) {
            l.right.parent = n;
        }
        l.right = n;
        n.parent = l;
    }
    private void replaceNode(Node oldn, Node newn) {
        if (oldn.parent == null) {
            root = newn;
        } else {
            if (oldn == oldn.parent.left)
                oldn.parent.left = newn;
            else
                oldn.parent.right = newn;
        }
        if (newn != null) {
            newn.parent = oldn.parent;
        }
    }
    public void insert(int key) {
        Node insertedNode = new Node(key, Color.RED, null, null);
        if (root == null) {
            root = insertedNode;
        } else {
            Node n = root;
            while (true) {
                if (key == n.key) {
                    return;
                } else if (key < n.key) {
                    if (n.left == null) {
                        n.left = insertedNode;
                        break;
                    } else {
                        n = n.left;
                    }
                } else {
                    if (n.right == null) {
                        n.right = insertedNode;
                        break;
                    } else {
                        n = n.right;
                    }
                }
            }
            insertedNode.parent = n;
        }
        insertHelper1(insertedNode);

        Node successor = successor(insertedNode);
        Node predecessor = predecessor(insertedNode);
        insertedNode.successor = successor;
        if (successor != null)
            successor.predecessor = insertedNode;
        insertedNode.predecessor = predecessor;
        if (predecessor != null)
        predecessor.successor = insertedNode;
        if (minimum == null) {
            minimum = insertedNode;
        }
        else if (insertedNode.key < minimum.key) {
            minimum = insertedNode;
        }
        if (maximum == null) {
            maximum = insertedNode;
        }
        else if (insertedNode.key > maximum.key) {
            maximum = insertedNode;
        }
    }
    private void insertHelper1(Node n) {
        if (n.parent == null)
            n.color = Color.BLACK;
        else
            insertHelper2(n);
    }
    private void insertHelper2(Node n) {
        if (nodeColor(n.parent) == Color.BLACK)
            return; // Tree is still valid
        else
            insertHelper3(n);
    }
    void insertHelper3(Node n) {
    	//Case 1
        if (nodeColor(n.uncle()) == Color.RED) {
            n.parent.color = Color.BLACK;
            n.uncle().color = Color.BLACK;
            n.grandparent().color = Color.RED;
            insertHelper1(n.grandparent());
        } else {
            insertHelper4(n);
        }
    }
    void insertHelper4(Node n) {
    	//Case 2
        if (n == n.parent.right && n.parent == n.grandparent().left) {
            rotateLeft(n.parent);
            n = n.left;
        } else if (n == n.parent.left && n.parent == n.grandparent().right) {
            rotateRight(n.parent);
            n = n.right;
        }
        
        //Case 3
        n.parent.color = Color.BLACK;
        n.grandparent().color = Color.RED;
        if (n == n.parent.left && n.parent == n.grandparent().left) {
            rotateRight(n.grandparent());
        } else {
            rotateLeft(n.grandparent());
        }
    }

    
    private static Node maximumNode(Node n) {
        if (n != null) {
            while (n.right != null) {
                n = n.right;
            }
            return n;
        }
        else {
            return null;
        }
    }
    private static Node minimumNode (Node n) {
        if (n != null) {
            while (n.left != null) {
                n = n.left;
            }
            return n;
        }
        else {
            return null;
        }
    }
    public void delete(int key) {
        Node n = lookupNode(key);

        if (n == null)
            return;  // intey not found, do nothing
        Node successor = n.successor;
        Node predecessor = n.predecessor;
        if (minimum == n) {
            minimum = n.successor;
        }
        if (maximum == n) {
            maximum = n.predecessor;
        }
        if (n.left != null && n.right != null) {
            // Copy key/value from predecessor and then delete it instead
            Node pred = maximumNode(n.left);
            n.key   = pred.key;
            n = pred;
        }

        //assert n.left == null || n.right == null;
        Node child = (n.right == null) ? n.left : n.right;
        if (nodeColor(n) == Color.BLACK) {
            n.color = nodeColor(child);
            deleteHelper1(n);
        }
        replaceNode(n, child);
        
        if (nodeColor(root) == Color.RED) {
            root.color = Color.BLACK;
        }

        if (successor != null)
        successor.predecessor = predecessor;
        if (predecessor != null)
        predecessor.successor = successor;

    }

    private void deleteHelper1(Node n) {
        if (n.parent == null)
            return;
        else
            deleteHelper2(n);
    }
    private void deleteHelper2(Node n) {
        if (nodeColor(n.sibling()) == Color.RED) {
            n.parent.color = Color.RED;
            n.sibling().color = Color.BLACK;
            if (n == n.parent.left)
                rotateLeft(n.parent);
            else
                rotateRight(n.parent);
        }
        deleteHelper3(n);
    }
    private void deleteHelper3(Node n) {
        if (nodeColor(n.parent) == Color.BLACK &&
            nodeColor(n.sibling()) == Color.BLACK &&
            nodeColor(n.sibling().left) == Color.BLACK &&
            nodeColor(n.sibling().right) == Color.BLACK)
        {
            n.sibling().color = Color.RED;
            deleteHelper1(n.parent);
        }
        else
            deleteHelper4(n);
    }
    private void deleteHelper4(Node n) {
        if (nodeColor(n.parent) == Color.RED &&
            nodeColor(n.sibling()) == Color.BLACK &&
            nodeColor(n.sibling().left) == Color.BLACK &&
            nodeColor(n.sibling().right) == Color.BLACK)
        {
            n.sibling().color = Color.RED;
            n.parent.color = Color.BLACK;
        }
        else
            deleteHelper5(n);
    }
    private void deleteHelper5(Node n) {
        if (n == n.parent.left &&
            nodeColor(n.sibling()) == Color.BLACK &&
            nodeColor(n.sibling().left) == Color.RED &&
            nodeColor(n.sibling().right) == Color.BLACK)
        {
            n.sibling().color = Color.RED;
            n.sibling().left.color = Color.BLACK;
            rotateRight(n.sibling());
        }
        else if (n == n.parent.right &&
                 nodeColor(n.sibling()) == Color.BLACK &&
                 nodeColor(n.sibling().right) == Color.RED &&
                 nodeColor(n.sibling().left) == Color.BLACK)
        {
            n.sibling().color = Color.RED;
            n.sibling().right.color = Color.BLACK;
            rotateLeft(n.sibling());
        }
        deleteHelper6(n);
    }
    private void deleteHelper6(Node n) {
        n.sibling().color = nodeColor(n.parent);
        n.parent.color = Color.BLACK;
        if (n == n.parent.left) {
            //assert nodeColor(n.sibling().right) == Color.RED;
            n.sibling().right.color = Color.BLACK;
            rotateLeft(n.parent);
        }
        else
        {
            //assert nodeColor(n.sibling().left) == Color.RED;
            n.sibling().left.color = Color.BLACK;
            rotateRight(n.parent);
        }
    }

    public static void main(String[] args) {
        RBTree t = new RBTree();
        int size = 100;
        /*Random r = new Random();
        
        ArrayList <Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < size; i ++) {
            items.add(i);
        }
        Collections.shuffle(items);
        for (int i = 0; i < size; i++) {
            int x = items.get(i);

            System.out.println("Inserting " + x);

            t.insert(x);
        }
        for (int i = 0; i < size/4; i++) {
            int deletionIndex = r.nextInt(size);
            t.delete(deletionIndex);
        }*/


        for (int i = 0; i < size; i++) {
            Node n = t.lookup(i);
            if (n != null)
            if (n.successor != null)
            if (n.predecessor != null)
            System.out.println(n.predecessor.key+"("+n.predecessor.color+")"+ "<-" + n.key +"("+n.color+")"+ "->" + n.successor.key+"("+n.successor.color+")");
        }
        
        List<Integer> list = new ArrayList<Integer>();
		File file = new File("file.txt");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;

		    while ((text = reader.readLine()) != null) {
		        list.add(Integer.parseInt(text));
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
		int i;
		for(i=0; i<list.size();i++){
			t.insert(list.get(i));
			System.out.println("After inserting "+list.get(i));
			System.out.println("Root: "+t.root.key);
			
			for (int j = 0; j <= i; j++) {
	            Node n = t.lookup(list.get(j));
	            if (n != null)
	            if (n.successor != null)
	            if (n.predecessor != null){
	            System.out.println(n.predecessor.key+"("+n.predecessor.color+")"+ "<-" + n.key +"("+n.color+")"+ "->" + n.successor.key+"("+n.successor.color+")");
	            System.out.println("Left:" +n.left == null ? "Nil" : n.left.key+" Right: "+n.right == null ? "Nil" : n.right.key);}
	        }
		}
        
        char ch;
		Scanner scan = new Scanner(System.in);
		do    
        {
            System.out.println("\nRed Black Tree Operations\n");
            System.out.println("1. insert ");
            System.out.println("2. search");
            System.out.println("3. delete");
            System.out.println("5. successor");
            System.out.println("6. predecessor");
            System.out.println("7. min");
            System.out.println("8. max");
 
            int choice = scan.nextInt();            
            switch (choice)
            {
            case 1 : 
                System.out.println("Enter integer element to insert");
                t.insert(Integer.parseInt(scan.next()));                     
                break;                          
            case 2 : 
                System.out.println("Enter integer element to search");
                System.out.println("Search result : "+ t.lookup(Integer.parseInt(scan.next())));
                break;                                          
            case 3 : 
            	System.out.println("Enter integer element to be deleted");
            	t.delete(Integer.parseInt(scan.next()));
                break;
            case 5 : 
                System.out.println("Enter the element");
                System.out.println(t.successor(t.lookup(Integer.parseInt(scan.next()))).key);
                break;  
            case 6 : 
                System.out.println("Enter the element");
                System.out.println(t.predecessor(t.lookup(Integer.parseInt(scan.next()))).key);
                break; 
            case 7 : 
            	System.out.println(t.minimum.key);
                break; 
            case 8 : 
            	System.out.println(t.maximum.key);
                break; 
            default : 
                System.out.println("Wrong Entry \n ");
                break;    
            }
           
            for (i = 0; i < size; i++) {
                Node n = t.lookup(i);
                if (n != null)
                if (n.successor != null)
                if (n.predecessor != null)
                System.out.println(n.predecessor.key+"("+n.predecessor.color+")"+ "<-" + n.key +"("+n.color+")"+ "->" + n.successor.key+"("+n.successor.color+")");
            }
    		System.out.println();
 
            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);                        
        } while (ch == 'Y'|| ch == 'y'); 
    }
}