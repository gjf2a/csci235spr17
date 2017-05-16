package edu.hendrix.csci235.proj8.example;

import edu.hendrix.modeselection.cluster.train.RealTimeTrainer;

public class TrainingDemo {
	public static final String FILENAME = "training1.txt";
	
	public static void main(String[] args) {
		RealTimeTrainer<Move> trainer = new RealTimeTrainer<>(Move.class, 32, 8, Move.FORWARD, FILENAME);
		trainer.run();
	}
}
