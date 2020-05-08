package be.ugent.myfestival.utilities


import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream


@GlideModule
class MyAppGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(20 * 1024 * 1024))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, 100 * 1024 * 1024))
    }
}