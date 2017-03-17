package ideas.vision.landmarks;

import modeselection.cluster.ShrinkingImageBSOC;
import modeselection.util.Util;
import modeselection.vision.AdaptedYUYVImage;
import modeselection.vision.config.BasicVisionBot;

public class LandmarkViewer extends BasicVisionBot {

	private ShrinkingImageBSOC bsoc;
	
	public LandmarkViewer(String filename) {
		bsoc = Util.fileToObject(new File(filename), ShrinkingImageBSOC::)
	}
	
	@Override
	public void grabImage(AdaptedYUYVImage img) {
		// TODO Auto-generated method stub
		
	}
	
}
