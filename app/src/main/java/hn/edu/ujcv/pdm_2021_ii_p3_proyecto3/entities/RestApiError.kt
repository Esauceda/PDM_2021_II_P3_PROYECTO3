package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

data class RestApiError(val httpStatus: String, val errorMessage: String, val errorDetails: String) {
}