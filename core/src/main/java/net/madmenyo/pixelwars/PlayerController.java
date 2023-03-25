package net.madmenyo.pixelwars;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class PlayerController {
    public boolean strafeLeft;
    public boolean strafeRight;
    public boolean moveForward;
    public boolean moveBackward;
    public boolean fire;


    private InputProcessor inputProcessor = new InputProcessor() {
        @Override
        public boolean keyDown(int keycode) {
            switch (keycode){
                case Input.Keys.W:
                    moveForward = true;
                    return true;
                case Input.Keys.S:
                    moveBackward = true;
                    return true;
                case Input.Keys.A:
                    strafeLeft = true;
                    return true;
                case Input.Keys.D:
                    strafeRight = true;
                    return true;
                case Input.Keys.SPACE:
                    fire = true;
                    return true;
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode){
                case Input.Keys.W:
                    moveForward = false;
                    return true;
                case Input.Keys.S:
                    moveBackward = false;
                    return true;
                case Input.Keys.A:
                    strafeLeft = false;
                    return true;
                case Input.Keys.D:
                    strafeRight = false;
                    return true;
                case Input.Keys.SPACE:
                    fire = false;
                    return true;
            }
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == 0){
                fire = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (button == 0){
                fire = false;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    };

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }
}
