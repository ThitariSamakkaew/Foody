package com.thitari.foody.data

import com.thitari.foody.data.database.LocalDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped // รองรับการปรับหมุนจอ และต่อไป เพิ่ม รีโพสิตอรีในวิวโมเดล
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource

}