import java.util.Vector;

// A single node in a graph
public class Node {
	int id;
	int state;

	// Adjacency lists for each layer
	Vector<Integer> follow;
	Vector<Integer> retweet;
	Vector<Integer> reply ;
	Vector<Integer> mention;

	public Node(){
		follow = new Vector<Integer>();
		mention = new Vector<Integer>();
		reply = new Vector<Integer>();
		retweet = new Vector<Integer>();
	}
}
