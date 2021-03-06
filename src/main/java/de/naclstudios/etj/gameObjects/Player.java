package de.naclstudios.etj.gameObjects;

import de.edgelord.saltyengine.components.DrawHitboxComponent;
import de.edgelord.saltyengine.components.animation.AnimationRender;
import de.edgelord.saltyengine.components.gfx.LightComponent;
import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.effect.Spritesheet;
import de.edgelord.saltyengine.effect.SpritesheetAnimation;
import de.edgelord.saltyengine.factory.ImageFactory;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.resource.InnerResource;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.transform.TransformRelationMode;
import de.edgelord.saltyengine.transform.Coordinates2f;
import de.edgelord.saltyengine.utils.Directions;
import de.edgelord.saltyengine.utils.SaltySystem;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;


public class Player extends GameObject {

    private int collectedKeyFragments = 0;
    public static final int REQUIREDKEYFRAGMENTS = 13;

    private float movementSpeed = 3000;

    private ImageFactory imageFactory = new ImageFactory(new InnerResource());

    private AnimationRender animationRender = new AnimationRender(this, "de.naclstudios.etj.gameObjects.player.animationRender");

    private Spritesheet mainCharSpriteSheet = new Spritesheet(imageFactory.getOptimizedImageResource("pictures/mainchar.png"), 73, 94);

    private SpritesheetAnimation walkUp = new SpritesheetAnimation(this);
    private SpritesheetAnimation walkDown = new SpritesheetAnimation(this);
    private SpritesheetAnimation walkRight = new SpritesheetAnimation(this);
    private SpritesheetAnimation walkLeft = new SpritesheetAnimation(this);

    private BufferedImage up = mainCharSpriteSheet.getManualSprite(1, 1);
    private BufferedImage down = mainCharSpriteSheet.getManualSprite(2, 1);
    private BufferedImage right = mainCharSpriteSheet.getManualSprite(3, 1);
    private BufferedImage left = mainCharSpriteSheet.getManualSprite(5, 1);

    private BufferedImage currentFreezeImage;

    private Coordinates[] walkUpSprites = {new Coordinates(1, 1), new Coordinates(1, 2), new Coordinates(1, 1), new Coordinates(1, 3)};
    private Coordinates[] walkDownSprites = {new Coordinates(2, 1), new Coordinates(2, 2), new Coordinates(2, 1), new Coordinates(2, 3)};
    private Coordinates[] walkRightSprites = {new Coordinates(3, 1), new Coordinates(3, 2), new Coordinates(3, 1), new Coordinates(3, 3)};
    private Coordinates[] walkLeftSprites = {new Coordinates(6, 1), new Coordinates(6, 2), new Coordinates(6, 1), new Coordinates(6, 3)};

    private Coordinates[] walkUpSpritesWeapon = {new Coordinates(1, 4), new Coordinates(1, 5), new Coordinates(1, 4), new Coordinates(1, 6)};
    private Coordinates[] walkDownSpritesWeapon = {new Coordinates(2, 4), new Coordinates(2, 5), new Coordinates(2, 4), new Coordinates(2, 6)};
    private Coordinates[] walkRightSpritesWeapon = {new Coordinates(4, 1), new Coordinates(4, 2), new Coordinates(4, 1), new Coordinates(4, 3)};
    private Coordinates[] walkLeftSpritesWeapon = {new Coordinates(5, 4), new Coordinates(5, 5), new Coordinates(5, 4), new Coordinates(5, 6)};

    private Directions.Direction currentDirection;
    private boolean freeze = true;

    private List<GameObject> touchingGameObjects = new LinkedList<>();

    private boolean hasWeapon = false;
    private int weaponCooldown = (int) (250 / SaltySystem.fixedTickMillis);
    private boolean cooldown = false;
    private int ticks = 0;

    private int secureTicks = 0;

    public Player() {
        super(Game.getHost().getCentrePosition(71, 91), 71, 91, "de.naclstudios.etj.gameObject.player");

        animationRender.setTicksPerFrame((int) (175 / SaltySystem.fixedTickMillis));

        addComponent(animationRender);
        currentDirection = Directions.Direction.DOWN;
        readAnimation();
        getPhysics().addTagToIgnore("de.naclstudios.etj.gameObjects.rat");
        getPhysics().addTagToIgnore("de.naclstudios.etj.gameObjects.keyFragment");
        getPhysics().addTagToIgnore("de.naclstudios.etj.gameObjects.bullet");
    }

    public void initialize() {
    }

    @Override
    public void onCollisionDetectionFinish(List<CollisionEvent> collisions) {

        int wallCount = 0;
        touchingGameObjects.clear();

        for (CollisionEvent collisionEvent : collisions) {

            touchingGameObjects.add(collisionEvent.getRoot());

            if (collisionEvent.getRoot().getTag().equals("de.naclstudios.etj.gameObjects.wall")) {
                wallCount++;
            }
        }

        if (wallCount >= 2) {
            try {
                SceneManager.setCurrentScene("end", false);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCollision(CollisionEvent e) {

        if (e.getRoot().getTag().equals("de.naclstudios.etj.gameObjects.weapon")) {
            setHasWeapon(true);
            e.getRoot().removeFromCurrentScene();
        }
    }

    @Override
    public void onFixedTick() {

        freeze = true;

        accelerateTo(movementSpeed, Input.getInput());

        if (secureTicks < 300) {
            secureTicks++;
        }

        if (Input.inputUp) {

            currentDirection = Directions.Direction.UP;
            freeze = false;
        }

        if (Input.inputDown) {

            currentDirection = Directions.Direction.DOWN;
            freeze = false;
        }

        if (Input.inputLeft) {

            currentDirection = Directions.Direction.LEFT;
            freeze = false;
        }

        if (Input.inputRight) {

            currentDirection = Directions.Direction.RIGHT;
            freeze = false;
        }

        if (currentDirection != null) {
            switch (currentDirection) {

                case RIGHT:
                    animationRender.setSpritesheetAnimation(walkRight);
                    currentFreezeImage = right;
                    setSlimHitbox();
                    break;
                case LEFT:
                    animationRender.setSpritesheetAnimation(walkLeft);
                    currentFreezeImage = left;
                    setSlimHitbox();
                    break;
                case UP:
                    animationRender.setSpritesheetAnimation(walkUp);
                    currentFreezeImage = up;
                    setBigHitbox();
                    break;
                case DOWN:
                    animationRender.setSpritesheetAnimation(walkDown);
                    currentFreezeImage = down;
                    setBigHitbox();
                    break;
            }
        }

        if (cooldown) {
            ticks++;
        }

        if (ticks == weaponCooldown) {
            cooldown = false;
            ticks = 0;
        }

        if (Input.keyboardInput.isSpace()) {
            shoot();
        }
    }

    @Override
    public void draw(SaltyGraphics graphics) {

        for (GameObject gameObject : touchingGameObjects) {
            //graphics.drawRect(gameObject);
        }

        if (freeze) {
            animationRender.disable();
            graphics.drawImage(currentFreezeImage, getX(), getY(), getWidth(), getHeight());
        } else {
            animationRender.enable();
        }
    }

    private void shoot() {

        if (hasWeapon && !cooldown) {

            cooldown = true;

            if (currentDirection == null) {

                SceneManager.getCurrentScene().addGameObject(new Bullet(new Coordinates2f(getCoordinates().getX() + 50, getCoordinates().getY() + 53), Directions.Direction.DOWN));
            } else {
                switch (currentDirection) {


                    case RIGHT:
                        SceneManager.getCurrentScene().addGameObject(new Bullet(new Coordinates2f(getX() + 25, getY() + 53), Directions.Direction.RIGHT));
                        break;
                    case LEFT:
                        SceneManager.getCurrentScene().addGameObject(new Bullet(new Coordinates2f(getX() + 25, getY() + 53), Directions.Direction.LEFT));
                        break;
                    case UP:
                        SceneManager.getCurrentScene().addGameObject(new Bullet(new Coordinates2f(getX() + 50, getY() + 53), Directions.Direction.UP));
                        break;
                    case DOWN:
                        SceneManager.getCurrentScene().addGameObject(new Bullet(new Coordinates2f(getX() + 50, getY() + 53), Directions.Direction.DOWN));
                        break;
                }
            }
        }
    }

    private void setSlimHitbox() {
        getHitboxAsSimpleHitbox().setOffsetX(14);
        getHitboxAsSimpleHitbox().setWidth(45);
    }

    private void setBigHitbox() {
        getHitboxAsSimpleHitbox().setWidth(71);
        getHitboxAsSimpleHitbox().setOffsetX(0);
    }

    public boolean isHasWeapon() {
        return hasWeapon;
    }

    public void setHasWeapon(boolean hasWeapon) {
        this.hasWeapon = hasWeapon;

        if (hasWeapon) {
            walkUp.setFrames(mainCharSpriteSheet.getManualFrames(walkUpSpritesWeapon));
            walkDown.setFrames(mainCharSpriteSheet.getManualFrames(walkDownSpritesWeapon));
            walkLeft.setFrames(mainCharSpriteSheet.getManualFrames(walkLeftSpritesWeapon));
            walkRight.setFrames(mainCharSpriteSheet.getManualFrames(walkRightSpritesWeapon));

            up = mainCharSpriteSheet.getManualSprite(1, 4);
            down = mainCharSpriteSheet.getManualSprite(2, 4);
            right = mainCharSpriteSheet.getManualSprite(4, 1);
            left = mainCharSpriteSheet.getManualSprite(5, 4);
        }
    }

    private void readAnimation() {
        walkUp.setFrames(mainCharSpriteSheet.getManualFrames(walkUpSprites));
        walkDown.setFrames(mainCharSpriteSheet.getManualFrames(walkDownSprites));
        walkRight.setFrames(mainCharSpriteSheet.getManualFrames(walkRightSprites));
        walkLeft.setFrames(mainCharSpriteSheet.getManualFrames(walkLeftSprites));
    }

    public void readAnimationWithWeapon() {
        mainCharSpriteSheet = new Spritesheet(imageFactory.getOptimizedImageResource("pictures/mainchar-with-weapon.png"), 143, 182);
        readAnimation();
    }

    public int getCollectedKeyFragments() {
        return collectedKeyFragments;
    }

    public void setCollectedKeyFragments(int collectedKeyFragments) {
        this.collectedKeyFragments = collectedKeyFragments;
    }
}
