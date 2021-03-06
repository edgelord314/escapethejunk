package de.naclstudios.etj.scenes;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.ui.elements.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

public class StartOverButton extends Button {

    public StartOverButton() {
        super("Try again!", Game.getHost().getCentrePosition(300, 100), 300, 100);

        setBackgroundColor(new Color(108, 54, 22));
    }

    public void onClick(MouseEvent mouseEvent) {

        try {
            SceneManager.setCurrentScene("game");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
