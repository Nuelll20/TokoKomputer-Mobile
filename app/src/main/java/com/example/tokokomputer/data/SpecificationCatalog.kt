package com.example.tokokomputer.data

import com.example.tokokomputer.model.DeviceType
import com.example.tokokomputer.model.GraphicsCardOption
import com.example.tokokomputer.model.MemoryOption
import com.example.tokokomputer.model.MonitorOption
import com.example.tokokomputer.model.ProcessorOption
import com.example.tokokomputer.model.StorageCapacity
import com.example.tokokomputer.model.StorageType

object SpecificationCatalog {

    val deviceTypes: List<DeviceType> =
        DeviceType.values().toList()

    val processors: List<ProcessorOption> =
        ProcessorOption.values().toList()

    val memoryOptions: List<MemoryOption> =
        MemoryOption.values().toList()

    val storageTypes: List<StorageType> =
        StorageType.values().toList()

    val graphicsCards: List<GraphicsCardOption> =
        GraphicsCardOption.values().toList()

    fun monitorOptions(deviceType: DeviceType): List<MonitorOption> {
        return MonitorOption.values()
            .filter { it.supportedDevice == deviceType }
    }

    fun storageCapacityOptions(
        storageType: StorageType
    ): List<StorageCapacity> {
        return when (storageType) {
            StorageType.HDD -> listOf(
                StorageCapacity.GB_512,
                StorageCapacity.TB_1,
                StorageCapacity.TB_2
            )

            StorageType.SSD_SATA,
            StorageType.SSD_NVME -> StorageCapacity.values().toList()
        }
    }
}