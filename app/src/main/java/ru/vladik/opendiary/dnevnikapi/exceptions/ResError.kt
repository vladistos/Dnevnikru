package ru.vladik.opendiary.dnevnikapi.exceptions

import ru.vladik.opendiary.dnevnikapi.models.v2.ErrorType

class ResError(type: ErrorType) : Exception()
