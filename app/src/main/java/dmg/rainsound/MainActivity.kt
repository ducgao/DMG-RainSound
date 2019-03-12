package dmg.rainsound

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewPropertyAnimator
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

  private val btnStart: Button by lazy { findViewById<Button>(R.id.btnStart) }
  private val btnGoodNight: Button by lazy { findViewById<Button>(R.id.btnGoodNight) }
  private val ivBackground: ImageView by lazy { findViewById<ImageView>(R.id.ivBackground) }

  private var mediaPlayer: MediaPlayer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

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

  private fun playSound() {
    val resID = resources.getIdentifier("sound", "raw", packageName)
    mediaPlayer = MediaPlayer.create(this@MainActivity, resID)
    mediaPlayer?.isLooping = true
    mediaPlayer?.start()
  }

  override fun onDestroy() {
    super.onDestroy()

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
