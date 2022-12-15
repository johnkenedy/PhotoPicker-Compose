package com.example.photopickercompose

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.photopickercompose.ui.theme.PhotoPickerComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoPickerComposeTheme {
                var selectedImageUri by remember {
                    mutableStateOf<Uri?>(null)
                }
                var selectedImageUris by remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }
                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = PickVisualMedia(),
                    onResult = { uri -> selectedImageUri = uri }
                )
                val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = PickMultipleVisualMedia(),
                    onResult = { uris -> selectedImageUris = uris }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)

                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                singlePhotoPickerLauncher,
                                getString(R.string.pick_photo)
                            )
                            Button(
                                multiplePhotoPickerLauncher,
                                getString(R.string.pick_multiple_photos)
                            )
                        }
                    }

                    item {
                        selectedImageUri?.let {
                            AsyncImage(it)
                        }
                    }

                    items(selectedImageUris) { uri ->
                        AsyncImage(uri)
                    }
                }
            }
        }
    }

    @Composable
    private fun AsyncImage(uri: Uri) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
    }

    @JvmName("ButtonSinglePhotos")
    @Composable
    private fun Button(
        singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
        buttonText: String
    ) {
        Button(onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = buttonText)
        }
    }

    @JvmName("ButtonMultiplePhotos")
    @Composable
    private fun Button(
        multiplePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>,
        buttonText: String
    ) {
        Button(onClick = {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = buttonText)
        }
    }
}