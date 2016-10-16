package com.github.h0tk3y.regexDsl

import java.util.regex.Pattern

class RegexContext internal constructor(var lastGroup: Int = 0) {
    internal val regexParts = StringBuilder()

    private fun addPart(part: String) {
        regexParts.append(part)
    }

    fun anyChar() = addPart(".")

    fun digit() = addPart("\\d")

    fun letter() = addPart("[[:alpha:]]")

    fun alphaNumeric() = addPart("[A-Za-z0-9]")

    fun whitespace() = addPart("\\s")

    fun wordBoundary() = addPart("\\b")

    fun wordCharacter() = addPart("\\w")

    fun startOfString() = addPart("^")
    fun endOfString() = addPart("$")

    fun literally(s: String) = addPart(Regex.escape(s))

    private fun addWithModifier(s: String, modifier: String) = addPart("(?:$s)$modifier")

    private fun pattern(block: RegexContext.() -> Unit): String {
        val innerContext = RegexContext(lastGroup)
        innerContext.block()
        lastGroup = innerContext.lastGroup
        return innerContext.regexParts.toString()
    }

    fun oneOrMore(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "+")
    fun oneOrMore(s: String) = oneOrMore { literally(s) }

    fun optional(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "?")
    fun optional(s: String) = optional { literally(s) }

    fun zeroOrMore(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "*")
    fun zeroOrMore(s: String) = zeroOrMore { literally(s) }

    infix fun Int.times(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "{$this}")
    infix fun Int.times(s: String) = this times { literally(s) }

    infix fun IntRange.times(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "{$first,$last}")
    infix fun IntRange.times(s: String) = this times { literally(s) }

    infix fun Int.timesOrMore(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "{$this,}")
    infix fun Int.timesOrMore(s: String) = this timesOrMore { literally(s) }

    infix fun Int.timesOrLess(block: RegexContext.() -> Unit) = addWithModifier(pattern(block), "{0,$this}")
    infix fun Int.timesOrLess(s: String) = this timesOrLess { literally(s) }

    fun group(block: RegexContext.() -> Unit): Int {
        val result = ++lastGroup
        addPart("(${pattern(block)})")
        return result
    }

    fun group(name: String, block: RegexContext.() -> Unit): Int {
        val result = ++lastGroup
        addPart("(?<$name>${pattern(block)})")
        return result
    }

    fun matchGroup(index: Int) = addPart("\\$index")
    fun matchGroup(name: String) = addPart("\\k<$name>")

    fun include(regex: Regex) {
        val pattern = regex.pattern
        addPart(pattern)
        lastGroup += Pattern.compile(pattern).matcher("").groupCount()
    }

    fun anyOf(vararg terms: String) = addPart(terms.joinToString("|", "(?:", ")") { Regex.escape(it) })

    fun anyOf(vararg blocks: RegexContext.() -> Unit) =
            addPart(blocks.joinToString("|", "(?:", ")") { pattern(it) })

    fun anyOf(vararg characters: Char) =
            addPart(characters.joinToString("", "[", "]").replace("\\", "\\\\").replace("^", "\\^"))

    fun anyOf(vararg ranges: CharRange) = addPart(ranges.joinToString("", "[", "]") { "${it.first}-${it.last}" })
}

fun regex(block: RegexContext.() -> Unit): Regex {
    val context = RegexContext()
    context.block()
    val pattern = context.regexParts.toString()
    return Regex(pattern)
}