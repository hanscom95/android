package com.artech.countdown

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.artech.countdown.utility.DBManager
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.content_setting.*
import kotlinx.android.synthetic.main.popup_setting.view.*
import java.util.*

class SettingActivity : AppCompatActivity() {

    val mDbManager: DBManager = DBManager(this, "count.db", null, 1)

    var arrow = -1
    var time = -1
    var round = -1
    var withdraw = -1

    var dbCount = -1

    var mSpeaker: Int = -1
    var mFont: Int = -1
    var mBackground: Int = -1
    var mLanguage: Int = -1

    var customBoolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbCount = mDbManager.selectCount()
        if(dbCount > 0) {
            val cursor: Cursor = mDbManager.selectSetting()
            while (cursor.moveToNext()) {
                mSpeaker = cursor.getInt(0)
                mFont = cursor.getInt(1)
                mBackground = cursor.getInt(2)
                mLanguage = cursor.getInt(3)
            }
        }


        val newConfig = Configuration()
        if(mLanguage == 0) {
            newConfig.locale = Locale.KOREAN
        }else if(mLanguage == 1) {
            newConfig.locale = Locale.ENGLISH
        }else if(mLanguage == 2) {
            newConfig.locale = Locale.JAPANESE
        }else if(mLanguage == 3) {
            newConfig.locale = Locale.CHINESE
        }
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics())


        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
        toolbar.setTitle("SETTING")
        toolbar.setLogo(R.mipmap.icon_timer)
        toolbar.title = "TIMER"
        toolbar.titleMarginStart = 43

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/NotoSansCJKkr_Bold.otf")
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/NotoSansCJKkr_Regular.otf")
        val typefaceRobotoBold = Typeface.createFromAsset(assets, "fonts/Roboto_Bold.otf")

        shootoff_text.setTypeface(typefaceRobotoBold)
        language_text.setTypeface(typefaceRobotoBold)
        app_info_text.setTypeface(typefaceRobotoBold)


        first_arrow_value_text.setTypeface(typefaceBold)
        first_arrow_title_text.setTypeface(typefaceBold)
        first_arrow_check_text.setTypeface(typefaceBold)

        second_arrow_value_text.setTypeface(typefaceBold)
        second_arrow_title_text.setTypeface(typefaceBold)
        second_arrow_check.setTypeface(typefaceBold)

        first_time_value_text.setTypeface(typefaceBold)
        first_time_title_text.setTypeface(typefaceBold)
        first_time_check.setTypeface(typefaceBold)

        second_time_value_text.setTypeface(typefaceBold)
        second_time_title_text.setTypeface(typefaceBold)
        second_time_check.setTypeface(typefaceBold)

        first_round_value_text.setTypeface(typefaceBold)
        first_round_title_text.setTypeface(typefaceBold)
        first_round_text.setTypeface(typefaceBold)
        first_round_check.setTypeface(typefaceBold)

        second_round_value_text.setTypeface(typefaceBold)
        second_round_title_text.setTypeface(typefaceBold)
        second_round_text.setTypeface(typefaceBold)
        second_round_check.setTypeface(typefaceBold)

        first_withdraw_value_text.setTypeface(typefaceBold)
        first_withdraw_title_text.setTypeface(typefaceBold)
        first_withdraw_check.setTypeface(typefaceBold)

        second_withdraw_value_text.setTypeface(typefaceBold)
        second_withdraw_title_text.setTypeface(typefaceBold)
        second_withdraw_check.setTypeface(typefaceBold)

        third_withdraw_value_text.setTypeface(typefaceBold)
        third_withdraw_title_text.setTypeface(typefaceBold)
        third_withdraw_check.setTypeface(typefaceBold)

        fourth_withdraw_value_text.setTypeface(typefaceBold)
        fourth_withdraw_title_text.setTypeface(typefaceBold)
        fourth_withdraw_check.setTypeface(typefaceBold)


        user_value_text.setTypeface(typefaceRegular)
        user_title_text.setTypeface(typefaceBold)

        info_text.setTypeface(typefaceBold)

        shootoff_text.setOnClickListener {
//            val intent = Intent(this, CountActivity::class.java)
//            intent.putExtra("arrow", 1)
//            intent.putExtra("time", 60)
//            intent.putExtra("round", 2)
//            intent.putExtra("withdraw", 60)
//            startActivity(intent)

            var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setSingleChoiceItems(resources.getStringArray(R.array.shoot_off), -1, DialogInterface.OnClickListener { dialogInterface, i ->
                val intent = Intent(this, ShootOffActivity::class.java)
                if(i == 0) {
                    intent.putExtra("time", 20)
                }else if(i == 1) {
                    intent.putExtra("time", 40)
                }else if(i == 2) {
                    intent.putExtra("time", 60)
                }

                intent.putExtra("arrow", 1)
                intent.putExtra("round", 1)
                intent.putExtra("withdraw", -1)

                dialogInterface.dismiss()
                startActivity(intent)

            }).show()

        }

        language_text.setOnClickListener {
            var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setSingleChoiceItems(resources.getStringArray(R.array.language), mLanguage, DialogInterface.OnClickListener { dialogInterface, i ->

                val newConfig = Configuration()
                if(i == 0) {
                    newConfig.locale = Locale.KOREAN
                }else if(i == 1) {
                    newConfig.locale = Locale.ENGLISH
                }else if(i == 2) {
                    newConfig.locale = Locale.JAPANESE
                }else if(i == 3) {
                    newConfig.locale = Locale.CHINESE
                }


                var arrayList = ArrayList<Int>()
                arrayList.add(mSpeaker)
                arrayList.add(mFont)
                arrayList.add(mBackground)
                arrayList.add(i)

                if (dbCount > 0) {
                    mDbManager.updateUser(arrayList)
                } else {
                    mDbManager.insertSetting(arrayList)
                }

                startActivity(getIntent())
                finish()

            }).show()
        }


        first_arrow_layout.setOnClickListener(View.OnClickListener {
            first_arrow_layout.setBackgroundResource(R.mipmap.btn_arrow_press)
            first_arrow_check_img.visibility = View.VISIBLE

            second_arrow_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            second_arrow_check_img.visibility = View.INVISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

            arrow = 3

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        second_arrow_layout.setOnClickListener(View.OnClickListener {
            first_arrow_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            first_arrow_check_img.visibility = View.INVISIBLE

            second_arrow_layout.setBackgroundResource(R.mipmap.btn_arrow_press)
            second_arrow_check_img.visibility = View.VISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

            arrow = 6

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })



        first_time_layout.setOnClickListener(View.OnClickListener {
            first_time_layout.setBackgroundResource(R.mipmap.btn_time_press)
            first_time_check_img.visibility = View.VISIBLE

            second_time_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            second_time_check_img.visibility = View.INVISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

            time = 120

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        second_time_layout.setOnClickListener(View.OnClickListener {
            first_time_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            first_time_check_img.visibility = View.INVISIBLE

            second_time_layout.setBackgroundResource(R.mipmap.btn_time_press)
            second_time_check_img.visibility = View.VISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

            time = 240

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })



        first_round_layout.setOnClickListener(View.OnClickListener {
            first_round_layout.setBackgroundResource(R.mipmap.btn_round_press)
            first_round_check_img.visibility = View.VISIBLE

            second_round_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            second_round_check_img.visibility = View.INVISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

            round = 8

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        second_round_layout.setOnClickListener(View.OnClickListener {
            first_round_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            first_round_check_img.visibility = View.INVISIBLE

            second_round_layout.setBackgroundResource(R.mipmap.btn_round_press)
            second_round_check_img.visibility = View.VISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

            round = 14

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })



        first_withdraw_layout.setOnClickListener(View.OnClickListener {
            first_withdraw_layout.setBackgroundResource(R.mipmap.btn_retrieve_press)
            first_withdraw_check_img.visibility = View.VISIBLE

            second_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            second_withdraw_check_img.visibility = View.INVISIBLE

            third_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            third_withdraw_check_img.visibility = View.INVISIBLE

            fourth_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            fourth_withdraw_check_img.visibility = View.INVISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

//            withdraw = 300
            withdraw = 60

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        second_withdraw_layout.setOnClickListener(View.OnClickListener {
            first_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            first_withdraw_check_img.visibility = View.INVISIBLE

            second_withdraw_layout.setBackgroundResource(R.mipmap.btn_retrieve_press)
            second_withdraw_check_img.visibility = View.VISIBLE

            third_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            third_withdraw_check_img.visibility = View.INVISIBLE

            fourth_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            fourth_withdraw_check_img.visibility = View.INVISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

//            withdraw = 600
//            withdraw = 180
            withdraw = 120

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        third_withdraw_layout.setOnClickListener(View.OnClickListener {
            first_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            first_withdraw_check_img.visibility = View.INVISIBLE

            second_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            second_withdraw_check_img.visibility = View.INVISIBLE

            third_withdraw_layout.setBackgroundResource(R.mipmap.btn_retrieve_press)
            third_withdraw_check_img.visibility = View.VISIBLE

            fourth_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            fourth_withdraw_check_img.visibility = View.INVISIBLE

//            withdraw = 900
//            withdraw = 300
            withdraw = 180

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        fourth_withdraw_layout.setOnClickListener(View.OnClickListener {
            first_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            first_withdraw_check_img.visibility = View.INVISIBLE

            second_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            second_withdraw_check_img.visibility = View.INVISIBLE

            third_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            third_withdraw_check_img.visibility = View.INVISIBLE

            fourth_withdraw_layout.setBackgroundResource(R.mipmap.btn_retrieve_press)
            fourth_withdraw_check_img.visibility = View.VISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

//            withdraw = 1200
//            withdraw = 600
            withdraw = 240

            if(customBoolean) {
                customBoolean = false
                time = -1
                round = -1
                withdraw = -1
            }

            if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                val intent = Intent(this, CountActivity::class.java)
                intent.putExtra("arrow", arrow)
                intent.putExtra("time", time)
                intent.putExtra("round", round)
                intent.putExtra("withdraw", withdraw)
                startActivity(intent)
            }
        })

        user_setting_layout.setOnClickListener {
            first_arrow_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            first_arrow_check_img.visibility = View.INVISIBLE

            second_arrow_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            second_arrow_check_img.visibility = View.INVISIBLE

            first_time_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            first_time_check_img.visibility = View.INVISIBLE

            second_time_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
            second_time_check_img.visibility = View.INVISIBLE

            first_round_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            first_round_check_img.visibility = View.INVISIBLE

            second_round_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            second_round_check_img.visibility = View.INVISIBLE

            first_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            first_withdraw_check_img.visibility = View.INVISIBLE

            second_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            second_withdraw_check_img.visibility = View.INVISIBLE

            third_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            third_withdraw_check_img.visibility = View.INVISIBLE

            fourth_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
            fourth_withdraw_check_img.visibility = View.INVISIBLE

            user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_press)

            val inflater =  layoutInflater
            val dialogView = inflater.inflate(R.layout.popup_setting, null)

            var builder = AlertDialog.Builder(this)

            builder.setView(dialogView)
            var alertDialog = builder.show()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.window.setLayout(607, 587)
            alertDialog.setOnDismissListener(DialogInterface.OnDismissListener {
                if(!customBoolean) {
                    time = -1
                    round = -1
                    withdraw = -1
                    user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)
                }
            })

            var pArrow = 0
            var pRound = 0
            var pTime = 0
            var pWithdraw = 0

            dialogView.arrow_up_button.setOnClickListener {
                ++pArrow
                dialogView.arrow_value_text.text = pArrow.toString()
            }


            dialogView.arrow_down_button.setOnClickListener {
                if(pArrow > 0) {
                    --pArrow
                    dialogView.arrow_value_text.text = pArrow.toString()
                }
            }


            dialogView.round_up_button.setOnClickListener {
                ++pRound
                dialogView.round_value_text.text = pRound.toString()
            }


            dialogView.round_down_button.setOnClickListener {
                if(pRound > 0) {
                    --pRound
                    dialogView.round_value_text.text = pRound.toString()
                }
            }


            dialogView.time_up_button.setOnClickListener {
                if(pTime < 3600) {
                    pTime += 5

                    var hour = pTime / 60
                    var minuite = pTime % 60
                    var hTime: String = if (hour < 10) "0" + hour else hour.toString()
                    var mTime: String = if (minuite < 10) "0" + minuite else minuite.toString()
                    dialogView.time_value_text.text = hTime + ":" + mTime
                }
                //dialogView.time_value_text.text = pTime.toString()
            }


            dialogView.time_down_button.setOnClickListener {
                if(pTime > 0) {
                    pTime -= 5
                    var hour = pTime/60
                    var minuite = pTime%60
                    var hTime:String = if(hour < 10) "0" + hour else hour.toString()
                    var mTime:String = if(minuite < 10) "0" + minuite else minuite.toString()
                    dialogView.time_value_text.text = hTime + ":" + mTime
                }
            }


            dialogView.withdraw_up_button.setOnClickListener {
                //20180919 max 삭제  if (pWithdraw < 60) {
                    //20180808 if (pWithdraw < 3600) {
                    //20180808 ++pWithdraw
                    pWithdraw +=5

                    //20180808 var hour = (pWithdraw*60)/60
                    //20180808 var minuite = (pWithdraw*60)%60
                    var hour = pWithdraw / 60
                    var minuite = pWithdraw % 60
                    var hTime:String = if(hour < 10) "0" + hour else hour.toString()
                    var mTime:String = if(minuite < 10) "0" + minuite else minuite.toString()
                    dialogView.withdraw_value_text.text = hTime + ":" + mTime
                //}
            }


            dialogView.withdraw_down_button.setOnClickListener {
                //20180808 if(pWithdraw > 0) {
                if(pWithdraw > 0) {
                    //20180808 --pWithdraw
                    pWithdraw -= 5

                    //20180808 var hour = (pWithdraw*60)/60
                    //20180808 var minuite = (pWithdraw*60)%60
                    var hour = pWithdraw / 60
                    var minuite = pWithdraw % 60
                    var hTime:String = if(hour < 10) "0" + hour else hour.toString()
                    var mTime:String = if(minuite < 10) "0" + minuite else minuite.toString()
                    dialogView.withdraw_value_text.text = hTime + ":" + mTime
                }
            }

            dialogView.confirm_layout.setOnClickListener {
                if(pArrow > 0 && pTime > 0 && pRound > 0 && pWithdraw > 0) {
                    arrow = pArrow
                    time = pTime
                    round = pRound
                    //withdraw = pWithdraw * 60
                    withdraw = pWithdraw
                    customBoolean = true


                    alertDialog.dismiss()

                    if(arrow > -1 && time > -1 && round > -1 && withdraw > -1) {
                        val intent = Intent(this, CountActivity::class.java)
                        intent.putExtra("arrow", arrow)
                        intent.putExtra("time", time)
                        intent.putExtra("round", round)
                        intent.putExtra("withdraw", withdraw)
                        startActivity(intent)
                    }
                }else {
                    Toast.makeText(this, getString(R.string.no_choice), Toast.LENGTH_SHORT).show()
                }
            }
        }

        info_layout.setOnClickListener {
            if(mLanguage == 0) {
                info_popup_img.setImageResource(R.mipmap.img_settingpopup_kr)
            }else if(mLanguage == 1) {
                info_popup_img.setImageResource(R.mipmap.img_settingpopup_eng)
            }else if(mLanguage == 2) {
                info_popup_img.setImageResource(R.mipmap.img_settingpopup_jp)
            }else if(mLanguage == 3) {
                info_popup_img.setImageResource(R.mipmap.img_settingpopup_ch)
            }

            setting_detail_layout.visibility = View.GONE
            info_layout.visibility = View.GONE


            info_popup_img.visibility = View.VISIBLE
            info_popup_text_layout.visibility = View.VISIBLE
        }

        info_popup_img.setOnClickListener {
            info_popup_img.visibility = View.GONE
            info_popup_text_layout.visibility = View.GONE

            setting_detail_layout.visibility = View.VISIBLE
            info_layout.visibility = View.VISIBLE
        }

        app_info_text.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        first_arrow_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
        first_arrow_check_text.visibility = View.INVISIBLE

        second_arrow_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
        second_arrow_check_img.visibility = View.INVISIBLE

        first_time_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
        first_time_check_img.visibility = View.INVISIBLE

        second_time_layout.setBackgroundResource(R.mipmap.btn_firsttwo_nonpress)
        second_time_check_img.visibility = View.INVISIBLE

        first_round_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
        first_round_check_img.visibility = View.INVISIBLE

        second_round_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
        second_round_check_img.visibility = View.INVISIBLE

        first_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
        first_withdraw_check_img.visibility = View.INVISIBLE

        second_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
        second_withdraw_check_img.visibility = View.INVISIBLE

        third_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
        third_withdraw_check_img.visibility = View.INVISIBLE

        fourth_withdraw_layout.setBackgroundResource(R.mipmap.btn_thirdfourth_nonpress)
        fourth_withdraw_check_img.visibility = View.INVISIBLE

        user_setting_layout.setBackgroundResource(R.mipmap.btn_assign_nonpress)

        customBoolean = false
        arrow = -1
        time = -1
        round = -1
        withdraw = -1


        dbCount = mDbManager.selectCount()
        if(dbCount > 0) {
            val cursor: Cursor = mDbManager.selectSetting()
            while (cursor.moveToNext()) {
                mSpeaker = cursor.getInt(0)
                mFont = cursor.getInt(1)
                mBackground = cursor.getInt(2)
                mLanguage = cursor.getInt(3)
            }
        }
    }

    override fun onBackPressed() {
        if(info_popup_img.visibility == View.VISIBLE) {
            info_popup_img.visibility = View.GONE
            info_popup_text_layout.visibility = View.GONE

            setting_detail_layout.visibility = View.VISIBLE
            info_layout.visibility = View.VISIBLE
        }else {
            super.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics())
        //setContentView(R.layout.activity_setting)
    }
}