package lt.ekgame.beatmap_analyzer.beatmap;

public class BreakPeriod {
	
	private int startTime, endTime;
	private boolean isBackground;
	private boolean isVideo;
	private String fileName;

	private BreakPeriod() {}
	
	public BreakPeriod(int startTime, int endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public BreakPeriod(String fileName, boolean isBackground) {
		if (isBackground) {
			this.isBackground = true;
		} else {
			this.isVideo = true;
		}
		this.fileName = fileName;
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

	public String getFileName() {
		return fileName;
	}
}
