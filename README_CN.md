# Kotlin Android Util
kotlinCommonUtils是一个Kotlin工具库，可以简化Android开发，使代码更加简洁和可读。

[English](README.md)

[![Jitpack](https://jitpack.io/v/peihua8858/kotlinCommonUtils.svg)](https://github.com/peihua8858)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen.svg)](https://github.com/peihua8858)
[![Star](https://img.shields.io/github/stars/peihua8858/kotlinCommonUtils.svg)](https://github.com/peihua8858/kotlinCommonUtils)


## Contents
-[最新版本](https://github.com/peihua8858/kotlinCommonUtils/releases/tag/1.1.1-beta32)<br>
-[如何引用](#Download)<br>
-[进阶使用](#Usage)<br>
-[权限](#Permission)<br>
-[如何提Issues](https://github.com/peihua8858/PictureSelector/wiki/%E5%A6%82%E4%BD%95%E6%8F%90Issues%3F)<br>
-[License](#License)<br>


## 如何引用

使用 Gradle

```sh
repositories {
  google()
  mavenCentral()
}

dependencies {
  // KotlinCommonUtils
  implementation 'com.github.peihua8858:kotlinCommonUtils:1.1.1-beta32'
}
```

或者 Maven:

```xml
<dependency>
  <groupId>com.github.peihua8858</groupId>
  <artifactId>kotlinCommonUtils</artifactId>
  <version>1.1.1-beta32</version>
</dependency>
```
## 权限
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## 进阶使用

一个简单的用例如下所示：

1、判断字符串是否为空

```kotlin
import com.fz.common.text.isNonEmpty
val spu:String? =null 
var result:String=""
if (spu.isNonEmpty()) {
    result = spu
}
```

2、判断List、Map或者Array 是否为空
```kotlin
//List
import com.fz.common.collections.isNonEmpty
//Array 
import com.fz.common.array.isNonEmpty

val list:List<String>? =null 
var result:List<String> = arrayListOf()
if (list.isNonEmpty()) {
    result = list
}

//Map 
import com.fz.common.map.isNonEmpty
val map:Map<String,String>? =null 
var result:Map<String,String> = hashMapOf()
if (map.isNonEmpty()) {
    result = map
}
```
3、权限 DSL用法
```kotlin
import com.fz.common.permissions.requestPermissionsDsl
requestPermissionsDsl(Manifest.permission.POST_NOTIFICATIONS) {
    onDenied {
         showToast("Denied")
    }
    onNeverAskAgain {
         showToast("Never ask again")
    }
    onGranted {
        showToast("Granted")
    }
}
```
4、使用ContentProvider保存图片文件到sd卡
```kotlin
import com.fz.common.utils.saveImageToGallery
val imageFile = File("D://images/5.jpg")
context.saveImageToGallery(imageFile, imageFile.name)
```
5、ViewModel协程用法
```kotlin
import com.fz.common.model.ViewModelState
class BottomTopMatchViewModel : ViewModel() {
    val matchTypeState: MutableLiveData<ViewModelState<SwimmingType>> =
    MutableLiveData()
    fun requestGetMatchType(goodsId: String) {
        request(matchTypeState) {
            val param = RequestParam()
            param.put("goods_id", goodsId)
            val httpResponse = ApiManager.productApi().requestGetMatchType(param.createRequestBody())
            if (httpResponse.isSuccessFull()) {
                val result = httpResponse.result
                if (result != null) {
                    return@request result
                }
            }
            throw NullPointerException("no data.")
          }
      }
      
 val matchTypeState2: MutableLiveData<ResultData<SwimmingType>> =
    MutableLiveData()
    fun requestGetMatchType(goodsId: String) {
        request(matchTypeState2) {
            val param = RequestParam()
            param.put("goods_id", goodsId)
            val httpResponse = ApiManager.productApi().requestGetMatchType(param.createRequestBody())
            if (httpResponse.isSuccessFull()) {
                val result = httpResponse.result
                if (result != null) {
                    return@request result
                }
            }
            throw NullPointerException("no data found.")
       }
    }
}
//Activity 监听
viewModel.matchTypeState.observe(this) {
    if (it.isStarting()) {
        showLoadingView()
    } else if (it.isSuccess()) {
        val swimmingType = it.data
       //....
    } else if (it.isError()) {
        showErrorView()
    }
}

//Activity 监听
viewModel.matchTypeState2.observe(this) {
    if (it.isStarting()) {
        showLoadingView()
    } else if (it.isSuccess()) {
        val swimmingType = it.data
       //....
    } else if (it.isError()) {
        showErrorView()
    }
}
```
6、网络状态
```kotlin
//kotlin  or java
import com.fz.common.network.NetworkUtil
if (NetworkUtil.isConnected(context, true)) {
     showToast("Internet connection.")
}else{
    showToast("Disconnected from the network. ")
}
```
7、视图动画
```kotlin
import com.fz.common.view.utils.animateIn
import com.fz.common.view.utils.animateOut
//进入动画
View.animateIn(true){
    onAnimationStart{
            //todo
    }
    onAnimationEnd{
          //todo
    }
    onAnimationCancel{
           //todo
    }
    onAnimationPause{
           //todo
    }
}
//退出动画
View.animateOut(true){
    onAnimationStart{
           //todo
    }
    onAnimationEnd{
          //todo
    }
    onAnimationCancel{
            //todo
    }
    onAnimationPause{
           //todo
    }
}
//其他动画如：透明度动画（View.animateAlpha()）、宽度展开折叠(View.animationWidth)等
```
8、Activity or Fragment 协程用法
```kotlin
import com.fz.common.utils.apiWithAsyncCreated
Activity/Fragment.apiWithAsyncCreated<T>{
    onStart{
       //网络请求前
       //todo
    }
    onRequest{
        //发起网络请求
        //todo  
    }
    onResponse{
        // 网络请求成功
        //todo   
    }
    onError{
        //网络请求失败
       //todo    
    }
    onComplete{
        //网络请求完成
        //todo    
    }
}
```
## License

```sh
Copyright 2023 peihua

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
