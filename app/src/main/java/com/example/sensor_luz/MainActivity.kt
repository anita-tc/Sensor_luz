
package com.example.sensor_luz

import android.os.Bundle
import android.graphics.Color
import android.view.View
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tvValores: TextView
    private lateinit var tvEstado: TextView

    private lateinit var sensorManager: SensorManager

    // Crear objeto para ocupar el sensor de luz
    private var SensorLuz: Sensor? = null

    // Layout principal
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvEstado = findViewById(R.id.tvEstado)
        tvValores = findViewById(R.id.tvValores)
        layout = findViewById(R.id.main)

        // Permitir el uso de los servicios de hardware
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Seleccionar el sensor de luz
        SensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Validar si existe el sensor
        if (SensorLuz == null) {
            tvEstado.text = "No hay sensor de luz"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    // Métodos para el uso del sensor
    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {

            val luz = event.values[0]

            tvValores.text = "Luz: $luz lx"

            if (luz < 50) {

                // Activar modo oscuro
                layout.setBackgroundColor(Color.BLACK)

                tvEstado.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)

                tvEstado.text = "Modo actual: Oscuro"

            } else if (luz > 500) {

                // Activar modo claro
                layout.setBackgroundColor(Color.WHITE)

                tvEstado.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)

                tvEstado.text = "Modo actual: Claro"
            }
        }
    }

    // Método para medir la precisión del sensor
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    // Método para activar el sensor
    override fun onResume() {
        super.onResume()

        SensorLuz?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    // Método para detener el sensor
    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }
}
