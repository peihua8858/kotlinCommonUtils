# Kotlin Android Util
kotlinCommonUtils is a Kotlin tool library that simplifies Android development, making the code more concise and readable.

[简体中文🇨🇳](README.md)

[![Jitpack](https://jitpack.io/v/peihua8858/kotlinCommonUtils.svg)](https://github.com/peihua8858)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen.svg)](https://github.com/peihua8858)
[![Star](https://img.shields.io/github/stars/peihua8858/kotlinCommonUtils.svg)](https://github.com/peihua8858/kotlinCommonUtils)


## Contents
-[Latest version](https://github.com/peihua8858/kotlinCommonUtils/releases/tag/1.1.1-beta32)<br>
-[Download](#Download)<br>
-[Usage](#Usage)<br>
-[Permission](#Permission)<br>
-[Issues](https://github.com/peihua8858/kotlinCommonUtils/wiki/%E5%A6%82%E4%BD%95%E6%8F%90Issues%3F)<br>
-[License](#License)<br>


## Download

Use Gradle

```sh
repositories {
  google()
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  // KotlinCommonUtils
  implementation 'com.github.peihua8858:kotlinCommonUtils:1.1.1-beta32'
}
```

Or Maven:

```xml
<dependency>
  <groupId>com.github.peihua8858</groupId>
  <artifactId>kotlinCommonUtils</artifactId>
  <version>1.1.1-beta32</version>
</dependency>
```
## Permission
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## Usage

A simple use case is shown below:

1、Check String is null

```kotlin
import com.fz.common.text.isNonEmpty
val spu:String? =null 
var result:String=""
if (spu.isNonEmpty()) {
    result = spu
}
```

2、Check List or Map is nonNull
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
3、Permission DSL
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
4、use ContentProvider save image to sdcard
```kotlin
import com.fz.common.utils.saveImageToGallery
val imageFile = File("D://images/5.jpg")
context.saveImageToGallery(imageFile, imageFile.name)
```
5、ViewModel use coroutine
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
//Activity observe
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

//Activity observe
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
6、Network status
```kotlin
//kotlin  or java
import com.fz.common.network.NetworkUtil
if (NetworkUtil.isConnected(context, true)) {
     showToast("Internet connection.")
}else{
    showToast("Disconnected from the network. ")
}
```
7、View animation
```kotlin
import com.fz.common.view.utils.animateIn
import com.fz.common.view.utils.animateOut
//Enter animation
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
//Exit animation
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
//Other animations such as: transparency animation (View.animateAlpha()), width expansion and folding (View.animationWidth), etc.
```
8、Activity or Fragment calls kotlin coroutine
```kotlin
import com.fz.common.utils.apiWithAsyncCreated
Activity/Fragment.apiWithAsyncCreated<T>{
    onStart{
       //Before network request
       //todo
    }
    onRequest{
        //Make a network request
        //todo  
    }
    onResponse{
        // Network request successful
        //todo   
    }
    onError{
        //Network request failed
       //todo    
    }
    onComplete{
        //Network request completed
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
