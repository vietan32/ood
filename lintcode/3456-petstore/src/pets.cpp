#include <iostream>
#include <string>

class Pet {
    virtual void bath(std::string name) = 0;
};

class Dog : public Pet {
public:
    void bath(std::string name) override {
        std::cout << "Dog: " << name << " -> Taking a bath.\n";
    }
};

class Cat : public Pet {
public:
    void bath(std::string name) override {
        std::cout << "Cat: " << name << " -> Taking a bath.\n";
    }
};
