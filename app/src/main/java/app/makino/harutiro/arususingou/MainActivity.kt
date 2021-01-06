package app.makino.harutiro.arususingou

import android.R.attr
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val outTextId = findViewById<TextView>(R.id.outText)
        val tateButtonId = findViewById<Button>(R.id.henkanButton)
        val arusSwitchId = findViewById<Switch>(R.id.arusSwitch)
        val inTextId = findViewById<EditText>(R.id.inText)
        val saveButtonId = findViewById<Button>(R.id.saveButton)
        val layoutId = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.Layout)
        val modeSwitchId = findViewById<Switch>(R.id.modeSwitch)

        //モールス信号格納
        var morusu = ""
        //アルス変換状態
        var arusuZyoutai: Boolean = false
        //縦表示状態
        var modeZyoutai:Boolean = false
        //モールス部分ごとに集める部分
        var morusuAtumari:String = ""

        //アルスモードにするか判断
        arusSwitchId.setOnCheckedChangeListener { compoundButton, isChecked ->
            arusuZyoutai = isChecked
        }
        //縦表示にするか判断
        modeSwitchId.setOnCheckedChangeListener { compoundButton, isChecked ->
            modeZyoutai = isChecked
        }

        //変換部分
        tateButtonId.setOnClickListener {
            //morusu初期化
            morusu = ""
            //入力データを受け取る
            var text:String = inTextId.text.toString() + " "
            //cher型に一文字ずつ格納
            var charAry = text.toCharArray()

            //信号変換
            for (ch in charAry) {



                when(ch){
                    //アルスモード
                    'ﾓ' -> {
                        morusuAtumari = morusuAtumari + "－"
                    }
                    'ｱ'->{
                        morusuAtumari = morusuAtumari + "・"
                    }
                    //アルマルモードスルーポイント
                    'ｯ','ﾁ','ｰ','ﾝ','!'-> {}
                    'ﾙ','ﾏ'->{}


                    //モールスモード
                    '－'->{
                        morusuAtumari = morusuAtumari + "－"
                    }
                    '・'->{
                        morusuAtumari = morusuAtumari + "・"
                    }

                    //空白
                    ' ','　'->{
                        println("ゴール"+morusuAtumari)
                        morusu = morusu + henkanHiragana(morusuAtumari)
                        println("出力"+morusu)
                        morusuAtumari = ""
                    }

                    //おそらくひらがなアンドその他
                    else->{

                        if(modeZyoutai == true){
                            //縦表示モード
                            morusu = morusu +"\n"+ henkan(ch.toString()) + "　[$ch]"
                        }else{
                            //横表示モード
                            morusu = morusu +"　"+ henkan(ch.toString())
                        }

                    }









                }
            }

            //アルマル信号変換
            if(arusuZyoutai == true){
                var morusuCharAry = morusu.toCharArray()

                morusu = ""

                for (mo in morusuCharAry){
                    morusu = morusu + henkanArus(mo.toString())
                }
            }

            //set
            outTextId.text = morusu
        }

        //クリップボードに保存
        saveButtonId.setOnClickListener {
            copyToClipboard(morusu)
        }

    }
    fun henkanHiragana(morusuMoto: String):String{
        return when(morusuMoto){
            "－－・－－"->"あ"
            "・－"-> "い"
            "・・－"->"う"
            "－・－－－"->"え"
            "・－・・・"->"お"

            "・－・・"->"か"
            "－・－・・"->"き"
            "・・・－"->"く"
            "－・－－"->"け"
            "－－－－"->"こ"

            "－・－・－"->"さ"
            "－－・－・"->"し"
            "－－－・－"->"す"
            "・－－－・"->"せ"
            "－－－・"->"そ"

            "－・"->"た"
            "・・－・"->"ち"
            "・－－・"->"つ"
            "・－・－－"->"て"
            "・・－・・"->"と"

            "・－・"->"な"
            "－・－・"->"に"
            "・・・・"->"ぬ"
            "－－・－"->"ね"
            "・・－－"->"の"

            "－・・・"->"は"
            "－－・・－"->"ひ"
            "－－・・"->"ふ"
            "・"->"へ"
            "－・・"->"ほ"

            "－・・－"->"ま"
            "・・－・－"->"み"
            "－"->"む"
            "－・・・－"->"め"
            "－・・－・"->"も"

            "・－－"->"や"
            "－・・－－"->"ゆ"
            "－－"->"よ"

            "・・・"->"ら"
            "－－・"->"り"
            "－・－－・"->"る"
            "－－－"->"れ"
            "・－・－"->"ろ"

            "－・－"->"わ"
            "・－－－"->"を"
            "・－・－・"->"ん"

            "・－－・－"->"ー"
            "・－・－・・"->"。"
            "・－・－・－"->"、"
            "・・"->"゛"

            ""->" "

            else -> "？"
        }
    }

    fun henkanArus(morusuMoto:String):String{
        return when(morusuMoto){
            " " -> " "
            "　" -> "　"

            "－" -> "ﾓｯﾁｰﾝ!"
            "・" -> "ｱﾙﾏﾙｯ!!"

            else -> morusuMoto
        }
    }

    fun henkan(textMoto: String): String {
        return when(textMoto){
            "あ" -> "－－・－－"
            "い" -> "・－"
            "う" -> "・・－"
            "え" -> "－・－－－"
            "お" -> "・－・・・"

            "か" -> "・－・・"
            "き" -> "－・－・・"
            "く" -> "・・・－"
            "け" -> "－・－－"
            "こ" -> "－－－－"

            "さ" -> "－・－・－"
            "し" -> "－－・－・"
            "す" -> "－－－・－"
            "せ" -> "・－－－・"
            "そ" -> "－－－・"

            "た" -> "－・"
            "ち" -> "・・－・"
            "つ" -> "・－－・"
            "て" -> "・－・－－"
            "と" -> "・・－・・"

            "な" -> "・－・"
            "に" -> "－・－・"
            "ぬ" -> "・・・・"
            "ね" -> "－－・－"
            "の" -> "・・－－"

            "は" -> "－・・・"
            "ひ" -> "－－・・－"
            "ふ" -> "－－・・"
            "へ" -> "・"
            "ほ" -> "－・・"

            "ま" -> "－・・－"
            "み" -> "・・－・－"
            "む" -> "－"
            "め" -> "－・・・－"
            "も" -> "－・・－・"

            "や" -> "・－－"
            "ゆ" -> "－・・－－"
            "よ" -> "－－"

            "ら" -> "・・・"
            "り" -> "－－・"
            "る" -> "－・－－・"
            "れ" -> "－－－"
            "ろ" -> "・－・－"

            "わ" -> "－・－"
            "を" -> "・－－－"
            "ん" -> "・－・－・"

            "ー" -> "・－－・－"
            "。" -> "・－・－・・"
            "、" -> "・－・－・－"
            "゛" -> "・・"

            " " -> " "
            "　" -> "　"

            "っ" -> "・－－・"

            //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝ここから濁音＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

            "が" -> "・－・・" + "　"+"・・"
            "ぎ" -> "－・－・・"+ "　"+"・・"
            "ぐ" -> "・・・－"+ "　"+"・・"
            "げ" -> "－・－－"+ "　"+"・・"
            "ご" -> "－－－－"+ "　"+"・・"

            "ざ" -> "－・－・－"+ "　"+"・・"
            "じ" -> "－－・－・"+ "　"+"・・"
            "ず" -> "－－－・－"+ "　"+"・・"
            "ぜ" -> "・－－－・"+ "　"+"・・"
            "ぞ" -> "－－－・"+ "　"+"・・"

            "だ" -> "－・"+ "　"+"・・"
            "ぢ" -> "・・－・"+ "　"+"・・"
            "づ" -> "・－－・"+ "　"+"・・"
            "で" -> "・－・－－"+ "　"+"・・"
            "ど" -> "・・－・・"+ "　"+"・・"

            "ば" -> "－・・・"+ "　"+"・・"
            "び" -> "－－・・－"+ "　"+"・・"
            "ぶ" -> "－－・・"+ "　"+"・・"
            "べ" -> "・"+ "　"+"・・"
            "ぼ" -> "－・・"+ "　"+"・・"




            else -> "？"
        }
    }

    fun copyToClipboard(text: String?) {

        val layoutId = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.Layout)

        //クリップボードの保存
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text)
        clipboard.setPrimaryClip(clip)

        //スナックバーの表示
        Snackbar.make(layoutId,"保存しました！！", Snackbar.LENGTH_SHORT).show()

    }
}