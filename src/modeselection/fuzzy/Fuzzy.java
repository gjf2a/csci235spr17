package modeselection.fuzzy;

public class Fuzzy {
	public static double encodeRising(double value, double start, double end) {
		return value > end ? 1.0 : value < start ? 0.0 : (value - start) / (end - start);
	}
	
	public static double encodeFalling(double value, double start, double end) {
		return value > end ? 0.0 : value < start ? 1.0 : (end - value) / (end - start);
	}
	
	public static double encodeTriangle(double value, double start, double peak, double end) {
		return value > end ? 0.0 : value < start ? 0.0 : value > peak ? (end - value) / (end - peak) : (value - start) / (peak - start);
	}
	
	public static double encodeTrapezoid(double value, double start, double peakStart, double peakEnd, double end) {
		return value > end ? 0.0 : value < start ? 0.0 : value > peakStart && value < peakEnd ? 1.0 : value >= peakEnd ? (end - value) / (end - peakEnd) : (value - start) / (peakStart - start);
	}
}
