package com.thitari.foody.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped // รองรับการปรับหมุนจอ และต่อไป เพิ่ม รีโพสิตอรีในวิวโมเดล
class Repository @Inject constructor(
    val remote: RemoteDataSource
) {

}