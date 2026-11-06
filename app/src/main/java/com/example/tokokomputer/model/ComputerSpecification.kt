package com.example.tokokomputer.model

enum class DeviceType(
    val label: String,
    val basePrice: Long
){
    DESKTOP("Komputer Dekstop", 1_500_000L),
    NOTEBOOK("Notebook", 3_500_000L)
}

enum class ProcessorOption(
    val label: String,
    val price: Long
){
    INTEL_I3("Intel Core i3", 1_800_000L),
    INTEL_I5("Intel Core i5", 3_000_000L),
    INTEL_I7("Intel Core i7", 4_800_000L),
    RYZEN_3("AMD Ryzen 3", 1_600_000L),
    RYZEN_5("AMD Ryzen 5", 2_800_000L),
    RYZEN_7("AMD Ryzen 7", 4_500_000L)
}
enum class MemoryOption(
    val label: String,
    val price: Long
){
    RAM_8_GB("8 GB", 400_000L),
    RAM_16_GB("16 GB", 750_000L),
    RAM_32_GB("32 GB", 1_400_000L)
}

enum class StorageType(
    val label: String
){
    HDD("HDD"),
    SSD_SATA("SSD SATA"),
    SSD_NVME("SSD NVMe")
}

enum class StorageCapacity(
    val label: String
){
    GB_256("256 GB"),
    GB_512("512 GB"),
    TB_1("1 TB"),
    TB_2("2 TB")
}

enum class MonitorOption(
    val label: String,
    val price: Long,
    val supportedDevice: DeviceType
){
    NOTEBOOK_14("14 inci", 1_200_200L, DeviceType.NOTEBOOK),
    NOTEBOOK_15("15,6 inci", 1_500_000L, DeviceType.NOTEBOOK),
    NOTEBOOK_16("16 inci", 1_900_000L, DeviceType.NOTEBOOK),
    DESKTOP_22("22 inci", 1_600_000L, DeviceType.DESKTOP),
    DESKTOP_24("24 inci", 2_000_000L, DeviceType.DESKTOP),
    DESKTOP_27("27 inci", 3_000_000L, DeviceType.DESKTOP)
}

enum class GraphicsCardOption(
    val label: String,
    val price: Long
) {
    INTEGRATED("Grafis Terintegrasi", 0L),
    GTX_1650("NVIDIA GTX 1650", 2_500_000L),
    RTX_3050("NVIDIA RTX 3050", 4_000_000L),
    RTX_4060("NVIDIA RTX 4060", 6_500_000L),
    RX_6600("AMD Radeon RX 6600", 3_600_000L)
}

data class ComputerSpecification(
    val deviceType: DeviceType? = null,
    val processor: ProcessorOption? = null,
    val memory: MemoryOption? = null,
    val storageType: StorageType? = null,
    val storageCapacity: StorageCapacity? = null,
    val monitor: MonitorOption? = null,
    val graphicsCard: GraphicsCardOption? = null
) {
    val isComplete: Boolean
        get() = deviceType != null &&
                processor != null &&
                memory != null &&
                storageType != null &&
                storageCapacity != null &&
                monitor != null &&
                graphicsCard != null
}

data class PriceDetail(
    val component: String,
    val selection: String,
    val price: Long
)

data class PriceEstimate(
    val details: List<PriceDetail>
) {
    val totalPrice: Long
        get() = details.sumOf { it.price }
}