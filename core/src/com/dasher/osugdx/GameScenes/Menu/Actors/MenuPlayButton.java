package com.dasher.osugdx.GameScenes.Menu.Actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.dasher.osugdx.GameScenes.Menu.MenuStage;
import com.dasher.osugdx.OsuGame;
import com.dasher.osugdx.GameScenes.Gameplay.GamePlayScreen;


import com.dasher.osugdx.Images.GameImage;

public class MenuPlayButton extends GameImage {
    public MenuPlayButton(OsuGame game, Texture texture, boolean forceCenter, MenuStage menuStage, Sound menuButtonClick) {
        super(game, texture, forceCenter);
        setScale(1/5f);
        toButton(menuButtonClick, game.audioManager, () -> Gdx.app.postRunnable(() -> menuStage.switchScreen(new GamePlayScreen(game))));
    }
}
