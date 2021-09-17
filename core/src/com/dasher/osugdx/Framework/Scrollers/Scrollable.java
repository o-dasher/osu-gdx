package com.dasher.osugdx.Framework.Scrollers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Actors.CenteredImage;
import com.dasher.osugdx.GameScenes.SoundSelect.BeatmapSetSelector;

public class Scrollable extends Stage implements GestureDetector.GestureListener {
    private float scrollMultiplier = 25;
    private float heightMultiplier = 1;
    private boolean isXScrollable = true;
    private boolean isYScrollable = true;

    public Scrollable() {
        super();
        init();
    }

    public Scrollable(Viewport viewport) {
        super(viewport);
        init();
    }

    public Scrollable(Viewport viewport, Batch batch) {
        super(viewport, batch);
        init();
    }

    public void setScrollable(boolean isXScrollable, boolean isYScrollable) {
        this.isXScrollable = isXScrollable;
        this.isYScrollable = isYScrollable;
    }

    private void init() {
        addListener(new ClickListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                if (!isLayouting()) {
                    float byX = isXScrollable? amountX * scrollMultiplier : 0;
                    float byY = isYScrollable? amountY * scrollMultiplier : 0;
                    addAction(Actions.moveBy(byX, byY, 0.025f));
                }
                return false;
            }
        });
    }

    public void setHeightMultiplier(float heightMultiplier) {
        this.heightMultiplier = heightMultiplier;
    }

    public boolean isLayouting() {
        boolean isLayouting = false;
        for (Actor actor: getActors()) {
            for (Action action: actor.getActions()) {
                if (action instanceof MoveByAction) {
                    isLayouting = true;
                    break;
                }
            }
        }
        return isLayouting;
    }

    public void layout() {
        for (int i = 0; i < getActors().size; i++) {
            Actor currentActor = getActors().get(i);
            if (currentActor instanceof CenteredImage) {
                CenteredImage currentImage = (CenteredImage) currentActor;
                for (Action action: currentImage.getActions()) {
                    if (action instanceof MoveByAction) {
                        currentImage.removeAction(action);
                    }
                }
                float height = currentActor.getHeight() * heightMultiplier;
                currentActor.setPosition(
                        currentImage.getCenterX(getViewport()),
                        currentImage.getCenterY(getViewport()) - ((height * i) / 2)
                );
                currentImage.addAction(
                        Actions.moveBy(
                                0,
                                height * (getActors().size - i),
                                0.25f
                        )
                );
            } else {
                throw new IllegalStateException("Scrollable actors needs to be of class CenteredImage");
            }
        }
    }

    public void setScrollMultiplier(float scrollMultiplier) {
        this.scrollMultiplier = scrollMultiplier;
    }

    private MoveToAction currentCorrectionAction = null;

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!isLayouting()) {
            Group root = getRoot();

            float upperBound = 0;
            float lowerBound = 0;
            for (Actor actor: getActors()) {
                upperBound = Math.max(upperBound, actor.getY());
                lowerBound = Math.min(lowerBound, actor.getY());
            }
            upperBound = -upperBound;
            lowerBound = -lowerBound;
            if (currentCorrectionAction != null && !currentCorrectionAction.isComplete()) {
                root.removeAction(currentCorrectionAction);
            }
            // reverted cause of stage logic
            if (root.getY() < upperBound) {
                root.addAction(updateCorrectionAction(upperBound));
            } else if (root.getY() > lowerBound) {
                root.addAction(updateCorrectionAction(lowerBound));
            }
        }
    }

    private MoveToAction updateCorrectionAction(float bound) {
        float correctionTime = 0.25f;
        return this.currentCorrectionAction = Actions.moveTo(
                getRoot().getX(), bound, correctionTime
        );
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (!isLayouting()) {
            float panScrollMultiplier = scrollMultiplier / 12.5f;
            float byX = isXScrollable? -(deltaX * panScrollMultiplier) : 0;
            float byY = isYScrollable? -(deltaY * panScrollMultiplier) : 0;
            addAction(Actions.moveBy(byX, byY));
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
