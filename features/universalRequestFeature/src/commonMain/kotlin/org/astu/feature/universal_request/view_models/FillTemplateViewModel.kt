package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
//import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.launch
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.exceptions.ApiException
import org.astu.infrastructure.utils.file.FileUtils

class FillTemplateViewModel(private val templateDTO: TemplateDTO) :
    StateScreenModel<FillTemplateViewModel.State>(State.Init), JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object Show : State()
        data object Done : State()
    }

    val error = mutableStateOf<String?>(null)

    val templateFields = mutableStateOf(listOf<TemplateFieldDTO>())
    val name = templateDTO.name
    val description = templateDTO.description
    val outputPath = mutableStateOf("")


    init {
        val fields = mutableListOf<TemplateFieldDTO>()
        templateDTO.fields.forEach { field ->
            fields.add(TemplateFieldDTO(field, ""))
        }
        templateFields.value = fields
        mutableState.value = State.Show
    }

    fun fill(){
        templateFields.value.forEach { field ->
            if(field.name.lowercase() == "ооо название организации".lowercase())
                updateField(field.copy(value = "ООО Кристалл"))
            if(field.name.lowercase() == "фио студента".lowercase())
                updateField(field.copy(value = "Усманов Азим Маннурович"))
            if(field.name.lowercase() == "последняя цифра направления".lowercase())
                updateField(field.copy(value = "4"))
            if(field.name.lowercase() == "название направления".lowercase())
                updateField(field.copy(value = "Программная инженерия"))
            if(field.name.lowercase() == "Номер договора".lowercase())
                updateField(field.copy(value = "115545215"))
            if(field.name.lowercase() == "День начала договора".lowercase())
                updateField(field.copy(value = "04"))
            if(field.name.lowercase() == "Месяц начала договора".lowercase())
                updateField(field.copy(value = "04"))
            if(field.name.lowercase() == "Две цифры года начала договора".lowercase())
                updateField(field.copy(value = "23"))
            if(field.name.lowercase() == "Тип практики".lowercase())
                updateField(field.copy(value = "производственная"))
            if(field.name.lowercase() == "День начала практики".lowercase())
                updateField(field.copy(value = "05"))
            if(field.name.lowercase() == "Месяц начала практики".lowercase())
                updateField(field.copy(value = "05"))
            if(field.name.lowercase() == "Две цифры года начала практики".lowercase())
                updateField(field.copy(value = "23"))
            if(field.name.lowercase() == "День конца практики".lowercase())
                updateField(field.copy(value = "06"))
            if(field.name.lowercase() == "Месяц конца практики".lowercase())
                updateField(field.copy(value = "06"))
            if(field.name.lowercase() == "Две цифры года конца практики".lowercase())
                updateField(field.copy(value = "23"))

            if(field.name.lowercase() == "Номер курса".lowercase())
                updateField(field.copy(value = "4"))

            if(field.name.lowercase() == "цифра направления".lowercase())
                updateField(field.copy(value = "4"))
            if(field.name.lowercase() == "направление".lowercase())
                updateField(field.copy(value = "Программная инженерия"))

            if(field.name.lowercase() == "форма обучения".lowercase())
                updateField(field.copy(value = "очная"))

            if(field.name.lowercase() == "название организации".lowercase())
                updateField(field.copy(value = "ООО Кристалл"))

            if(field.name.lowercase() == "День договора".lowercase())
                updateField(field.copy(value = "04"))
            if(field.name.lowercase() == "Месяц договора".lowercase())
                updateField(field.copy(value = "04"))
            if(field.name.lowercase() == "год договора".lowercase())
                updateField(field.copy(value = "23"))

            if(field.name.lowercase() == "Год начала практики".lowercase())
                updateField(field.copy(value = "2023"))

            if(field.name.lowercase() == "Год конца практики".lowercase())
                updateField(field.copy(value = "2023"))

            if(field.name.lowercase() == "Руководитель практики от университета".lowercase())
                updateField(field.copy(value = "Морозов А. В."))

            if(field.name.lowercase() == "Директор ИИТИК".lowercase())
                updateField(field.copy(value = "Белов С. В."))

            if(field.name.lowercase() == "День конца практики".lowercase())
                updateField(field.copy(value = "06"))
            if(field.name.lowercase() == "Месяц конца практики".lowercase())
                updateField(field.copy(value = "06"))
            if(field.name.lowercase() == "Две цифры года конца практики".lowercase())
                updateField(field.copy(value = "23"))
        }
    }

    fun updateField(field: TemplateFieldDTO) {
        templateFields.value = templateFields.value.map { toReplace ->
            if (toReplace.name == field.name) {
                return@map field
            }
            toReplace
        }
    }

    fun fillTemplate() {
        val repository by GlobalDIContext.inject<UniversalTemplateRepository>()
        val isNotFilled = templateFields.value.any{it.value.isBlank()}

        if(isNotFilled) {
            error.value = "Не все поля заполнены"
            return
        }

        screenModelScope.launch {
            runCatching {
                repository.fillTemplate(templateDTO, templateFields.value)
            }.onFailure {
                when(it){
                    is ApiException -> error.value = it.message
                    else -> {
                        error.value = "Что-то случилось при скачивании :("
                    }
                }

            }.onSuccess { bytes ->
                FileUtils.saveFile(bytes, templateDTO.name,"docx")
            }
        }
    }

    fun setOutputPath(string: String) {
        outputPath.value = string
    }
}