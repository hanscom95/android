package com.artech.countdown

import android.database.Cursor
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationSet
import android.widget.Toast
import com.artech.countdown.utility.DBManager
import kotlinx.android.synthetic.main.activity_count.*
import kotlinx.android.synthetic.main.content_count.*
import kotlinx.android.synthetic.main.popup_count.*
import kotlinx.android.synthetic.main.popup_count.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask


class CountActivity_BK : AppCompatActivity() {

    val mDbManager: DBManager = DBManager(this, "count.db", null, 1)

    var time: Int = 0
    var waiting: Int = 9//13//11
    var max: Int = 241
    var round: Int = 0
    var withdraw: Int = 0
    var roundFlag: Int = 0

    var timer = Timer()
    var attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
    var sound = SoundPool.Builder().setAudioAttributes(attributes).build()
    var soundId_1 = 0
    var soundId_2 = 0
    var soundId_3 = 0
    var soundId_4 = 0

    var mArrow: Int = -1
    var mTime: Int = -1
    var mRound: Int = -1
    var mWithdraw: Int = -1

    var mSpeaker: Int = -1
    var mFont: Int = -1
    var mBackground: Int = -1
    var mLanguage: Int = -1
    var mBeforeFont: Int = -1

    var mPlayBoolean: Boolean = false
    var mPlayFlag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        setSupportActionBar(toolbar)
        toolbar.setLogo(R.mipmap.icon_timer)
        toolbar.title = "TIMER"
        toolbar.titleMarginStart = 43

        mArrow = intent.getIntExtra("arrow", 0)
        mTime = intent.getIntExtra("time", 0)
        mRound = intent.getIntExtra("round", 0)
        mWithdraw = intent.getIntExtra("withdraw", 0)

        max = mTime//+1
        round = mRound
        withdraw = mWithdraw//+3//6//3


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val typefaceRobotoBold = Typeface.createFromAsset(assets, "fonts/Roboto_Bold.otf")
        val typefaceBold = Typeface.createFromAsset(assets, "fonts/NotoSansCJKkr_Bold.otf")

        option_text.setTypeface(typefaceRobotoBold)
        shootoff_text.setTypeface(typefaceRobotoBold)
        count_text.setTypeface(typefaceBold)
        min_text.setTypeface(typefaceBold)
        sec_text.setTypeface(typefaceBold)

        val dbCount = mDbManager.selectCount()
        if(dbCount > 0) {
            val cursor: Cursor = mDbManager.selectSetting()
            while (cursor.moveToNext()) {
                mSpeaker = cursor.getInt(0)
                mFont = cursor.getInt(1)
                mBackground = cursor.getInt(2)
                mLanguage = cursor.getInt(3)
            }
        }

        setOptionChange()

        /*var min_first: Int = -1
        var min_second: Int = -1
        var sec_first: Int = -1
        var sec_second: Int = -1

        if ((10 / 60) > 9) {
            min_first = (10 / 600)
            min_second = ((10 / 60) % 10)
        } else {
            min_first = 0
            min_second = ((10 / 60) % 10)
        }

        if ((withdraw % 60) > 9) {
            sec_first = ((10 % 60) / 10)
            sec_second = ((10 % 60) % 10)
        } else {
            sec_first = 0
            sec_second = ((10 % 60) % 10)
        }*/
        fontColor(0, 0, 1, 0)


        //Timer("SettingUp", false).schedule(1000) {doSomething()}
        soundId_1 = sound.load(this, R.raw.buzzer, 1)
        soundId_2 = sound.load(this, R.raw.buzzer2, 1)
        soundId_3 = sound.load(this, R.raw.buzzer3, 1)
        soundId_4 = sound.load(this, R.raw.buzzer4, 1)

        option_text.setOnClickListener {
            val inflater =  layoutInflater
            val dialogView = inflater.inflate(R.layout.popup_count, null)

            var builder = AlertDialog.Builder(this)

            builder.setView(dialogView)
            var alertDialog = builder.show()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.window.setLayout(671, 587)


            if(mSpeaker > -1 && mFont > -1 && mBackground > -1) {
                var speaker:Int = mSpeaker
                var font:Int = mFont
                var background:Int = mBackground

                if(speaker == 0) {
                    dialogView.speaker_radio_group.check(R.id.speaker_one_radio)
                }else if(speaker == 1) {
                    dialogView.speaker_radio_group.check(R.id.speaker_two_radio)
                }else if(speaker == 2) {
                    dialogView.speaker_radio_group.check(R.id.speaker_three_radio)
                }else if(speaker == 3) {
                    dialogView.speaker_radio_group.check(R.id.speaker_four_radio)
                }



                if(font == 0) {
                    dialogView.font_radio_group.check(R.id.font_one_radio)
                }else if(font == 1) {
                    dialogView.font_radio_group.check(R.id.font_two_radio)
                }else if(font == 2) {
                    dialogView.font_radio_group.check(R.id.font_three_radio)
                }else if(font == 3) {
                    dialogView.font_radio_group.check(R.id.font_four_radio)
                }else if(font == 4) {
                    dialogView.font_radio_group.check(R.id.font_five_radio)
                }else if(font == 5) {
                    dialogView.font_radio_group.check(R.id.font_six_radio)
                }else if(font == 6) {
                    dialogView.font_radio_group.check(R.id.font_seven_radio)
                }

                if(background == 0) {
                    dialogView.background_radio_group.check(R.id.background_one_radio)
                }else if(background == 1) {
                    dialogView.background_radio_group.check(R.id.background_two_radio)
                }

            }

            dialogView.speaker_radio_group.setOnCheckedChangeListener { radioGroup, i ->
                if(i == R.id.speaker_one_radio)
                    sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)
                else if(i == R.id.speaker_two_radio)
                    sound.play(soundId_2, 1.0f, 1.0f, 0, 0, 1.0f)
                else if(i == R.id.speaker_three_radio)
                    sound.play(soundId_3, 1.0f, 1.0f, 0, 0, 1.0f)
                else if(i == R.id.speaker_four_radio)
                    sound.play(soundId_4, 1.0f, 1.0f, 0, 0, 1.0f)
            }

            dialogView.confirm_layout.setOnClickListener {

                var speaker = -1
                var font = -1
                var background = -1

                var arrayList = ArrayList<Int>()

                if(dialogView.speaker_radio_group.checkedRadioButtonId == R.id.speaker_one_radio) {
                    speaker = 0
                }else if(dialogView.speaker_radio_group.checkedRadioButtonId == R.id.speaker_two_radio) {
                    speaker = 1
                }else if(dialogView.speaker_radio_group.checkedRadioButtonId == R.id.speaker_three_radio) {
                    speaker = 2
                }else if(dialogView.speaker_radio_group.checkedRadioButtonId == R.id.speaker_four_radio) {
                    speaker = 3
                }

                if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_one_radio) {
                    font = 0
                }else if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_two_radio) {
                    font = 1
                }else if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_three_radio) {
                    font = 2
                }else if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_four_radio) {
                    font = 3
                }else if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_five_radio) {
                    font = 4
                }else if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_six_radio) {
                    font = 5
                }else if(dialogView.font_radio_group.checkedRadioButtonId == R.id.font_seven_radio) {
                    font = 6
                }

                if(dialogView.background_radio_group.checkedRadioButtonId == R.id.background_one_radio) {
                    background = 0
                }else if(dialogView.background_radio_group.checkedRadioButtonId == R.id.background_two_radio) {
                    background = 1
                }

                arrayList.add(speaker)
                arrayList.add(font)
                arrayList.add(background)
                arrayList.add(mLanguage)


                if(speaker > -1 && font > -1 && background > -1) {
                    if (dbCount > 0) {
                        mDbManager.updateUser(arrayList)
                    } else {
                        mDbManager.insertSetting(arrayList)
                    }

                    mSpeaker = speaker
                    mBackground = background
                    mBeforeFont = font
//                    if(mFont > 0) {
                        mFont = font
//                    }

                    setOptionChange()

                    alertDialog.dismiss()
                }else {
                    Toast.makeText(this,getString(R.string.no_choice), Toast.LENGTH_LONG).show()
                }
            }
        }

        play_button.setOnClickListener {
            if(!mPlayBoolean) {
                mPlayBoolean = true

                timer = Timer()
                timer.schedule(timerTask { doSomething() }, 1000, 1000)
            }
        }

        stop_button.setOnClickListener {
            if(mPlayBoolean) {
                mPlayBoolean = false
                timer.cancel()
                timer.purge()
            }
        }

        pause_button.setOnClickListener {
            if(mPlayBoolean) {
                if (mPlayFlag == 0) {
                    waiting = 0
                }else if (mPlayFlag == 1) {
                    time = max-1
                } else if (mPlayFlag == 2) {
                    time = withdraw-1
                }

            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(mPlayBoolean) {
            mPlayBoolean = false
            timer.cancel()
            timer.purge()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(timer != null) {
            timer.cancel()
            timer.purge()
        }

        sound.release()
    }

    fun doSomething() {
        if (round > 0) {
            if (mPlayFlag == 0) {
                var min_first: Int = -1
                var min_second: Int = -1
                var sec_first: Int = -1
                var sec_second: Int = -1

                if ((waiting / 60) > 9) {
                    min_first = (waiting / 600)
                    min_second = ((waiting / 60) % 10)
                } else {
                    min_first = 0
                    min_second = ((waiting / 60) % 10)
                }

                if ((waiting % 60) > 9) {
                    sec_first = ((waiting % 60) / 10)
                    sec_second = ((waiting % 60) % 10)
                } else {
                    sec_first = 0
                    sec_second = ((waiting % 60) % 10)
                }

                val clock: String = "" + (if ((waiting / 60) > 9) (waiting / 60) else "0" + (waiting / 60)) + " : " + (if ((waiting % 60) > 9) (waiting % 60) else "0" + (waiting % 60))
                runOnUiThread {
                    //                    if(waiting == 13 && mBeforeFont == -1) {
                    if(waiting == 9 && mBeforeFont == -1) {
                        mBeforeFont = mFont
                        setOptionChange()
                    }


//                    if (waiting > 9) {
//                        if((waiting-9)%2 == 1) {
                    if((waiting == 9) || (waiting == 7)) {
                        if (mSpeaker == 0)
                            sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)
                        else if (mSpeaker == 1)
                            sound.play(soundId_2, 1.0f, 1.0f, 0, 0, 1.0f)
                        else if (mSpeaker == 2)
                            sound.play(soundId_3, 1.0f, 1.0f, 0, 0, 1.0f)
                        else if (mSpeaker == 3)
                            sound.play(soundId_4, 1.0f, 1.0f, 0, 0, 1.0f)
                        else
                            sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)
//                        }
                    }

//                        if(waiting == 13) {
                    if(waiting == 9) {
                        count_text.visibility = View.VISIBLE
                        count_text.text = getString(R.string.waiting_message)
                        count_detail_layout.visibility = View.GONE
                    }
//                    } else {
                    fontColor(min_first, min_second, sec_first, sec_second)
//                    }

                    waiting--

                    if (waiting == -1) {
                        mPlayFlag++
                        roundFlag++
                    }
                }
            } else {
                time += 1

                var value: Int = -1
                var bellTime: Int = -1
                var last: Int = -1


                if (mPlayFlag == 1) {
                    value = max - time
                    bellTime = 2
                    last = max
                } else if (mPlayFlag == 2) {
                    if(withdraw < 0) {
                        time = 0
                        waiting = 9
                        mFont = mBeforeFont

                        round--
                        mPlayFlag = 0

                        return
                    }

                    value = withdraw - time
                    bellTime = 4//7//4
                    last = withdraw
                }

                var min_first: Int = -1
                var min_second: Int = -1
                var sec_first: Int = -1
                var sec_second: Int = -1

                if ((value / 60) > 9) {
                    min_first = (value / 600)
                    min_second = ((value / 60) % 10)
                } else {
                    min_first = 0
                    min_second = ((value / 60) % 10)
                }

                if ((value % 60) > 9) {
                    sec_first = ((value % 60) / 10)
                    sec_second = ((value % 60) % 10)
                } else {
                    sec_first = 0
                    sec_second = ((value % 60) % 10)
                }


                val clock: String = "" + (if ((mTime / 60) > 9) (mTime / 60) else "0" + (mTime / 60)) + " : " + (if ((mTime % 60) > 9) (mTime % 60) else "0" + (mTime % 60))
                runOnUiThread {


                    //                    if (time < bellTime) {
                    if(mPlayFlag == 1) {
                        if(time == 1) {
                            //count_detail_layout.visibility = View.GONE//20180808
                            //count_text.visibility = View.VISIBLE//20180808
                            //val text = String.format(getString(R.string.command_messages), clock, mArrow)//20180808
                            //count_text.text = text//20180808

                            if (mSpeaker == 0)
                                sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)
                            else if (mSpeaker == 1)
                                sound.play(soundId_2, 1.0f, 1.0f, 0, 0, 1.0f)
                            else if (mSpeaker == 2)
                                sound.play(soundId_3, 1.0f, 1.0f, 0, 0, 1.0f)
                            else if (mSpeaker == 3)
                                sound.play(soundId_4, 1.0f, 1.0f, 0, 0, 1.0f)
                            else
                                sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)

                        }

                    } else {
                        if(time == 1 || time == 3 || time == 5) {
                            if(time == 1) {
                                count_detail_layout.visibility = View.GONE
                                count_text.visibility = View.VISIBLE
                                count_text.text = getString(R.string.frequency_message)
                            }

//                            if(time%2 == 1) {
                            if (mSpeaker == 0)
                                sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)
                            else if (mSpeaker == 1)
                                sound.play(soundId_2, 1.0f, 1.0f, 0, 0, 1.0f)
                            else if (mSpeaker == 2)
                                sound.play(soundId_3, 1.0f, 1.0f, 0, 0, 1.0f)
                            else if (mSpeaker == 3)
                                sound.play(soundId_4, 1.0f, 1.0f, 0, 0, 1.0f)
                            else
                                sound.play(soundId_1, 1.0f, 1.0f, 0, 0, 1.0f)
//                            }
                        }

                    }

                    if(time == 1) {
                        roundColor()
                    }
//                    } else {
                    if(value == 30) {
                        mFont = 0 // red
                        //20180808 start
                        if(mFont == 1)
                            mFont = 2 // red
                        else
                            mFont = 1 // red
                        //20180808 end
                        setOptionChange()
                    }
                    //20180808 if (mPlayFlag == 1 && time == 10) {
                    if (mPlayFlag == 1 && time == 1) {
                        count_detail_layout.visibility = View.VISIBLE
                        count_text.visibility = View.GONE
                    }
                    fontColor(min_first, min_second, sec_first, sec_second)
//                    }


                    if (time == last) {
                        time = 0
//                        waiting = 11
                        waiting = 9
                        mFont = mBeforeFont

                        if (mPlayFlag == 2) {
                            round--
                            mPlayFlag = 0
                        } else {
                            if(round == 1) {
                                round = 0
                                mPlayFlag = 0


                                count_detail_layout.visibility = View.GONE
                                count_text.visibility = View.VISIBLE
                                count_text.text = getString(R.string.time_end_message)
                            }else {
                                mPlayFlag++
                            }
                        }
                    }
                }
            }
        }else {
            runOnUiThread {
                message_layout.visibility = View.GONE
                count_main_layout.visibility = View.GONE
                count_finish_layout.visibility = View.VISIBLE
            }

            mPlayBoolean = false

            timer.cancel()
            timer.purge()
        }
    }

    fun setOptionChange() {
        if(mFont == 0) {
            count_detail_img.setImageResource(R.mipmap.img_round_r)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_r)
            time_center_text.setImageResource(R.mipmap.img_colon_r)
            count_text.setTextColor(getColor(R.color.red))
            finish_text.setTextColor(getColor(R.color.red))
            min_text.setTextColor(getColor(R.color.red))
            sec_text.setTextColor(getColor(R.color.red))
        }else if(mFont == 1) {
            count_detail_img.setImageResource(R.mipmap.img_round_y)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_y)
            time_center_text.setImageResource(R.mipmap.img_colon_y)
            count_text.setTextColor(getColor(R.color.yellow))
            finish_text.setTextColor(getColor(R.color.yellow))
            min_text.setTextColor(getColor(R.color.yellow))
            sec_text.setTextColor(getColor(R.color.yellow))
        }else if(mFont == 2) {
            count_detail_img.setImageResource(R.mipmap.img_round_yg)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_yg)
            time_center_text.setImageResource(R.mipmap.img_colon_yg)
            count_text.setTextColor(getColor(R.color.green))
            finish_text.setTextColor(getColor(R.color.green))
            min_text.setTextColor(getColor(R.color.green))
            sec_text.setTextColor(getColor(R.color.green))
        }else if(mFont == 3) {
            count_detail_img.setImageResource(R.mipmap.img_round_m)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_m)
            time_center_text.setImageResource(R.mipmap.img_colon_m)
            count_text.setTextColor(getColor(R.color.mint))
            finish_text.setTextColor(getColor(R.color.mint))
            min_text.setTextColor(getColor(R.color.mint))
            sec_text.setTextColor(getColor(R.color.mint))
        }else if(mFont == 4) {
            count_detail_img.setImageResource(R.mipmap.img_round_b)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_b)
            time_center_text.setImageResource(R.mipmap.img_colon_b)
            count_text.setTextColor(getColor(R.color.blue))
            finish_text.setTextColor(getColor(R.color.blue))
            min_text.setTextColor(getColor(R.color.blue))
            sec_text.setTextColor(getColor(R.color.blue))
        }else if(mFont == 5) {
            count_detail_img.setImageResource(R.mipmap.img_round_p)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_p)
            time_center_text.setImageResource(R.mipmap.img_colon_p)
            count_text.setTextColor(getColor(R.color.purple))
            finish_text.setTextColor(getColor(R.color.purple))
            min_text.setTextColor(getColor(R.color.purple))
            sec_text.setTextColor(getColor(R.color.purple))
        }else if(mFont == 6) {
            count_detail_img.setImageResource(R.mipmap.img_round_w)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_w)
            time_center_text.setImageResource(R.mipmap.img_colon_white)
            count_text.setTextColor(getColor(R.color.white))
            finish_text.setTextColor(getColor(R.color.white))
            min_text.setTextColor(getColor(R.color.white))
            sec_text.setTextColor(getColor(R.color.white))
        }

        if(mBackground == 0) {
            count_background_layout.background = getDrawable(R.color.black)
        }else if(mBackground == 1) {
            count_background_layout.background = getDrawable(R.color.white)
        }

        fontColor(0, 0, 0, 0)

        roundColor()
    }

    fun fontColor(min_first:Int, min_second:Int, sec_first:Int, sec_second:Int) {
        if(mFont == 0) {
            count_detail_img.setImageResource(R.mipmap.img_round_r)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_r)
            time_center_text.setImageResource(R.mipmap.img_colon_r)
            count_text.setTextColor(getColor(R.color.red))
            finish_text.setTextColor(getColor(R.color.red))
            min_text.setTextColor(getColor(R.color.red))
            sec_text.setTextColor(getColor(R.color.red))
        }else if(mFont == 1) {
            count_detail_img.setImageResource(R.mipmap.img_round_y)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_y)
            time_center_text.setImageResource(R.mipmap.img_colon_y)
            count_text.setTextColor(getColor(R.color.yellow))
            finish_text.setTextColor(getColor(R.color.yellow))
            min_text.setTextColor(getColor(R.color.yellow))
            sec_text.setTextColor(getColor(R.color.yellow))
        }else if(mFont == 2) {
            count_detail_img.setImageResource(R.mipmap.img_round_yg)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_yg)
            time_center_text.setImageResource(R.mipmap.img_colon_yg)
            count_text.setTextColor(getColor(R.color.green))
            finish_text.setTextColor(getColor(R.color.green))
            min_text.setTextColor(getColor(R.color.green))
            sec_text.setTextColor(getColor(R.color.green))
        }else if(mFont == 3) {
            count_detail_img.setImageResource(R.mipmap.img_round_m)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_m)
            time_center_text.setImageResource(R.mipmap.img_colon_m)
            count_text.setTextColor(getColor(R.color.mint))
            finish_text.setTextColor(getColor(R.color.mint))
            min_text.setTextColor(getColor(R.color.mint))
            sec_text.setTextColor(getColor(R.color.mint))
        }else if(mFont == 4) {
            count_detail_img.setImageResource(R.mipmap.img_round_b)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_b)
            time_center_text.setImageResource(R.mipmap.img_colon_b)
            count_text.setTextColor(getColor(R.color.blue))
            finish_text.setTextColor(getColor(R.color.blue))
            min_text.setTextColor(getColor(R.color.blue))
            sec_text.setTextColor(getColor(R.color.blue))
        }else if(mFont == 5) {
            count_detail_img.setImageResource(R.mipmap.img_round_p)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_p)
            time_center_text.setImageResource(R.mipmap.img_colon_p)
            count_text.setTextColor(getColor(R.color.purple))
            finish_text.setTextColor(getColor(R.color.purple))
            min_text.setTextColor(getColor(R.color.purple))
            sec_text.setTextColor(getColor(R.color.purple))
        }else if(mFont == 6) {
            count_detail_img.setImageResource(R.mipmap.img_round_w)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_w)
            time_center_text.setImageResource(R.mipmap.img_colon_white)
            count_text.setTextColor(getColor(R.color.white))
            finish_text.setTextColor(getColor(R.color.white))
            min_text.setTextColor(getColor(R.color.white))
            sec_text.setTextColor(getColor(R.color.white))
        }else {
            count_detail_img.setImageResource(R.mipmap.img_round_w)
            alarmclock_img.setImageResource(R.mipmap.img_alarmclock_w)
            time_center_text.setImageResource(R.mipmap.img_colon_white)
            count_text.setTextColor(getColor(R.color.white))
            finish_text.setTextColor(getColor(R.color.white))
            min_text.setTextColor(getColor(R.color.white))
            sec_text.setTextColor(getColor(R.color.white))
        }


        if (min_first == 0) {
            if(mFont == 0)
                min_first_text.setImageResource(R.mipmap.img_0_r)
            else if(mFont == 1)
                min_first_text.setImageResource(R.mipmap.img_0_y)
            else if(mFont == 2)
                min_first_text.setImageResource(R.mipmap.img_0_yg)
            else if(mFont == 3)
                min_first_text.setImageResource(R.mipmap.img_0_m)
            else if(mFont == 4)
                min_first_text.setImageResource(R.mipmap.img_0_b)
            else if(mFont == 5)
                min_first_text.setImageResource(R.mipmap.img_0_p)
            else if(mFont == 6)
                min_first_text.setImageResource(R.mipmap.img_0_w)
            else
                min_first_text.setImageResource(R.mipmap.img_0_w)

        } else if (min_first == 1) {
            if(mFont == 0)
                min_first_text.setImageResource(R.mipmap.img_1_r)
            else if(mFont == 1)
                min_first_text.setImageResource(R.mipmap.img_1_y)
            else if(mFont == 2)
                min_first_text.setImageResource(R.mipmap.img_1_yg)
            else if(mFont == 3)
                min_first_text.setImageResource(R.mipmap.img_1_m)
            else if(mFont == 4)
                min_first_text.setImageResource(R.mipmap.img_1_b)
            else if(mFont == 5)
                min_first_text.setImageResource(R.mipmap.img_1_p)
            else if(mFont == 6)
                min_first_text.setImageResource(R.mipmap.img_1_w)
            else
                min_first_text.setImageResource(R.mipmap.img_1_w)
        } else if (min_first == 2) {
            if(mFont == 0)
                min_first_text.setImageResource(R.mipmap.img_2_r)
            else if(mFont == 1)
                min_first_text.setImageResource(R.mipmap.img_2_y)
            else if(mFont == 2)
                min_first_text.setImageResource(R.mipmap.img_2_yg)
            else if(mFont == 3)
                min_first_text.setImageResource(R.mipmap.img_2_m)
            else if(mFont == 4)
                min_first_text.setImageResource(R.mipmap.img_2_b)
            else if(mFont == 5)
                min_first_text.setImageResource(R.mipmap.img_2_p)
            else if(mFont == 6)
                min_first_text.setImageResource(R.mipmap.img_2_w)
            else
                min_first_text.setImageResource(R.mipmap.img_2_w)
        } else if (min_first == 3) {
            if(mFont == 0)
                min_first_text.setImageResource(R.mipmap.img_3_r)
            else if(mFont == 1)
                min_first_text.setImageResource(R.mipmap.img_3_y)
            else if(mFont == 2)
                min_first_text.setImageResource(R.mipmap.img_3_yg)
            else if(mFont == 3)
                min_first_text.setImageResource(R.mipmap.img_3_m)
            else if(mFont == 4)
                min_first_text.setImageResource(R.mipmap.img_3_b)
            else if(mFont == 5)
                min_first_text.setImageResource(R.mipmap.img_3_p)
            else if(mFont == 6)
                min_first_text.setImageResource(R.mipmap.img_3_w)
            else
                min_first_text.setImageResource(R.mipmap.img_3_w)
        } else if (min_first == 4) {
            if(mFont == 0)
                min_first_text.setImageResource(R.mipmap.img_4_r)
            else if(mFont == 1)
                min_first_text.setImageResource(R.mipmap.img_4_y)
            else if(mFont == 2)
                min_first_text.setImageResource(R.mipmap.img_4_yg)
            else if(mFont == 3)
                min_first_text.setImageResource(R.mipmap.img_4_m)
            else if(mFont == 4)
                min_first_text.setImageResource(R.mipmap.img_4_b)
            else if(mFont == 5)
                min_first_text.setImageResource(R.mipmap.img_4_p)
            else if(mFont == 6)
                min_first_text.setImageResource(R.mipmap.img_4_w)
            else
                min_first_text.setImageResource(R.mipmap.img_4_w)
        } else if (min_first == 5) {
            if(mFont == 0)
                min_first_text.setImageResource(R.mipmap.img_5_r)
            else if(mFont == 1)
                min_first_text.setImageResource(R.mipmap.img_5_y)
            else if(mFont == 2)
                min_first_text.setImageResource(R.mipmap.img_5_yg)
            else if(mFont == 3)
                min_first_text.setImageResource(R.mipmap.img_5_m)
            else if(mFont == 4)
                min_first_text.setImageResource(R.mipmap.img_5_b)
            else if(mFont == 5)
                min_first_text.setImageResource(R.mipmap.img_5_p)
            else if(mFont == 6)
                min_first_text.setImageResource(R.mipmap.img_5_w)
            else
                min_first_text.setImageResource(R.mipmap.img_5_w)
        }


        if (min_second == 0) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_0_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_0_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_0_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_0_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_0_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_0_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_0_w)
            else
                min_second_text.setImageResource(R.mipmap.img_0_w)
        } else if (min_second == 1) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_1_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_1_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_1_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_1_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_1_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_1_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_1_w)
            else
                min_second_text.setImageResource(R.mipmap.img_1_w)
        } else if (min_second == 2) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_2_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_2_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_2_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_2_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_2_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_2_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_2_w)
            else
                min_second_text.setImageResource(R.mipmap.img_2_w)
        } else if (min_second == 3) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_3_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_3_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_3_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_3_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_3_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_3_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_3_w)
            else
                min_second_text.setImageResource(R.mipmap.img_3_w)
        } else if (min_second == 4) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_4_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_4_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_4_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_4_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_4_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_4_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_4_w)
            else
                min_second_text.setImageResource(R.mipmap.img_4_w)
        } else if (min_second == 5) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_5_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_5_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_5_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_5_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_5_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_5_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_5_w)
            else
                min_second_text.setImageResource(R.mipmap.img_5_w)
        } else if (min_second == 6) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_6_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_6_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_6_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_6_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_6_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_6_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_6_w)
            else
                min_second_text.setImageResource(R.mipmap.img_6_w)
        } else if (min_second == 7) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_7_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_7_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_7_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_7_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_7_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_7_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_7_w)
            else
                min_second_text.setImageResource(R.mipmap.img_7_w)
        } else if (min_second == 8) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_8_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_8_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_8_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_8_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_8_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_8_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_8_w)
            else
                min_second_text.setImageResource(R.mipmap.img_8_w)
        } else if (min_second == 9) {
            if(mFont == 0)
                min_second_text.setImageResource(R.mipmap.img_9_r)
            else if(mFont == 1)
                min_second_text.setImageResource(R.mipmap.img_9_y)
            else if(mFont == 2)
                min_second_text.setImageResource(R.mipmap.img_9_yg)
            else if(mFont == 3)
                min_second_text.setImageResource(R.mipmap.img_9_m)
            else if(mFont == 4)
                min_second_text.setImageResource(R.mipmap.img_9_b)
            else if(mFont == 5)
                min_second_text.setImageResource(R.mipmap.img_9_p)
            else if(mFont == 6)
                min_second_text.setImageResource(R.mipmap.img_9_w)
            else
                min_second_text.setImageResource(R.mipmap.img_9_w)
        }


        if (sec_first == 0) {
            if(mFont == 0)
                sec_first_text.setImageResource(R.mipmap.img_0_r)
            else if(mFont == 1)
                sec_first_text.setImageResource(R.mipmap.img_0_y)
            else if(mFont == 2)
                sec_first_text.setImageResource(R.mipmap.img_0_yg)
            else if(mFont == 3)
                sec_first_text.setImageResource(R.mipmap.img_0_m)
            else if(mFont == 4)
                sec_first_text.setImageResource(R.mipmap.img_0_b)
            else if(mFont == 5)
                sec_first_text.setImageResource(R.mipmap.img_0_p)
            else if(mFont == 6)
                sec_first_text.setImageResource(R.mipmap.img_0_w)
            else
                sec_first_text.setImageResource(R.mipmap.img_0_w)
        } else if (sec_first == 1) {
            if(mFont == 0)
                sec_first_text.setImageResource(R.mipmap.img_1_r)
            else if(mFont == 1)
                sec_first_text.setImageResource(R.mipmap.img_1_y)
            else if(mFont == 2)
                sec_first_text.setImageResource(R.mipmap.img_1_yg)
            else if(mFont == 3)
                sec_first_text.setImageResource(R.mipmap.img_1_m)
            else if(mFont == 4)
                sec_first_text.setImageResource(R.mipmap.img_1_b)
            else if(mFont == 5)
                sec_first_text.setImageResource(R.mipmap.img_1_p)
            else if(mFont == 6)
                sec_first_text.setImageResource(R.mipmap.img_1_w)
            else
                sec_first_text.setImageResource(R.mipmap.img_1_w)
        } else if (sec_first == 2) {
            if(mFont == 0)
                sec_first_text.setImageResource(R.mipmap.img_2_r)
            else if(mFont == 1)
                sec_first_text.setImageResource(R.mipmap.img_2_y)
            else if(mFont == 2)
                sec_first_text.setImageResource(R.mipmap.img_2_yg)
            else if(mFont == 3)
                sec_first_text.setImageResource(R.mipmap.img_2_m)
            else if(mFont == 4)
                sec_first_text.setImageResource(R.mipmap.img_2_b)
            else if(mFont == 5)
                sec_first_text.setImageResource(R.mipmap.img_2_p)
            else if(mFont == 6)
                sec_first_text.setImageResource(R.mipmap.img_2_w)
            else
                sec_first_text.setImageResource(R.mipmap.img_2_w)
        } else if (sec_first == 3) {
            if(mFont == 0)
                sec_first_text.setImageResource(R.mipmap.img_3_r)
            else if(mFont == 1)
                sec_first_text.setImageResource(R.mipmap.img_3_y)
            else if(mFont == 2)
                sec_first_text.setImageResource(R.mipmap.img_3_yg)
            else if(mFont == 3)
                sec_first_text.setImageResource(R.mipmap.img_3_m)
            else if(mFont == 4)
                sec_first_text.setImageResource(R.mipmap.img_3_b)
            else if(mFont == 5)
                sec_first_text.setImageResource(R.mipmap.img_3_p)
            else if(mFont == 6)
                sec_first_text.setImageResource(R.mipmap.img_3_w)
            else
                sec_first_text.setImageResource(R.mipmap.img_3_w)
        } else if (sec_first == 4) {
            if(mFont == 0)
                sec_first_text.setImageResource(R.mipmap.img_4_r)
            else if(mFont == 1)
                sec_first_text.setImageResource(R.mipmap.img_4_y)
            else if(mFont == 2)
                sec_first_text.setImageResource(R.mipmap.img_4_yg)
            else if(mFont == 3)
                sec_first_text.setImageResource(R.mipmap.img_4_m)
            else if(mFont == 4)
                sec_first_text.setImageResource(R.mipmap.img_4_b)
            else if(mFont == 5)
                sec_first_text.setImageResource(R.mipmap.img_4_p)
            else if(mFont == 6)
                sec_first_text.setImageResource(R.mipmap.img_4_w)
            else
                sec_first_text.setImageResource(R.mipmap.img_4_w)
        } else if (sec_first == 5) {
            if(mFont == 0)
                sec_first_text.setImageResource(R.mipmap.img_5_r)
            else if(mFont == 1)
                sec_first_text.setImageResource(R.mipmap.img_5_y)
            else if(mFont == 2)
                sec_first_text.setImageResource(R.mipmap.img_5_yg)
            else if(mFont == 3)
                sec_first_text.setImageResource(R.mipmap.img_5_m)
            else if(mFont == 4)
                sec_first_text.setImageResource(R.mipmap.img_5_b)
            else if(mFont == 5)
                sec_first_text.setImageResource(R.mipmap.img_5_p)
            else if(mFont == 6)
                sec_first_text.setImageResource(R.mipmap.img_5_w)
            else
                sec_first_text.setImageResource(R.mipmap.img_5_w)
        }


        if (sec_second == 0) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_0_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_0_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_0_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_0_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_0_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_0_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_0_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_0_w)
        } else if (sec_second == 1) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_1_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_1_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_1_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_1_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_1_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_1_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_1_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_1_w)
        } else if (sec_second == 2) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_2_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_2_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_2_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_2_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_2_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_2_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_2_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_2_w)
        } else if (sec_second == 3) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_3_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_3_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_3_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_3_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_3_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_3_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_3_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_3_w)
        } else if (sec_second == 4) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_4_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_4_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_4_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_4_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_4_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_4_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_4_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_4_w)
        } else if (sec_second == 5) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_5_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_5_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_5_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_5_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_5_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_5_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_5_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_5_w)
        } else if (sec_second == 6) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_6_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_6_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_6_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_6_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_6_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_6_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_6_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_6_w)
        } else if (sec_second == 7) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_7_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_7_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_7_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_7_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_7_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_7_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_7_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_7_w)
        } else if (sec_second == 8) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_8_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_8_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_8_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_8_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_8_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_8_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_8_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_8_w)
        } else if (sec_second == 9) {
            if(mFont == 0)
                sec_second_text.setImageResource(R.mipmap.img_9_r)
            else if(mFont == 1)
                sec_second_text.setImageResource(R.mipmap.img_9_y)
            else if(mFont == 2)
                sec_second_text.setImageResource(R.mipmap.img_9_yg)
            else if(mFont == 3)
                sec_second_text.setImageResource(R.mipmap.img_9_m)
            else if(mFont == 4)
                sec_second_text.setImageResource(R.mipmap.img_9_b)
            else if(mFont == 5)
                sec_second_text.setImageResource(R.mipmap.img_9_p)
            else if(mFont == 6)
                sec_second_text.setImageResource(R.mipmap.img_9_w)
            else
                sec_second_text.setImageResource(R.mipmap.img_9_w)
        }
    }

    fun roundColor() {
        if (roundFlag == 1) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_1_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_1_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_1_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_1_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_1_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_1_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_1_white)
            else
                count_img.setImageResource(R.mipmap.img_1_white)
        } else if (roundFlag == 2) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_2_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_2_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_2_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_2_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_2_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_2_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_2_white)
            else
                count_img.setImageResource(R.mipmap.img_2_white)
        } else if (roundFlag == 3) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_3_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_3_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_3_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_3_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_3_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_3_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_3_white)
            else
                count_img.setImageResource(R.mipmap.img_3_white)
        } else if (roundFlag == 4) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_4_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_4_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_4_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_4_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_4_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_4_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_4_white)
            else
                count_img.setImageResource(R.mipmap.img_4_white)
        } else if (roundFlag == 5) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_5_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_5_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_5_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_5_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_5_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_5_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_5_white)
            else
                count_img.setImageResource(R.mipmap.img_5_white)
        } else if (roundFlag == 6) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_6_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_6_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_6_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_6_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_6_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_6_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_6_white)
            else
                count_img.setImageResource(R.mipmap.img_6_white)
        } else if (roundFlag == 7) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_7_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_7_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_7_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_7_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_7_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_7_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_7_white)
            else
                count_img.setImageResource(R.mipmap.img_7_white)
        } else if (roundFlag == 8) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_8_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_8_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_8_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_8_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_8_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_8_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_8_white)
            else
                count_img.setImageResource(R.mipmap.img_8_white)
        } else if (roundFlag == 9) {
            if(mFont == 0)
                count_img.setImageResource(R.mipmap.img_9_red)
            else if(mFont == 1)
                count_img.setImageResource(R.mipmap.img_9_yellow)
            else if(mFont == 2)
                count_img.setImageResource(R.mipmap.img_9_yellowgreen)
            else if(mFont == 3)
                count_img.setImageResource(R.mipmap.img_9_mint)
            else if(mFont == 4)
                count_img.setImageResource(R.mipmap.img_9_blue)
            else if(mFont == 5)
                count_img.setImageResource(R.mipmap.img_9_pink)
            else if(mFont == 6)
                count_img.setImageResource(R.mipmap.img_9_white)
            else
                count_img.setImageResource(R.mipmap.img_9_white)
        }else if(roundFlag > 9){
            count_img.visibility = View.GONE
            count_img_text.visibility = View.VISIBLE
            count_img_text.text = ""+roundFlag
        }
    }
}