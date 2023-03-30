package ru.vladik.dnevnikru.dnevnikapi.exceptions

import ru.vladik.dnevnikru.dnevnikapi.models.v2.ErrorType

class ResError(type: ErrorType) : Exception()
