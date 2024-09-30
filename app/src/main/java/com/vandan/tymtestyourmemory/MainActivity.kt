package com.vandan.tymtestyourmemory

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vandan.tymtestyourmemory.ui.theme.TYMTestYourMemoryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TYMTestYourMemoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var message by remember {
        mutableStateOf("")
    }

    var winningCount by remember {
        mutableIntStateOf(0)
    }

    var arr by remember {
        mutableStateOf(ArrayList<Int>())
    }

    var resArr by remember {
        mutableStateOf(ArrayList<Int>())
    }

    var clicked1 by remember {
        mutableStateOf(false)
    }

    var clicked2 by remember {
        mutableStateOf(false)
    }

    var clicked3 by remember {
        mutableStateOf(false)
    }

    var clicked4 by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column {
            Text(
                text = "TEST YOUR MEMORY",
                modifier=Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )
            Row {
                ColorBlock(Color.Red,Color.Gray,{GlobalScope.launch(Dispatchers.Main){ resArr.add(1);checkCondition(arr,resArr,{clicked1 = !clicked1},{clicked2 = !clicked2},{clicked3 = !clicked3},{clicked4 = !clicked4},{message = it},1,{winningCount++},{winningCount = 0})}},clicked1)
                ColorBlock(Color.Blue,Color.Gray,{GlobalScope.launch(Dispatchers.Main){ resArr.add(2);checkCondition(arr,resArr,{clicked1 = !clicked1},{clicked2 = !clicked2},{clicked3 = !clicked3},{clicked4 = !clicked4},{message = it},2,{winningCount++},{winningCount = 0})}},clicked2)
            }
            Text(
                text = "Winning Count : $winningCount",
                modifier=Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )
            Row {
                ColorBlock(Color.Green,Color.Gray,{GlobalScope.launch(Dispatchers.Main) { resArr.add(3);checkCondition(arr,resArr,{clicked1 = !clicked1},{clicked2 = !clicked2},{clicked3 = !clicked3},{clicked4 = !clicked4},{message = it},3,{winningCount++},{winningCount = 0})}},clicked3)
                ColorBlock(Color.Yellow,Color.Gray,{GlobalScope.launch(Dispatchers.Main) { resArr.add(4);checkCondition(arr,resArr,{clicked1 = !clicked1},{clicked2 = !clicked2},{clicked3 = !clicked3},{clicked4 = !clicked4},{message = it},4,{winningCount++},{winningCount = 0})}},clicked4)
            }
            Button(onClick = {
                GlobalScope.launch (Dispatchers.Main){
                    onStart(
                        resArr=resArr,
                        arr=arr,
                        onClicked1 = {clicked1 = !clicked1},
                        onClicked2 = {clicked2 = !clicked2},
                        onClicked3 = {clicked3 = !clicked3},
                        onClicked4 = {clicked4 = !clicked4},
                        messageChange = {message = it}
                        )
                }
            }){
                Text(
                    text = "START TEST",
                    modifier=Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Text(
                text = message,
                modifier=Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

suspend fun checkCondition(arr: ArrayList<Int>, resArr: ArrayList<Int>, onClicked1: () -> Unit, onClicked2: () -> Unit, onClicked3: () -> Unit, onClicked4: () -> Unit,messageChange:(String)->Unit, i: Int,increseWinningCount:()->Unit,resetWinningCount:()->Unit){

    when(i){
        1->{onClicked1();delay(200);onClicked1();delay(200)}
        2->{onClicked2();delay(200);onClicked2();delay(200)}
        3->{onClicked3();delay(200);onClicked3();delay(200)}
        4->{onClicked4();delay(200);onClicked4();delay(200)}
    }
    messageChange("Checking")
    try {
        if (resArr.size <= arr.size) {
            for (i in 0 until resArr.size) {
                if (resArr[i] != arr[i]) {
                    resetWinningCount()
                    messageChange("You Failed Restart The Test")
                    return
                }
            }
            if (resArr.equals(arr)) {
                increseWinningCount()
                delay(500)
                onResume(resArr, arr, onClicked1, onClicked2, onClicked3, onClicked4, messageChange)
            }
        } else {
            resetWinningCount()
            messageChange("You Failed Restart The Test")
        }
    }catch (e:Exception){
        resetWinningCount()
        messageChange("You Failed Restart The Test")
    }
}

suspend fun onStart(resArr: ArrayList<Int>,arr: ArrayList<Int>,onClicked1:()->Unit,onClicked2:()->Unit,onClicked3:()->Unit,onClicked4:()->Unit,messageChange:(String)->Unit) {
    messageChange("New Test Started")
    resArr.clear()
    arr.clear()
    var randomInt = Random.nextInt(1,5)
    arr.add(randomInt)
    Log.d("Vandan","$arr")
    for(i in arr){
        when(i){
            1->{onClicked1();delay(500);onClicked1();delay(200)}
            2->{onClicked2();delay(500);onClicked2();delay(200)}
            3->{onClicked3();delay(500);onClicked3();delay(200)}
            4->{onClicked4();delay(500);onClicked4();delay(200)}
        }
    }
}

suspend fun onResume(resArr: ArrayList<Int>,arr: ArrayList<Int>,onClicked1:()->Unit,onClicked2:()->Unit,onClicked3:()->Unit,onClicked4:()->Unit,messageChange:(String)->Unit) {
    messageChange("Test Resumed")
    resArr.clear()
    var randomInt = Random.nextInt(1,5)
    arr.add(randomInt)
    Log.d("Vandan","$arr"+resArr)
    for(i in arr){
        when(i){
            1->{onClicked1();delay(500);onClicked1();delay(500)}
            2->{onClicked2();delay(500);onClicked2();delay(500)}
            3->{onClicked3();delay(500);onClicked3();delay(500)}
            4->{onClicked4();delay(500);onClicked4();delay(500)}
        }
    }
}



@Composable
fun ColorBlock(nonClickedColor:Color,clickedColor: Color,onClick:()->Unit,clicked:Boolean) {
    Button(
        onClick = {onClick()},
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(clicked)clickedColor else nonClickedColor
        )
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TYMTestYourMemoryTheme {
        Greeting("Android")
    }
}