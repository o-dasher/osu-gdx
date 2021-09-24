package lt.ekgame.beatmap_analyzer.beatmap.taiko;

import com.badlogic.gdx.math.Vector2;


public class TaikoDrumroll extends TaikoObject {
	
	private double pixelLength;
	private boolean isBig;

	public TaikoDrumroll(Vector2 position, int startTime, int hitSound, double pixelLength, boolean isBig) {
		super(position, startTime, startTime, hitSound);
		this.pixelLength = pixelLength;
		this.isBig = isBig;
	}

	@Override
	public TaikoObject clone() {
		return new TaikoDrumroll(new Vector2(position), startTime, hitSound, pixelLength, isBig);
	}
	
	@Override
	public int getCombo() {
		return 0;
	}
}
