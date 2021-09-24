package lt.ekgame.beatmap_analyzer.beatmap.ctb;

import com.badlogic.gdx.math.Vector2;

public class CatchBananas extends CatchObject {

	public CatchBananas(Vector2 position, int startTime, int endTime, int hitSound) {
		super(position, startTime, endTime, hitSound, false);
	}

	@Override
	public CatchObject clone() {
		return new CatchBananas(new Vector2(position), startTime, endTime, hitSound);
	}

}
