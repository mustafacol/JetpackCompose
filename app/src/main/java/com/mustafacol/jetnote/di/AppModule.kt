package com.mustafacol.jetnote.di

import android.content.Context
import androidx.room.Room
import com.mustafacol.jetnote.data.NoteDatabase
import com.mustafacol.jetnote.data.NoteDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun providesNotesDao(noteDatabase: NoteDatabase):NoteDatabaseDao = noteDatabase.noteDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context):NoteDatabase = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "notes_db")
        .fallbackToDestructiveMigration()
        .build()
}