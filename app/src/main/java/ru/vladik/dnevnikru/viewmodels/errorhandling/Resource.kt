package ru.vladik.dnevnikru.viewmodels.errorhandling

/**

Класс представляет ресурс, содержащий данные определенного типа [T].

Содержит информацию об ошибке [error], значении [value] и прогрессе загрузки [loadingProgress].

Состояние ресурса определяется [state] ([State]).
 */
class Resource<T> {

    /**

    Ошибка, которая возникла при загрузке ресурса.
     */
    var error: ResourceError? = null
        private set
    /**

    Значение ресурса.
     */
    var value: T? = null
        private set
    /**

    Прогресс загрузки ресурса.
     */
    var loadingProgress: Float = 0.0F
        private set
    /**

    Состояние ресурса: [State.LOADING] - ресурс загружается, [State.READY] - ресурс готов к использованию,
    [State.ERROR] - при загрузке возникла ошибка.
     */
    var state: State = State.LOADING
        private set
    /**

    Опубликовать сообщение об ошибке [message] и [cause] и установить состояние ресурса в [State.ERROR].
     */
    fun postError(message: String, cause: Throwable?) {
        this.state = State.ERROR
        this.error = ResourceError(message, cause)
    }
    /**

    Опубликовать значение ресурса [value] и установить состояние ресурса в [State.READY].
     */
    fun postValue(value: T) {
        this.state = State.READY
        this.value = value
    }
    /**

    Опубликовать прогресс загрузки ресурса [loadingProgress] и установить состояние ресурса в [State.LOADING].
     */
    fun postLoading(loadingProgress: Float) {
        this.state = State.LOADING
        this.loadingProgress = loadingProgress
    }
    /**

    Внутренний класс, представляющий ошибку, возникшую при загрузке ресурса.
     */
    class ResourceError(val message: String, val cause: Throwable?)
    /**

    Возможные состояния ресурса: [State.ERROR] - при загрузке возникла ошибка, [State.LOADING] - ресурс загружается,
    [State.READY] - ресурс готов к использованию.
     */
    enum class State {
        ERROR,
        LOADING,
        READY
    }
}