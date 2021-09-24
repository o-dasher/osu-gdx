package lt.ekgame.beatmap_analyzer.beatmap.taiko;


import com.badlogic.gdx.math.Vector2;

public class TaikoCircle extends TaikoObject {
	
	private TaikoColor color;
	private boolean isBig;

	public TaikoCircle(Vector2 position, int startTime, int hitSound, TaikoColor color, boolean isBig) {
		super(position, startTime, startTime, hitSound);
		this.color = color;
		this.isBig = isBig;
	}

	@Override
	public TaikoObject clone() {
		return new TaikoCircle(new Vector2(position), startTime, hitSound, color, isBig);
	}
	
	public TaikoColor getColor() {
		return color;
	}
	
	public boolean isBig() {
		return isBig;
	}

	public enum TaikoColor {
		RED, BLUE
	}
}
