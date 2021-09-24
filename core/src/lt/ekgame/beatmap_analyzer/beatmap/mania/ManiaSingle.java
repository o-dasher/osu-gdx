package lt.ekgame.beatmap_analyzer.beatmap.mania;

import com.badlogic.gdx.math.Vector2;


public class ManiaSingle extends ManiaObject {

	public ManiaSingle(Vector2 position, int startTime, int hitSound) {
		super(position, startTime, startTime, hitSound);
	}

	@Override
	public ManiaObject clone() {
		return new ManiaSingle(new Vector2(position), startTime, hitSound);
	}

}
