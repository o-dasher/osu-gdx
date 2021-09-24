package lt.ekgame.beatmap_analyzer.beatmap.taiko;


import com.badlogic.gdx.math.Vector2;

import lt.ekgame.beatmap_analyzer.beatmap.HitObject;


public abstract class TaikoObject extends HitObject {

	public TaikoObject(Vector2 position, int startTime, int endTime, int hitSound) {
		super(position, startTime, endTime, hitSound);
	}
}
