package com.mygdx.game.handler.input.impl;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

/**
 * @Author wujx37877
 * @Description //TODO
 * @Date 15:22 2022/2/7
 */
public class MouseDraggedHandler extends InputAdapter {
    private Body groundBody;
    private Body hitBody;
    private Camera camera;
    private World world;
    private MouseJoint mouseJoint;

    public MouseDraggedHandler(Camera camera, World world, Body groundBody) {
        this.groundBody = groundBody;
        this.camera = camera;
        this.world = world;
    }

    /**
     * we instantiate this vector and the callback here so we don't irritate the
     * GC
     **/
    Vector3 testPoint = new Vector3();
    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.getBody() == groundBody)
                return true;

            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;
        }
    };

    /** another temporary vector **/
    Vector2 target = new Vector2();

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        camera.unproject(testPoint.set(x, y, 0));
        target.set(testPoint.x, testPoint.y);
        // if a mouse joint exists we simply update
        // the target of the joint based on the new
        // mouse coordinates
        if (mouseJoint != null) {
            mouseJoint.setTarget(target);
        }
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int newParam) {
        // translate the mouse coordinates to world coordinates
        testPoint.set(x, y, 0);
        camera.unproject(testPoint);

        // ask the world which bodies are within the given
        // bounding box around the mouse pointer
        hitBody = null;
        world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f,
                testPoint.x + 0.1f, testPoint.y + 0.1f);

        // if we hit something we create a new mouse joint
        // and attach it to the hit body.
        if (hitBody != null) {
            MouseJointDef def = new MouseJointDef();
            def.bodyA = groundBody;
            def.bodyB = hitBody;
            def.collideConnected = true;
            def.target.set(testPoint.x, testPoint.y);
            def.maxForce = 1000.0f * hitBody.getMass();

            mouseJoint = (MouseJoint) world.createJoint(def);
            hitBody.setAwake(true);
        }

        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        // if a mouse joint exists we simply destroy it
        if (mouseJoint != null) {
            world.destroyJoint(mouseJoint);
            mouseJoint = null;
        }
        return false;
    }
}
