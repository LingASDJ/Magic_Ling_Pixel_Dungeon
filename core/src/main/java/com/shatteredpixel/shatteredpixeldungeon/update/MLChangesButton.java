package com.shatteredpixel.shatteredpixeldungeon.update;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.noosa.Game;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PlatformSupport;

import java.io.File;
import java.util.Objects;

public class MLChangesButton extends StyledButton {
	public static String updateProgress = "";

	private static float updateProgressValue = 0f;
	private static boolean downloadSuccess = false;
	private static boolean downloadStart = false;
	private static boolean downloadFailure = false;

	protected static File file;

	public MLChangesButton(Chrome.Type type, String label) {
		super(type, label);
		Updates.checkForUpdate();
	}

	boolean updateShown = false;

	@Override
	public void update() {
		super.update();

		if (!updateShown && Updates.updateAvailable()) {
			updateShown = true;
			text(Messages.get(TitleScene.class, "update"));
		} else if(downloadFailure) {
			text(Messages.get(MLChangesButton.class, "updatefailed"));
		} else if(updateProgressValue<100f && downloadStart){
			text(updateProgress);
		} else if(downloadSuccess) {
			text(Messages.get(MLChangesButton.class, "downloadsuccessyeah"));
		}

		if(downloadSuccess){
			textColor(ColorMath.interpolate(0xFFFFFF, Window.TITLE_COLOR, 0.5f + (float) Math.sin(Game.timeTotal * 5) / 2f));
		} else if (updateShown && downloadFailure) {
			textColor(ColorMath.interpolate(0xFFFFFF, Window.ANSDO_COLOR, 0.5f + (float) Math.sin(Game.timeTotal * 5) / 2f));
		} else if(updateShown) {
			textColor(ColorMath.interpolate(0xFFFFFF, Window.Pink_COLOR, 0.5f + (float) Math.sin(Game.timeTotal * 5) / 2f));
		}
	}

	@Override
	protected void onClick() {
		if (Updates.updateAvailable()) {
			AvailableUpdateData update = Updates.updateData();
			ShatteredPixelDungeon.scene().addToFront(new WndUpdate(update));
		} else {
			ChangesScene.changesSelected = 0;
			ShatteredPixelDungeon.switchNoFade(ChangesScene.class);
		}
	}

	public class WndUpdate extends Window {

		protected static final int WIDTH_P = 120;
		protected static final int WIDTH_L = 144;

		protected static final int MARGIN = 2;
		protected static final int BUTTON_HEIGHT = 18;

		private PlatformSupport.UpdateCallback listener = new PlatformSupport.UpdateCallback() {
			@Override
			public void onDownloading(boolean isDownloading) {
			}

			@Override
			public void onStart(String url) {
				updateProgress = Messages.get(MLChangesButton.class,"downloading");
				downloadStart = true;
			}

			@SuppressWarnings("DefaultLocale")
			@Override
			public void onProgress(long progress, long total, boolean isChanged) {
				updateProgressValue = (float) progress / total;
				updateProgress = String.format("%.2f", updateProgressValue * 100) + "%";
			}

			@Override
			public void onFinish(File file) {
				downloadSuccess = true;
				downloadFailure = false;
				downloadStart = false;
				MLChangesButton.file = file;
				updateProgress = Messages.get(MLChangesButton.class,"downloadsuccess");
				GLog.pink(Messages.get(MLChangesButton.class, "downloadsuccessling"));
			}

			@Override
			public void onError(Exception e) {
				updateProgress = Messages.get(MLChangesButton.class,"downloadingfailed");
				downloadFailure = true;
				downloadStart = false;
			}

			@Override
			public void onCancel() {
				updateProgress =  Messages.get(MLChangesButton.class,"downloadingfailed");
				downloadFailure = true;
				downloadStart = false;
			}
		};

		public WndUpdate(AvailableUpdateData update) {
			super();

			int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

			float pos = 0;
			IconTitle tfTitle = new IconTitle(NetIcons.get(NetIcons.GLOBE), update.versionName == null ? Messages.get(TitleScene.ChangesButton.class, "title") : Messages.get(TitleScene.ChangesButton.class, "versioned_title", update.versionName));
			tfTitle.setRect(0, pos, width, 0);
			add(tfTitle);

			pos = tfTitle.bottom() + 2 * MARGIN;

			layoutBody(pos, update.desc == null ? Messages.get(TitleScene.ChangesButton.class, "desc") : update.desc, update);
		}

		protected void layoutBody(float pos, String message, AvailableUpdateData update) {
			int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

			HealthBar downloadProgress = new HealthBar() {
				@Override
				public synchronized void update() {
					super.update();
					this.visible = !updateProgress.isEmpty();
					this.level(updateProgressValue);
				}
			};
			downloadProgress.setSize(width, 2);
			downloadProgress.setPos(0, pos);
			add(downloadProgress);

			RenderedTextBlock tfMesage = PixelScene.renderTextBlock(6);
			tfMesage.text(message, width);
			tfMesage.setPos(0, pos+8);
			add(tfMesage);

			pos = tfMesage.bottom() + 2 * MARGIN*2;

			RedButton btn = new RedButton(Messages.get(MLChangesButton.class,"download1")) {
				@Override
				protected void onClick() {
					if(Objects.equals(update.URL1, "null")){
						ShatteredPixelDungeon.scene().add( new WndError( Messages.get(MLChangesButton.class, "null") ) );
					} else if(DeviceCompat.isDesktop()){
						ShatteredPixelDungeon.platform.openURI( update.URL1 );
					} else if (!downloadSuccess) {
						if(downloadFailure) downloadFailure = false;
						Game.platform.updateGame(update.URL1, listener);
					} else {
						Game.platform.install(file);
					}
				}

				@Override
				public void update() {
					if (!updateProgress.isEmpty()) {
						text(updateProgress);
					}
				}
			};
			btn.setRect(0, pos+4, width, BUTTON_HEIGHT);
			add(btn);
			pos += BUTTON_HEIGHT + MARGIN*3;

			RedButton btn2 = new RedButton(Messages.get(MLChangesButton.class,"download2")) {
				@Override
				protected void onClick() {
					if(Objects.equals(update.URL2, "null")){
						ShatteredPixelDungeon.scene().add( new WndError( Messages.get(MLChangesButton.class, "null") ) );
					} else if(DeviceCompat.isDesktop()){
						ShatteredPixelDungeon.platform.openURI( update.URL2 );
					} else if (!downloadSuccess) {
						if(downloadFailure) downloadFailure = false;
						Game.platform.updateGame(update.URL2, listener);
					} else {
						Game.platform.install(file);
					}
				}

				@Override
				public void update() {
					if (!updateProgress.isEmpty()) {
						text(updateProgress);
					}
				}
			};
			btn2.setRect(0, pos, width, BUTTON_HEIGHT);
			add(btn2);
			pos += BUTTON_HEIGHT + MARGIN;
			RedButton btn3 = new RedButton(Messages.get(MLChangesButton.class,"download3")) {
				@Override
				protected void onClick() {
					if(Objects.equals(update.URL3, "null")){
						ShatteredPixelDungeon.scene().add( new WndError( Messages.get(MLChangesButton.class, "null") ) );
					} else if(DeviceCompat.isDesktop()){
						ShatteredPixelDungeon.platform.openURI( update.URL3 );
					} else if (!downloadSuccess) {
						if(downloadFailure) downloadFailure = false;
						Game.platform.updateGame(update.URL3, listener);
					} else {
						Game.platform.install(file);
					}
				}

				@Override
				public void update() {
					if (!updateProgress.isEmpty()) {
						text(updateProgress);
					}
				}
			};
			btn3.setRect(0, pos, width, BUTTON_HEIGHT);
			add(btn3);
			pos += BUTTON_HEIGHT + MARGIN;


			RedButton btn4 = new RedButton(Messages.get(MLChangesButton.class,"blog")) {
				@Override
				protected void onClick() {
					if(Objects.equals(update.URL4, "null")){
						ShatteredPixelDungeon.scene().add( new WndError( Messages.get(MLChangesButton.class, "null") ) );
					} else {
						ShatteredPixelDungeon.platform.openURI( update.URL4 );
					}
				}

				@Override
				public void update() {
					if (!updateProgress.isEmpty()) {
						text(updateProgress);
					}
				}
			};
			btn4.setRect(0, pos, width, BUTTON_HEIGHT);
			add(btn4);


			pos += BUTTON_HEIGHT + MARGIN;

			resize(width, (int) (pos - MARGIN));
		}

	}
}