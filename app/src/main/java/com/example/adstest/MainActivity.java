package com.example.adstest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeCustomFormatAd;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NativeCustomFormatAd.OnCustomClickListener {
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (count == 0) {
            loadPauseAd();
            count++;
        }
    }

    private void loadPauseAd() {

        // Ignore request Pause Ads
        ImageView mPauseAdsImage = findViewById(R.id.image);
        View mPauseAdsContainer = findViewById(R.id.container);
        AdLoader.Builder builder = new AdLoader.Builder(this,  "/" + "22116117889" + "/" + "mbcshahed");
        builder.forCustomFormatAd("11965102", nativeCustomFormatAd -> {
            nativeCustomFormatAd.recordImpression();
            // handle custom template loaded successfully
            String imageUrl = null;
            if (nativeCustomFormatAd.getImage("Image") != null) {
                imageUrl = String.valueOf(nativeCustomFormatAd.getImage("Image").getUri());
            }

            Glide.with(this)
                    .load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                          mPauseAdsImage.setImageDrawable(resource);
                            return false;

                        }
                    })
                    .into(mPauseAdsImage);
            try {
                nativeCustomFormatAd.getDisplayOpenMeasurement().setView(mPauseAdsContainer);
                boolean start = nativeCustomFormatAd.getDisplayOpenMeasurement().start();
                Log.e("PADSTEST", start + "");
            } catch (Exception ignored) {
            }
            mPauseAdsImage.setOnClickListener(unusedView -> nativeCustomFormatAd.performClick("Image"));
            // Begin Open Measurement
        }, this);


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError adError) {
                mPauseAdsContainer.setVisibility(View.GONE);
            }
        }).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setReturnUrlsForImageAssets(true)
                .build();
        builder.withNativeAdOptions(adOptions);

        AdManagerAdRequest.Builder newRequest = new AdManagerAdRequest.Builder()
                .addCustomTargeting("ShahidpageType", "playerPage")
                .addCustomTargeting("ShahiduserType", "anonymous")
                .addCustomTargeting("shahid_localization", "en");
                /*.addCustomTargeting(AdsUtils.DISPLAY_SHOW_NAME, ProductUtil.getTitle(mProductModel))
                .addCustomTargeting(AdsUtils.DISPLAY_SHOW_DIALECT, ProductUtil.getDialectName(mProductModel))
                .addCustomTargeting(AdsUtils.DISPLAY_SHOW_GENRE, ProductUtil.getGenres(mProductModel))
                .addCustomTargeting(AdsUtils.DISPLAY_CONTENT_TYPE, ProductUtil.getProductType(mProductModel).toLowerCase())
                .addCustomTargeting(AdsUtils.LANGUAGE, LocaleContextWrapper.getCurrentLanguage());*/

       /* if (!TextUtils.isEmpty(AppConfigManager.getInstance().getLotameAudienceAbbreviation())) {
            newRequest.addCustomTargeting(LOTAME_AUDIENCE, AppConfigManager.getInstance().getLotameAudienceAbbreviation());
        }*/

        adLoader.loadAd(newRequest.build());
    }

    @Override
    public void onCustomClick(@NonNull NativeCustomFormatAd nativeCustomFormatAd, @NonNull String s) {

    }
}