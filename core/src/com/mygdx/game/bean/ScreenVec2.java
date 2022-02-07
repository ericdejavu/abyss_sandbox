package com.mygdx.game.bean;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.constant.BoxConstant;

/**
 * @Author wujx37877
 * @Description screen to box2d 坐标变换
 * @Date 9:46 2022/2/7
 */
public class ScreenVec2 extends Vector2 {
    private Vector2 boxVec2 = new Vector2();

    public ScreenVec2(float x, float y) {
        super(x, y);
    }

    public Vector2 getBox() {
        boxVec2.x = x / BoxConstant.PPM;
        boxVec2.y = y / BoxConstant.PPM;
        return boxVec2;
    }
}
