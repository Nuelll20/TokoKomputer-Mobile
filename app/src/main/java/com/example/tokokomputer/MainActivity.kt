
package com.example.tokokomputer

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

import com.example.tokokomputer.data.SpecificationCatalog
import com.example.tokokomputer.domain.PriceCalculator
import com.example.tokokomputer.model.*
import com.example.tokokomputer.viewmodel.LoginViewModel
import com.example.tokokomputer.viewmodel.ComputerViewModel

class MainActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var computerViewModel: ComputerViewModel

    private lateinit var loginScreen: View
    private lateinit var calculatorScreen: View

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvLoginError: TextView
    private lateinit var tvWelcome: TextView

    private lateinit var spinnerDeviceType: Spinner
    private lateinit var spinnerProcessor: Spinner
    private lateinit var spinnerMemory: Spinner
    private lateinit var spinnerStorageType: Spinner
    private lateinit var spinnerStorageCapacity: Spinner
    private lateinit var spinnerMonitor: Spinner
    private lateinit var spinnerGraphicsCard: Spinner

    private lateinit var tvComputerError: TextView
    private lateinit var tvIncompleteHint: TextView
    private lateinit var btnCalculate: Button
    private lateinit var resultCard: View
    private lateinit var tvTotalPrice: TextView
    private lateinit var priceDetailsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.parseColor("#2563EB")
        window.navigationBarColor = Color.WHITE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]

        computerViewModel =
            ViewModelProvider(this)[ComputerViewModel::class.java]

        bindViews()
        setupListeners()

        renderLoginState(loginViewModel.uiState.value)
        renderComputerState(computerViewModel.uiState.value)
    }

    private fun bindViews() {

        loginScreen = findViewById(R.id.loginScreen)
        calculatorScreen = findViewById(R.id.calculatorScreen)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvLoginError = findViewById(R.id.tvLoginError)

        tvWelcome = findViewById(R.id.tvWelcome)

        spinnerDeviceType = findViewById(R.id.spinnerDeviceType)
        spinnerProcessor = findViewById(R.id.spinnerProcessor)
        spinnerMemory = findViewById(R.id.spinnerMemory)
        spinnerStorageType = findViewById(R.id.spinnerStorageType)
        spinnerStorageCapacity = findViewById(R.id.spinnerStorageCapacity)
        spinnerMonitor = findViewById(R.id.spinnerMonitor)
        spinnerGraphicsCard = findViewById(R.id.spinnerGraphicsCard)

        tvComputerError = findViewById(R.id.tvComputerError)
        tvIncompleteHint = findViewById(R.id.tvIncompleteHint)

        btnCalculate = findViewById(R.id.btnCalculate)

        resultCard = findViewById(R.id.resultCard)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        priceDetailsContainer =
            findViewById(R.id.priceDetailsContainer)
    }

    private fun setupListeners() {

        // LOGIN

        findViewById<Button>(R.id.btnLogin).setOnClickListener {

            loginViewModel.login(
                usernameOrEmail = etUsername.text.toString(),
                password = etPassword.text.toString()
            )

            renderLoginState(loginViewModel.uiState.value)
        }

        // LOGOUT

        findViewById<Button>(R.id.btnLogout).setOnClickListener {

            loginViewModel.logout()

            renderLoginState(loginViewModel.uiState.value)
        }

        // CALCULATE PRICE

        btnCalculate.setOnClickListener {

            computerViewModel.calculatePrice()

            renderComputerState(computerViewModel.uiState.value)
        }

        // RESET CONFIGURATION

        findViewById<Button>(R.id.btnReset).setOnClickListener {

            computerViewModel.resetConfiguration()

            renderComputerState(computerViewModel.uiState.value)
        }
    }

    // LOGIN UI

    private fun renderLoginState(state: com.example.tokokomputer.viewmodel.LoginUiState) {

        if (state.isLoggedIn && state.user != null) {

            loginScreen.visibility = View.GONE
            calculatorScreen.visibility = View.VISIBLE

            tvWelcome.text = "Halo, ${state.user.displayName}"

        } else {

            loginScreen.visibility = View.VISIBLE
            calculatorScreen.visibility = View.GONE
        }

        if (state.errorMessage != null) {

            tvLoginError.text = state.errorMessage
            tvLoginError.visibility = View.VISIBLE

        } else {

            tvLoginError.text = ""
            tvLoginError.visibility = View.GONE
        }
    }

    // COMPUTER CONFIGURATION UI

    private fun renderComputerState(
        state: com.example.tokokomputer.viewmodel.ComputerUiState
    ) {

        val specification = state.specification

        // DEVICE TYPE

        configureSpinner(
            spinner = spinnerDeviceType,
            items = SpecificationCatalog.deviceTypes,
            selected = specification.deviceType,
            hint = "Pilih jenis perangkat",
            label = { it.label }
        ) {
            computerViewModel.selectDeviceType(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // PROCESSOR

        configureSpinner(
            spinner = spinnerProcessor,
            items = SpecificationCatalog.processors,
            selected = specification.processor,
            hint = "Pilih prosesor",
            label = { it.label }
        ) {
            computerViewModel.selectProcessor(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // MEMORY

        configureSpinner(
            spinner = spinnerMemory,
            items = SpecificationCatalog.memoryOptions,
            selected = specification.memory,
            hint = "Pilih kapasitas RAM",
            label = { it.label }
        ) {
            computerViewModel.selectMemory(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // STORAGE TYPE

        configureSpinner(
            spinner = spinnerStorageType,
            items = SpecificationCatalog.storageTypes,
            selected = specification.storageType,
            hint = "Pilih tipe penyimpanan",
            label = { it.label }
        ) {
            computerViewModel.selectStorageType(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // STORAGE CAPACITY

        val capacityOptions =
            specification.storageType?.let {
                SpecificationCatalog.storageCapacityOptions(it)
            } ?: emptyList()

        configureSpinner(
            spinner = spinnerStorageCapacity,
            items = capacityOptions,
            selected = specification.storageCapacity,
            hint = "Pilih kapasitas penyimpanan",
            label = { it.label }
        ) {
            computerViewModel.selectStorageCapacity(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // MONITOR

        val monitorOptions =
            specification.deviceType?.let {
                SpecificationCatalog.monitorOptions(it)
            } ?: emptyList()

        configureSpinner(
            spinner = spinnerMonitor,
            items = monitorOptions,
            selected = specification.monitor,
            hint = "Pilih ukuran monitor",
            label = { it.label }
        ) {
            computerViewModel.selectMonitor(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // GRAPHICS CARD

        configureSpinner(
            spinner = spinnerGraphicsCard,
            items = SpecificationCatalog.graphicsCards,
            selected = specification.graphicsCard,
            hint = "Pilih kartu grafis",
            label = { it.label }
        ) {
            computerViewModel.selectGraphicsCard(it)
            renderComputerState(computerViewModel.uiState.value)
        }

        // ERROR MESSAGE

        if (state.errorMessage != null) {

            tvComputerError.text = state.errorMessage
            tvComputerError.visibility = View.VISIBLE

        } else {

            tvComputerError.text = ""
            tvComputerError.visibility = View.GONE
        }

        // COMPLETION STATUS

        tvIncompleteHint.visibility =
            if (state.canCalculate) View.GONE else View.VISIBLE

        btnCalculate.isEnabled = state.canCalculate

        btnCalculate.alpha =
            if (state.canCalculate) 1f else 0.5f

        // PRICE RESULT

        if (state.priceEstimate != null) {

            resultCard.visibility = View.VISIBLE

            tvTotalPrice.text = state.formattedTotalPrice

            renderPriceDetails(state.priceEstimate.details)

        } else {

            resultCard.visibility = View.GONE

            priceDetailsContainer.removeAllViews()
        }
    }

    // SPINNER HELPER

    private fun <T> configureSpinner(
        spinner: Spinner,
        items: List<T>,
        selected: T?,
        hint: String,
        label: (T) -> String,
        onSelected: (T) -> Unit
    ) {

        val labels = mutableListOf(hint)

        labels.addAll(items.map(label))

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            labels
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.onItemSelectedListener = null
        spinner.adapter = adapter

        val selectedIndex = selected?.let {
            val index = items.indexOf(it)
            if (index >= 0) index + 1 else 0
        } ?: 0

        spinner.setSelection(selectedIndex, false)

        spinner.isEnabled = items.isNotEmpty()

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position == 0) return

                    val selectedItem = items[position - 1]

                    if (selectedItem != selected) {
                        onSelected(selectedItem)
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {}
            }
    }

    // PRICE DETAILS

    private fun renderPriceDetails(
        details: List<PriceDetail>
    ) {

        priceDetailsContainer.removeAllViews()

        details.forEach { detail ->

            val row = LinearLayout(this).apply {

                orientation = LinearLayout.HORIZONTAL

                gravity = Gravity.CENTER_VERTICAL

                setPadding(
                    0,
                    10.dp(),
                    0,
                    10.dp()
                )
            }

            val information = TextView(this).apply {

                text = "${detail.component}\n${detail.selection}"

                setTextColor(Color.parseColor("#E2E8F0"))

                textSize = 12f
            }

            val price = TextView(this).apply {

                text = PriceCalculator.formatRupiah(detail.price)

                setTextColor(Color.WHITE)

                textSize = 13f

                setTypeface(null, android.graphics.Typeface.BOLD)

                gravity = Gravity.END
            }

            row.addView(
                information,
                LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
            )

            row.addView(
                price,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )

            priceDetailsContainer.addView(row)

            val divider = View(this).apply {
                setBackgroundColor(
                    Color.parseColor("#354563")
                )
            }

            priceDetailsContainer.addView(
                divider,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.dp()
                )
            )
        }
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
