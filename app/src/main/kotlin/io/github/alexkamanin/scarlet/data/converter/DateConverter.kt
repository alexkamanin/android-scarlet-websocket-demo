package io.github.alexkamanin.scarlet.data.converter

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateConverter @Inject constructor() {

	private companion object {

		val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
		val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
	}

	fun convert(dateValue: String): String {
		val date: Date = inputFormat.parse(dateValue) as Date
		return outputFormat.format(date)
	}
}