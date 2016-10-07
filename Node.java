package binomialHeap;

import Assignment.BinomialHeap2.BinomialHeapNode;

public class Node {
	private int key;
	private int degree; 
	private Node parent;
	private Node sibling;
	private Node child; 
	public Node(int k) {
		key = k;
		degree = 0;
		parent = null;
		sibling = null;
		child = null;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getSibling() {
		return sibling;
	}
	public void setSibling(Node sibling) {
		this.sibling = sibling;
	}
	public Node getChild() {
		return child;
	}
	public void setChild(Node child) {
		this.child = child;
	}
	
	public int getSize() {
		int size = 1;
		size += (child == null) ? 0 : child.getSize();
		size += (sibling == null) ? 0 : sibling.getSize();
		return size;
	}
	
	public Node getMinimum() {
		Node y = this;
		Node x = this;
		int min = x.key;

		while (x != null) {
			if (x.key < min) {
				min = x.key;
				y = x;
			}
			x = x.sibling;
		}
		return y;
	}
	
	public Node getNode(int key) {
		Node temp = this;
		Node node = null;
		while (temp != null) {
			if (temp.key == key) {
				node = temp;
				break;
			}
			if (temp.child == null)
				temp = temp.sibling;
			else {
				node = temp.child.getNode(key);
				if (node == null)
					temp = temp.sibling;
				else
					break;
			}
		}
		return node;
	}
	
	public Node reverse(Node s) {
		Node rev;
		if (sibling != null)
			rev = sibling.reverse(this);
		else
			rev = this;
		sibling = s;
		return rev;
	}
	
	public String toString() {
	      return "key = " + key + ", degree = " + degree;
	    }
	
	public String print(int depth) {
	      String result = "";
	      
	      for (int i = 0; i < depth; i++)
	        result += "  ";
	      
	      result += toString() + "\n";
	      
	      Node x = child;
	      while (x != null) {
	        result += x.print(depth + 1);
	        x = x.sibling;
	      }
	      
	      return result;
	    }
}
