package zoo

import spock.lang.Specification
import spock.lang.Unroll

import javax.management.RuntimeErrorException

class ZooTest extends Specification {

    Animal handmadeAnimal

    def setup() {
        handmadeAnimal = Mock(Animal)
    }

    def cleanup() {
        println "clean up"
    }

    def "zoo with cat and dog"() {
        when:
        def zoo = new Zoo(Arrays.asList(new Cat(), new Dog()))

        then:
        zoo.singASong()
    }

    def "zoo with handmadeAnimal"() {
        when:
        def zoo = new Zoo(Arrays.asList(handmadeAnimal))

        then:
        zoo.singASong()
    }

    def "zoo with handmadeAnimal with sound"() {
        when:
        handmadeAnimal.makeSound() >> "I am handmade animal"
        def zoo = new Zoo(Arrays.asList(handmadeAnimal))

        then:
        zoo.singASong()
    }

    def "zoo with 2 handmadeAnimals"() {
        when:
        def zoo = new Zoo(Arrays.asList(handmadeAnimal, handmadeAnimal))
        zoo.singASong()

        then:
        2 * handmadeAnimal.makeSound() >>> ["1111111", "222222", "333333"]
    }

    def "zoo with exception"() {
        given:
        handmadeAnimal.makeSound() >> { throw new RuntimeException("hahaha") }
        def zoo = new Zoo(Arrays.asList(handmadeAnimal))

        when:
        zoo.singASong()

        then:
        def e = thrown(Exception)
        e instanceof RuntimeException
        e.getMessage() == "hahaha"
    }

    @Unroll
    def "zoo with some situation"() {
        when:
        handmadeAnimal.makeSound() >> sound1 + " ~~~ " + sound2
        def zoo = new Zoo(Arrays.asList(handmadeAnimal))

        then:
        zoo.singASong()

        where:
        sound1 | sound2
        "BOW"  | "BOW"
        "MEOW" | "MEOW"
    }
}
