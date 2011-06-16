//Copyright Lewis Hemens 2011
package com.lewish.asciigram.client.tools;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.inject.Inject;
import com.lewish.asciigram.client.Canvas;
import com.lewish.asciigram.client.Cell;
import com.lewish.asciigram.client.CssStyles;

//TODO: full of hacks! add refreshDraw(area) method to canvas
public class TextTool extends Tool {

	private final Canvas canvas;
	private Cell currentCell;

	@Inject
	public TextTool(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void mouseDown(Cell cell) {
		selectCell(cell);
	}

	private void selectCell(Cell cell) {
		if (currentCell != null) {
			currentCell.removeStyleName(CssStyles.Selected);
		}
		currentCell = cell;
		if (currentCell != null) {
			currentCell.addStyleName(CssStyles.Selected);
		}
	}

	private void moveSelect(int dx, int dy) {
		if (currentCell != null) {
			selectCell(canvas.getCell(currentCell.x + dx, currentCell.y + dy));
		}
	}

	@Override
	public void cleanup() {
		canvas.refreshDraw();
		canvas.commitDraw();
		selectCell(null);
	}

	@Override
	public void keyPress(KeyPressEvent event) {
			if (event.getCharCode() > 31 && event.getCharCode() < 127) {
				canvas.draw(currentCell, String.valueOf(event.getCharCode()));
				currentCell.pushValue(String.valueOf(event.getCharCode()));
				currentCell.pushHighlight();
				moveSelect(1, 0);
			}
			//canvas.refreshDraw();
		}
	
	@Override
	public void specialKeyPress(int keyCode) {
		if (currentCell == null)
			return;
		switch (keyCode) {
		case KeyCodes.KEY_DOWN:
			moveSelect(0, 1);
			break;
		case KeyCodes.KEY_UP:
			moveSelect(0, -1);
			break;
		case KeyCodes.KEY_LEFT:
			moveSelect(-1, 0);
			break;
		case KeyCodes.KEY_RIGHT:
			moveSelect(1, 0);
			break;
		case KeyCodes.KEY_DELETE:
			canvas.draw(currentCell, null);
			canvas.highlight(currentCell, false);
			currentCell.pushHighlight();
			currentCell.pushValue(null);
			moveSelect(1, 0);
			break;
		case KeyCodes.KEY_BACKSPACE:
			moveSelect(-1, 0);
			canvas.draw(currentCell, null);
			canvas.highlight(currentCell, false);
			currentCell.pushHighlight();
			currentCell.pushValue(currentCell.commitValue);
			break;
		case KeyCodes.KEY_ENTER:
			canvas.refreshDraw();
			canvas.commitDraw();
			break;
		}
		//canvas.refreshDraw();
	}

	@Override
	public String getLabel() {
		return "Text";
	}

	@Override
	public String getDescription() {
		return "Click and type to add text, press return to commit changes";
	}

	@Override
	public String getImageUrl() {
		return "images/texttool.png";
	}
}
