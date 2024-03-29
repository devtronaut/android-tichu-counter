package io.github.devtronaut.android_tichu_counter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.devtronaut.android_tichu_counter.R
import io.github.devtronaut.android_tichu_counter.data.entities.TeamRound
import io.github.devtronaut.android_tichu_counter.databinding.FragmentRoundProgressBinding
import io.github.devtronaut.android_tichu_counter.databinding.RoundResultTrBinding
import io.github.devtronaut.android_tichu_counter.domain.enums.tichu_states.TichuState

/**
 * Fragment ot display the round progress of each team on the scoreboard screen.
 *
 * Copyright (C) 2022 Devtronaut
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * Find a copy of the GNU AGPLv3 in the root-level file "LICENCE".
 */
class RoundProgressFragment : Fragment() {
    companion object {
        private const val TEAM_ROUND = "team_round"

        @JvmStatic
        fun getInstance(roundOfTeam: ArrayList<TeamRound>) =
            RoundProgressFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(TEAM_ROUND, roundOfTeam)
                }
            }
    }

    private var _binding: FragmentRoundProgressBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var teamRounds: ArrayList<TeamRound>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            teamRounds = it.getParcelableArrayList<TeamRound>(TEAM_ROUND) as ArrayList<TeamRound>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRoundProgressBinding.inflate(inflater, null, false)
        val view = binding.root

        fillRounds()

        return view
    }

    private fun fillRounds() {
        teamRounds.forEach { round ->
            val trRound = RoundResultTrBinding.inflate(layoutInflater)

            trRound.tvRoundPoints.text = round.roundScore.toString()

            with(round) {
                setTichu(trRound.tvRoundTichu, tichu)
                setGrandTichu(trRound.tvRoundGrandTichu, grandTichu)

                if (doubleWin) setDoubleWin(trRound.tvRoundDoubleWin)
            }

            binding.tlRoundsTeam.addView(trRound.root)
        }
    }

    private fun setTichu(textView: TextView, tichuState: TichuState) {
        if (tichuState == TichuState.SUCCESS) {
            textView.setText(R.string.tichu_short)
            textView.setTextColor(resolveColor(R.color.green))
        } else if (tichuState == TichuState.FAILURE) {
            textView.setText(R.string.tichu_short)
            textView.setTextColor(resolveColor(R.color.red))
        }
    }

    private fun setGrandTichu(textView: TextView, tichuState: TichuState) {
        if (tichuState == TichuState.SUCCESS) {
            textView.setText(R.string.grand_tichu_short)
            textView.setTextColor(resolveColor(R.color.green))
        } else if (tichuState == TichuState.FAILURE) {
            textView.setText(R.string.grand_tichu_short)
            textView.setTextColor(resolveColor(R.color.red))
        }
    }

    private fun setDoubleWin(textView: TextView) {
        textView.setText(R.string.double_win_short)
        textView.setTextColor(resolveColor(R.color.green))
    }

    private fun resolveColor(id: Int): Int {
        return resources.getColor(id, requireActivity().application.theme)
    }
}