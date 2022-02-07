package com.mygdx.game.input.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * @Author wujx37877
 * @Description //TODO
 * @Date 15:55 2022/2/7
 */
public class ExitGameHandler extends InputAdapter {
    @Override
    public boolean keyDown (int keycode) {
        if (Input.Keys.ESCAPE == keycode) {
            Gdx.app.exit();
        }
        return false;
    }
}
