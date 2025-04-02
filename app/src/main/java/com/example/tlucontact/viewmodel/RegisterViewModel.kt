//package com.example.tlucontact.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.liveData
//import com.example.tlucontact.network.RetrofitClient
//import com.example.tlucontact.model.RegisterRequest
//import kotlinx.coroutines.Dispatchers
//import retrofit2.HttpException
//import java.io.IOException
//
//class RegisterViewModel : ViewModel() {
//    fun register(username: String, email: String, password: String) = liveData<Result<Unit>>(Dispatchers.IO) {
//        try {
//            val response = RetrofitClient.getInstance().create(ApiService::class.java).register(RegisterRequest(username, email, password)).execute()
//            if (response.isSuccessful) {
//                emit(Result.success(Unit))
//            } else {
//                emit(Result.failure<Unit>(HttpException(response)))
//            }
//        } catch (e: IOException) {
//            emit(Result.failure<Unit>(e))
//        } catch (e: HttpException) {
//            emit(Result.failure<Unit>(e))
//        }
//    }
//}