# regex-dsl
A Kotlin DSL for regular expressions

![Logo](https://dl.dropbox.com/s/v9cqloqxsior71w/regex_dsl_1.png)

[![Release](https://jitpack.io/v/h0tk3y/regex-dsl.svg)](https://jitpack.io/#h0tk3y/regex-dsl)
![Kotlin version](https://img.shields.io/badge/kotlin-1.0.4-blue.svg)

Provides a Groovy-style builder for regex with safe quantifiers and groups. Readable and extensible.
    
## Gradle dependency

    repositories {
	    ...
		maven { url "https://jitpack.io" }
	}
    
    dependencies {
	    compile 'com.github.h0tk3y:regex-dsl:v0.1'
	}
    
## Currently supports:

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
