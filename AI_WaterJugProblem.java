import java.util.*;

class Jug{
	private int quantity;
	private int capacity;
	
	Jug(int q, int c){
		this.capacity = c;
		this.quantity = q;
	}
	
	int value(){
		return quantity;
	}
	
	int getCapacity(){
		return this.capacity;
	}
	
	boolean canFill(){
		if(this.quantity < this.capacity) return true;
		else return false;
	}
	
	boolean canEmpty(){
		if(this.quantity>0) return true;
		else return false;
	}
	
	boolean canGive(Jug j){
		if(j.value() < j.getCapacity() && this.quantity > 0) return true;
		else return false;
	}
	
	public String toString(){
		return Integer.toString(this.quantity);
	}
}

class State{
	Jug j1;
	Jug j2;
	State(Jug j1, Jug j2){
		this.j1 = j1;
		this.j2 = j2;
	}
	
	public String toString(){
		return "("+this.j1+","+this.j2+")";
	}
	
	boolean isSameAs(State s){
		int jug1 = this.j1.value();
		int jug2 = this.j2.value();
		int goal1 = s.j1.value();
		int goal2 = s.j2.value();
		
		if(goal1 == -1){
			if(goal2==jug2) return true;
		}
		if(goal2 == -1){
			if(goal1==jug1) return true;
		}
		if(goal1==jug1 && goal2==jug2){
			return true;
		}
		
		else return false;
	}
}

class WaterJug{
	int g1, g2;
	int i1 = 0;
	int i2 = 0;
	State goalState;	
	State initialState;
	boolean[][] checkDFS;
	boolean[][] checkBFS;
	boolean goalDFS = false;
	boolean goalBFS = false;
	ArrayDeque<State> q = new ArrayDeque<State>();
	
	
	WaterJug(int j1, int j2, int g1, int g2, int i1, int i2){
		this.g1 = g1;
		this.g2 = g2;
		this.i1 = i1;
		this.i2 = i2;
		this.checkDFS = new boolean[j1+1][j2+1];
		this.checkBFS = new boolean[j1+1][j2+1];
		for(int i=0; i<j1;i++) for(int j=0; j<j2; j++){
			checkDFS[i][j] = false;
			checkBFS[i][j] = false;
		}
		this.initialState = new State(new Jug(i1,j1), new Jug(i2,j2));
		this.goalState = new State(new Jug(g1,j1), new Jug(g2,j2));
	}
	
	WaterJug(int j1, int j2, int g1, int g2){
		this.g1 = g1;
		this.g2 = g2;
		this.checkDFS = new boolean[j1+1][j2+1];
		this.checkBFS = new boolean[j1+1][j2+1];
		for(int i=0; i<=j1;i++) for(int j=0; j<=j2; j++) {
			checkDFS[i][j] = false;
			checkBFS[i][j] = false;
		}
		this.initialState = new State(new Jug(i1,j1), new Jug(i2,j2));
		this.goalState = new State(new Jug(g1,j1), new Jug(g2,j2));
	}
	
	
	//*************** BFS *******************
	
	
	void computeBFS(){
		System.out.println("\n********* BFS **********");
		nextStatesBFS(initialState);
		if(goalBFS == false) System.out.println("Goal State Not Possible");
	}
	
	public void nextStatesBFS(State currentState){
		Jug j1 = currentState.j1;
		Jug j2 = currentState.j2;
		
		if(checkBFS[j1.value()][j2.value()] == false){
			
			checkBFS[j1.value()][j2.value()] = true;
			
			System.out.println(currentState);
			
			if(currentState.isSameAs(goalState)){
				System.out.println("*****Goal State Reached*****");
				goalBFS = true;
			}
			
			if(goalBFS == true) return;
			
			if(j1.canFill()){
				State child = new State(
					new Jug(j1.getCapacity(), j1.getCapacity()),
					new Jug(j2.value(), j2.getCapacity())
				);
				q.add(child);
			}
			if(j2.canFill()){
				State child = new State(
					new Jug(j1.value(), j1.getCapacity()),
					new Jug(j2.getCapacity(), j2.getCapacity())
				);
				q.add(child);
			}
			if(j1.canEmpty()){
				State child = new State(
					new Jug(0, j1.getCapacity()),
					new Jug(j2.value(), j2.getCapacity())
				);
				q.add(child);
			}
			if(j2.canEmpty()){
				State child = new State(
					new Jug(j1.value(), j1.getCapacity()),
					new Jug(0, j2.getCapacity())
				);
				q.add(child);
			}
			if(j1.canGive(j2)){
				int available = j2.getCapacity()-j2.value();
				int offer = j1.value();
				int transfer = Math.min(offer, available);
				State child = new State(
					new Jug(j1.value()-transfer,j1.getCapacity()),
					new Jug(j2.value()+transfer,j2.getCapacity())
				);
				q.add(child);
			}
			if(j2.canGive(j1)){
				int available = j1.getCapacity()-j1.value();
				int offer = j2.value();
				int transfer = Math.min(offer, available);
				State child = new State(
					new Jug(j1.value()+transfer,j1.getCapacity()),
					new Jug(j2.value()-transfer,j2.getCapacity())
				);
				q.add(child);
			}
		}
		State nextState = q.pollFirst();
		if(nextState == null) return;
		nextStatesBFS(nextState);
	}
	
	// **************DFS***************
	
	void computeDFS(){
		System.out.println("\n********* DFS **********");
		nextStatesDFS(initialState);
		if(goalDFS == false) System.out.println("Goal State Not Possible");
	}
	
	public void nextStatesDFS(State currentState){
		Jug j1 = currentState.j1;
		Jug j2 = currentState.j2;
		
		if(checkDFS[j1.value()][j2.value()] == true) return;
		checkDFS[j1.value()][j2.value()] = true;
		
		System.out.println(currentState);
		
		if(currentState.isSameAs(goalState)){
			System.out.println("*****Goal State Reached*****");
			goalDFS = true;
		}
		
		if(goalDFS == true) return;
		
		if(j1.canFill()){
			State child = new State(
				new Jug(j1.getCapacity(), j1.getCapacity()),
				new Jug(j2.value(), j2.getCapacity())
			);
			nextStatesDFS(child);
			if(goalDFS == true) return;
		}
		if(j2.canFill()){
			State child = new State(
				new Jug(j1.value(), j1.getCapacity()),
				new Jug(j2.getCapacity(), j2.getCapacity())
			);
			nextStatesDFS(child);
			if(goalDFS == true) return;
		}
		if(j1.canEmpty()){
			State child = new State(
				new Jug(0, j1.getCapacity()),
				new Jug(j2.value(), j2.getCapacity())
			);
			nextStatesDFS(child);
			if(goalDFS == true) return;
		}
		if(j2.canEmpty()){
			State child = new State(
				new Jug(j1.value(), j1.getCapacity()),
				new Jug(0, j2.getCapacity())
			);
			nextStatesDFS(child);
			if(goalDFS == true) return;
		}
		if(j1.canGive(j2)){
			int available = j2.getCapacity()-j2.value();
			int offer = j1.value();
			int transfer = Math.min(offer, available);
			State child = new State(
				new Jug(j1.value()-transfer,j1.getCapacity()),
				new Jug(j2.value()+transfer,j2.getCapacity())
			);
			nextStatesDFS(child);
			if(goalDFS == true) return;
		}
		if(j2.canGive(j1)){
			int available = j1.getCapacity()-j1.value();
			int offer = j2.value();
			int transfer = Math.min(offer, available);
			State child = new State(
				new Jug(j1.value()+transfer,j1.getCapacity()),
				new Jug(j2.value()-transfer,j2.getCapacity())
			);
			nextStatesDFS(child);
			if(goalDFS == true) return;
		}
	}
	
}

public class AI_WaterJugProblem{

	public static void computeProblem(int j1, int j2, int g1, int g2){
		WaterJug w = new WaterJug(j1, j2, g1, g2);
		w.computeDFS();
		w.computeBFS();
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter maximum capacity of jug 1");
		int j1 = sc.nextInt();
		System.out.println("Enter maximum capacity of jug 2");
		int j2 = sc.nextInt();
		System.out.println("\nNOTE: If jug has no particular goal state, enter -1, do not leave input blank!!");
		System.out.println("Enter goal state capacity of jug 1");
		int g1 = sc.nextInt();
		System.out.println("Enter goal state capacity of jug 2");
		int g2 = sc.nextInt();
		
		computeProblem(j1, j2, g1, g2);
	}
}



// #################################### OUTPUT ####################################

// ############### TEST CASE 1: Goal State => (2,*) , same as mentioned in experiment aim ####################
// Enter maximum capacity of jug 1
// 4
// Enter maximum capacity of jug 2
// 3

// NOTE: If jug has no particular goal state, enter -1, do not leave input blank!!
// Enter goal state capacity of jug 1
// 2
// Enter goal state capacity of jug 2
// -1

// ********* DFS **********
// (0,0)
// (4,0)
// (4,3)
// (0,3)
// (3,0)
// (3,3)
// (4,2)
// (0,2)
// (2,0)
// *****Goal State Reached*****

// ********* BFS **********
// (0,0)
// (4,0)
// (0,3)
// (4,3)
// (1,3)
// (3,0)
// (1,0)
// (3,3)
// (0,1)
// (4,2)
// (4,1)
// (0,2)
// (2,3)
// *****Goal State Reached*****


// ####################### TEST CASE 2: Goal State => (2,1) ###############################
// Enter goal state capacity of jug 1
// 2
// Enter goal state capacity of jug 2
// 1

// ********* DFS **********
// (0,0)
// (4,0)
// (4,3)
// (0,3)
// (3,0)
// (3,3)
// (4,2)
// (0,2)
// (2,0)
// (2,3)
// (4,1)
// (0,1)
// (1,0)
// (1,3)
// Goal State Not Possible

// ********* BFS **********
// (0,0)
// (4,0)
// (0,3)
// (4,3)
// (1,3)
// (3,0)
// (1,0)
// (3,3)
// (0,1)
// (4,2)
// (4,1)
// (0,2)
// (2,3)
// (2,0)
// Goal State Not Possible