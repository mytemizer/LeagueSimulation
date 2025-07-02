package com.mytemizer.leaguesimulator.ui.nextmatch.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme


@Composable
fun ResetTournamentDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.reset_tournament_dialog_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(stringResource(R.string.reset_tournament_dialog_message))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.reset_tournament_dialog_confirm),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.reset_tournament_dialog_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun ResetTournamentDialogPreview() {
    LeagueSimulatorTheme {
        ResetTournamentDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}