package com.hutong.relax.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "主线程id：${mainLooper.thread.id}")
//        GlobalScope.launch {  } 开启一个协程
//        val job = GlobalScope.launch {
//            delay(6000)
//            Log.e(TAG, "协程执行结束-- 线程Id：${Thread.currentThread().id}")
//        }

//        asyncDemo()
//        asyncDemo()
        //getResult2()


//        syncFindBook1()
        syncFindBook2()
        Log.d(TAG, "main Thread finished!")
    }

    fun asyncDemo() {
        val res2 = GlobalScope.async {
            var res = getResult2()
            Log.e(TAG, "async {}的方法 ${res}")
            return@async
        }
        Log.e(TAG, "result= $res2 ,asyncDemo.threadid=${Thread.currentThread().id}")
    }

    fun syncFindBook2() {
        GlobalScope.launch {
            var result = findBook2(
                "实习生小李",
                {
                    Log.d(
                        TAG,
                        it + " onError threadid= ${Thread.currentThread().id}"
                    )
                })  // 异步数据,像同步代码一样return了
            if (!result) {
                result =
                    findBook2(
                        "老员工老王",
                        { Log.d(TAG, it + " onError threadid= ${Thread.currentThread().id}") })
            }
            if (!result) {
                result =
                    findBook2(
                        "领班老赵",
                        { Log.d(TAG, it + " onError  threadid= ${Thread.currentThread().id}") })
            }
            if (!result) {
                result =
                    findBook2(
                        "仓库管理员",
                        { Log.d(TAG, it + " onError  threadid= ${Thread.currentThread().id}") })
            }
            if (!result) {
                result =
                    findBook2(
                        "扫地僧",
                        { Log.d(TAG, it + " onError  threadid= ${Thread.currentThread().id}") })
            }
        }
    }

    private suspend fun getResult2(): Int {
        delay(4000)
        Log.e(TAG, "suspend:getResult2() = ${2} getResult2.threadid=${Thread.currentThread().id}")
        return 2
    }

    suspend fun findBook2(name: String, onError: (String) -> Unit): Boolean {
        return GlobalScope.async() {
            Log.d(TAG, "我是 $name 我开始找书,threadid= ${Thread.currentThread().id}")
            val random = Random()
            //产生一个0到9的随机数
            val seed = random.nextInt(10)
            //找书的时间是一个1-9秒的随机数
            Thread.sleep((1000 * seed).toLong())
            if (seed < 0) {//模拟一个找不到书的大概率事件
                Log.d(TAG, "${TAG} 找到了,threadid= ${Thread.currentThread().id}")
                true  //lamdba表达式 true是返回值代表找到了书
            } else {
                onError("啊,${name} 没找到书啊,threadid= ${Thread.currentThread().id}");
                false  //没有找到书
            }
        }.await()
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName + "----"
    }
}