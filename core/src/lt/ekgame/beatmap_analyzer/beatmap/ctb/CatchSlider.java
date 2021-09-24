package lt.ekgame.beatmap_analyzer.beatmap.ctb;

import com.badlogic.gdx.math.Vector2;


public class CatchSlider extends CatchObject {

	public CatchSlider(Vector2 position, int startTime, int endTime, int hitSound, boolean isNewCombo) {
		super(position, startTime, endTime, hitSound, isNewCombo);
	}

	@Override
	public CatchObject clone() {
		return new CatchSlider(new Vector2(position), startTime, endTime, hitSound, isNewCombo);
	}

}
