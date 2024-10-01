package com.example.kotlin_flows_context_preservation_exception_handeling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlin_flows_context_preservation_exception_handeling.ui.theme.Kotlin_flows_context_preservation_exception_handelingTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlin_flows_context_preservation_exception_handelingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                   // collector()
                    exceptionHandelingCollector()

                }
            }
        }
    }
}


fun producer():Flow<Int>{
    return flow {
         for (i in 1..10){
             delay(1000)
             Log.d("kotlinflowscontext", "producer${Thread.currentThread()}")
             emit(i)
             throw Exception("Error in producer")
         }
    }.catch {
        //also we can call a catch here it will give us extra functionality
        //like if we get an exception based on that exception we can emit another value
        //like:
        emit(-1)
    }
}

//fun collector(){
//    CoroutineScope(Dispatchers.Main).launch {
//        producer()
//            .map {
//                it*2
//                Log.d("kotlinflowscontext", "map:${Thread.currentThread()}")
//            }
//           .flowOn(Dispatchers.IO)



            /*when we have to switch context we have to tell flow the context with flowOn function
           //above protion of flowOn will run in another context and below protion of flowOn will run in main thread
           //like map will run in another thread and collect will run in main thread.
           //we can also use multiple flowOn*/



//          .collect{
               //Log.d("kotlinflowscontext","$it")
//            Log.d("kotlinflowscontext", "consumer:${Thread.currentThread()}")
//        }
//    }
//}

fun exceptionHandelingCollector(){
    CoroutineScope(Dispatchers.Main).launch {

        try {
            producer().collect{
                Log.d("kotlinflowscontext", "$it")
               // throw Exception("Error in consumer")
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("kotlinflowscontext", e.message.toString())
        }

        //we can handle exception both form emiter(producer) and collector(consumer) form here

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kotlin_flows_context_preservation_exception_handelingTheme {

    }
}