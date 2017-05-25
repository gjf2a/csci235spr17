package edu.hendrix.modeselection.gui;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ArrayNavigator<T> extends Navigator<T> {
	private ArrayList<T> items = new ArrayList<>();
	
	public void add(T item) {
		items.add(item);
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public T getCurrent() {
		return items.get(getCurrentIndex());
	}
	
	public void doAll(Consumer<T> consumer) {
		for (T item: items) {
			consumer.accept(item);
		}
	}
}
