package com.shatteredpixel.shatteredpixeldungeon.update;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.utils.Callback;

import java.util.Date;

public class Updates {

	public static UpdateService service;

	public static boolean supportsUpdates() {
		return service != null;
	}

	private static Date lastCheck = null;
	private static final long CHECK_DELAY = 1000 * 60 * 60; //1 hour

	public static boolean supportsUpdatePrompts() {
		return supportsUpdates() && service.supportsUpdatePrompts();
	}

	public static boolean supportsBetaChannel() {
		return supportsUpdates() && service.supportsBetaChannel();
	}

	public static void checkForUpdate() {
		if (!supportsUpdatePrompts()) return;
		if (lastCheck != null && (new Date().getTime() - lastCheck.getTime()) < CHECK_DELAY) return;

		//We do this so that automatically enabled beta checking (for users who DLed a beta) persists afterward
		if (SPDSettings.betas()) {
			SPDSettings.betas(true);
		}

		service.checkForUpdate(!SPDSettings.WiFi(), SPDSettings.betas(), new UpdateService.UpdateResultCallback() {
			@Override
			public void onUpdateAvailable(AvailableUpdateData update) {
				lastCheck = new Date();
				updateData = update;
			}

			@Override
			public void onNoUpdateFound() {
				lastCheck = new Date();
			}

			@Override
			public void onConnectionFailed() {
				lastCheck = null;
			}
		});
	}

	public static void launchUpdate(AvailableUpdateData data) {
		service.initializeUpdate(data);
	}

	private static AvailableUpdateData updateData = null;

	public static boolean updateAvailable() {
		return updateData != null;
	}

	public static AvailableUpdateData updateData() {
		return updateData;
	}

	public static void clearUpdate() {
		updateData = null;
		lastCheck = null;
	}

	public static boolean supportsReviews() {
		return supportsUpdates() && service.supportsReviews();
	}

	public static void launchReview(Callback callback) {
		if (supportsUpdates()) {
			service.initializeReview(new UpdateService.ReviewResultCallback() {
				@Override
				public void onComplete() {
					callback.call();
				}
			});
		} else {
			callback.call();
		}
	}

	public static void openReviewURI() {
		if (supportsUpdates()) {
			service.openReviewURI();
		}
	}

}
