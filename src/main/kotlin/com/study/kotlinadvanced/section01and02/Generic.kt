package com.study.kotlinadvanced.section01and02

fun main() {
    val num = 3
    num.toSuperString2() // "Int: 3"

    val str = "ABC"
    str.toSuperString2() // "String: ABC"
    println("${str::class.java.name}: $str")

    val numbers = listOf(1, 2f, 3.0)
    // ✔️ inline, reified 함수를 활용한 코틀린 함수 예시
    numbers.filterIsInstance<Float>() // [2f]
    numbers.filterIsInstance<Double>() // [3.0]
}

//fun <T> T.toSuperString() {
//    println("${T::class.java.name}: $this") // ⚠️ERROR : Generic 함수는 런타임 때 클래스 정보가 사라짐
//}
// ☑️ inline 함수로 변환
inline fun <reified T> T.toSuperString2() {
    println("${T::class.java.name}: $this") // ⚠️ERROR : Generic 함수는 런타임 때 클래스 정보가 사라짐
}

// T의 정보를 가져오고 싶을 때 타입 별 함수 생성해야함
fun List<*>.hasAnyInstanceOfString(): Boolean {
    return this.any {it is String}
}

fun List<*>.hasAnyInstanceOrInt(): Boolean {
    return this.any {it is Int}
}

// ☑️ T의 정보를 가져오고 싶을 때 inline, reified 활용
// inline 함수 : 코드의 본문을 호출 지점으로 이동시켜 컴파일되는 함수
inline fun <reified T> List<*>.hasAnyInstanceOf(): Boolean {
    return this.any {it is T}
}

// 제네릭은 여러개 사용
class TypeErase<T, R, P> {

}