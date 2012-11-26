package pt.bombap.playn.natal.core;

import playn.core.Image;
import playn.core.Layer;
import playn.core.ResourceCallback;
import pt.bombap.playn.extensions.sprites.SpriteAnimator;

public class SpriteAnimatorView extends View {

	protected SpriteView spriteView;
	private SpriteAnimator spriteAnimator;
	

	private ResourceCallback<? super View> callback;
	
	public SpriteAnimatorView(SpriteView spriteView, Integer ...animationIndices) {
		this.spriteView = spriteView;
		spriteAnimator = new SpriteAnimator(spriteView.getSprite(), animationIndices);
	}
	
	public SpriteAnimatorView(SpriteView spriteView, String ...animationIds) {
		this.spriteView = spriteView;
		spriteAnimator = new SpriteAnimator(spriteView.getSprite(), animationIds);
	}

	public void setRepeat(boolean repeat) {
		spriteAnimator.setRepeat(repeat);
	}

	public void setAnimationRate(int animationRate) {
		spriteAnimator.setAnimationRate(animationRate);
	}
		
	public void start() {
		spriteAnimator.start();
	}

	public void pause() {
		spriteAnimator.pause();
	}

	public void stop() {
		spriteAnimator.stop();
	}

	public boolean isReady() {
		return spriteView.isReady();
	}


	@Override
	public Layer getLayer() {
		return spriteView.getLayer();
	}

	@Override
	public void addCallback(final ResourceCallback<? super View> callback) {
		spriteView.addCallback(callback);
	}
	
	/**
	 * 
	 * @return current image displayed in the sprite
	 */
	public Image getImage() {
		return spriteView.getImage();
	}

	@Override
	public float getWidth() {
		return getImage().width();
	}

	@Override
	public float getHeight() {
		return getImage().height();
	}

	@Override
	public void setOrigin(float x, float y) {
		getLayer().setOrigin(x, y);
	}

	@Override
	public void setScale(float x, float y) {
		getLayer().setScale(x, y);
	}

	@Override
	public void setTranslation(float x, float y) {
		getLayer().setTranslation(x, y);
	}

	@Override
	public void setRotation(float angle) {
		getLayer().setRotation(angle);
	}

	@Override
	public void update(float delta) {
		spriteAnimator.update(delta);
	}

	@Override
	public void paint(float alpha) {
	}
	
	


}
