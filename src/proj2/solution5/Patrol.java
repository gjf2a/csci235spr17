package proj2.solution5;

import java.io.IOException;

import lejos.hardware.motor.Motor;
import modeselection.ModeSelector;
import modeselection.MotorFlagger;
import modeselection.Transitions;

public class Patrol {
	public static void main(String[] args) throws IOException {
		MotorFlagger<Condition> patrolFlag = new MotorFlagger<>(Motor.D);
		patrolFlag.add2(Condition.PATROL_IN_PROGRESS, Condition.PATROL_AT_END, i -> i < 4200);
		patrolFlag.add2(Condition.TURN_IN_PROGRESS, Condition.TURN_AT_END, i -> i < 450);
		
		Transitions<Condition,Mode> patrol = new Transitions<>();
		patrol.add(Condition.PATROL_IN_PROGRESS, Mode.PATROLLING)
			  .add(Condition.PATROL_AT_END, Mode.TURNING);

		Transitions<Condition,Mode> turn = new Transitions<>();
		turn.add(Condition.TURN_IN_PROGRESS, Mode.TURNING)
			.add(Condition.TURN_AT_END, Mode.PATROLLING);

		ModeSelector<Condition,Mode> controller = new ModeSelector<>(Condition.class, Mode.class, Mode.PATROLLING)
				.flagger(patrolFlag)
				.mode(Mode.PATROLLING, patrol, () -> {
					Motor.D.resetTachoCount();
					Motor.A.forward();
					Motor.D.forward();
				})
				.mode(Mode.TURNING, turn, () -> {
					Motor.D.resetTachoCount();
					Motor.A.backward();
					Motor.D.forward();
				});
		controller.control();
	}
}
