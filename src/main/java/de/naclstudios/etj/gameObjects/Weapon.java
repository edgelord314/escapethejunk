package de.naclstudios.etj.gameObjects;

import de.edgelord.saltyengine.components.animation.AnimationRender;
import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.effect.Spritesheet;
import de.edgelord.saltyengine.effect.SpritesheetAnimation;
import de.edgelord.saltyengine.factory.ImageFactory;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.resource.InnerResource;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.transform.Coordinates2f;
import de.edgelord.saltyengine.utils.SaltySystem;

public class Weapon extends GameObject {

    private ImageFactory imageFactory = new ImageFactory(new InnerResource());
    private Spritesheet spritesheet = new Spritesheet(imageFactory.getOptimizedImageResource("pictures/weapon-on_ground.png"), 533, 478);
    private SpritesheetAnimation animation = new SpritesheetAnimation(this);
    private AnimationRender animationRender = new AnimationRender(this, "de.naclstudios.etj.gameObjects.weapon.animationRender", animation, (int) (175 * SaltySystem.fixedTickMillis));

    public Weapon(Coordinates2f position) {
        super(position, 72, 65, "de.naclstudios.etj.gameObjects.weapon");

        animation.setFrames(spritesheet.getManualFrames(new Coordinates(1, 1), new Coordinates(2, 1)));

        addComponent(animationRender);
        // removeComponent(DEFAULT_PHYSICS_NAME);
        setStationary(true);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onCollision(CollisionEvent collisionEvent) {

    }

    @Override
    public void onFixedTick() {

    }

    @Override
    public void draw(SaltyGraphics graphics) {

    }
}
