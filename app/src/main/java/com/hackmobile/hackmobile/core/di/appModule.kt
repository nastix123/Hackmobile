package com.hackmobile.hackmobile.core.di

import com.example.hotelhackapp.presentation.MainListViewModel
import com.hackmobile.hackmobile.data.ReservationRepositoryImpl
import com.hackmobile.hackmobile.data.remote.ReservationApi
import com.hackmobile.hackmobile.domain.BluetoothManager
import com.hackmobile.hackmobile.domain.CommonRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single<CommonRepository> { ReservationRepositoryImpl(get()) }



    viewModel { MainListViewModel(get(), get(), androidContext()) }
    single { BluetoothManager(androidContext()) }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ReservationApi> {
        get<Retrofit>().create(ReservationApi::class.java)
    }
}
