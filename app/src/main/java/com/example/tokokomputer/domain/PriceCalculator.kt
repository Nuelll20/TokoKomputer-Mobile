package com.example.tokokomputer.domain

import com.example.tokokomputer.model.ComputerSpecification
import com.example.tokokomputer.model.DeviceType
import com.example.tokokomputer.model.PriceDetail
import com.example.tokokomputer.model.PriceEstimate
import com.example.tokokomputer.model.StorageCapacity
import com.example.tokokomputer.model.StorageType
import java.text.NumberFormat
import java.util.Locale

object PriceCalculator {

    fun calculate(
        specification: ComputerSpecification
    ): PriceEstimate {
        val deviceType = requireNotNull(specification.deviceType) {
            "Jenis perangkat belum dipilih."
        }

        val processor = requireNotNull(specification.processor) {
            "Prosesor belum dipilih."
        }

        val memory = requireNotNull(specification.memory) {
            "Memori belum dipilih."
        }

        val storageType = requireNotNull(specification.storageType) {
            "Tipe penyimpanan belum dipilih."
        }

        val storageCapacity = requireNotNull(specification.storageCapacity) {
            "Kapasitas penyimpanan belum dipilih."
        }

        val monitor = requireNotNull(specification.monitor) {
            "Ukuran monitor belum dipilih."
        }

        val graphicsCard = requireNotNull(specification.graphicsCard) {
            "Kartu grafis belum dipilih."
        }

        require(monitor.supportedDevice == deviceType) {
            "Ukuran monitor tidak sesuai dengan jenis perangkat."
        }

        require(
            storageType != StorageType.HDD ||
                    storageCapacity != StorageCapacity.GB_256
        ) {
            "HDD 256 GB tidak tersedia."
        }

        val storagePrice = calculateStoragePrice(
            type = storageType,
            capacity = storageCapacity
        )

        val details = listOf(
            PriceDetail(
                component = "Jenis perangkat",
                selection = deviceType.label,
                price = deviceType.basePrice
            ),
            PriceDetail(
                component = "Prosesor",
                selection = processor.label,
                price = processor.price
            ),
            PriceDetail(
                component = "Memori",
                selection = memory.label,
                price = memory.price
            ),
            PriceDetail(
                component = "Penyimpanan",
                selection = "${storageType.label} ${storageCapacity.label}",
                price = storagePrice
            ),
            PriceDetail(
                component = "Monitor",
                selection = monitor.label,
                price = monitor.price
            ),
            PriceDetail(
                component = "Kartu grafis",
                selection = graphicsCard.label,
                price = graphicsCard.price
            )
        )

        return PriceEstimate(details = details)
    }

    private fun calculateStoragePrice(
        type: StorageType,
        capacity: StorageCapacity
    ): Long {
        return when (type) {
            StorageType.HDD -> when (capacity) {
                StorageCapacity.GB_256 -> 300_000L
                StorageCapacity.GB_512 -> 450_000L
                StorageCapacity.TB_1 -> 650_000L
                StorageCapacity.TB_2 -> 1_100_000L
            }

            StorageType.SSD_SATA -> when (capacity) {
                StorageCapacity.GB_256 -> 450_000L
                StorageCapacity.GB_512 -> 750_000L
                StorageCapacity.TB_1 -> 1_250_000L
                StorageCapacity.TB_2 -> 2_200_000L
            }

            StorageType.SSD_NVME -> when (capacity) {
                StorageCapacity.GB_256 -> 550_000L
                StorageCapacity.GB_512 -> 900_000L
                StorageCapacity.TB_1 -> 1_600_000L
                StorageCapacity.TB_2 -> 3_000_000L
            }
        }
    }

    fun formatRupiah(price: Long): String {
        return NumberFormat
            .getCurrencyInstance(Locale("id", "ID"))
            .format(price)
            .replace(",00", "")
    }
}