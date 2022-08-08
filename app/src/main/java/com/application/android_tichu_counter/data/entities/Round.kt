package com.application.android_tichu_counter.data.entities

import androidx.room.*
import com.application.android_tichu_counter.domain.enums.teams.Team
import com.application.android_tichu_counter.domain.enums.teams.Team.*
import com.application.android_tichu_counter.domain.enums.tichu_states.TichuState
import com.application.android_tichu_counter.domain.enums.tichu_states.TichuState.*
import java.io.Serializable
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
    var firstTeamTichu: TichuState,

    @ColumnInfo(name = "second_team_tichu_success")
    var secondTeamTichu: TichuState,

    @ColumnInfo(name = "first_team_grandtichu_success")
    var firstTeamGrandtichu: TichuState,

    @ColumnInfo(name = "second_team_grandtichu_success")
    var secondTeamGrandtichu: TichuState,

    @ColumnInfo(name = "first_team_double_win")
    var firstTeamDoubleWin: Boolean,

    @ColumnInfo(name = "second_team_double_win")
    var secondTeamDoubleWin: Boolean,

    @ColumnInfo(name = "first_team_round_score")
    var firstTeamRoundScore: Int,

    @ColumnInfo(name = "second_team_round_score")
    var secondTeamRoundScore: Int
) : Serializable {
    @Ignore
    constructor(fkGameId: String, roundIndex: Int) :
            this(
                UUID.randomUUID().toString(), fkGameId, roundIndex,
                NA, NA, NA, NA,
                false, false,
                0, 0
            )

    @Ignore
    constructor(
        fkGameId: String,
        roundIndex: Int,
        firstTeamTichu: TichuState,
        secondTeamTichu: TichuState,
        firstTeamGrandtichu: TichuState,
        secondTeamGrandtichu: TichuState,
        firstTeamDoubleWin: Boolean,
        secondTeamDoubleWin: Boolean,
        firstTeamRoundScore: Int,
        secondTeamRoundScore: Int
    ) :
            this(
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

    fun calculateScoreByTeam(team: Team, roundPoints: Int) {
        if (team == FIRST_TEAM) {
            calculateFirstTeamScore(roundPoints)
            calculateSecondTeamScore(100 - roundPoints)
        } else if (team == SECOND_TEAM) {
            calculateSecondTeamScore(roundPoints)
            calculateFirstTeamScore(100 - roundPoints)
        }
    }

    private fun calculateFirstTeamScore(roundPoints: Int) {
        var v = if (firstTeamDoubleWin) {
            200
        } else if (secondTeamDoubleWin) {
            0
        } else {
            roundPoints
        }

        v += firstTeamTichu.getNormalScore()
        v += firstTeamGrandtichu.getGrandScore()

        firstTeamRoundScore = v
    }

    private fun calculateSecondTeamScore(roundPoints: Int) {
        var v = if (secondTeamDoubleWin) {
            200
        } else if (firstTeamDoubleWin) {
            0
        } else {
            roundPoints
        }

        v += secondTeamTichu.getNormalScore()
        v += secondTeamGrandtichu.getGrandScore()

        secondTeamRoundScore = v
    }

    fun isValidState(): Boolean {
        if (firstTeamTichu == SUCCESS && secondTeamTichu == SUCCESS) {
            return false
        } else if (firstTeamTichu == SUCCESS && secondTeamGrandtichu == SUCCESS) {
            return false
        } else if (firstTeamGrandtichu == SUCCESS && secondTeamTichu == SUCCESS) {
            return false
        } else if (firstTeamGrandtichu == SUCCESS && secondTeamGrandtichu == SUCCESS) {
            return false
        }

        return true
    }

    fun isDoubleWinPossibleForTeam(team: Team): Boolean {
        if (team == FIRST_TEAM) {
            return secondTeamTichu != SUCCESS
                    && secondTeamGrandtichu != SUCCESS
                    && firstTeamTichu != FAILURE
                    && firstTeamGrandtichu != FAILURE
        }

        return firstTeamTichu != SUCCESS
                && firstTeamGrandtichu != SUCCESS
                && secondTeamTichu != FAILURE
                && secondTeamGrandtichu != FAILURE
    }

    fun changeTichuForTeam(team: Team) {
        if (team == FIRST_TEAM) {
            firstTeamTichu = firstTeamTichu.nextState()
            firstTeamGrandtichu = NA
        } else if (team == SECOND_TEAM) {
            secondTeamTichu = secondTeamTichu.nextState()
            secondTeamGrandtichu = NA
        }
    }

    fun changeGrandTichuForTeam(team: Team) {
        if (team == FIRST_TEAM) {
            firstTeamGrandtichu = firstTeamGrandtichu.nextState()
            firstTeamTichu = NA
        } else if (team == SECOND_TEAM) {
            secondTeamGrandtichu = secondTeamGrandtichu.nextState()
            secondTeamTichu = NA
        }
    }

    fun setDoubleWinForTeam(team: Team) {
        if (team == FIRST_TEAM) {
            firstTeamDoubleWin = true
            secondTeamDoubleWin = false
        } else if (team == SECOND_TEAM) {
            secondTeamDoubleWin = true
            firstTeamDoubleWin = false
        }
    }

    override fun toString(): String {
        return "Tichu State:" +
                "\n1 Tichu:                 $firstTeamTichu" +
                "\n1 Grand Tichu:           $firstTeamGrandtichu" +
                "\n1 Double Win Possible:   ${isDoubleWinPossibleForTeam(FIRST_TEAM)}" +
                "\n1 Round Score:           $firstTeamRoundScore\n" +
                "\n2 Tichu:                 $secondTeamTichu" +
                "\n2 Grand Tichu:           $secondTeamGrandtichu" +
                "\n2 Double Win Possible:   ${isDoubleWinPossibleForTeam(SECOND_TEAM)}" +
                "\n2 Round Score:           $secondTeamRoundScore\n" +
                "\nTichu Combination Valid: ${isValidState()}\n"
    }
}