package com.application.android_tichu_counter.data.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "rounds",
    foreignKeys = [ForeignKey(
        entity = Game::class,
        childColumns = ["fk_game_id"],
        parentColumns = ["game_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Round(
    @PrimaryKey
    @ColumnInfo(name = "round_id")
    var roundId: String,

    @ColumnInfo(name = "fk_game_id")
    var fkGameId: String,

    @ColumnInfo(name = "round_index")
    var roundIndex: Int,

    @ColumnInfo(name = "first_team_tichu_success")
    var firstTeamTichu: Boolean?,

    @ColumnInfo(name = "second_team_tichu_success")
    var secondTeamTichu: Boolean?,

    @ColumnInfo(name = "first_team_grandtichu_success")
    var firstTeamGrandtichu: Boolean?,

    @ColumnInfo(name = "second_team_grandtichu_success")
    var secondTeamGrandtichu: Boolean?,

    @ColumnInfo(name = "first_team_double_win")
    var firstTeamDoubleWin: Boolean,

    @ColumnInfo(name = "second_team_double_win")
    var secondTeamDoubleWin: Boolean,

    @ColumnInfo(name = "first_team_round_score")
    var firstTeamRoundScore: Int,

    @ColumnInfo(name = "second_team_round_score")
    var secondTeamRoundScore: Int
) {
    @Ignore
    constructor(fkGameId: String, roundIndex: Int) : this(
        UUID.randomUUID().toString(),
        fkGameId,
        roundIndex,
        null,
        null,
        null,
        null,
        false,
        false,
        -1,
        -1
    )

    @Ignore
    constructor(
        fkGameId: String,
        roundIndex: Int,
        firstTeamTichu: Boolean?,
        secondTeamTichu: Boolean?,
        firstTeamGrandtichu: Boolean?,
        secondTeamGrandtichu: Boolean?,
        firstTeamDoubleWin: Boolean,
        secondTeamDoubleWin: Boolean,
        firstTeamRoundScore: Int,
        secondTeamRoundScore: Int
    ) : this(
        UUID.randomUUID().toString(),
        fkGameId,
        roundIndex,
        firstTeamTichu,
        secondTeamTichu,
        firstTeamGrandtichu,
        secondTeamGrandtichu,
        firstTeamDoubleWin,
        secondTeamDoubleWin,
        firstTeamRoundScore,
        secondTeamRoundScore
    )

    fun changeFirstTeamTichu() {
        firstTeamGrandtichu = null

        firstTeamTichu = rotateBool(firstTeamTichu)
    }

    fun changeSecondTeamTichu() {
        secondTeamGrandtichu = null

        secondTeamTichu = rotateBool(secondTeamTichu)
    }

    fun changeFirstTeamGrandtichu() {
        firstTeamTichu = null

        firstTeamGrandtichu = rotateBool(firstTeamGrandtichu)
    }

    fun changeSecondTeamGrandtichu() {
        secondTeamTichu = null

        secondTeamGrandtichu = rotateBool(secondTeamGrandtichu)
    }

    fun setFirstTeamDoubleWin() {
        firstTeamDoubleWin = true
    }

    fun setSecondTeamDoubleWin() {
        secondTeamDoubleWin = true
    }

    fun calculateFirstTeamScore(roundPoints: Int): Int {
        var v = if (firstTeamDoubleWin) {
            200
        } else {
            roundPoints
        }

        if (firstTeamTichu == true) {
            v += 100
        } else if (firstTeamTichu == false) {
            v -= 100
        }

        if (firstTeamGrandtichu == true) {
            v += 200
        } else if (firstTeamGrandtichu == false) {
            v -= 200
        }

        firstTeamRoundScore = v
        return firstTeamRoundScore
    }

    fun calculateSecondTeamScore(roundPoints: Int): Int {
        var v = if (secondTeamDoubleWin) {
            200
        } else {
            roundPoints
        }

        if (secondTeamTichu == true) {
            v += 100
        } else if (secondTeamTichu == false) {
            v -= 100
        }

        if (secondTeamGrandtichu == true) {
            v += 200
        } else if (secondTeamGrandtichu == false) {
            v -= 200
        }

        secondTeamRoundScore = v
        return secondTeamRoundScore
    }

    private fun rotateBool(bool: Boolean?): Boolean? {
        return when (bool) {
            null -> true
            true -> false
            false -> null
        }
    }
}