package dmg.rainsound

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.ViewPropertyAnimator
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

  private val btnStart: Button by lazy { findViewById<Button>(R.id.btnStart) }
  private val btnGoodNight: Button by lazy { findViewById<Button>(R.id.btnGoodNight) }
  private val ivBackground: ImageView by lazy { findViewById<ImageView>(R.id.ivBackground) }
  private val adView: AdView by lazy { findViewById<AdView>(R.id.adView) }

  private var mediaPlayer: MediaPlayer? = null

  private val adListener = object : AdListener() {
    override fun onAdLoaded() {
      println("DMG: add loaded")
    }

    override fun onAdFailedToLoad(errorCode: Int) {
      println("DMG: add load failed with code $errorCode")
      Handler().postDelayed({
        adView.loadAd(AdRequest.Builder().build())
      }, 5000)
    }

    override fun onAdOpened() {
      println("DMG: add opened")
    }

    override fun onAdLeftApplication() {
      println("DMG: add left application")
    }

    override fun onAdClosed() {
      println("DMG: add closed")
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    MobileAds.initialize(this, "ca-app-pub-3656670221855991~8612734762")

    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    actionBar?.hide()
    supportActionBar?.hide()

    btnStart.setOnClickListener {
      btnStart.isEnabled = false
      btnStart.animate().setDuration(1000).alpha(0F).doAfter {
        btnGoodNight.animate().setDuration(1000).alpha(1F).doAfter {
          btnGoodNight.animate().setDuration(1000).alpha(0F).doAfter {
            ivBackground.animate().setDuration(1000).alpha(1F).doAfter {
              playSound()
            }
          }
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()

    adView.adListener = adListener
    adView.loadAd(AdRequest.Builder().build())
  }

  private fun playSound() {
    val resID = resources.getIdentifier("sound", "raw", packageName)
    mediaPlayer = MediaPlayer.create(this@MainActivity, resID)
    mediaPlayer?.isLooping = true
    mediaPlayer?.start()
  }

  override fun onDestroy() {
    super.onDestroy()

    adView.adListener = null

    mediaPlayer?.stop()
    mediaPlayer?.release()
  }

  private fun ViewPropertyAnimator.doAfter(action: () -> Unit) {
    setListener(object : AnimatorListener {
      override fun onAnimationRepeat(animation: Animator?) {}

      override fun onAnimationEnd(animation: Animator?) {
        action.invoke()
      }

      override fun onAnimationCancel(animation: Animator?) {}

      override fun onAnimationStart(animation: Animator?) {}
    })
  }

  override fun onBackPressed() {}
}
