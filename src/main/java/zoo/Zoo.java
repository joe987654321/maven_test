package zoo;

import java.util.List;

public class Zoo {

    private List<Animal> animalList;

    public Zoo(List<Animal> animalList) {
        this.animalList = animalList;
    }

    public void singASong() {
        animalList.forEach(animal -> System.out.println(animal.makeSound()));
    }
}
