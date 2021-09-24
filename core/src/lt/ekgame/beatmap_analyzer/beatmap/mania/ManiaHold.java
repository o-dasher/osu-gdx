package lt.ekgame.beatmap_analyzer.beatmap.mania;

import com.badlogic.gdx.math.Vector2;


public class ManiaHold extends ManiaObject {

	public ManiaHold(Vector2 position, int startTime, int endTime, int hitSound) {
		super(position, startTime, endTime, hitSound);
	}

	@Override
	public ManiaObject clone() {
		return new ManiaHold(new Vector2(position), startTime, endTime, hitSound);
	}

}
