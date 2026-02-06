package com.lyncan.opus.Modules

import android.content.Context
import androidx.room.Room
import com.lyncan.opus.DAOs.AttendanceDAO
import com.lyncan.opus.DAOs.SubjectDAO
import com.lyncan.opus.DAOs.TimeTableDAO
import com.lyncan.opus.Repositories.AppDatabase
import com.lyncan.opus.Repositories.AttendanceRepository
import com.lyncan.opus.Repositories.SubjectRepository
import com.lyncan.opus.Repositories.TimeTableRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "opus_database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideAttendanceDao(database: AppDatabase): AttendanceDAO =
        database.attendanceDAO()

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDAO =
        database.subjectDAO()

    @Provides
    fun provideAttendanceRepository(dao: AttendanceDAO): AttendanceRepository =
        AttendanceRepository(dao)

    @Provides
    fun provideSubjectRepository(dao: SubjectDAO): SubjectRepository =
        SubjectRepository(dao)

    @Provides
    fun provideTimeTableRepository(dao: TimeTableDAO): TimeTableRepository = TimeTableRepository(dao)
}