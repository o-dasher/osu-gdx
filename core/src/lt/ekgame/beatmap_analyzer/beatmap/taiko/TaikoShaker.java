package lt.ekgame.beatmap_analyzer.beatmap.taiko;

import com.badlogic.gdx.math.Vector2;

import lt.ekgame.beatmap_analyzer.beatmap.HitObject;

public class TaikoShaker extends TaikoObject {

	public TaikoShaker(Vector2 position, int startTime, int endTime, int hitSound) {
		super(position, startTime, endTime, hitSound);
	}

	@Override
	public HitObject clone() {
		return new TaikoShaker(new Vector2(position), startTime, endTime, hitSound);
	}

	@Override
	public int getCombo() {
		return 0;
	}
}
