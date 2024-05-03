package com.xbot.ui.animation

import android.view.animation.Interpolator
import androidx.compose.animation.core.Easing
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FastOutLinearInInterpolator : LookupTableInterpolator(VALUES) {
    companion object {
        private val VALUES = floatArrayOf(
            0.0000f, 0.0001f, 0.0002f, 0.0005f, 0.0008f, 0.0013f, 0.0018f,
            0.0024f, 0.0032f, 0.0040f, 0.0049f, 0.0059f, 0.0069f, 0.0081f,
            0.0093f, 0.0106f, 0.0120f, 0.0135f, 0.0151f, 0.0167f, 0.0184f,
            0.0201f, 0.0220f, 0.0239f, 0.0259f, 0.0279f, 0.0300f, 0.0322f,
            0.0345f, 0.0368f, 0.0391f, 0.0416f, 0.0441f, 0.0466f, 0.0492f,
            0.0519f, 0.0547f, 0.0574f, 0.0603f, 0.0632f, 0.0662f, 0.0692f,
            0.0722f, 0.0754f, 0.0785f, 0.0817f, 0.0850f, 0.0884f, 0.0917f,
            0.0952f, 0.0986f, 0.1021f, 0.1057f, 0.1093f, 0.1130f, 0.1167f,
            0.1205f, 0.1243f, 0.1281f, 0.1320f, 0.1359f, 0.1399f, 0.1439f,
            0.1480f, 0.1521f, 0.1562f, 0.1604f, 0.1647f, 0.1689f, 0.1732f,
            0.1776f, 0.1820f, 0.1864f, 0.1909f, 0.1954f, 0.1999f, 0.2045f,
            0.2091f, 0.2138f, 0.2184f, 0.2232f, 0.2279f, 0.2327f, 0.2376f,
            0.2424f, 0.2473f, 0.2523f, 0.2572f, 0.2622f, 0.2673f, 0.2723f,
            0.2774f, 0.2826f, 0.2877f, 0.2929f, 0.2982f, 0.3034f, 0.3087f,
            0.3141f, 0.3194f, 0.3248f, 0.3302f, 0.3357f, 0.3412f, 0.3467f,
            0.3522f, 0.3578f, 0.3634f, 0.3690f, 0.3747f, 0.3804f, 0.3861f,
            0.3918f, 0.3976f, 0.4034f, 0.4092f, 0.4151f, 0.4210f, 0.4269f,
            0.4329f, 0.4388f, 0.4448f, 0.4508f, 0.4569f, 0.4630f, 0.4691f,
            0.4752f, 0.4814f, 0.4876f, 0.4938f, 0.5000f, 0.5063f, 0.5126f,
            0.5189f, 0.5252f, 0.5316f, 0.5380f, 0.5444f, 0.5508f, 0.5573f,
            0.5638f, 0.5703f, 0.5768f, 0.5834f, 0.5900f, 0.5966f, 0.6033f,
            0.6099f, 0.6166f, 0.6233f, 0.6301f, 0.6369f, 0.6436f, 0.6505f,
            0.6573f, 0.6642f, 0.6710f, 0.6780f, 0.6849f, 0.6919f, 0.6988f,
            0.7059f, 0.7129f, 0.7199f, 0.7270f, 0.7341f, 0.7413f, 0.7484f,
            0.7556f, 0.7628f, 0.7700f, 0.7773f, 0.7846f, 0.7919f, 0.7992f,
            0.8066f, 0.8140f, 0.8214f, 0.8288f, 0.8363f, 0.8437f, 0.8513f,
            0.8588f, 0.8664f, 0.8740f, 0.8816f, 0.8892f, 0.8969f, 0.9046f,
            0.9124f, 0.9201f, 0.9280f, 0.9358f, 0.9437f, 0.9516f, 0.9595f,
            0.9675f, 0.9755f, 0.9836f, 0.9918f, 1.0000f
        )
    }
}

class SineCosineEasing(private val n: Int) : Easing {
    override fun transform(fraction: Float): Float {
        return 0.5f + (sin(n * fraction * PI) * cos(n * fraction * PI)).toFloat()
    }
}

class EmphasizedEasing : LookupTableInterpolator(VALUES), Easing {
    override fun transform(fraction: Float): Float {
        return getInterpolation(fraction)
    }

    companion object {
        private val VALUES = floatArrayOf(
            0.0000f, 0.0008f, 0.0016f, 0.0024f, 0.0032f, 0.0057f, 0.0083f,
            0.0109f, 0.0134f, 0.0171f, 0.0218f, 0.0266f, 0.0313f, 0.0360f,
            0.0431f, 0.0506f, 0.0581f, 0.0656f, 0.0733f, 0.0835f, 0.0937f,
            0.1055f, 0.1179f, 0.1316f, 0.1466f, 0.1627f, 0.1810f, 0.2003f,
            0.2226f, 0.2468f, 0.2743f, 0.3060f, 0.3408f, 0.3852f, 0.4317f,
            0.4787f, 0.5177f, 0.5541f, 0.5834f, 0.6123f, 0.6333f, 0.6542f,
            0.6739f, 0.6887f, 0.7035f, 0.7183f, 0.7308f, 0.7412f, 0.7517f,
            0.7621f, 0.7725f, 0.7805f, 0.7879f, 0.7953f, 0.8027f, 0.8101f,
            0.8175f, 0.8230f, 0.8283f, 0.8336f, 0.8388f, 0.8441f, 0.8494f,
            0.8546f, 0.8592f, 0.8630f, 0.8667f, 0.8705f, 0.8743f, 0.8780f,
            0.8818f, 0.8856f, 0.8893f, 0.8927f, 0.8953f, 0.8980f, 0.9007f,
            0.9034f, 0.9061f, 0.9087f, 0.9114f, 0.9141f, 0.9168f, 0.9194f,
            0.9218f, 0.9236f, 0.9255f, 0.9274f, 0.9293f, 0.9312f, 0.9331f,
            0.9350f, 0.9368f, 0.9387f, 0.9406f, 0.9425f, 0.9444f, 0.9460f,
            0.9473f, 0.9486f, 0.9499f, 0.9512f, 0.9525f, 0.9538f, 0.9551f,
            0.9564f, 0.9577f, 0.9590f, 0.9603f, 0.9616f, 0.9629f, 0.9642f,
            0.9654f, 0.9663f, 0.9672f, 0.9680f, 0.9689f, 0.9697f, 0.9706f,
            0.9715f, 0.9723f, 0.9732f, 0.9741f, 0.9749f, 0.9758f, 0.9766f,
            0.9775f, 0.9784f, 0.9792f, 0.9801f, 0.9808f, 0.9813f, 0.9819f,
            0.9824f, 0.9829f, 0.9835f, 0.9840f, 0.9845f, 0.9850f, 0.9856f,
            0.9861f, 0.9866f, 0.9872f, 0.9877f, 0.9882f, 0.9887f, 0.9893f,
            0.9898f, 0.9903f, 0.9909f, 0.9914f, 0.9917f, 0.9920f, 0.9922f,
            0.9925f, 0.9928f, 0.9931f, 0.9933f, 0.9936f, 0.9939f, 0.9942f,
            0.9944f, 0.9947f, 0.9950f, 0.9953f, 0.9955f, 0.9958f, 0.9961f,
            0.9964f, 0.9966f, 0.9969f, 0.9972f, 0.9975f, 0.9977f, 0.9979f,
            0.9981f, 0.9982f, 0.9983f, 0.9984f, 0.9986f, 0.9987f, 0.9988f,
            0.9989f, 0.9991f, 0.9992f, 0.9993f, 0.9994f, 0.9995f, 0.9995f,
            0.9996f, 0.9996f, 0.9997f, 0.9997f, 0.9997f, 0.9998f, 0.9998f,
            0.9998f, 0.9999f, 0.9999f, 1.0000f, 1.0000f
        )
    }
}

abstract class LookupTableInterpolator protected constructor(
    private val mValues: FloatArray
) : Interpolator {

    private val mStepSize: Float = 1f / (mValues.size - 1)

    override fun getInterpolation(input: Float): Float {
        if (input >= 1.0f) {
            return 1.0f
        }
        if (input <= 0f) {
            return 0f
        }

        // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
        // we lerp (linearly interpolate) in the return statement
        val position = (input * (mValues.size - 1)).toInt().coerceAtMost(mValues.size - 2)

        // Calculate values to account for small offsets as the lookup table has discrete values
        val quantized = position * mStepSize
        val diff = input - quantized
        val weight = diff / mStepSize

        // Linearly interpolate between the table values
        return mValues[position] + weight * (mValues[position + 1] - mValues[position])
    }
}
