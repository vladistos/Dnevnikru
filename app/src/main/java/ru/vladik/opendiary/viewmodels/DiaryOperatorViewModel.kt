package ru.vladik.opendiary.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.vladik.opendiary.viewmodels.errorhandling.ResourceLiveData


/**

Запускает блок кода в отдельном потоке, обрабатывает и отправляет данные в [resourceData].
* @param resourceData LiveData для отправки данных.
* @param block Блок кода для выполнения.
 */
fun <T> ViewModel.launchToLiveData(resourceData: ResourceLiveData<T>, block: suspend () -> T?) = viewModelScope.launchAsync {
    try {
        resourceData.postResProgress(0F)
        val value = block()
        if (value != null) {
            resourceData.postResValue(value)
        } else {
            resourceData.postResError("Resource is null", NullPointerException())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        resourceData.postResError(e.message.orEmpty(), e)
    }
}
