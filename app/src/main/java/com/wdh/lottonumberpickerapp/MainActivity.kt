package com.wdh.lottonumberpickerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.wdh.lottonumberpickerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private var didRun = false

    private val pickNumberSet = hashSetOf<Int>()

    private val numberTextViewList:List<TextView> by lazy {
        listOf<TextView>(
            binding.randomLottoNumOne,
            binding.randomLottoNumTwo,
            binding.randomLottoNumThree,
            binding.randomLottoNumFour,
            binding.randomLottoNumFive,
            binding.randomLottoNumSix
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setNumberPicker()

        initRunButton()

        initAddButton()

        initClearButton()


    }

    private fun setNumberPicker(){
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 45
    }


    private fun initRunButton(){
        binding.runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, i ->
                val textView = numberTextViewList[index]
                textView.text = i.toString()
                textView.isVisible = true

                setNumberBackground(i, textView)
            }
        }
    }

    private fun setNumberBackground(number:Int, textView: TextView){
        when(number){
            in 1..10 -> textView.background =ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background =ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background =ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background =ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background =ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)){
                        continue
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()

        return pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size).sorted()
    }

    private fun initAddButton(){
        binding.addButton.setOnClickListener {
            if (didRun){
                Toast.makeText(this, "초기화 후에 시도해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.size >= 5){
                Toast.makeText(this, "번호는 5개까지만 설멍할 수 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(binding.numberPicker.value)){
                Toast.makeText(this, "이미 선택한 번호입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = binding.numberPicker.value.toString()

            pickNumberSet.add(binding.numberPicker.value)

            setNumberBackground(binding.numberPicker.value, textView)



        }
    }

    private fun initClearButton(){
        binding.clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }
}