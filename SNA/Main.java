public class Main {
	
	 public static void main(String [] args) {
		
		 Graph graph = new Graph();
		 
		 // Read graph from file. Initalise all nodes and their adjacency lists
		 graph.initialise();
		 
		 // Print details and state of every node
		 //graph.display();
		 
		 // Run single stage model and multi-stage models with high influencers. R1 = 0.3, R2 = 0.45, beta = 0.5
		 graph.runHighInfluenceSimulation(0.5, 0.3, 0.45);
		 
		 // Run single stage model and multi-stage model with low influencers. R1 = 0.24, R2 = 0.3, beta = 0.5
		 graph.runLowInfluenceSimulation(0.5, 0.24, 0.3);	
		 
	 }
	 
}
