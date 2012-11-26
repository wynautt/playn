package pt.bombap.playn.extensions.sprites;

import java.util.List;

public class SpriteAnimator {

	protected Sprite sprite;

	protected int animationRate = -1; //millis
	private int elapsedTime = 0;

	protected Integer[] animationIndices;
	protected String[] animationIds;
	protected int currentIndex = 0;

	protected boolean repeat = true;
	protected boolean started = false, paused = false;


	public SpriteAnimator(Sprite sprite, Integer ...animationIndices) {
		this.sprite = sprite;
		this.animationIndices = animationIndices;
	}

	public SpriteAnimator(Sprite sprite, String ...animationIds) {
		this.sprite = sprite;
		this.animationIds = animationIds;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public void setAnimationRate(int animationRate) {
		this.animationRate = animationRate;
	}

	private boolean isUsingIds() {
		return animationIds != null;
	}

	public void start() {
		elapsedTime = 0;
		currentIndex = 0;
		started = true;
	}
	
	public void pause() {
		paused = !paused;
	}
	
	public void stop() {
		started = false;
	}

	public boolean isRunning() {
		return started && !paused;
	}

	private <T> void updateSprite(T[] animation) {
		currentIndex = (currentIndex + 1) % animation.length;
		sprite.setSprite(animation[currentIndex]);
		if(currentIndex == 0 && !repeat) {
			stop();
		}
	}

	private void updateSprite() {
		if(isUsingIds()) {
			updateSprite(animationIds);
		} else {
			updateSprite(animationIndices);
		}
	}

	public void paint(float alpha) {

	}

	//TODO: state pattern (started, paused, stopped)
	public void update(float delta) {
		if(isRunning()) {
			elapsedTime += delta;
			if(animationRate < 0 || elapsedTime > animationRate) {
				updateSprite();
				elapsedTime = 0;
			}
		}
	}

}
