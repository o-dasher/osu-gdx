package lt.ekgame.beatmap_analyzer.beatmap.osu;

import com.badlogic.gdx.math.Vector2;


public class OsuSpinner extends OsuObject {
	private OsuSpinner() {
		super(null, 0, 0, 0, false);
	}
	
	public OsuSpinner(Vector2 position, int startTime, int endTime, int hitSound, boolean isNewCombo) {
		super(position, startTime, endTime, hitSound, isNewCombo);
	}

	@Override
	public OsuObject clone() {
		return new OsuSpinner(new Vector2(position), startTime, endTime, hitSound, isNewCombo);
	}
}
