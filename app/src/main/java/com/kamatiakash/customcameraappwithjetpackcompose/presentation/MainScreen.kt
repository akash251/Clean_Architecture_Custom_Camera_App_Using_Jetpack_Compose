package com.kamatiakash.customcameraappwithjetpackcompose.presentation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kamatiakash.customcameraappwithjetpackcompose.R


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val permissions = if (Build.VERSION.SDK_INT <= 28){
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }else listOf(Manifest.permission.CAMERA)

    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions)

    if (!permissionState.allPermissionsGranted){
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }


    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val screeHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var previewView:PreviewView


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // we will show camera preview once permission is granted
        if (permissionState.allPermissionsGranted){
            Box(modifier = Modifier
                .height(screeHeight * 0.85f)
                .width(screenWidth)) {
                AndroidView(
                    factory = {
                        previewView = PreviewView(it)
                        viewModel.showCameraPreview(previewView, lifecycleOwner)
                        previewView
                    },
                    modifier = Modifier
                        .height(screeHeight * 0.85f)
                        .width(screenWidth)
                )
            }
        }

        Box(
            modifier = Modifier
                .height(screeHeight*0.15f),
            contentAlignment = Alignment.Center
        ){
            IconButton(onClick = {
                if (permissionState.allPermissionsGranted){
                    viewModel.captureAndSave(context)
                }
                else{
                    Toast.makeText(
                        context,
                        "Please accept permission in app settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }) {

                Icon(painter =
                painterResource(id =
                R.drawable.ic_baseline_camera_alt_24),
                    contentDescription = "",
                    modifier = Modifier.size(45.dp),
                    tint = Color.Magenta
                )

            }
        }

    }
}