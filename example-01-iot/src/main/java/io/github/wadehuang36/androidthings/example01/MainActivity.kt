package io.github.wadehuang36.androidthings.example01

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import java.io.IOException

private const val LED_PIN_NAME = "BCM18" // !!! Change to the pin name you use.
private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity() {
    private var ledGpio: Gpio? = null
    private lateinit var ledSwitch: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = PeripheralManager.getInstance()
        try {
            ledGpio = manager.openGpio(LED_PIN_NAME)

            ledGpio?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

            ledSwitch = findViewById(R.id.ledSwitch)

            ledSwitch.isChecked = ledGpio!!.value

            ledSwitch.setOnCheckedChangeListener { _, isChecked ->
                Log.d(TAG, "Led is ${if (isChecked) "on" else "off"}")
                ledGpio?.value = isChecked
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error on PeripheralIO API", e)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            ledGpio?.value = false
            ledGpio?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error on PeripheralIO API", e)
        }
    }
}
