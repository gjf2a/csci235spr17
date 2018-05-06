package edu.hendrix.modeselection.gui.clusterpoints;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import javax.imageio.ImageIO;

import edu.hendrix.modeselection.cluster.BSOCSimpleEdge;
import edu.hendrix.modeselection.cluster.BoundedSelfOrgCluster;
import edu.hendrix.modeselection.cluster.Clusterer;
import edu.hendrix.modeselection.cluster.kmeans.KMeans;
import edu.hendrix.modeselection.cluster.kmeans.PlusPlusSeed;
import edu.hendrix.modeselection.gui.Alerter;
import edu.hendrix.modeselection.util.Util;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ClusterPointsController {
	@FXML
	Canvas canvas;
	@FXML
	TextField kValue;
	@FXML
	CheckBox scrambled;
	
	ArrayList<ClusterPoint> points = new ArrayList<>();
	ClusterStats stats = new ClusterStats();
	FileChooser chooser = new FileChooser();
	
	@FXML
	void initialize() {
		redrawSourcePoints();
		
		chooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*"));
		
		canvas.setOnMouseClicked(mouse -> {
			ClusterPoint cp = new ClusterPoint(mouse.getX(), mouse.getY());
			points.add(cp);
			redrawSourcePoints();
			stats = new ClusterStats(points);
		});
	}
	
	@FXML
	void openPoints() {
		Alerter.fileIO(chooser.showOpenDialog(null), f -> {
			stats = ClusterStats.fromFile(f);
			points = stats.getPoints();
			redrawSourcePoints();
		});
	}
	
	@FXML
	void savePoints() {
		Alerter.fileIO(chooser.showSaveDialog(null), f -> {
			stats.toFile(f);
		});
	}
	
	@FXML
	void openImage() {
		Alerter.fileIO(chooser.showOpenDialog(null), f -> {
			BufferedImage img = ImageIO.read(f);
			int threshold = findMeanGrayValue(img);
			setupPoints(threshold, img);
			redrawSourcePoints();
		});
	}
	
	int findMeanGrayValue(BufferedImage img) {
		int total = 0;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				total += Util.getGray(img.getRGB(x, y));
			}
		}
		return total / (img.getHeight() * img.getWidth());
	}
	
	void setupPoints(int threshold, BufferedImage img) {
		points.clear();
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				if (Util.getGray(img.getRGB(x, y)) <= threshold) {
					points.add(new ClusterPoint(x * canvas.getWidth() / img.getWidth(), y * canvas.getHeight() / img.getHeight()));
				}
			}
		}
		stats = new ClusterStats(points);
	}
	
	@FXML
	void summaryReport() {
		Alerter.fileIO(chooser.showSaveDialog(null), f -> {
			stats.createSummaryReport(f);
		});
	}
	
	void redrawSourcePoints() {
		canvas.getGraphicsContext2D().setFill(Color.WHITE);
		canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (ClusterPoint cp: points) {
			cp.plot(canvas, Color.BLACK, 4);
		}
	}
	
	@FXML
	void plusPlusSeed() {
		createClusters(k -> new PlusPlusSeed<>(k, ClusterPoint::distance, i -> i), Color.BLUE, "++seed");
	}
	
	@FXML
	void kMeans() {
		createClusters(k -> new KMeans<>(k, ClusterPoint::distance, i -> i), Color.RED, "kMeans++");
	}
	
	@FXML
	void bsoc() {
		createClusters(k -> new BoundedSelfOrgCluster<>(k, ClusterPoint::distance, i -> i), Color.GREEN, "BSOC");
	}
	
	@FXML
	void bsocSimpleEdge() {
		createClusters(k -> new BSOCSimpleEdge<>(k, ClusterPoint::distance, i -> i), Color.GREEN, "BSOCSimpleEdge");
	}
	
	void createClusters(Function<Integer,Clusterer<ClusterPoint,ClusterPoint>> maker, Color dotColor, String name) {
		numClusters().ifPresent(k -> {
			redrawSourcePoints();
			Clusterer<ClusterPoint,ClusterPoint> clusters = maker.apply(k);
			ArrayList<ClusterPoint> ordering = new ArrayList<>(points);
			if (scrambled.isSelected()) {
				Collections.shuffle(ordering);
			}
			for (ClusterPoint trainPoint: ordering) {clusters.train(trainPoint);}
			for (ClusterPoint centerPoint: clusters.getAllIdealInputs()) {
				centerPoint.plot(canvas, dotColor, 12);
			}
			stats.addClusterSolution(clusters, scrambled.isSelected(), name);
		});
	}
	
	Optional<Integer> numClusters() {
		try {
			int k = Integer.parseInt(kValue.getText());
			if (k >= 2) {
				return Optional.of(k);
			} else {
				Alerter.errorBox(k + " is less than 2");
				return Optional.empty();
			}
		} catch (NumberFormatException exc) {
			Alerter.errorBox(kValue.getText() + " is not an integer");
			return Optional.empty();
		}
	}
}
