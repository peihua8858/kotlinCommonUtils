package com.fz.commutils.demo.api

import androidx.annotation.NonNull
import com.fz.commutils.demo.model.HttpResponse
import com.fz.commutils.demo.model.PointResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/*

                  _ooOoo_
                 o8888888o
                 88" . "88
                 (| -_- |)
                 O\  =  /O
              ____/`---'\____
            .'  \\|     |//  `.
           /  \\|||  :  |||//  \
          /  _||||| -:- |||||-  \
          |   | \\\  -  /// |   |
          | \_|  ''\---/''  |   |
          \  .-\__  `-`  ___/-. /
        ___`. .'  /--.--\  `. . __
     ."" '<  `.___\_<|>_/___.'  >'"".
    | | :  `- \`.;`\ _ /`;.`/ - ` : | |
    \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                  `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        佛祖保佑       永无BUG

*/ /**
 * 项目名称: ZAFUL
 * 类描述:
 * 用户api
 *
 * @author: create by linjinyan
 * 创建时间:2018/9/25 9:19
 */
interface UserApi {


    /**
     * 获取积分列表
     *
     * @param request
     * @return
     */
    @POST("user/get_points_record")
    suspend fun requestPoints(@NonNull @Body request: RequestBody): HttpResponse<PointResponse?>?
}