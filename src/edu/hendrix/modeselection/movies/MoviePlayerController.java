package edu.hendrix.modeselection.movies;

import java.io.File;
import java.util.Optional;

import edu.hendrix.modeselection.gui.AdaptedYUYVRenderer;
import edu.hendrix.modeselection.gui.Alerter;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class MoviePlayerController {
	
	@FXML
	Canvas frame;
	@FXML
	Canvas node;
	
	@FXML
	TextField frameNum;
	@FXML
	TextField nodeNum;
	@FXML
	TextField numNodes;
	@FXML
	TextField shrink;
	
	Optional<NodeSelector> bsocNodes = Optional.empty();
	Optional<Movie> movie = Optional.empty();
	
	@FXML
	void open() {
		DirectoryChooser chooser = new DirectoryChooser();
		File chosen = chooser.showDialog(null);
		if (chosen != null) {
			try {
				Movie m = new Movie(chosen);
				movie = Optional.of(m);
				showFrame(m);
			} catch (Exception exc) {
				Alerter.errorBox("Can't find file " + chosen.getName());
			}
		}
	}
	
	@FXML
	void about() {
		Alerter.infoBox("MoviePlayer, by Gabriel Ferrer (ferrer@hendrix.edu)\nShows movies recorded by an EV3\nAllows building of a BSOC based on a movie.");
	}
	
	@FXML
	void createBSOC() {
		try {
			int nodes = Integer.parseInt(numNodes.getText());
			int shrinkNum = Integer.parseInt(shrink.getText());
			movie.ifPresent(m -> {
				bsocNodes = Optional.of(new NodeSelector(m.createFrom(nodes, shrinkNum)));
				showNode(bsocNodes.get());
			});
			
		} catch (Exception exc) {
			Alerter.errorBox(exc.getMessage());
		}
	}
	
	@FXML
	void frameLeft() {
		movie.ifPresent(m -> {
			m.prev();
			showFrame(m);
		});
	}
	
	@FXML
	void frameRight() {
		movie.ifPresent(m -> {
			m.next();
			showFrame(m);
		});
	}
	
	void showFrame(Movie m) {
		AdaptedYUYVRenderer.placeOnCanvas(m.getFrame(), frame);
		frameNum.setText(Integer.toString(m.getFrameIndex()));
		bsocNodes.ifPresent(nodes -> {
			nodes.jumpTo(m.getFrame());
			showNode(nodes);
		});
	}
	
	@FXML
	void nodeLeft() {
		bsocNodes.ifPresent(nodes -> {
			nodes.prev();
			showNode(nodes);
		});
	}
	
	@FXML
	void nodeRight() {
		bsocNodes.ifPresent(nodes -> {
			nodes.next();
			showNode(nodes);
		});
	}
	
	void showNode(NodeSelector nodes) {
		AdaptedYUYVRenderer.placeOnCanvas(nodes.referenceInput(), node);
		nodeNum.setText(Integer.toString(nodes.nodeNum()));
	}
	
	@FXML
	void initialize() {
		// Intentionally left blank
	}
}
