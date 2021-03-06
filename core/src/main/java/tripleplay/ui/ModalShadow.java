//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2018, Triple Play Authors - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui;

import playn.core.Color;
import playn.core.Surface;
import playn.scene.Layer;
import playn.scene.Mouse;
import playn.scene.Pointer;
import playn.scene.Touch;

import react.Signal;
import react.SignalView;

import pythagoras.f.Dimension;
import pythagoras.f.IDimension;

/**
 * A layer that fills a region with shadow and absorbs all pointer, mouse and touch interactions
 * that land on it. This is primarily intended to be displayed below modal interfaces which must
 * prevent interaction with any interfaces over which they are displayed.
 */
public class ModalShadow extends Layer {
    private final IDimension size;
    private int color;
    private Signal<ModalShadow> clicked = Signal.create();

    public ModalShadow (IDimension size) {
        this.size = size;
        this.setColor(0x000000, 0.5f);
        this.events().connect(new Pointer.Listener() {
            public void onStart (Pointer.Interaction iact) {
                iact.capture();
                clicked.emit(ModalShadow.this);
            }
        });
        this.events().connect(new Mouse.Listener() {
            public void onButton (Mouse.ButtonEvent event, Mouse.Interaction iact) {
                iact.capture();
                clicked.emit(ModalShadow.this);
            }
        });
        this.events().connect(new Touch.Listener() {
            public void onStart (Touch.Interaction iact) {
                iact.capture();
                clicked.emit(ModalShadow.this);
            }
        });
    }

    public ModalShadow (float width, float height) {
        this(new Dimension(width, height));
    }

    public float width () { return size.width(); }
    public float height () { return size.height(); }

    public void setColor(int color, float alpha) {
        this.color = Color.withAlpha(color, Math.round(alpha * 255));
    }

    /**
     * A signal emitted when the a touch, pointer or mouse press is happens on the shadow layer.
     * This can be useful in situations where one wants to dismiss a modal dialog when the user taps
     * anywhere outside the dialog.
     */
    public SignalView<ModalShadow> clicked () {
        return this.clicked;
    }

    protected void paintImpl (Surface surf) {
        surf.setFillColor(color).fillRect(0, 0, width(), height());
    }
}
