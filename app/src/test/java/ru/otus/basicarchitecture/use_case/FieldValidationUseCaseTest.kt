package ru.otus.basicarchitecture.use_case

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("FieldValidationUseCase should")
class FieldValidationUseCaseTest {

    private val fieldValidationUseCase: FieldValidationUseCase = FieldValidationUseCase.Impl()

    @Nested
    @DisplayName("validate name correctly")
    inner class NameValidationTest {

        @ParameterizedTest(name = "return true for invalid name: {0}")
        @ValueSource(
            strings = [
                "111",
                "asd--",
                "asd-",
                "Abcde12345!",
                "jo..hn",
                "ma__rk"
            ]
        )
        fun `isNameInvalid returns true for invalid names`(name: String) {
            assertTrue(fieldValidationUseCase.isNameInvalid(name), "Expected '$name' to be invalid")
        }

        @ParameterizedTest(name = "return false for valid name: {0}")
        @ValueSource(
            strings = [
                "John",
                "Alice",
                "Иван",
                "Мария",
                "Jo",
                "A",
                "Xy"
            ]
        )
        fun `isNameInvalid returns false for valid names`(name: String) {
            assertFalse(fieldValidationUseCase.isNameInvalid(name), "Expected '$name' to be valid")
        }
    }

    @Nested
    @DisplayName("validate surname correctly")
    inner class SurnameValidationTest {

        @ParameterizedTest(name = "return true for invalid surname: {0}")
        @ValueSource(
            strings = [
                "111",
                "doe--",
                "smi--thh",
                "o''neal",
                "m_r__r",
                "Verylongsurnamethatexceedstheeighteencharacters"
            ]
        )
        fun `isSurnameInvalid returns true for invalid surnames`(surname: String) {
            assertTrue(
                fieldValidationUseCase.isSurnameInvalid(surname),
                "Expected '$surname' to be invalid"
            )
        }

        @ParameterizedTest(name = "return false for valid surname: {0}")
        @ValueSource(
            strings = [
                "Doe",
                "Smith",
                "Петров",
                "Сидорова",
                "O'Neil",
                "Van",
                "Da",
                "Li"
            ]
        )
        fun `isSurnameInvalid returns false for valid surnames`(surname: String) {
            assertFalse(
                fieldValidationUseCase.isSurnameInvalid(surname),
                "Expected '$surname' to be valid"
            )
        }
    }

    @Nested
    @DisplayName("validate age correctly")
    inner class AgeValidationTest {

        @ParameterizedTest(name = "return true for future birth date: {0}")
        @ValueSource(
            longs = [
                1000L,
                1000000L,
                1000000000L
            ]
        )
        fun `isAgeInvalid returns true for future birth dates`(offset: Long) {
            val futureTimestamp = System.currentTimeMillis() + offset
            assertTrue(fieldValidationUseCase.isAgeInvalid(futureTimestamp))
        }

        @Test
        fun `isAgeInvalid returns true for null birthDate`() {
            assertTrue(fieldValidationUseCase.isAgeInvalid(null))
        }

        @Test
        fun `isAgeInvalid returns false for 18-year-old person`() {
            val eighteenYearsAgo =
                System.currentTimeMillis() - (18 * 365.25 * 24 * 60 * 60 * 1000).toLong()
            assertFalse(fieldValidationUseCase.isAgeInvalid(eighteenYearsAgo))
        }

        @Test
        fun `isAgeInvalid returns false for 19-year-old person`() {
            val nineteenYearsAgo =
                System.currentTimeMillis() - (19 * 365.25 * 24 * 60 * 60 * 1000).toLong()
            assertFalse(fieldValidationUseCase.isAgeInvalid(nineteenYearsAgo))
        }

        @Test
        fun `isAgeInvalid returns true for 17-year-old person`() {
            val seventeenYearsAgo =
                System.currentTimeMillis() - (17 * 365.25 * 24 * 60 * 60 * 1000).toLong()
            assertTrue(fieldValidationUseCase.isAgeInvalid(seventeenYearsAgo))
        }
    }
}