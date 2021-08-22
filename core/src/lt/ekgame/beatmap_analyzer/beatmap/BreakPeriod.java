package lt.ekgame.beatmap_analyzer.beatmap;

public class BreakPeriod {
	
	private int startTime, endTime;
	private boolean isBackground;
	private String backgroundFileName;

	private BreakPeriod() {}
	
	public BreakPeriod(int startTime, int endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public BreakPeriod(String backgroundFileName) {
		this.isBackground = true;
		this.backgroundFileName = backgroundFileName;
	}
	
	public BreakPeriod clone() {
		return new BreakPeriod(startTime, endTime);
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public boolean isBackground() {
		return isBackground;
	}

	public String getBackgroundFileName() {
		return backgroundFileName;
	}
}
