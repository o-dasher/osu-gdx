package lt.ekgame.beatmap_analyzer.beatmap.ctb;

import com.badlogic.gdx.math.Vector2;

import lt.ekgame.beatmap_analyzer.beatmap.HitObject;

public abstract class CatchObject extends HitObject {
	
	protected boolean isNewCombo;

	public CatchObject(Vector2 position, int startTime, int endTime, int hitSound, boolean isNewCombo) {
		super(position, startTime, endTime, hitSound);
		this.isNewCombo = isNewCombo;
	}
	
	public boolean isNewCombo() {
		return isNewCombo;
	}

}
