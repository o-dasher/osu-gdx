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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;

public class Scrollable<T extends Actor> extends Stage implements GestureDetector.GestureListener {
    private final Array<T> items = new Array<>();
    private final Table table = new Table();
    private float scrollMultiplier = 25;
    private float heightMultiplier = 1;
    private boolean isXScrollable = true;
    private boolean isYScrollable = true;
    private int align = Align.center;

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
        table.setFillParent(true);
        addListener(new ClickListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                if (isNotLayouting()) {
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

    public boolean isNotLayouting() {
        boolean isLayouting = false;
        for (Actor actor: getActors()) {
            for (Action action: actor.getActions()) {
                if (action instanceof MoveByAction) {
                    isLayouting = true;
                    break;
                }
            }
        }
        return !isLayouting;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void layout() {
        for (int i = 0; i < items.size; i++) {
            T currentActor = items.get(i);
            for (Action action: currentActor.getActions()) {
                if (action instanceof MoveByAction) {
                    currentActor.removeAction(action);
                }
            }
            float height = currentActor.getHeight() * heightMultiplier;
            currentActor.setPosition(
                    CenteringHelper.getCenterX(currentActor.getWidth()),
                    CenteringHelper.getCenterY(currentActor.getHeight()) - ((height * i) / 2)
            );
            currentActor.addAction(
                    Actions.moveBy(
                            0,
                            height * (items.size - i),
                            1
                    )
            );
        }
    }

    public void addItem(T item) {
        items.add(item);
        table.add(item).right();
        super.addActor(item);
    }

    @Override
    public void addActor(Actor actor) {
        System.out.println("For scrollable use addItem() instead!...");
        System.exit(-1);
    }

    public void setScrollMultiplier(float scrollMultiplier) {
        this.scrollMultiplier = scrollMultiplier;
    }

    private MoveToAction currentCorrectionAction = null;

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isNotLayouting()) {
            Group root = getRoot();

            float upperBound = 0;
            float lowerBound = 0;
            for (T actor: items) {
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
        if (isNotLayouting()) {
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
