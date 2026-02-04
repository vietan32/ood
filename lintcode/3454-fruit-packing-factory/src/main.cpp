#include <iostream>

class IFactory {
public:
    virtual void pack() = 0; 
};

class Apple : public IFactory {
public:
    void pack() override;    
};

class Banana : public IFactory {
    void pack() override;
};

void Apple::pack() {
    std::cout << "This is an Apple.\n";
}

void Banana::pack() {
    std::cout << "This is a Banana.\n";
}

class FruitFactory {
public:
    static IFactory * pack_fruit(std::string fruit) {
        if (fruit == "apple") {
            return new Apple();
        } else if (fruit == "banana") {
            return new Banana();
        } else {
            std::cout << "Wrong fruit!\n";
            return nullptr;
        }
    }
};

int main() {
    IFactory * fruit = FruitFactory::pack_fruit("apple");
    fruit->pack();
    fruit = FruitFactory::pack_fruit("banana");
    fruit->pack();
    fruit = FruitFactory::pack_fruit("hello");
    return 0;
}
