package com.example.final_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var expression = ""
    private lateinit var etInput : EditText
    private lateinit var reset : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etInput = findViewById(R.id.etInput)
        reset = findViewById(R.id.Reset)

        reset.setOnClickListener {
            expression = ""
            etInput.setText("")
        }
    }

    fun onNumberClick(view: View) {
        val button = view as Button
        val number = button.text.toString()
        expression += number
        etInput.setText(expression)
    }

    fun onOperatorClick(view: View) {
        val button = view as Button
        val operator = button.text.toString()
        expression += operator
        etInput.setText(expression)
    }

    fun onEqualClick(view: View) {
        try {
            val result = evalExpression(expression)
            etInput.setText(result.toString())
            expression = ""
        } catch (e: Exception) {
            etInput.setText("Error")
        }
    }

    private fun evalExpression(expression: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm()
                    else if (eat('-'.toInt())) x -= parseTerm()
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor()
                    else if (eat('/'.toInt())) x /= parseFactor()
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor()
                if (eat('-'.toInt())) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('('.toInt())) {
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch in '0'.toInt()..'9'.toInt() || ch == '.'.toInt()) {
                    while (ch in '0'.toInt()..'9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = expression.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }

                return x
            }
        }.parse()
    }

    override fun onResume() {
        super.onResume()
        setButtonClickListeners()
    }

    private fun setButtonClickListeners() {
        val buttons = listOf<Button>(
            findViewById(R.id.btnZero),
            findViewById(R.id.btnOne),
            findViewById(R.id.btnTwo),
            findViewById(R.id.btnThree),
            findViewById(R.id.btnFour),
            findViewById(R.id.btnFive),
            findViewById(R.id.btnSix),
            findViewById(R.id.btnSeven),
            findViewById(R.id.btnEight),
            findViewById(R.id.btnNine),
            findViewById(R.id.btnDot),
            findViewById(R.id.btnPlus),
            findViewById(R.id.btnMinus),
            findViewById(R.id.btnMultiply),
            findViewById(R.id.btnDivide)
        )

        for (button in buttons) {
            button.setOnClickListener {
                onNumberClick(it)
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            onEqualClick(it)
        }
    }
}