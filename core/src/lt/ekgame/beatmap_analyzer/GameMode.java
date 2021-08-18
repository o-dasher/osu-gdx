package lt.ekgame.beatmap_analyzer;

public enum GameMode {
	
	UNKNOWN(-1), OSU(0), TAIKO(1), CATCH(2), MANIA(3);
	
	int numValue;
	
	GameMode(int numValue) {
		this.numValue = numValue;
	}
	
	public static GameMode fromInt(int value) {
		for (GameMode gamemode : GameMode.values())
			if (gamemode.numValue == value)
				return gamemode;
		return UNKNOWN;
	}
}
