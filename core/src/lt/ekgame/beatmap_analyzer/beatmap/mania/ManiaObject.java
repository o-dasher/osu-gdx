package lt.ekgame.beatmap_analyzer.beatmap.mania;

import com.badlogic.gdx.math.Vector2;

import lt.ekgame.beatmap_analyzer.beatmap.Beatmap;
import lt.ekgame.beatmap_analyzer.beatmap.HitObject;
import lt.ekgame.beatmap_analyzer.beatmap.TimingPoint;
import lt.ekgame.beatmap_analyzer.utils.MathUtils;

public abstract class ManiaObject extends HitObject {
	
	private int collumn;

	public ManiaObject(Vector2 position, int startTime, int endTime, int hitSound) {
		super(position, startTime, endTime, hitSound);
	}
	
	@Override
	public void finalize(TimingPoint current, TimingPoint parent, Beatmap beatmap) {
		int numCollumns = ((ManiaBeatmap)beatmap).getCollumns(); 
		collumn = MathUtils.calculateManiaCollumn(position.x, numCollumns);
	}
	
	public int getCollumn() {
		return collumn;
	}

}
