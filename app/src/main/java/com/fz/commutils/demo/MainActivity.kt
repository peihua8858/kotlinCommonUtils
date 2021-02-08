package com.fz.commutils.demo

import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.fz.common.network.NetworkUtil
import com.fz.common.utils.*
import com.fz.common.view.utils.setAfterTextChanged
import com.fz.toast.ToastCompat
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 测试验证activity
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val checkbox: CheckBox = findViewById(R.id.checkbox)
        newFragment(TestFragment::class.java,null)
//        checkbox.isChecked.toBoolean {
//            KLog.d("result>>>isChecked：$it")
//        }
//        editText.setAfterTextChanged {
//            ToastCompat.showMessage(this, it)
//        }
//        textView.toString {
//            KLog.d("result>>>toString：$it")
//        }
        textView.setOnClickListener {
            if (NetworkUtil.isConnected(this,true)) {
                KLog.d("result>>>yes>>有网络")
            }
//            null.copyToClipBoard({
//                "sdffff"
//            }, {
//                ToastCompat.showMessage(this, if (it) "拷贝成功" else "拷贝失败")
//            })
        }
//        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            val result = isChecked.yes {
//                KLog.d("result>>>yes>>isChecked：$isChecked")
//                return@yes buttonView
//            }
//            result.otherwise {
//                KLog.d("result>>>yes>>result：${result}")
//            }
//            val result1 =  isChecked.no {
//                KLog.d("result>>>no>>isChecked：$isChecked")
//                return@no buttonView
//            }
//            result1.otherwise {
//                KLog.d("result>>>no>>result1：${result1}")
//            }
//        }
    }
}