package com.a.dproject.mvvm.fragment



import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentFfMpegMediaPlayerBinding
import com.a.dproject.mvvm.viewmodel.FFMpegMediaPlayerViewModel
import com.a.processor.ListFragmentAnnotation
import com.byteflow.learnffmpeg.media.FFMediaPlayer
import com.byteflow.learnffmpeg.media.FFMediaPlayer.*


@ListFragmentAnnotation("本地播放器",parentName = "ffmpeg")
class FFMpegMediaPlayerFragment : ArtBaseFragment() , View.OnClickListener,  SurfaceHolder.Callback, FFMediaPlayer.EventCallback{

    lateinit var binding: FragmentFfMpegMediaPlayerBinding
    lateinit var viewModel: FFMpegMediaPlayerViewModel
    lateinit var mMediaPlayer:FFMediaPlayer

    var isTouch = false


    var id:Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_ff_mpeg_media_player
    }

    override fun getStoneId(): Int {
        return 0
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getContentId(), container, false)
        initViewModel()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()
        initData()
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(FFMpegMediaPlayerViewModel::class.java)
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id

    }

 
    private fun initObserver() {
    }


    private fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.onClickListener = this
        binding.surfaceView.holder.addCallback(this)
        binding.seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
               isTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mMediaPlayer.seekToPosition(binding.seekBar.progress.toFloat())
                isTouch = false

            }

        })



    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            mMediaPlayer.play()
        }
    }

    override fun onPause() {
        super.onPause()
        mMediaPlayer.pause()
    }


    private fun initData() {
    }



    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                else -> {

                }
            }
        }
    }

        
    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = FFMpegMediaPlayerFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        mMediaPlayer.play()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mMediaPlayer.unInit()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mMediaPlayer = FFMediaPlayer()
        mMediaPlayer.addEventCallback(this)
        val path = requireActivity().baseContext.filesDir.absolutePath
        val mVideoPath =   path+ "/byteflow/one_piece.mp4"
        mMediaPlayer.init(mVideoPath,VIDEO_RENDER_ANWINDOW,holder.surface)
    }

    override fun onPlayerEvent(msgType: Int, msgValue: Float) {
        binding.root.post {
            when(msgType) {
                MSG_DECODER_READY ->
                    onDecoderReady()
                MSG_DECODING_TIME -> {
                    if(isTouch) {
                        binding.seekBar.setProgress(msgValue.toInt())
                    }
                }
                else -> {

                }
            }
        }
    }


    fun onDecoderReady(){
        val videoWidth = mMediaPlayer.getMediaParams(MEDIA_PARAM_VIDEO_WIDTH).toInt()
        val videoHeight = mMediaPlayer.getMediaParams(MEDIA_PARAM_VIDEO_HEIGHT).toInt()

        if(videoHeight * videoWidth != 0) {
            binding.surfaceView.setAspectRatio(videoWidth,videoHeight)
        }

        val duration = mMediaPlayer.getMediaParams(MEDIA_PARAM_VIDEO_DURATION).toInt()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.seekBar.min = 0
        }

        binding.seekBar.max = duration
    }


}
