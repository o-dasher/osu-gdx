package lt.ekgame.beatmap_analyzer.beatmap.ctb;

import com.badlogic.gdx.math.Vector2;


public class CatchFruit extends CatchObject {

	public CatchFruit(Vector2 position, int startTime, int hitSound, boolean isNewCombo) {
		super(position, startTime, startTime, hitSound, isNewCombo);
	}

	@Override
	public CatchObject clone() {
		return new CatchFruit(new Vector2(position), startTime, hitSound, isNewCombo);
	}
}
