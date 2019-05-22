package BrainGame.handlers;

import BrainGame.GUI.canvas.ClickMode;

public interface ModeHandler {
    void handle(double x, double y, ClickMode mode);
}
