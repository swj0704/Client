package com.example.client.start

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.client.MainActivity
import com.example.client.R
import com.example.client.api.DTO.GetResponse
import com.example.client.api.DTO.UserResponse
import com.example.client.api.RetrofitHelper
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnGoSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val sweetAlertDialog =
                SweetAlertDialog(this@LoginActivity, SweetAlertDialog.PROGRESS_TYPE)
            sweetAlertDialog.progressHelper.barColor = Color.parseColor("#0DE930")
            sweetAlertDialog
                .setTitleText("로그인 중")
                .setCancelable(false)
            sweetAlertDialog.show()
            RetrofitHelper().getGetAPI().getUser(email = IDText.text.toString(), password = passwordText.text.toString()).enqueue(object : Callback<GetResponse<UserResponse>> {
                override fun onResponse(
                    call: Call<GetResponse<UserResponse>>,
                    response: Response<GetResponse<UserResponse>>
                ) {
                    if(response.isSuccessful){
                        if(response.body()!!.result.size > 0){
                            sweetAlertDialog.dismiss()

                            val dialog = SweetAlertDialog(this@LoginActivity, SweetAlertDialog.SUCCESS_TYPE)

                            dialog.setCancelable(false)

                            dialog.setTitleText("로그인이 완료 되었습니다")
                                .setConfirmClickListener {
                                    dialog.dismiss()
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("id",response.body()!!.result[0].id)
                                    startActivity(intent)
                                    finish()
                                }
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<GetResponse<UserResponse>>, t: Throwable) {
                    showFailDialog(sweetAlertDialog)
                }

            })


        }
    }
    fun showFailDialog(sweetAlertDialog: SweetAlertDialog){
        sweetAlertDialog.dismiss()

        val dialog = SweetAlertDialog(this@LoginActivity, SweetAlertDialog.SUCCESS_TYPE)

        dialog.setCancelable(false)

        dialog.setTitleText("로그인에 실패하였습니다")
            .setConfirmClickListener {
                dialog.dismiss()
            }
            .show()
    }
}