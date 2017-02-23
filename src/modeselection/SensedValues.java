package modeselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;

public class SensedValues<C extends Enum<C>> {
	private EnumSet<C> conditions;
	private EnumMap<C,Double> raw;
	
	public SensedValues(Class<C> flags) {
		conditions = EnumSet.noneOf(flags);
		raw = new EnumMap<>(flags);
	}
	
	public boolean contains(C flag) {
		return conditions.contains(flag);
	}
	
	public void add(C flag) {
		conditions.add(flag);
	}
	
	public void rawValue(C sensor, double value) {
		raw.put(sensor, value);
	}
	
	public boolean hasValueFor(C sensor) {
		return raw.containsKey(sensor);
	}
	
	public double getValueFor(C sensor) {
		return raw.get(sensor);
	}
	
	public Set<C> flagsOnly() {
		return Collections.unmodifiableSet(conditions);
	}
	
	public ArrayList<String> getStateReport() {
		ArrayList<String> report = new ArrayList<>();
		for (C cond: conditions) {
			report.add(cond.name());
		}
		for (Entry<C, Double> sensor: raw.entrySet()) {
			report.add(String.format("%s:%5.2f", sensor.getKey().name(), sensor.getValue()));
		}
		return report;
	}
}
