package binomialHeap;

public class BinomialHeap {

	private Node Node;
	private int size;
	
	public BinomialHeap(){
		this.Node = null;
		this.size = 0;
	}
	
	public Node getNode() {
		return Node;
	}
	public void setNode(Node node) {
		Node = node;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getMinimum() {
		return Node.getMinimum().getKey();
	}
	
	private void union(Node binHeap) {
		merge(binHeap);

		Node prevTemp = null;
		Node temp = Node;
		Node nextTemp = Node.getSibling();

		while (nextTemp != null) {
			if ((temp.getDegree() != nextTemp.getDegree())
					|| ((nextTemp.getSibling() != null) 
							&& (nextTemp.getSibling().getDegree() == temp.getDegree()))) {
				prevTemp = temp;
				temp = nextTemp;
			} else {
				if (temp.getKey() <= nextTemp.getKey()) {
					temp.setSibling(nextTemp.getSibling());
					nextTemp.setParent(temp);
					nextTemp.setSibling(temp.getChild());
					temp.setChild(nextTemp);
					temp.setDegree(temp.getDegree()+1);;
				} else {
					if (prevTemp == null) {
						Node = nextTemp;
					} else {
						prevTemp.setSibling(nextTemp);
					}
					temp.setParent(nextTemp);
					temp.setSibling(nextTemp.getChild());
					nextTemp.setChild(temp);
					nextTemp.setDegree(nextTemp.getDegree()+1);
					temp = nextTemp;
				}
			}
			nextTemp = temp.getSibling();
		}
	}
	
	private void merge(Node binHeap) {
		Node node1 = Node, node2 = binHeap;
		while ((node1 != null) && (node2 != null)) {
			if (node1.getDegree() == node2.getDegree()) {
				Node tmp = node2;
				node2 = node2.getSibling();
				tmp.setSibling(node1.getSibling());
				node1.setSibling(tmp);
				node1 = tmp.getSibling();
			} else {
				if (node1.getDegree() < node2.getDegree()) {
					if ((node1.getSibling() == null) || (node1.getSibling().getDegree() > node2.getDegree())) {
						Node tmp = node2;
						node2 = node2.getSibling();
						tmp.setSibling(node1.getSibling());
						node1.setSibling(tmp);
						node1 = tmp.getSibling();
					} else {
						node1 = node1.getSibling();
					}
				} else {
					Node tmp = node1;
					node1 = node2;
					node2 = node2.getSibling();
					node1.setSibling(tmp);
					if (tmp == Node) {
						Node = node1;
					}
				}
			}
		}

		if (node1 == null) {
			node1 = Node;
			while (node1.getSibling() != null) {
				node1 = node1.getSibling();
			}
			node1.setSibling(node2);
		}
	}
	
	public void heapInsert(int x) {
		if (x > 0) {
			Node temp = new Node(x);
			if (Node != null) {
				union(temp);
				size++;
			} else {
				Node = temp;
				size = 1;
			}
		}
	}
	
	public int extractMin() {
		if (Node == null)
			return -1;

		Node temp = Node;
		Node prevTemp = null;
		Node minNode = Node.getMinimum();
		while (temp.getKey() != minNode.getKey()) {
			prevTemp = temp;
			temp = temp.getSibling();
		}

		if (prevTemp == null) {
			Node = temp.getSibling();
		} else {
			prevTemp.setSibling(temp.getSibling());
		}
		temp = temp.getChild();
		Node tempNode = temp;
		while (temp != null) {
			temp.setParent(null);
			temp = temp.getSibling();
		}

		if ((Node == null) && (tempNode == null)) {
			size = 0;
		} else {
			if ((Node == null) && (tempNode != null)) {
				Node = tempNode.reverse(null);
				size = Node.getSize();
			} else {
				if ((Node != null) && (tempNode == null)) {
					size = Node.getSize();
				} else {
					union(tempNode.reverse(null));
					size = Node.getSize();
				}
			}
		}

		return minNode.getKey();
	}
	
	public void decreaseKeyValue(int x, int k) {
		Node temp = Node.getNode(x);
		if (temp == null)
			return;
		temp.setKey(k);
		Node tempParent = temp.getParent();

		while ((tempParent != null) && (temp.getKey() < tempParent.getKey())) {
			int z = temp.getKey();
			temp.setKey(tempParent.getKey());
			tempParent.setKey(z);

			temp = tempParent;
			tempParent = tempParent.getParent();
		}
	}
	
	public void delete(int value) {
		if ((Node != null) && (Node.getNode(value) != null)) {
			decreaseKeyValue(value, getMinimum() - 1);
			extractMin();
		}
	}
	    
    public String print() {
        String result = "";
        System.out.println("Root: "+Node.getKey());
        
        Node x = Node;
        while (x != null) {
          result += x.print(0);
          x = x.getSibling();
        }
        
        return result;
      }
    
    public static BinomialHeap makeHeap(){
    	return new BinomialHeap();
    }
    
	public static void main(String[] Args) {
		BinomialHeap b = makeHeap();

		createTestHeap(b);
		b.print();
		System.out.println("Heap after insertion and union operations:");		
		System.out.println(b.print());
		
		System.out.println("Minimum: " + b.getMinimum());
		
		System.out.println("Extracted Minimum: "+b.extractMin());
		System.out.println("Heap after Extract-Min:");
		System.out.println(b.print());
		
		System.out.println("Extracted Minimum: "+b.extractMin());
		System.out.println("Heap after Extract-Min:");
		System.out.println(b.print());
		
		System.out.println("Minimum: " + b.getMinimum());
		b.decreaseKeyValue(42, 19);
		System.out.println("Heap after Decrease-Key:");
		System.out.println(b.print());
		
		b.delete(8);
		System.out.println("Heap after Delete:");
		System.out.println(b.print());
	}
	
	public static void createTestHeap(BinomialHeap b){
		
		b.heapInsert(37);
		//System.out.println(b.print());
		b.heapInsert(41);
		//System.out.println(b.print());
		b.heapInsert(56);
		//System.out.println(b.print());
		b.heapInsert(23);
		System.out.println(b.print());
		
		BinomialHeap temp = new BinomialHeap();
		temp.heapInsert(89);
		temp.heapInsert(47);
		temp.heapInsert(30);
		temp.heapInsert(13);
		temp.heapInsert(65);
		System.out.println(temp.print());
		
		b.union(temp.Node);
		System.out.println(b.print());
		
		b.decreaseKeyValue(41, 14);
		System.out.println("Heap after Decrease-Key:");
		System.out.println(b.print());
		
		b.delete(65);
		System.out.println("Heap after Delete:");
		System.out.println(b.print());
		
		System.out.println("Extracted Minimum: "+b.extractMin());
		System.out.println("Heap after Extract-Min:");
		System.out.println(b.print());
		
		/*BinomialHeap temp1 = new BinomialHeap();
		temp1.heapInsert(1);
		temp1.heapInsert(6);
		temp1.heapInsert(16);
		temp1.heapInsert(12);
		temp1.heapInsert(25);
		temp1.heapInsert(8);
		temp1.heapInsert(14);
		temp1.heapInsert(29);
		temp1.heapInsert(26);
		temp1.heapInsert(23);
		temp1.heapInsert(18);
		temp1.heapInsert(11);
		temp1.heapInsert(17);
		temp1.heapInsert(38);
		temp1.heapInsert(42);
		temp1.heapInsert(27);
		
		b.union(temp1.Node);*/
	}
	
	

}
