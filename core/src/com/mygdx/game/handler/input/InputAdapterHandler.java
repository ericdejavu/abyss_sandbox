package com.mygdx.game.handler.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wujx37877
 * @Description //TODO
 * @Date 15:37 2022/2/7
 */
public class InputAdapterHandler implements InputProcessor {
    private static Map<String, InputProcessor> eventMap = new ConcurrentHashMap<>();
    private static InputAdapterHandler inputAdapterHandler;

    public static void addInputProcessor(String name, InputProcessor processor) {
        eventMap.put(name, processor);
    }

    public static boolean removeProcessor(String name) {
        if (eventMap.containsKey(name)) {
            return eventMap.remove(name) != null;
        }
        return false;
    }

    public static synchronized InputAdapterHandler GetInstance() {
        if (inputAdapterHandler == null) {
            inputAdapterHandler = new InputAdapterHandler();
        }
        return inputAdapterHandler;
    }

    private InputAdapterHandler() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.keyTyped(character);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.touchDown(screenX, screenY, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.touchUp(screenX, screenY, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.touchDragged(screenX, screenY, pointer);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.mouseMoved(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        for (Map.Entry<String, InputProcessor> entity : eventMap.entrySet()) {
            InputProcessor processor = (InputProcessor) entity.getValue();
            processor.scrolled(amountX, amountY);
        }
        return false;
    }
}
