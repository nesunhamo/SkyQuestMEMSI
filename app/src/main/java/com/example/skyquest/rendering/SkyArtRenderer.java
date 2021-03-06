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

  private final ObjectRenderer skyArtPokeball = new ObjectRenderer();
  private final ObjectRenderer skyArtPikachu = new ObjectRenderer();

  public SkyArtRenderer() {}

  public void createOnGlThread(Context context) throws IOException {

    skyArtPokeball.createOnGlThread(
            context, "models/pokeball.obj", "models/ball_texture.png");
    skyArtPokeball.setMaterialProperties(0.0f, 1f, 1.0f, 8.0f);
    skyArtPokeball.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending);

    skyArtPikachu.createOnGlThread(
            context, "models/pikachu.obj", "models/pikachu_texture.png");
    skyArtPikachu.setMaterialProperties(0.0f, 1f, 1.0f, 8.0f);
    skyArtPikachu.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending);

  }

  public void draw(
      float[] viewMatrix,
      float[] projectionMatrix,
      AugmentedImage augmentedImage,
      Anchor centerAnchor,
      float[] colorCorrectionRgba) {
    
    float[] tintColor =
        convertHexToColor(TINT_COLORS_HEX[augmentedImage.getIndex() % TINT_COLORS_HEX.length]);

    /** ------------------- Edit augmented reality offsets over here, help marked by "To Do" --------------------- **/

    Pose anchorPose = centerAnchor.getPose();
    Pose worldBoundaryPose = anchorPose.compose(
            Pose.makeTranslation(
                    0f * augmentedImage.getExtentX(),    /* TODO The float value is the amount by which to multiple the QR code size
                                                                For example, 1f * augmentedImage.getExtentX() would result in a translation of 8cm */
                    -1.0f * augmentedImage.getExtentX(), /* This will move back and forth on the platform*/
                    -0.3f * augmentedImage.getExtentZ()) /* This will move up and down */
    );
    Pose finalPose = worldBoundaryPose.compose(
            Pose.makeRotation(-1, 0, 0, 1)
    );

    // TODO you can change this for bigger objects
    float scaleFactor = 0.065f;
    float[] modelMatrix = new float[16];

    finalPose.toMatrix(modelMatrix, 0);
    // TODO test with/out pokeball
//    skyArtPokeball.updateModelMatrix(modelMatrix, scaleFactor);
//    skyArtPokeball.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
    skyArtPikachu.updateModelMatrix(modelMatrix, scaleFactor);
    skyArtPikachu.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);

    /** ------------------- Edit section end --------------------- **/
}

  private static float[] convertHexToColor(int colorHex) {
    // colorHex is in 0xRRGGBB format
    float red = ((colorHex & 0xFF0000) >> 16) / 255.0f * TINT_INTENSITY;
    float green = ((colorHex & 0x00FF00) >> 8) / 255.0f * TINT_INTENSITY;
    float blue = (colorHex & 0x0000FF) / 255.0f * TINT_INTENSITY;
    return new float[] {red, green, blue, TINT_ALPHA};
  }
}
