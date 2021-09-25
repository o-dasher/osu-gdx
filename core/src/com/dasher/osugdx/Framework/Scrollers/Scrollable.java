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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dasher.osugdx.Framework.Actors.ActorHelper;
import com.dasher.osugdx.Framework.Helpers.CenteringHelper;
import com.dasher.osugdx.Framework.Tasks.ClockTask;

import org.jetbrains.annotations.NotNull;

public class Scrollable<T extends Actor> extends Stage implements GestureDetector.GestureListener {
    private final Array<T> items = new Array<>();
    private float scrollMultiplier = 25;
    private float yMultiplier = 1;
    private float xMultiplier = 1;
    private float hoverXMultiplier = 0;
    private float hoverYMultiplier = 0;
    private boolean isXScrollable = true;
    private boolean isYScrollable = true;
    private boolean isLayouting = true;
    private boolean isStairCased = false;
    private boolean isFirstStairCaseX = true;
    private boolean isFirstStairCaseY = true;
    private boolean isHoverAbleItems = false;
    private int alignX = Align.center;
    private final IdentityMap<Actor, ScrollItemData> baseData = new IdentityMap<>();
    private float stairCaseMultiplier = 25f;
    private float stairCaseAdjustTime = 1;
    private float layoutTime = 0;
    private float minStairCaseAdjustTime = 0;

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
                    onScroll();
                }
                return false;
            }
        });
        minStairCaseAdjustTime = stairCaseAdjustTime / 10;
    }

    public void setYMultiplier(float yMultiplier) {
        this.yMultiplier = yMultiplier;
    }

    public boolean isNotLayouting() {
        return !isLayouting;
    }

    public void setAlignX(int alignX) {
        this.alignX = alignX;
    }

    public void layout() {
        getRoot().getActions().clear();
        baseData.clear();
        isFirstStairCaseX = true;
        isFirstStairCaseY = true;
        for (int i = 0; i < items.size; i++) {
            T currentActor = items.get(i);
            currentActor.clearActions();
            float height = currentActor.getHeight() * yMultiplier;
            float x;
            float y = CenteringHelper.getCenterY(currentActor.getHeight()) - height * i / 2;
            switch (alignX) {
                case Align.right:
                    x = getViewport().getWorldWidth() - currentActor.getWidth() * xMultiplier;
                    break;
                case Align.left:
                    x = -currentActor.getWidth() + currentActor.getWidth() * xMultiplier;
                    break;
                case Align.center:
                default:
                    x = CenteringHelper.getCenterX(currentActor.getWidth());
                    break;
            }
            ScrollItemData scrollItemData = new ScrollItemData();
            scrollItemData.baseVec.x = x;
            scrollItemData.baseVec.y = y;
            currentActor.setPosition(x, y);
            baseData.put(currentActor, scrollItemData);
            float hBy = height * (items.size - i);
            if (layoutTime <= 0) {
                isLayouting = true;
                currentActor.setPosition(currentActor.getX(), currentActor.getY() + hBy);
                isLayouting = false;
            } else {
                currentActor.addAction(
                        Actions.sequence(
                                Actions.run(() -> isLayouting = true),
                                Actions.moveBy(
                                        0,
                                        hBy
                                ),
                                Actions.run(() -> {
                                    isLayouting = false;
                                    scrollItemData.baseVec.x = currentActor.getX();
                                    scrollItemData.baseVec.y = currentActor.getY();
                                })
                        )
                );
            }
        }
    }

    public void addItem(T item) {
        items.add(item);
        if (isHoverAbleItems) {
            item.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (baseData.containsKey(item)) {
                        ScrollItemData data = baseData.get(item);
                        data.extraVec.x = -(item.getWidth() * hoverXMultiplier);
                        data.extraVec.y = -(item.getHeight() * hoverYMultiplier);
                        data.isHovered = true;
                    }
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    if (baseData.containsKey(item)) {
                        ScrollItemData data = baseData.get(item);
                        data.extraVec.set(0, 0);
                        data.isHovered = false;
                    }
                }
            });
        }
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
    public Array<T> visibleActors = new Array<>();

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isNotLayouting()) {
            Group root = getRoot();
            visibleActors.clear();

            float upperBound = 0;
            float lowerBound = 0;

            for (int i = 0; i < items.size; i++) {
                T item = items.get(i);
                upperBound = Math.max(upperBound, item.getY());
                lowerBound = Math.min(lowerBound, item.getY());
                item.setDebug(false);
                if (item.getStage() != null && ActorHelper.actorIsVisible(item) && isStairCased) {
                    visibleActors.add(item);
                }
            }

            if (isStairCased) {
                final int midIndex = (visibleActors.size - 1) / 2;
                if (!(midIndex >= visibleActors.size) && midIndex >= 0) {
                    Actor centerActor = visibleActors.get(midIndex);
                    Array<T> upActors = new Array<>();
                    Array<T> downActors = new Array<>();
                    for (T item: items) {
                        if (item.getY() > centerActor.getY()) {
                            upActors.add(item);
                        } else {
                            downActors.add(item);
                        }
                    }
                    if (setNotLayoutingStaircaseTask != null) {
                        setNotLayoutingStaircaseTask.update(delta);
                    }
                    upActors.sort((a, b) -> (int) (a.getY() - b.getY()));
                    downActors.sort((a, b) -> (int) (b.getY() - a.getY()));
                    float realAdjustTime = stairCaseAdjustTime;
                    if (isFirstStairCaseX || isFirstStairCaseY) {
                        realAdjustTime = minStairCaseAdjustTime;
                        if (isFirstStairCaseX) {
                            isFirstStairCaseX = false;
                        }
                        if (isFirstStairCaseY) {
                            isFirstStairCaseY = false;
                        }
                    }
                    stairCaseEffect(upActors, realAdjustTime);
                    stairCaseEffect(downActors, realAdjustTime);
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

    private final Vector2 nextStairCaseCoordinate = new Vector2();
    private ClockTask setNotLayoutingStaircaseTask;

    private void stairCaseEffect(@NotNull Array<T> itemsPart, float realAdjustTime) {
        for (int i = 0; i < itemsPart.size; i++) {
            Actor actor = itemsPart.get(i);
            ScrollItemData itemData = baseData.get(actor);
            if (itemData.baseVec == null) {
                return;
            }

            nextStairCaseCoordinate.set(
                    Math.min(
                            getViewport().getWorldWidth() - actor.getWidth() * 0.05f,
                            itemData.baseVec.x + (i + 1) * stairCaseMultiplier + itemData.extraVec.x
                    ), actor.getY() + itemData.extraVec.y
            );

            actor.addAction(Actions.moveTo(nextStairCaseCoordinate.x, nextStairCaseCoordinate.y, realAdjustTime));
        }

        /*
        if (isStairCased) {
            if (!isLayouting) {
                if (setNotLayoutingStaircaseTask == null) {
                    setNotLayoutingStaircaseTask = new ClockTask(realAdjustTime * 2) {
                        @Override
                        public void run() {
                            System.out.print("AA");
                            isLayouting = false;
                        }
                    };
                }
            }
        }

         */
    }

    public Array<T> getItems() {
        return items;
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
        if (!isLayouting) {
            float panScrollMultiplier = scrollMultiplier / 12.5f;
            float byX = isXScrollable? -(deltaX * panScrollMultiplier) : 0;
            float byY = isYScrollable? -(deltaY * panScrollMultiplier) : 0;
            addAction(Actions.moveBy(byX, byY));
            onScroll();
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

    public void setXMultiplier(float xMultiplier) {
        this.xMultiplier = xMultiplier;
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

    @Override
    public void dispose() {
        super.dispose();
        for (T item: items) {
            if (item instanceof Disposable) {
                ((Disposable) item).dispose();
            }
        }
    }

    public void onScroll() {

    }

    public void setHoverAbleItems(boolean hoverAbleItems) {
        this.isHoverAbleItems = hoverAbleItems;
    }

    public void setHoverXMultiplier(float hoverXMultiplier) {
        this.hoverXMultiplier = hoverXMultiplier;
    }

    public void setHoverYMultiplier(float hoverYMultiplier) {
        this.hoverYMultiplier = hoverYMultiplier;
    }

    public void setMinStairCaseAdjustTime(float minStairCaseAdjustTime) {
        this.minStairCaseAdjustTime = minStairCaseAdjustTime;
    }
}
