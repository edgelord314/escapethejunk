package de.naclstudios.etj.gameObjects;

import de.edgelord.saltyengine.components.DrawHitboxComponent;
import de.edgelord.saltyengine.components.animation.AnimationRender;
import de.edgelord.saltyengine.core.ImageLoader;
import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.effect.Spritesheet;
import de.edgelord.saltyengine.effect.SpritesheetAnimation;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.transform.Coordinates2f;
import de.edgelord.saltyengine.utils.Directions;
import de.naclstudios.etj.main.EscapeTheJunk;

import java.util.Random;

public class Rat extends GameObject {

    private int health = 2;
    private int ticks = 0;
    private Random random = new Random();

    private Spritesheet spritesheet = new Spritesheet(ImageLoader.getOrLoadImage("rat", "pictures/rat.png"), 477, 235);

    private SpritesheetAnimation walkRight = new SpritesheetAnimation(this);
    private SpritesheetAnimation walkLeft = new SpritesheetAnimation(this);

    private AnimationRender render = new AnimationRender(this, "animaitonRender");

    public Rat(Coordinates2f position) {
        super(position, 56, 28, "de.naclstudios.etj.gameObjects.rat");

        removeComponent(DEFAULT_PHYSICS_NAME);

        walkRight.setFrames(spritesheet.getManualFrames(new Coordinates(1, 1), new Coordinates(1, 2), new Coordinates(1, 1), new Coordinates(1, 3)));
        walkLeft.setFrames(spritesheet.getManualFrames(new Coordinates(2, 1), new Coordinates(2, 2), new Coordinates(2, 1), new Coordinates(2, 3)));

        addComponent(render);

        setStationary(true);
    }

    public void initialize() {

    }

    public void onCollision(CollisionEvent e) {
    }

    public void onFixedTick() {

        if (health <= 0) {
            EscapeTheJunk.sounds.play("rat_dies");
            spawnKey();
            removeFromCurrentScene();
        }

        if (ticks == 50) {
            ticks = 0;
            runAway();
        } else {
            ticks++;
        }
    }

    public void onTick() {

    }

    public void draw(SaltyGraphics graphics) {

    }

    private void spawnKey() {
        SceneManager.getCurrentScene().addGameObject(new KeyFragment(getPosition()));
    }

    private void runAway() {

        int randomDir = random.nextInt(4);

        if (randomDir == 0) {
            if (getPosition().getY() >= 105) {
                move(15f, Directions.Direction.UP);
                return;
            }
        }

        if (randomDir == 1) {
            if (getPosition().getY() <= 840) {
                move(15f, Directions.Direction.DOWN);
                return;
            }
        }

        if (randomDir == 2) {
            if (getPosition().getX() <= 1600 - EscapeTheJunk.currentWallDelta) {
                render.setSpritesheetAnimation(walkRight);
                move(15f, Directions.Direction.RIGHT);
                return;
            }
        }

        if (randomDir == 3) {
            if (getPosition().getX() >= EscapeTheJunk.currentWallDelta) {
                render.setSpritesheetAnimation(walkLeft);
                move(15f, Directions.Direction.LEFT);
            }
        }
    }

    public void bulletHit() {
        health--;
    }
}
