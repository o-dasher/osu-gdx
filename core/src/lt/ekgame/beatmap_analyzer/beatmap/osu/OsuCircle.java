package lt.ekgame.beatmap_analyzer.beatmap.osu;

import com.badlogic.gdx.math.Vector2;


public class OsuCircle extends OsuObject {
	private OsuCircle() {
		super(null, 0, 0, 0, false);
	}

	public OsuCircle(Vector2 position, int timestamp, int hitSound, boolean isNewCombo) {
		super(position, timestamp, timestamp, hitSound, isNewCombo);
	}

	@Override
	public OsuObject clone() {
		return new OsuCircle(new Vector2(position), startTime, hitSound, isNewCombo);
	}

}
