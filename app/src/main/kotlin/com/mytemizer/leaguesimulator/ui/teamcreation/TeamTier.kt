package com.mytemizer.leaguesimulator.ui.teamcreation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mytemizer.leaguesimulator.R


enum class TeamTier {
    WORLD_CLASS,
    EXCELLENT,
    GOOD,
    MIXED
}

@Composable
fun TeamTier.getDisplayName(): String {
    return when (this) {
        TeamTier.WORLD_CLASS -> stringResource(R.string.team_tier_world_class)
        TeamTier.EXCELLENT -> stringResource(R.string.team_tier_excellent)
        TeamTier.GOOD -> stringResource(R.string.team_tier_good)
        TeamTier.MIXED -> stringResource(R.string.team_tier_mixed)
    }
}