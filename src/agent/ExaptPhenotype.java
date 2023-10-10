package agent;

import java.util.ArrayList;
import java.util.List;

public class ExaptPhenotype extends Phenotype{
	private List<Phenotype> neighbors;
	private int nodeNumber;
	private double fitness;
	public ExaptPhenotype(int nodenumber1, double fitness1, List<Phenotype> neighbors1) {
		this.neighbors = neighbors1;
		this.nodeNumber = nodenumber1;
		this.fitness = fitness1;
	}
	
// The mainbranchNumber is the number of nodes that should be on the main branch. 
//The fitness multiplier is related to the fitness, which is calculated based on the multiplier, the node number, and the branch the node is in.
//The junction1 is the node number of the place where the downwards branch starts. It is important to note that node numbers start at 0.
//The downBranchNumber is the number of nodes on the downwards branch
//The upBranchNumber is the number of nodes on the branch starting at the end of the downwards branch.
//The nodeNumbers start at 0, juntion1 is the nodenumber of the first junction.
public static Phenotype getFirst(int mainbranchNumber, double fitnessMultiplier, int junction1, int downBranchNumber, int upBranchNumber) {
	List<Phenotype> mainBranch = new ArrayList<Phenotype>();
	List<ArrayList<Phenotype>> edges = new ArrayList<ArrayList<Phenotype>>();
	
	for(int k = 0; k < mainbranchNumber; k++) {
		List<Phenotype> neighbors = new ArrayList<Phenotype>();
		mainBranch.add(new ExaptPhenotype(k, fitnessMultiplier*k, neighbors));
	}
	//Finds the junction
	int junctionA = junction1;
//	if(mainbranchNumber%2 == 0) {
//		junctionA = mainbranchNumber/2;
//	}else {
//		junctionA = ((mainbranchNumber - 1)/2) + 1;
//	}
	//Makes the downwards branch
	
	for(int h = 0; h < downBranchNumber; h++) {
		List<Phenotype> neighbors2 = new ArrayList<Phenotype>();
		mainBranch.add(new ExaptPhenotype(mainbranchNumber + h, (junctionA - h)*fitnessMultiplier, neighbors2));
	}
	//Testing
	System.out.print("Start" + mainBranch.get(0).getNeighbors().size() + "END");
	if(mainBranch.get(0).getNeighbors().size() == 0) {
		System.out.print(")");
	}
	for(int z = 0; z < mainBranch.get(0).getNeighbors().size(); z++) {
		if(mainBranch.get(0).getNeighbors().get(z) == null) {
			System.out.print("NUll");
		}
		System.out.print("A");
		System.out.print(mainBranch.get(0).getNeighbors().get(z));
	}
	//Testing
	double firstFitness = (junctionA - (downBranchNumber - 1))*fitnessMultiplier;
	for(int m = 1; m < upBranchNumber; m++) {
		List<Phenotype> neighbors3 = new ArrayList<Phenotype>();
		if(fitnessMultiplier > 1) {
			mainBranch.add(new ExaptPhenotype((mainBranch.size() - 1) + m, firstFitness + (m)*fitnessMultiplier*fitnessMultiplier, neighbors3));
		}else {
			mainBranch.add(new ExaptPhenotype((mainBranch.size() - 1) + m, firstFitness + (m)*2*fitnessMultiplier, neighbors3));
		}
	}
	mainBranch.get(0).getNeighbors().add(mainBranch.get(1));
	//Makes the main branch of the graph (above); adds the neighbors (below) (If this doesn't work, possibly make the getNeighbors an arraylist and re-make ExaptPhenotype with the new neighbors?
	for(int j = 1; j < (mainbranchNumber - 1); j++) {
		mainBranch.get(j).getNeighbors().add(mainBranch.get(j - 1));
		mainBranch.get(j).getNeighbors().add(mainBranch.get(j + 1));
	}
	mainBranch.get(mainbranchNumber - 1).getNeighbors().add(mainBranch.get(mainbranchNumber - 2));
	//New branch 3 & 4 stuff
	mainBranch.get(junctionA).getNeighbors().add(mainBranch.get(mainbranchNumber));
	mainBranch.get(mainbranchNumber).getNeighbors().add(mainBranch.get(junctionA));
	mainBranch.get(mainbranchNumber).getNeighbors().add(mainBranch.get(mainbranchNumber + 1));
	for(int y = mainbranchNumber + 1; y < (mainBranch.size() - 1); y++) {
		mainBranch.get(y).getNeighbors().add(mainBranch.get(y - 1));
		mainBranch.get(y).getNeighbors().add(mainBranch.get(y + 1));
	}
	mainBranch.get(mainBranch.size() - 1).getNeighbors().add(mainBranch.get(mainBranch.size() - 2));
	
	for(int i = 0; i < mainbranchNumber; i++) {
		ArrayList<Phenotype> placeholder = new ArrayList<Phenotype>();
		if(i == 0) {
			placeholder.add(mainBranch.get(1));
		}else {
			//Checks if we have junctionA
			if(i == junctionA) {
				placeholder.add(mainBranch.get(i - 1));
				placeholder.add(mainBranch.get(i + 1));
				placeholder.add(mainBranch.get(mainbranchNumber));
			}else {
				if(i == mainbranchNumber - 1) {
					placeholder.add(mainBranch.get(mainbranchNumber - 2));
				}else {
					placeholder.add(mainBranch.get(i - 1));
					placeholder.add(mainBranch.get(i + 1));
				}
				}
			}
		edges.add(placeholder);
		
		///Add the rest to edges, the list of edges in the network. Then extend this to encompass the entire graph.
	}
	
	//Adds the branch3 edges
	for(int g = mainbranchNumber; g < mainBranch.size(); g++) {
		ArrayList<Phenotype> placeholder3 = new ArrayList<Phenotype>();
		if(g == mainbranchNumber) {
			placeholder3.add(mainBranch.get(junctionA));
			placeholder3.add(mainBranch.get(g + 1));
		}else {
			if(g == mainBranch.size() - 1) {
				placeholder3.add(mainBranch.get(g - 1));
			}else {
				placeholder3.add(mainBranch.get(g - 1));
				placeholder3.add(mainBranch.get(g + 1));
			}
		}
		edges.add(placeholder3);
	}
	//Testing
	System.out.print("Start" + mainBranch.get(0).getNeighbors().size() + "END");
	System.out.print("Start" + mainBranch.get(junctionA).getNeighbors().size() + "END");
	System.out.print("Edges" + edges.get(junctionA +1).size() +"END");
	//Testing
	return mainBranch.get(0);
	
}
	
	



//public static void buildGraph(int mainNodeNumber, int branch2NodeNumber, int branch3NodeNumber, int junctionNodeA, int junctionNodeB, int localOptima, int globalOptima) {
//	ArrayList<int[]>  edgesMain  = new ArrayList<int[]>();
//	ArrayList<int[]>  edgesBranch2  = new ArrayList<int[]>();
//	ArrayList<int[]>  edgesBranch3  = new ArrayList<int[]>();
//	for(int j = 0; j < localOptima; j++) {
//		int[] a = new int[2];
//		a[0] = j*(mainNodeNumber/localOptima);
//		a[1] = (j+1)*(mainNodeNumber/localOptima);
//		edgesMain.add(a);
//	}
//	for(int i = 0; i < (junctionNodeA - junctionNodeB); i++) {
//		int[] b = new int[2];
//		b[0] = junctionNodeA - ((junctionNodeA - junctionNodeB)/branch2NodeNumber)*i;
//		b[1] = junctionNodeA - ((junctionNodeA - junctionNodeB)/branch2NodeNumber)*(i + 1);
//		edgesBranch2.add(b);
//	}
//	for(int k = 0; k < (globalOptima - junctionNodeB); k++) {
//		int[] c = new int[2];
//		c[0] = junctionNodeB + (globalOptima - junctionNodeB)*k;
//		c[1] = junctionNodeB + (globalOptima - junctionNodeB)*(k + 1);
//		edgesBranch3.add(c);
//	}
//}


@Override
public List<Phenotype> getNeighbors() {
	return this.neighbors;
}


@Override
public Phenotype getIdenticalCopy() {
	// TODO Auto-generated method stub
	Phenotype copy =  new ExaptPhenotype(this.nodeNumber, this.fitness, this.neighbors);
	return copy;
}

public int getNumber() {
	return this.nodeNumber;
}

@Override
public void mutate() {
	// TODO Auto-generated method stub
	
}
}
