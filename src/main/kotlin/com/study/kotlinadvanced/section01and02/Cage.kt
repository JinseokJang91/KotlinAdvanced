package com.study.kotlinadvanced.section01and02

fun main() {
    // ☑️ ️Cage에 잉어를 넣은 후 빼보자
    // -> getFirst 메소드 사용
    val cage = Cage()
    cage.put(Carp("잉어"))
    //val carp: Carp = cage.getFirst() // ⚠️ERROR : Type mismatch 발생

    // ❓ 에러를 어떻게 해결할 수 있을까?
    // 1. as 를 사용한 Type Casting
    //  - but, cage에 Carp가 아닌 GoldFish를 넣는 경우가 생길 수 있음
    //    => Runtime에 올라가야 에러를 찾을 가능성이 높음
    //    => ClassCastException
    val carp2: Carp = cage.getFirst() as Carp

    // 2. Safe Type Casting과 Elvis Operator 사용
    //  - but, 동일하게 GoldFish를 넣으면 에러 발생
    //    => IllegalArgumentException
    val carp3: Carp = cage.getFirst() as? Carp ?: throw IllegalArgumentException()

    // ⭐ 해결방법
    // 제네릭 적용 - 금붕어, 잉어를 넣는 Cage를 각각 구성
    val cage_carp = Cage2<Carp>()
    cage_carp.put(Carp("잉어"))
    val carp4: Carp = cage_carp.getFirst() // 💡SUCCESS

    // ☑️ ️금붕어 Cage에 금붕어 한 마리를 넣고, 물고기 Cage에 금붕어를 옮겨보자
    // -> moveFrom 메소드 사용
    val goldFishCage = Cage2<GoldFish>()
    goldFishCage.put(GoldFish("금붕어"))

    val fishCage = Cage2<Fish>()
    fishCage.put(GoldFish("금붕어")) // 그냥 금붕어를 넣는건 가능
    //fishCage.moveFrom1(goldFishCage) // ⚠️ERROR : Type mismatch 발생
    // -> 사실은 Cage2<GoldFish>를 Cage2<Fish>에 넣으려고 했던 것 (Cage to Cage)
    // -> Cage2<GoldFish>와 Cage2<Fish>의 관계는 아무것도 아님(무공변 또는 불공변 관계)
    // -> 클래스 간 상속관계가 제네릭 클래스에서 유지되지 않음

    // ✔️ Java의 배열은 공변하다
    // -> String <-> Object 관계가 String[] <-> Object[] 관계에도 적용됨
    // ✔️ Java의 컬렉션은 무공변하다
    // -> String <-> Object 관계가 List<String> <-> List<Object> 관계에 적용되지 않음

    // ❓ 에러를 어떻게 해결할 수 있을까?
    // 1. moveFrom 함수 호출 시 Cage2<Fish>와 Cage2<GoldFish> 사이의 관계를 만들어주자
    //  - 상위/하위타입을 구성
    //  - 제네릭 타입 앞에 "out"을 붙여주면 타입 클래스의 상속관계가 제네릭까지 이어지게 됨
    //    => out : variance annotation (변성 어노테이션)
    fishCage.moveFrom2(goldFishCage) // 💡SUCCESS


    val fishCage2 = Cage2<Fish>()
    val goldFishCage2 = Cage2<GoldFish>()
    goldFishCage2.put(GoldFish("금붕어"))
    //goldFishCage2.moveTo(fishCage2) // ⚠️ERROR : Type mismatch 발생
    // -> Case2<Fish> => Cage2<GoldFish> 로 넣으려고 하기 때문 (상위타입 => 하위타입)

    // ❓ 에러를 어떻게 해결할 수 있을까?
    // 1. moveTo 함수 호출 시 Cage2<Fish>와 Cage2<GoldFish> 사이의 관계를 만들어주자
    //  - 상위/하위타입을 반대로 구성
    //  - 제네릭 타입 앞에 "in"을 붙여주면 타입 클래스의 상속관계가 반대로 됨
    //  - 반공변(contra-variant)하게 만들어야 한다
    //    => "in" 사용
    goldFishCage2.moveTo2(fishCage2) // 💡SUCCESS

    // ✔️ 정리하면 "out" : 생산자, 공변 / "in" : 소비자, 반공변
    // -> 단, 함수 파라미터 입장에서 해당 역할이 성립됨
    // -> 아래와 같이 변수 선언 시 moveFrom, moveTo에 설정한 관계가 적용되지 않음
    // -> 공변/반공변 관계 설정을 위해서는 변수에 in/out 입력 필요
    //val cage3: Cage2<Fish> = Cage2<GoldFish>() // ⚠️ERROR
    val cage4: Cage2<out Fish> = Cage2<GoldFish>() // 💡SUCCESS
    val cage5: Cage2<in GoldFish> = Cage2<Fish>() // 💡SUCCESS

    // ☑️ Cage 자체를 공변하게 만들 수는 없을까?
    // -> Cage 클래스를 생산만 하는 클래스로 만들기 (Cage3)
    // -> 클래스 제네릭 자체에 out 붙이기 (out 붙이면 생산만 가능)
    val fishCage4 = Cage3<Fish>()
    val animalCage4: Cage3<Animal> = fishCage4

    // -> 반대도 가능 (소비만 하는 클래스)
    val animalCage5 = Cage4<Animal>()
    val fishCage5: Cage4<Fish> = animalCage5

    // ☑️ 제네릭 제약을 통한 Bird 구현
    val cage7 = Cage6(mutableListOf(Eagle(), Sparrow()))
    // 크기순 정렬
    cage7.printAfterSorting()

    // 제네릭 제약을 Non-null 한정에 사용하기
    //Cage2<GoldFish?>() // ⚠️ERROR
    // => Cage2<T : Any>
    // => Any 적용 시 Non-null 한정으로 변경
}

class Cage {
    private val animals: MutableList<Animal> = mutableListOf()

    fun getFirst(): Animal {
        return animals.first()
    }

    fun put(animal: Animal) {
        this.animals.add(animal)
    }

    fun moveFrom(cage: Cage) {
        this.animals.addAll(cage.animals)
    }
}

// 제네릭을 적용한 Cage
class Cage2<T : Any> {
    private val animals: MutableList<T> = mutableListOf()

    fun getFirst(): T {
        return animals.first()
    }

    fun put(animal: T) {
        this.animals.add(animal)
    }

    fun moveFrom1(cage: Cage2<T>) {
        this.animals.addAll(cage.animals)
    }

    fun moveFrom2(otherCage: Cage2<out T>) {
        // "out"을 붙이면 데이터를 꺼낼 수만 있음
        // => 생산자 역할만 가능
        otherCage.getFirst()
        otherCage.animals
        //otherCage.put(Carp("잉어")) // ⚠️ERROR
        //otherCage.put(this.getFirst()) // ⚠️ERROR
        this.animals.addAll(otherCage.animals)
    }

    fun moveTo(otherCage: Cage2<T>) {
        otherCage.animals.addAll(this.animals)
    }

    fun moveTo2(otherCage: Cage2<in T>) {
        // "in"을 붙이면 데이터를 넣을 수만 있음
        // => 소비자 역할만 가능
        otherCage.animals.addAll(this.animals)
    }
}

// 생산만 하는 클래스
// => 이 경우 클래스 자체를 공변하게 만들 수 있음
// => 상위타입에 하위타입 삽입 시 동작 가능하게 만들 수 있음
class Cage3<out T> {
    private val animals: MutableList<T> = mutableListOf()

    fun getFirst(): T {
        return this.animals.first()
    }

    fun getAll(): List<T> {
        return this.animals
    }
}

// 소비만 하는 클래스
class Cage4<in T> {
    private val animals: MutableList<T> = mutableListOf()

    fun put(animal: T) {
        this.animals.add(animal)
    }

    fun putAll(animals: List<T>) {
        this.animals.addAll(animals)
    }
}

// 제네릭 제약
class Cage5<T : Animal> {
    private val animals: MutableList<T> = mutableListOf()

    fun getFirst(): T {
        return animals.first()
    }

    fun put(animal: T) {
        this.animals.add(animal)
    }

    fun moveFrom(cage: Cage5<T>) {
        this.animals.addAll(cage.animals)
    }
}

// 제네릭 제약, 제한조건 여러개
class Cage6<T> (
    private val animals: MutableList<T> = mutableListOf()
) where T : Animal, T : Comparable<T> {
    fun printAfterSorting() {
        this.animals.sorted()
            .map { it.name }
            .let { println(it) }
    }

    fun getFirst(): T {
        return animals.first()
    }

    fun put(animal: T) {
        this.animals.add(animal)
    }
}

abstract class Bird(
    name: String,
    private val size: Int,
) : Animal(name), Comparable<Bird> {
    override fun compareTo(other: Bird): Int {
        return this.size.compareTo(other.size)
    }
}

class Sparrow : Bird("참새", 100)
class Eagle : Bird("독수리", 500)

// 제네릭 함수로 변환하기
// BEFORE
fun List<String>.hasIntersection(other: List<String>): Boolean {
    // intersect : 교집합인지 비교
    return (this.toSet() intersect other.toSet()).isNotEmpty()
}

// AFTER
// List<String>, List<Int> 모두 활용 가능
fun <T> List<T>.hasIntersection(other: List<T>): Boolean {
    // intersect : 교집합인지 비교
    return (this.toSet() intersect other.toSet()).isNotEmpty()
}