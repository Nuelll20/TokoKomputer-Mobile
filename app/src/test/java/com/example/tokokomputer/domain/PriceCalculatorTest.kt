package com.example.tokokomputer.domain

import com.example.tokokomputer.model.ComputerSpecification
import com.example.tokokomputer.model.DeviceType
import com.example.tokokomputer.model.GraphicsCardOption
import com.example.tokokomputer.model.MemoryOption
import com.example.tokokomputer.model.MonitorOption
import com.example.tokokomputer.model.ProcessorOption
import com.example.tokokomputer.model.StorageCapacity
import com.example.tokokomputer.model.StorageType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PriceCalculatorTest {

    @Test
    fun completeDesktopSpecification_returnsCorrectPrice() {
        val specification = ComputerSpecification(
            deviceType = DeviceType.DESKTOP,
            processor = ProcessorOption.INTEL_I5,
            memory = MemoryOption.RAM_16_GB,
            storageType = StorageType.SSD_NVME,
            storageCapacity = StorageCapacity.TB_1,
            monitor = MonitorOption.DESKTOP_24,
            graphicsCard = GraphicsCardOption.RTX_3050
        )

        val result = PriceCalculator.calculate(specification)

        assertEquals(
            12_850_000L,
            result.totalPrice
        )
    }

    @Test
    fun incompleteSpecification_throwsException() {
        val incompleteSpecification = ComputerSpecification(
            deviceType = DeviceType.DESKTOP
        )

        assertThrows(IllegalArgumentException::class.java) {
            PriceCalculator.calculate(incompleteSpecification)
        }
    }

    @Test
    fun notebookMonitorForDesktop_throwsException() {
        val specification = ComputerSpecification(
            deviceType = DeviceType.DESKTOP,
            processor = ProcessorOption.INTEL_I5,
            memory = MemoryOption.RAM_16_GB,
            storageType = StorageType.SSD_NVME,
            storageCapacity = StorageCapacity.TB_1,
            monitor = MonitorOption.NOTEBOOK_14,
            graphicsCard = GraphicsCardOption.RTX_3050
        )

        assertThrows(IllegalArgumentException::class.java) {
            PriceCalculator.calculate(specification)
        }
    }
}