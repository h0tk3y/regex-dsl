# regex-dsl
A Kotlin DSL for regular expressions

Provides a Groovy-style builder for regex

    val r = regex {
        group("prefix") {
            literally("+")
            oneOrMore { digit(); letter() }
        }
        3 times { digit() }
        literally(";")
        matchGroup("prefix")
    }
    
Currently supports:

* `literally("text")` that escapes the `text`
* `anyChar()`, `digit()`, `letter()`, `alphaNumeric()`, `whitespace()`, `wordBoundary()`, `wordCharacter()`
* `startOfString()`, `endOfString()`
* `optional("text")` and `optional { /* inner regex */ }`,

 `oneOrMore("text")` and `oneOrMore { /* inner regex */ }`,
 
 `zeroOrMore("text")` and `zeroOrMore { /* inner regex */ }`
 
* `n times ("text")` and `n times { /* inner regex */ }`,
 
  `n timesOrMore ("text")` and `n timesOrMore { /* inner regex */ }`,
  
  `n timesOrLess ("text")` and `n timesOrLess { /* inner regex */ }`,
  
  `n..m times ("text")` and `n..m times { /* inner regex */ }`
  
* `index = group { /* inner regex */ }` and `matchGroup(index)`,
 
  `group("name") { /* inner regex */ }` and `matchGroup("name")`
  
* `anyOf('a', 'b', 'c')`, 

  `anyOf('a'..'z', 'A'..'Z', '0'..'9')`, 
  
  `anyOf("abc", "def", "xyz")`, 
  
  `anyOf({ /* inner regex*/ }, { /* inner regex *? })`
  
* `include(anotherRegex)`
