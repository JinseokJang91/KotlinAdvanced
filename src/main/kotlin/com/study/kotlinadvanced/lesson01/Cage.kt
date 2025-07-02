package com.study.kotlinadvanced.lesson01

fun main() {
    // ☑️ ️Cage에 잉어를 넣은 후 빼보자
    // => getFirst 메소드 사용
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
    val carp4: Carp = cage_carp.getFirst() // 💡 SUCCESS

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
    fishCage.moveFrom2(goldFishCage) // 💡 SUCCESS
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
class Cage2<T> {
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

    fun moveFrom2(cage: Cage2<out T>) {
        this.animals.addAll(cage.animals)
    }
}