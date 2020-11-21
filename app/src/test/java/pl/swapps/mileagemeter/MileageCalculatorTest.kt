package pl.swapps.mileagemeter

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.converters.ConversionFailedException
import junitparams.converters.Converter
import junitparams.converters.Param
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

@RunWith(JUnitParamsRunner::class)
class MileageCalculatorTest {

    private val January_1_2020 = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))

    @Test
    @Parameters(
        "10000, 1, 2020-02-01T10:15:30+01:00[Europe/Warsaw], 846",
        "20000, 1, 2020-02-01T10:15:30+01:00[Europe/Warsaw], 1693",
        "20000, 1, 2021-01-01T10:15:30+01:00[Europe/Warsaw], 20000",
        "20000, 3, 2023-01-01T10:15:30+01:00[Europe/Warsaw], 60000",
        "20000, 1, 2020-07-01T10:15:30+02:00[Europe/Warsaw], 9945"
    )
    fun `should calculate correct mileage`(
        declaredYearlyMileage: Long,
        contractLength: Int,
        @Param(converter = ZonedDateTimeConverter::class) now: ZonedDateTime,
        expectedMileage: Long
    ) {
        // given
        val calculator = MileageCalculator(declaredYearlyMileage, contractLength, January_1_2020)

        // when
        val mileage = calculator.calculateCurrentTarget(now)

        // then
        assertThat(mileage.targetMileage, `is`(expectedMileage))
    }
}

class ZonedDateTimeConverter: Converter<Param, ZonedDateTime> {

    override fun initialize(annotation: Param?) {

    }

    override fun convert(param: Any?): ZonedDateTime {
        try {
            return ZonedDateTime.parse(param.toString())
        } catch (e: DateTimeParseException) {
            throw ConversionFailedException(e.localizedMessage);
        }
    }

}