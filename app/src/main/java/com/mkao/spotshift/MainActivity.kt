package com.mkao.spotshift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkao.spotshift.ui.theme.SpotShiftTheme
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainUI()

        }
    }
}

@Composable
fun MainUI(modifier: Modifier = Modifier) {
    var  points by remember {
        mutableStateOf(0)
    }
    var istimerrunning by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp,end = 5.dp,start =5.dp)) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "points : $points",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
                )
            Button(onClick = {
                istimerrunning = !istimerrunning
                points = 0}) {

                Text(text =  if (istimerrunning) "Reset" else "Start")

            }
            Timer(istimerOn = istimerrunning){
                istimerrunning = false

            }
        }
        Shiftball(enabled = istimerrunning){
            points ++
        }


    }
}
@Composable
fun Timer(
    time: Int = 30000,
    istimerOn: Boolean = false,
    onTimeOff: () -> Unit = {}
) {
    var curTime by remember {
        mutableStateOf(time)
    }

    LaunchedEffect(key1 = curTime, key2 = istimerOn) {
        if (!istimerOn) {
            curTime = time
            return@LaunchedEffect
        }
        if (curTime > 0) {
            delay(1000)
            curTime -= 1000
        } else {
            onTimeOff()
        }
    }

    Text(
        text = if (istimerOn) (curTime / 1000).toString() else (time / 1000).toString(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Shiftball(radius: Float = 100f,enabled : Boolean = false, onClick:()->Unit = {}){
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val colors = listOf(
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.Magenta,
            Color.Cyan,
            Color.DarkGray,
            Color.Gray
        )

        var ballColor by remember {
            mutableStateOf(colors.random())
        }

        var  ballposition by remember {
            mutableStateOf(randomOffset(
                radius = radius,
                width = constraints.maxWidth,
                height = constraints.maxHeight))
        }

        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(enabled) {
                    if (!enabled) {
                        return@pointerInput
                    }
                    detectTapGestures {
                        val distance = sqrt(
                            (it.x - ballposition.x).pow(2) + (it.y - ballposition.y).pow(2)
                        )
                        if (distance <= radius) {
                            ballposition = randomOffset(
                                radius = radius,
                                width = constraints.maxWidth,
                                height = constraints.maxHeight
                            )
                            onClick()
                            ballColor = colors.random()
                        }
                    }

                }){
            drawCircle(
                color = ballColor,
                radius = radius,
                center = ballposition
            )
        }
    }

}
private fun randomOffset (radius: Float,width:Int,height:Int) :Offset{
return Offset(
    x = Random.nextInt(radius.roundToInt(),width-radius.roundToInt()).toFloat(),
    y = Random.nextInt(radius.roundToInt(),height-radius.roundToInt()).toFloat()
)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotShiftTheme {
        MainUI()
    }
}