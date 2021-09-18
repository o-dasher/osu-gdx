package com.dasher.osugdx.Framework.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ActorHelper {
    private ActorHelper() {}

    /**
     * Returns if the actor is visible or not. Useful to implement 2D culling.
     **/
    private static boolean actorIsVisible(@NotNull Actor actor, float screenWidth, float screenHeight) {
        Vector2 actorStagePos = actor.localToStageCoordinates(new Vector2(0,0));
        Vector2 actorStagePosTl = actor.localToStageCoordinates(new Vector2(actor.getWidth(), actor.getHeight()));

        Vector3 actorPixelPos = new Vector3(actorStagePos.x, actorStagePos.y, 0);
        Vector3 actorPixelPosTl = new Vector3(actorStagePosTl.x, actorStagePosTl.y, 0);

        actorPixelPos = actor.getStage().getCamera().project(actorPixelPos);
        actorPixelPosTl = actor.getStage().getCamera().project(actorPixelPosTl);

        return !(
                actorPixelPosTl.x < 0 || actorPixelPos.x > screenWidth ||
                        actorPixelPosTl.y < 0 || actorPixelPos.y > screenHeight
        );
    }

    public static boolean actorIsVisible(Actor actor, @NotNull Viewport viewport) {
        return actorIsVisible(actor, viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    public static boolean actorIsVisible(Actor actor) {
        return actorIsVisible(actor, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static @Nullable Actor FindClosestActor(@NotNull Array<? extends Actor> data, float target, Points searchType) {
        Actor closest = data.get(0);
        if (closest == null || data.size == 1) {
            return closest;
        }
        float compare = searchType == Points.x? closest.getX() : closest.getY();
        float leastDistance = Math.abs(compare - target);
        for (int i = 0; i < data.size; i++) {
            Actor current = data.get(i);
            compare = searchType == Points.x? current.getX() : current.getY();
            float currentDistance = Math.abs(compare - target);
            if (currentDistance < leastDistance) {
                closest = current;
                leastDistance = currentDistance;
            }
        }
        return closest;
    }
}
