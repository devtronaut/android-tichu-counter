package com.application.android_tichu_counter.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.application.android_tichu_counter.R
import com.application.android_tichu_counter.databinding.ActivityMainBinding
import com.application.android_tichu_counter.ui.activities.ScoreboardActivity.Companion.KEY_TEAM_1
import com.application.android_tichu_counter.ui.activities.ScoreboardActivity.Companion.KEY_TEAM_2
import com.application.android_tichu_counter.ui.fragments.TeamNameDialogFragment

/**
 * MainActivity for the starting screen of the application.
 *
 * Extends BaseActivity to be affine to in-app language changes.
 * Implements TeamNameDialogListener to listen to TeamNameDialog inputs.
 *
 * @property KEY_TEAM_1 intent key for team name 1
 * @property KEY_TEAM_2 intent key for team name 2
 *
 * @author Devtronaut
 */
class MainActivity : BaseActivity(), TeamNameDialogFragment.TeamNameDialogListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    /**
     * Create view, instantiate ui components, set listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setOnClickListeners()

        Log.d(TAG, "Create view.")
    }

    // Set listeners for the important ui components
    private fun setOnClickListeners(){
        with(binding){
            mbNewgame.setOnClickListener {
                startGame()
            }

            mbLoadgame.setOnClickListener {
                loadGame()
            }

            mbSettings.setOnClickListener {
                startSettings()
            }

            fabInfo.setOnClickListener {
                startInfo()
            }
        }

        Log.d(TAG, "Set OnClickListeners")
    }

    // Start the ScoreBoardActivity after asking for input on the team names.
    private fun startGame(){
        showTeamNameDialog()

        Log.d(TAG, "Start Game clicked.")
    }

    // Load a previously started game from the saved ones.
    private fun loadGame(){
        val intent = Intent(this, LoadGameActivity::class.java)
        startActivity(intent)

        Log.d(TAG, "Load Game clicked.")
    }

    // Start the settings activity
    private fun startSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

        Log.d(TAG, "Settings clicked.")
    }

    // Start the info/ impressum activity
    private fun startInfo(){
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)

        Log.d(TAG, "Displaying info/ impressum")
    }

    // Show the team name dialog fragment
    private fun showTeamNameDialog(){
        val dialog = TeamNameDialogFragment()
        dialog.show(supportFragmentManager, "TeamNameDialogFragment")

        Log.d(TAG, "Showing team name dialog.")
    }

    /**
     * Retrieve team names from the inputs and pass them in an intent to the scoreboard activity.
     * Dismiss the dialog.
     */
    override fun onDialogSaveClicked(dialog: DialogFragment) {
        // Retrieve team names
        val team1 = dialog.dialog?.findViewById<EditText>(R.id.et_dialog_name_team1)?.text.toString()
        val team2 = dialog.dialog?.findViewById<EditText>(R.id.et_dialog_name_team2)?.text.toString()

        // Create intent for the scoreboard activity and put the team names as extras
        val intent = Intent(this, ScoreboardActivity::class.java)

        if(team1 != ""){
            intent.putExtra(KEY_TEAM_1, team1)
        }

        if(team2 != ""){
            intent.putExtra(KEY_TEAM_2, team2)
        }

        // Start the scoreboard activity
        startActivity(intent)

        Log.d(TAG, "$team1 $team2")

        // Dismiss the dialog
        dialog.dismiss()
        Log.d(TAG, "Dialog gone after saving team names.")

        Log.d(TAG, "Save team names clicked.")
    }

    /**
     * Dismiss the dialog, if the back button is clicked
     */
    override fun onDialogBackClicked(dialog: DialogFragment) {
        dialog.dismiss()

        Log.d(TAG, "Dialog dismissed.")
    }
}