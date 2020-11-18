package com.a.dproject.mvvm.fragment


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.a.dproject.R
import com.a.dproject.databinding.FragmentMoshiBinding
import com.a.dproject.mvvm.viewmodel.MoshiViewModel
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis


@ListFragmentAnnotation("网路库,携程,Moshi")
class MoshiFragment : ArtBaseFragment(), CoroutineScope {

    protected lateinit var binding: FragmentMoshiBinding
    lateinit var viewModel: MoshiViewModel

    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_moshi
    }

    override fun getStoneId(): Int {
        return 0
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getContentId(), container, false)
        initViewModel()
        initView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initData()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MoshiViewModel::class.java)
        viewModel.init()
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id

    }


    private fun initObserver() {
        viewModel.itemList.observe(viewLifecycleOwner, Observer {
            it.size.toString().toast()
        })

        viewModel.result.observe(viewLifecycleOwner, Observer {
            it.path.toString().toast()

        })

    }

    suspend fun test1() {
        Timber.d("1")
        val job = GlobalScope.launch() {
            Timber.d("2")
        }

        Timber.d("3")
        job.join()
        Timber.d("4")

    }

    suspend fun test2() {
        Timber.d("1")
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Timber.d("2")
        }

        Timber.d("3")
        job.join()
        Timber.d("4")

    }

    private suspend fun testCoroutineCancel() {
        Timber.d("1")
        val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
            Timber.d("2")
            delay(1000)
            Timber.d("3")
        }
        job.cancel()
//        job.join()
        Timber.d("4")
        job.join()

    }

    private suspend fun testCoroutineCancel2() {
        Timber.d("1")
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Timber.d("2")
            delay(1000)
            Timber.d("3")
        }
//        job.join()
        Timber.d("4")
        job.cancel()
        job.join()
        delay(50)

    }

    fun testAsync1() {
        runBlocking {
//            testCoroutineCancel2()
            val time = measureTimeMillis {
                val one = async {
                    delay(100)
                    100
                }
                val two = async {
                    delay(200)
                    200
                }

                val log = "The answer is ${one.await()} and ${two.await()}"
                Timber.d(log)
            }

            Timber.d("Complated in $time ms")
        }
    }


    private fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.message.setOnClickListener {
            viewModel.getDataByRetrofit()
        }
        binding.tvEvent.setOnClickListener {
            testCoroutine()
        }

        binding.tvEvent.setOnLongClickListener {

            launch {
//                testCoroutineCancel2()
//                testAsync1()

                testCoroutineToken()
            }
            "launch run".toast()
            true
        }


    }

    fun testCoroutineToken() {
        launch {
            val token = requestToken()
            val post = createPost(token, "item1")
            processPost(post)
        }
    }

    suspend fun requestToken(): String {
        delay(1000)
        Timber.d("requestToken")
        return "thisisatoken"
    }

    suspend fun createPost(token: String, item: String): String {
        delay(2000)
        Timber.d("createPost:${token}")
        return "url+${token}${item}"
    }

    suspend fun processPost(post: String) {
        delay(3000)
        Timber.d("processPost${post}")
    }

    private fun testCoroutine() {
        launch {
            viewModel.fileDownload(URL)
        }
    }

    private fun initData() {
    }


    companion object {
        class FileDownloadResult(val path: String, val status: FileDownloadStatus)

        enum class FileDownloadStatus {
            Successful,
            StorageError,
            OthersError
        }

        private fun getAvailableSize(path: String?): Long {
            var size: Long = -1

            if (path != null) {
                val fs = StatFs(path)
                val blockSize = fs.blockSizeLong
                val availableBlockSize = fs.availableBlocksLong
                size = blockSize * availableBlockSize
            }
            return size
        }

        suspend fun storageFile(url: String, context: Context): FileDownloadResult =
            suspendCoroutine { continuation ->

                var isHasError = false

                val result = kotlin.runCatching {
                    val client = OkHttpClient()
                    val request = Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()
                    var dirPath = ""
                    if (response.isSuccessful) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            dirPath =
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .toString()
                            val dir = File(dirPath)
                            if (!dir.exists()) {
                                dir.mkdir()
                            }
                        }

                        val file = dirPath + "/" + Uri.parse(url).lastPathSegment
                        val downloadedFile = File(file)

                        val fileExtension =
                            MimeTypeMap.getFileExtensionFromUrl(downloadedFile.absolutePath)
                        val mimeType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.ROOT))
                        val values = ContentValues()

                        val sink: BufferedSink

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            values.put(MediaStore.Images.Media.DESCRIPTION, downloadedFile.name)
                            values.put(MediaStore.Images.Media.DISPLAY_NAME, downloadedFile.name)
                            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                            values.put(MediaStore.Images.Media.TITLE, downloadedFile.name)
                            values.put(
                                MediaStore.Images.Media.RELATIVE_PATH,
                                "${Environment.DIRECTORY_PICTURES}/${downloadedFile.name}"
                            )
                            val insertUri = context.contentResolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values
                            )

                            dirPath = context.getExternalFilesDir("")?.absolutePath ?: ""

                            if (insertUri != null) {
                                val outputStream =
                                    context.contentResolver.openOutputStream(insertUri)
                                if (outputStream != null) {
                                    sink = outputStream.sink().buffer()
                                } else {
                                    return@runCatching FileDownloadResult(
                                        dirPath,
                                        FileDownloadStatus.OthersError
                                    )
                                }
                            } else {
                                return@runCatching FileDownloadResult(
                                    dirPath,
                                    FileDownloadStatus.OthersError
                                )
//                                return@runCatching FileDownloadStatus.OthersError
                            }
                        } else {
                            sink = downloadedFile.sink(true).buffer()
                        }

                        val responseBody =
                            response.body ?: return@runCatching FileDownloadResult(
                                dirPath,
                                FileDownloadStatus.OthersError
                            )
//                        response.body ?: return@runCatching FileDownloadStatus.OthersError

                        try {
                            val contentLength = responseBody.contentLength()
                            if (contentLength > getAvailableSize(dirPath)) {

                                continuation.resume(
                                    FileDownloadResult(
                                        dirPath,
                                        FileDownloadStatus.StorageError
                                    )
                                )
                            }
                            var totalRead: Long = 0
                            var lastRead: Long

                            do {
                                lastRead = responseBody.source().read(sink.buffer(), BUFFER_SIZE)
                                if (lastRead == -1L) {
                                    break
                                }
                                totalRead += lastRead
                                sink.emitCompleteSegments()
                            } while (true)
                            sink.writeAll(responseBody.source())
                            sink.close()
                            responseBody.close()

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                                values.put(
                                    MediaStore.Images.Media.DATA,
                                    downloadedFile.absolutePath
                                )
                                context.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                            }
                        } catch (e: IOException) {
                            responseBody.close()
                            sink.close()
                        }

                    }
                    return@runCatching FileDownloadResult(dirPath, FileDownloadStatus.Successful)
                }.onFailure {
                    isHasError = true
                    continuation.resumeWithException(it)
                }

                if (!isHasError) continuation.resumeWith(result)

            }

        const val BUFFER_SIZE = 4096L
        const val PARAM_DEFAULT_ID = "param_default_id"
        const val URL =
            "https://unsplash.com/photos/ooEjKDNW47o/download?force=true&w=1920"

        fun newInstance(id: Long = 0L): Fragment = MoshiFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


}
