package com.task.modelViewer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Floating top bar showing model count and add button.
 * Uses transparent/frosted appearance to not block the canvas.
 */
@Composable
fun TopActionBar(
    modelCount: Int,
    onAddModel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title + model count badge
        Surface(
            color = Color(0xCC121212),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ViewInAr,
                    contentDescription = null,
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "3D Viewer",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (modelCount > 0) {
                    Surface(
                        color = Color(0xFF00E5FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "$modelCount",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Add model FAB-style button
        FloatingActionButton(
            onClick = onAddModel,
            containerColor = Color(0xFF00E5FF),
            contentColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add 3D Model",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
