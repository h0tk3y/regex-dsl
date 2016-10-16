package com.github.h0tk3y.regexDsl

import org.junit.Assert.*
import org.junit.Test

class RegexDslTest {
    @Test fun literals() {
        val s = ".:!([pattern])!:."
        val r = regex {
            literally(s)
        }

        assertTrue(r in s)
    }

    @Test fun quantifiers() {
        val s = "test"
        val testRange = 0..5

        fun testRepeats(r: Regex, rule: (Int) -> Boolean) {
            for (i in testRange) {
                val text = s.repeat(i)
                assertEquals("${r.pattern}, $text", rule(i), r.matchEntire(text) != null)
            }
        }

        testRepeats(regex { optional(s) }, { it in 0..1 })
        testRepeats(regex { oneOrMore(s) }, { it >= 1 })
        testRepeats(regex { zeroOrMore(s) }, { it >= 0 })
        testRepeats(regex { 3 times s }, { it == 3 })
        testRepeats(regex { 4 timesOrMore s }, { it >= 4 })
        testRepeats(regex { 2 timesOrLess s }, { it <= 2 })
        testRepeats(regex { 2..3 times s }, { it in 2..3 })
    }

    @Test fun nestedRegex() {
        val r = regex {
            optional { anyChar() }
            2 times { literally("abc") }
        }

        assertTrue(r in "xabcabc")
        assertTrue(r in "abcabc")
    }

    @Test fun specials() {
        val r = regex {
            anyChar()
            digit()
            wordCharacter()
            wordBoundary()
            whitespace()
            letter()
            alphaNumeric()
        }

        assertTrue(r in "a1b ab")
    }

    @Test fun anchors() {
        val r = regex {
            startOfString()
            literally("abc")
            endOfString()
        }

        assertTrue(r in "abc")
        assertTrue(r !in " abc")
        assertTrue(r !in "abc ")
    }

    @Test fun groups() {
        var groupId: Int = -1
        val r = regex {
            group("myGroup") {
                literally("abc")
                anyChar()
            }
            groupId = group {
                literally("def")
                digit()
            }
            matchGroup("myGroup")
            matchGroup(groupId)
        }

        val match = r.matchEntire("abcxdef2abcxdef2")!!
        assertEquals("def2", match.groupValues[groupId])

        assertFalse(r in "abcxdef1abcydef2")
    }

    @Test fun any() {
        val r = regex {
            anyOf("abc", "def")
            anyOf({ literally("s") }, { 3 times { anyChar() } })
            anyOf('1', '2', '3', '4')
            anyOf('a'..'z', 'A'..'Z')
        }

        assertTrue(r in "abcs1a")
        assertTrue(r in "defxxx3Z")
    }

    @Test fun included() {
        val r = regex {
            anyOf({ literally("inner") }, { anyChar() })
        }
        val q = regex {
            literally("outerStart")
            include(r)
            literally("outerEnd")
        }

        assertTrue(q in "outerStart outerEnd")
        assertTrue(q in "outerStartinnerouterEnd")
    }

    @Test fun includeGroupIndices() {
        val inner = regex {
            group("someGroup") {
                literally("x")
            }
        }

        var g1 = -1
        val outerNoInclude = regex {
            group { literally("abc") }
            g1 = group { literally("def") }
        }

        var g2 = -1
        val outerInclude = regex {
            group { literally("abc") }
            include(inner)
            g2 = group { literally("def") }
        }

        val mNoInclude = outerNoInclude.matchEntire("abcdef")
        assertEquals("def", mNoInclude!!.groupValues[g1])

        val mInclude = outerInclude.matchEntire("abcxdef")
        assertEquals("def", mInclude!!.groupValues[g2])
    }
}

fun main(args: Array<String>) {

}