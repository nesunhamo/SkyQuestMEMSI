/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.skyquest.rendering;

import android.content.Context;

import com.example.skyquest.common.rendering.ObjectRenderer;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;

import java.io.IOException;

/** Renders an augmented image. */
public class SkyArtRenderer {
  private static final String TAG = "SkyArtRenderer";

  private static final float TINT_INTENSITY = 0.1f;
  private static final float TINT_ALPHA = 1.0f;
  private static final int[] TINT_COLORS_HEX = {
    0x000000, 0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4, 0x00BCD4,
    0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFFEB3B, 0xFFC107, 0xFF9800,
  };

  private final ObjectRenderer skyArtAndy = new ObjectRenderer();

  public SkyArtRenderer() {}

  public void createOnGlThread(Context context) throws IOException {

//    imageFrameUpperLeft.createOnGlThread(
//        context, "models/frame_upper_left.obj", "models/frame_base.png");
//    imageFrameUpperLeft.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameUpperLeft.setBlendMode(BlendMode.AlphaBlending);

    skyArtAndy.createOnGlThread(
            context, "models/pikachu.obj", "models/Textura.Pikachu.1.png", "models/P_007_Texture_Poleball.png");
    skyArtAndy.setMaterialProperties(0.0f, 1f, 1.0f, 3.0f);
    skyArtAndy.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending);

  }

  public void draw(
      float[] viewMatrix,
      float[] projectionMatrix,
      AugmentedImage augmentedImage,
      Anchor centerAnchor,
      float[] colorCorrectionRgba) {
    
    float[] tintColor =
        convertHexToColor(TINT_COLORS_HEX[augmentedImage.getIndex() % TINT_COLORS_HEX.length]);

    Pose anchorPose = centerAnchor.getPose();
    Pose worldBoundaryPose = anchorPose.compose(
            Pose.makeTranslation(
                    0f * augmentedImage.getExtentX(),
                    -1.6f * augmentedImage.getExtentX(), /* This will move back and forth on the platform */
                    -0.6f * augmentedImage.getExtentZ())
    );
    Pose finalPose = worldBoundaryPose.compose(
            Pose.makeRotation(-1, 0, 0, 1)
    );

    // TODO you can change this for bigger objects
    float scaleFactor = 0.1f;
    float[] modelMatrix = new float[16];

    finalPose.toMatrix(modelMatrix, 0);
    skyArtAndy.updateModelMatrix(modelMatrix, scaleFactor);
    skyArtAndy.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
}

  private static float[] convertHexToColor(int colorHex) {
    // colorHex is in 0xRRGGBB format
    float red = ((colorHex & 0xFF0000) >> 16) / 255.0f * TINT_INTENSITY;
    float green = ((colorHex & 0x00FF00) >> 8) / 255.0f * TINT_INTENSITY;
    float blue = (colorHex & 0x0000FF) / 255.0f * TINT_INTENSITY;
    return new float[] {red, green, blue, TINT_ALPHA};
  }
}
