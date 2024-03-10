package com.shatteredpixel.shatteredpixeldungeon.update;

//TODO with review functionality, this service is about more than just updates
// perhaps rename to PlatformService, StoreService, DistributionService, etc?
public abstract class UpdateService {

	public static abstract class UpdateResultCallback {
		public abstract void onUpdateAvailable( AvailableUpdateData update );
		public abstract void onNoUpdateFound();
		public abstract void onConnectionFailed();
	}

	//whether the service supports offering update notifications via an ingame prompt
	public abstract boolean supportsUpdatePrompts();

	//whether the service supports an opt-in channel for betas
	public abstract boolean supportsBetaChannel();

	public abstract void checkForUpdate( boolean useMetered, boolean includeBetas, UpdateResultCallback callback );

	public abstract void initializeUpdate( AvailableUpdateData update );

	public static abstract class ReviewResultCallback {
		public abstract void onComplete();
	}

	//whether the service supports prompts to review the game via and ingame prompt
	public abstract boolean supportsReviews();

	public abstract void initializeReview( ReviewResultCallback callback );

	public abstract void openReviewURI();

}
