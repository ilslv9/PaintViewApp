package com.ilslv.rocketbanktest

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.ilslv.rocketbanktest.paint_view.AlgorithmType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var freeHeight: Int = 0
    private var freeWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paint_view.setGenerateImageSize(dpToPx(512f), dpToPx(512f))
        calculateScreenSize()
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {

        screen_size_hint.text = "Max image width: $freeWidth, height: $freeHeight"

        generate_button.setOnClickListener {
            val userWidth = paint_view_width.text.toString().toIntOrNull()
            val userHeight = paint_view_height.text.toString().toIntOrNull()
            if (userWidth != null && userHeight != null) {
                val params = LinearLayout.LayoutParams(userWidth, userHeight)
                params.leftMargin = dpToPx(16f)
                params.rightMargin = dpToPx(16f)
                params.topMargin = dpToPx(36f)
                paint_view.layoutParams = params
                paint_view.setGenerateImageSize(userWidth, userHeight)
            }
        }

        paint_view_width.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userWidth = s.toString().toIntOrNull()
                userWidth?.let {
                    if (it > freeWidth) {
                        Toast.makeText(
                            applicationContext,
                            "Image width should be less than screen width",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        paint_view_height.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userHeight = s.toString().toIntOrNull()
                userHeight?.let {
                    if (it > freeHeight) {
                        Toast.makeText(
                            applicationContext,
                            "Image height should be less than screen height",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        speed_control.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                paint_view.animationCoefficient = 1f / progress
                Toast.makeText(applicationContext, progress.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        ArrayAdapter.createFromResource(
            this,
            R.array.algorithms_names,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            algorithm_picker.adapter = adapter
            algorithm_picker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    /*Nothing*/
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val algorithms = resources.getStringArray(R.array.algorithms_names)
                    val item = algorithms[position]
                    paint_view.fillType = AlgorithmType.valueOf(item.toUpperCase())
                }
            }
        }

    }

    private fun calculateScreenSize() {
        val firstHeightParam = algorithm_picker.height
        val secondHeightParam = generate_button.height
        val thirdHeightParam = speed_hint.height
        val fourthHeightParam = screen_size_hint.height
        val marginsSum = dpToPx(170f)
        freeHeight =
            getScreenSize().second - firstHeightParam - secondHeightParam - thirdHeightParam - fourthHeightParam - marginsSum
        freeWidth = getScreenSize().first - dpToPx(32f)
    }

}
