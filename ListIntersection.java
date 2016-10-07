package Assignment;

public class ListIntersection {
	static class Node {
		int data;
		Node next;

		Node(int d) {
			data = d;
			next = null;
		}
	}

	static Node listA, listB;

	public static void main(String[] args) {
		ListIntersection list = new ListIntersection();
		list.listA = new Node(10);
		list.listA.next = new Node(20);
		list.listA.next.next = new Node(30);
		list.listA.next.next.next = new Node(40);
		list.listB = new Node(30);
		list.listB.next = new Node(40);
		System.out.println("The intersecting node is " + list.getIntersection());
	}

	int getIntersection() {
		int c1 = getLength(listA);
		int c2 = getLength(listB);
		int d;
		if (c1 > c2) {
			d = c1 - c2;
			return getIntersectionNode(d, listA, listB);
		} else {
			d = c2 - c1;
			return getIntersectionNode(d, listB, listA);
		}
	}

	int getIntersectionNode(int d, Node listA, Node listB) {
		int i;
		Node current1 = listA;
		Node current2 = listB;
		for (i = 0; i < d; i++) {
			if (current1 == null) {
				return -1;
			}
			current1 = current1.next;
		}
		while (current1 != null && current2 != null) {
			if (current1.data == current2.data) {
				return current1.data;
			}
			current1 = current1.next;
			current2 = current2.next;
		}
		return -1;
	}

	int getLength(Node node) {
		Node current = node;
		int count = 0;
		while (current != null) {
			count++;
			current = current.next;
		}
		return count;
	}
}