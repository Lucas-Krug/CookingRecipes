package de.lucas.cookingrecipes.details.presentation.videoplayer

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun VideoPlayer(videoLink: String) {
    val uri = Uri.parse(videoLink)
    val videoId = uri.getQueryParameter("v")

    AndroidView(
        factory = {
            val view = YouTubePlayerView(it)
            view.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.cueVideo(videoId ?: "", 0f)
                    }
                }
            )
            view
        },
        modifier = Modifier.fillMaxWidth()
    )
}