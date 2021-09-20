package com.dasher.osugdx.Framework.Scrollers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Actors.ActorHelper;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;

import org.jetbrains.annotations.NotNull;

public class Scrollable<T extends Actor> extends Stage implements GestureDetector.GestureListener {
    private final Array<T> items = new Array<>();
    private float scrollMultiplier = 25;
    private float heightMultiplier = 1;
    private float widthMultiplier = 1;
    private boolean isXScrollable = true;
    private boolean isYScrollable = true;
    private boolean isLayouting = true;
    private boolean isStairCased = false;
    private int alignX = Align.center;
    private final IdentityMap<Actor, Vector2> basePoints = new IdentityMap<>();
    private float stairCaseMultiplier = 25f;
    private float stairCaseAdjustTime = 1;

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
        return !isLayouting;
    }

    public void setAlignX(int alignX) {
        this.alignX = alignX;
    }

    public void layout() {
        basePoints.clear();
        for (int i = 0; i < items.size; i++) {
            T currentActor = items.get(i);
            currentActor.clearActions();
            float height = currentActor.getHeight() * heightMultiplier;
            float x;
            float y = CenteringHelper.getCenterY(currentActor.getHeight()) - height * i / 2;
            switch (alignX) {
                case Align.right:
                    x = getViewport().getWorldWidth() - currentActor.getWidth() * widthMultiplier;
                    break;
                case Align.left:
                    x = -currentActor.getWidth() + currentActor.getWidth() * widthMultiplier;
                    break;
                case Align.center:
                default:
                    x = CenteringHelper.getCenterX(currentActor.getWidth());
                    break;
            }
            Vector2 vec = new Vector2(x, y);
            currentActor.setPosition(vec.x, vec.y);
            basePoints.put(currentActor, vec);
            currentActor.addAction(
                    Actions.sequence(
                            Actions.run(() -> isLayouting = true),
                            Actions.moveBy(
                                    0,
                                    height * (items.size - i)
                            ),
                            Actions.run(() -> {
                                isLayouting = false;
                                vec.x = currentActor.getX();
                                vec.y = currentActor.getY();
                            })
                    )
            );
        }
    }

    public void addItem(T item) {
        items.add(item);
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
            Array<Actor> visibleActors = new Array<>();

            float upperBound = 0;
            float lowerBound = 0;

            for (int i = 0; i < items.size; i++) {
                Actor item = items.get(i);
                upperBound = Math.max(upperBound, item.getY());
                lowerBound = Math.min(lowerBound, item.getY());
                item.setDebug(false);
                if (ActorHelper.actorIsVisible(item) && isStairCased) {
                    visibleActors.add(item);
                }
            }

            if (isStairCased) {
                final int midIndex = (visibleActors.size - 1) / 2;
                if (!(midIndex >= visibleActors.size) && midIndex >= 0) {
                    Actor centerActor = visibleActors.get(midIndex);
                    centerActor.setDebug(true);
                    Array<T> upActors = new Array<>();
                    Array<T> downActors = new Array<>();
                    for (T item: items) {
                        if (item.getY() > centerActor.getY()) {
                            upActors.add(item);
                        } else {
                            downActors.add(item);
                        }
                    }
                    upActors.sort((a, b) -> (int) (a.getY() - b.getY()));
                    downActors.sort((a, b) -> (int) (b.getY() - a.getY()));
                    stairCaseEffect(upActors);
                    stairCaseEffect(downActors);
                }
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

    private void stairCaseEffect(@NotNull Array<T> itemsPart) {
        for (int i = 0; i < itemsPart.size; i++) {
            Actor actor = itemsPart.get(i);
            Vector2 point = basePoints.get(actor);
            actor.addAction(
                Actions.moveTo(
                        Math.min(
                                getViewport().getWorldWidth() - actor.getWidth() * 0.05f,
                                point.x + (i + 1) * stairCaseMultiplier
                        ),
                        actor.getY(),
                        stairCaseAdjustTime
                )
            );
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

    public void setWidthMultiplier(float widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    public void setStairCased(boolean stairCased) {
        isStairCased = stairCased;
    }

    private void ensureStairCased() {
        if (!isStairCased) {
            throw new IllegalStateException(
                    "You need to set StairCased to true before using this method"
            );
        }
    }

    public void setStairCaseMultiplier(float stairCaseMultiplier) {
        ensureStairCased();
        this.stairCaseMultiplier = stairCaseMultiplier;
    }

    public void setStairCaseAdjustTime(float stairCaseAdjustTime) {
        ensureStairCased();
        this.stairCaseAdjustTime = stairCaseAdjustTime;
    }
}
