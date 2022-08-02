package com.application.android_tichu_counter.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.android_tichu_counter.data.TichuDatabase
import com.application.android_tichu_counter.data.entities.Game
import com.application.android_tichu_counter.data.entities.helper.GameWithRounds
import com.application.android_tichu_counter.data.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GameViewModel(application: Application): AndroidViewModel(application) {
    val allGames: Flow<List<Game>>
    private val repository: GameRepository

    init {
        val dao = TichuDatabase.getInstance(application).gameDao()
        repository = GameRepository(dao)
        allGames = repository.getAllGames()
    }

    fun getGameWithRounds(gameId: String): Flow<GameWithRounds> {
        return repository.getGameWithRounds(gameId)
    }

    fun addGame(game: Game) = viewModelScope.launch(Dispatchers.IO){
        repository.insertOne(game)
    }

    fun updateGame(game: Game) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateOne(game)
    }

    fun deleteGame(game: Game) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteOne(game)
    }
}