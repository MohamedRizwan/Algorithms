package Assignment;

public class RelabelToFrontTest {

	public static void main(String[] args){
		
		int[][] capacities = {{0,16,0,0,0,12},
				{0,0,10,12,0,0},
				{0,4,0,14,0,0},
				{0,0,0,0,0,20},
				{0,0,0,0,7,4},
				{0,0,0,0,0,0}
		};
		FlowNetwork network = new FlowNetwork(6, 0, 5, capacities);
		RelabelToFront relabelToFront = new RelabelToFront(network);
		System.out.println("Maximum Flow: "+relabelToFront.getMaxFlow());
		
	}
}
