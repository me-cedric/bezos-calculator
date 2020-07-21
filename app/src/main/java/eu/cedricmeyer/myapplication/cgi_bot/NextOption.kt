package eu.cedricmeyer.myapplication.cgi_bot

interface INextOption {
    val varId: String?
    val default: Boolean?
    val pattern: String? /* String | String? */
    val type: String? /* 'string' | 'regex' */
    val handler: String?
    val validationCode: String?
    val nextMessage: String?
}

open class NextFinder(
    var options: MutableList<INextOption>,
    var config: CgiBotConfiguration
) {
    open fun findNextFromCode(nextCodeFunction: String, chatState: ChatState): String? {
        var path: String? = null
        val nextCodes: List<String> = this.options
            .filter { next: INextOption -> next.validationCode !== null }
            .map { next: INextOption -> next.validationCode } as List<String>
        val nextCodeResult = this.executeNextCode(nextCodeFunction, nextCodes, chatState)
        this.options.forEach { option: INextOption ->
            if (option.validationCode === nextCodeResult) {
                path = option.nextMessage
            }
        }
        return path
    }

    open fun findNextFromRegex(chatState: ChatState): String? {
        var path: String? = null
        this.options.forEach { option: INextOption ->
            var test: Regex? = null
            if (option.pattern !== null && (option.type === "string" || option.type === "regex")) {
                test = Regex(option.pattern!!, RegexOption.IGNORE_CASE)
            }
            if (test != null && option.varId !== null) {
                val varToCheck = chatState.getValue(option.varId!!) as String
                if (test.matches(varToCheck)) {
                    path = option.nextMessage
                }
            }
        }

        return path
    }

    open fun executeNextCode(nextCodeFunction: String, codes: List<String>, chatState: ChatState) = this.config.executeNextFunction(nextCodeFunction, codes, chatState.values)
}