package lt.ekgame.beatmap_analyzer.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.stream.Collectors;

import lt.ekgame.beatmap_analyzer.GameMode;

public class Mods {
	
	public static final Mods NOMOD = new Mods();
	
	private final int speedChangingFlags = Mod.DOUBLE_TIME.getBit() | Mod.NIGHTCORE.getBit() | Mod.HALF_TIME.getBit();
	private final int mapChangingFlags = speedChangingFlags | Mod.EASY.getBit() | Mod.HARDROCK.getBit();
	
	private final Array<Mod> mods = new Array<>();
	private int modFlags;
	
	public Mods(Array<Mod> mods) {
		for (Mod mod : mods)
			if (!this.mods.contains(mod, true))
				this.mods.add(mod);
		calculateFlags();
	}
	
	public Mods(Mod... mods) {
		this(new Array<>(mods));
	}
	
	public static Mods parse(int flags) {
		Array<Mod> result = new Array<>();
		for (Mod mod : Mod.values())
			if ((flags & mod.getBit()) > 0)
				result.add(mod);
		return new Mods(result);
	}
	
	public static Mods parse(String mods) {
		Array<Mod> result = new Array<>();
        int length = mods.length();
        for (int i = 0; i < length; i += 2) {
        	String modString = mods.substring(i, Math.min(length, i + 2));
        	if (modString.length() != 2)
        		continue;
        	
        	Mod mod = Mod.parse(modString);
        	if (mod != null)
        		result.add(mod);
        }
        return new Mods(result);
	}
	
	public boolean isRanked() {
		return Arrays.stream(mods.items).allMatch(Mod::isRanked);
	}
	
	public Mods withoutUnranked() {
		return new Mods(new Array<>(Arrays.stream(mods.items).filter(Mod::isRanked).toArray(Mod[]::new)));
	}
	
	public Array<Mod> getMods() {
		return mods;
	}
	
	public String toString() {
		return Arrays.stream(mods.items).map(Mod::getShortName).collect(Collectors.joining()).toUpperCase();
	}
	
	public boolean isNoMod() {
		return mods.isEmpty();
	}
	
	private void calculateFlags() {
		for (Mod mod : mods)
			modFlags |= mod.getBit();
	}
	
	public int getFlags() {
		return modFlags;
	}
	
	public boolean has(Mod mod) {
		return (mod.getBit() & modFlags) > 0;
	}
	
	public boolean isMapChanging() {
		return (mapChangingFlags & modFlags) > 0;
	}
	
	public boolean isSpeedChanging() {
		return (speedChangingFlags & modFlags) > 0;
	}
	
	public double getSpeedMultiplier() {
		if (has(Mod.DOUBLE_TIME) || has(Mod.NIGHTCORE))
			return 1.5;
		else if (has(Mod.HALF_TIME))
			return 0.75;
		return 1;
	}
	
	public double getScoreMultiplier(GameMode mode) {
		double multiplier = 1;

		if (has(Mod.NO_FAIL)) multiplier *= 0.5;
		if (has(Mod.HALF_TIME)) multiplier *= mode == GameMode.MANIA ? 0.5 : 0.3;
		if (has(Mod.HIDDEN)) multiplier *= mode == GameMode.MANIA ? 1 : 1.06;
		if (has(Mod.FLASHLIGHT)) multiplier *= mode == GameMode.MANIA ? 1 : 1.12;
		
		if (has(Mod.HARDROCK))
			multiplier *= mode == GameMode.CATCH ? 1.12 : mode == GameMode.MANIA ? 1 : 1.06;
		
		if (has(Mod.DOUBLE_TIME) || has(Mod.NIGHTCORE)) 
			multiplier *= mode == GameMode.CATCH ? 1.06 : mode == GameMode.MANIA ? 1 : 1.12;
		
		// TODO: if we get around implementing map converting to mania, there are xK mods.
		
		return multiplier;
	}
}
