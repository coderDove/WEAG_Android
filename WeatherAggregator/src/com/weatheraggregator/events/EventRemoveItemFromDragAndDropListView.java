package com.weatheraggregator.events;

public class EventRemoveItemFromDragAndDropListView {

    private int position;

    public EventRemoveItemFromDragAndDropListView(final int position) {
	this.position = position;
    }

    public int getPosition() {
	return position;
    }

}
