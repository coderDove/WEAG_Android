package com.mobeta.android.dslv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class UndoView extends LinearLayout {

    private Button btnUndo;
    private int mPosition;

    public UndoView(Context context) {
	super(context);
	initControls();
    }

    public UndoView(Context context, AttributeSet attrs) {
	super(context, attrs);
	initControls();
    }

    public void initControls() {
	View view = View.inflate(getContext(), R.layout.undoview, this);
	btnUndo = (Button) view.findViewById(R.id.btnUndo);
    }

    public void setOnUndoClickListener(OnClickListener listener) {
	btnUndo.setOnClickListener(listener);
    }

    public int getPosition() {
	return mPosition;
    }

    public void setPosition(int position) {
	this.mPosition = position;
    }

}
