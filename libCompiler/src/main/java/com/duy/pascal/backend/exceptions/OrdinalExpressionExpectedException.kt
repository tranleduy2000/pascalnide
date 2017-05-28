package com.duy.pascal.backend.exceptions

import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.backend.runtime.exception.RuntimePascalException

/**
 * This exception will be thrown if the variable can not working with ...

 * Created by Duy on 06-Apr-17.
 */
class OrdinalExpressionExpectedException : RuntimePascalException {

    constructor(line: LineInfo) : super(line) {}

    constructor() {}

    constructor(line: LineInfo, mes: String) : super(line, mes) {}
}
