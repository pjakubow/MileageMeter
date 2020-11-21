package pl.swapps.mileagemeter

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

class MileageCalculator(
    private val declaredYearlyMileage: Long,
    private val contractLength: Int,
    private val contractStartDate: ZonedDateTime
) {

    fun calculateCurrentTarget(now: ZonedDateTime = ZonedDateTime.now()): Mileage {
        val endOfContract = contractStartDate.plusYears(contractLength.toLong())
        val daysInContract = ChronoUnit.DAYS.between(contractStartDate, endOfContract)
        val mileagePerDay = declaredYearlyMileage.toDouble() * contractLength / daysInContract
        val daysSinceContractStarted = ChronoUnit.DAYS.between(contractStartDate, now)
        val targetMileage = mileagePerDay * daysSinceContractStarted
        return Mileage(now, floor(targetMileage).toLong())
    }
}

data class Mileage(val targetDate: ZonedDateTime, val targetMileage: Long)