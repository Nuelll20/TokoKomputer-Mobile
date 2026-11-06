package com.example.tokokomputer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokokomputer.domain.PriceCalculator
import com.example.tokokomputer.model.ComputerSpecification
import com.example.tokokomputer.model.DeviceType
import com.example.tokokomputer.model.GraphicsCardOption
import com.example.tokokomputer.model.MemoryOption
import com.example.tokokomputer.model.MonitorOption
import com.example.tokokomputer.model.PriceEstimate
import com.example.tokokomputer.model.ProcessorOption
import com.example.tokokomputer.model.StorageCapacity
import com.example.tokokomputer.model.StorageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ComputerUiState(
    val specification: ComputerSpecification = ComputerSpecification(),
    val priceEstimate: PriceEstimate? = null,
    val errorMessage: String? = null
) {
    val canCalculate: Boolean
        get() = specification.isComplete

    val formattedTotalPrice: String
        get() = priceEstimate?.let {
            PriceCalculator.formatRupiah(it.totalPrice)
        } ?: ""
}

class ComputerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ComputerUiState())
    val uiState: StateFlow<ComputerUiState> = _uiState.asStateFlow()

    fun selectDeviceType(deviceType: DeviceType) {
        val currentSpecification = _uiState.value.specification

        val compatibleMonitor = currentSpecification.monitor
            ?.takeIf { it.supportedDevice == deviceType }

        updateSpecification(
            currentSpecification.copy(
                deviceType = deviceType,
                monitor = compatibleMonitor
            )
        )
    }

    fun selectProcessor(processor: ProcessorOption) {
        updateSpecification(
            _uiState.value.specification.copy(
                processor = processor
            )
        )
    }

    fun selectMemory(memory: MemoryOption) {
        updateSpecification(
            _uiState.value.specification.copy(
                memory = memory
            )
        )
    }

    fun selectStorageType(storageType: StorageType) {
        val currentSpecification = _uiState.value.specification

        val compatibleCapacity = currentSpecification.storageCapacity
            ?.takeUnless {
                storageType == StorageType.HDD &&
                        it == StorageCapacity.GB_256
            }

        updateSpecification(
            currentSpecification.copy(
                storageType = storageType,
                storageCapacity = compatibleCapacity
            )
        )
    }

    fun selectStorageCapacity(capacity: StorageCapacity) {
        updateSpecification(
            _uiState.value.specification.copy(
                storageCapacity = capacity
            )
        )
    }

    fun selectMonitor(monitor: MonitorOption) {
        val deviceType = _uiState.value.specification.deviceType

        if (deviceType != null && monitor.supportedDevice != deviceType) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Monitor tidak sesuai dengan jenis perangkat."
            )
            return
        }

        updateSpecification(
            _uiState.value.specification.copy(
                monitor = monitor
            )
        )
    }

    fun selectGraphicsCard(graphicsCard: GraphicsCardOption) {
        updateSpecification(
            _uiState.value.specification.copy(
                graphicsCard = graphicsCard
            )
        )
    }

    fun calculatePrice() {
        try {
            val estimate = PriceCalculator.calculate(
                _uiState.value.specification
            )

            _uiState.value = _uiState.value.copy(
                priceEstimate = estimate,
                errorMessage = null
            )
        } catch (exception: IllegalArgumentException) {
            _uiState.value = _uiState.value.copy(
                priceEstimate = null,
                errorMessage = exception.message
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun resetConfiguration() {
        _uiState.value = ComputerUiState()
    }

    private fun updateSpecification(
        specification: ComputerSpecification
    ) {
        _uiState.value = _uiState.value.copy(
            specification = specification,
            priceEstimate = null,
            errorMessage = null
        )
    }
}