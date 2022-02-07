package com.mygdx.game.bean;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.constant.BoxConstant;

/**
 * @Author wujx37877
 * @Description screen to box2d 坐标变换
 * @Date 9:46 2022/2/7
 */
public class ScreenVec3 extends Vector3 {
    private Vector3 boxVec3 = new Vector3();

    public ScreenVec3(float x, float y, float z) {
        super(x, y, z);
    }

    public Vector3 getBox() {
        boxVec3.x = x / BoxConstant.PPM;
        boxVec3.y = y / BoxConstant.PPM;
        boxVec3.z = z / BoxConstant.PPM;
        return boxVec3;
    }
}
