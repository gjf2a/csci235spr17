package proj8.example;

import modeselection.cluster.train.BSOCTrainer;

public class BSOCTrainingDemo {
	public static final String FILENAME = "demo1.txt";
	
	public static void main(String[] args) {
		BSOCTrainer<Move> trainer = new BSOCTrainer<>(Move.class, 32, 8, Move.FORWARD, FILENAME);
		trainer.run();
	}
}
