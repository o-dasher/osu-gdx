package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class ActorHelper {
    private ActorHelper() {}

    /**
     * Returns if the actor is visible or not. Useful to implement 2D culling.
     **/
    public static boolean actorIsVisible(Actor actor, Viewport viewport) {
        Vector2 actorStagePos = actor.localToStageCoordinates(new Vector2(0,0));
        Vector2 actorStagePosTl = actor.localToStageCoordinates(new Vector2(
                actor.getWidth(),
                actor.getHeight()));

        Vector3 actorPixelPos = new Vector3(actorStagePos.x, actorStagePos.y, 0);
        Vector3 actorPixelPosTl = new Vector3(actorStagePosTl.x, actorStagePosTl.y, 0);

        actorPixelPos = actor.getStage().getCamera().project(actorPixelPos);
        actorPixelPosTl = actor.getStage().getCamera().project(actorPixelPosTl);

        return !(actorPixelPosTl.x < 0 ||
                actorPixelPos.x > viewport.getWorldWidth() ||
                actorPixelPosTl.y < 0 ||
                actorPixelPos.y > viewport.getWorldHeight()
        );
    }
}
