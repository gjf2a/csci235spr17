package edu.hendrix.csci235.proj8.example;

import java.io.FileNotFoundException;

import edu.hendrix.modeselection.cluster.train.RealTimeTrainer;

public class RetrainingDemo {
	public static final String FILENAME = "training1.txt";
	
	public static void main(String[] args) throws FileNotFoundException {
		RealTimeTrainer<Move> trainer = new RealTimeTrainer<>(Move.class, FILENAME);
		trainer.run();
	}
}
