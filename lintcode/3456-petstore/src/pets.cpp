#include "pets.h"
#include <iostream>

void Dog::bath(const std::string & name) const {
    std::cout << "Dog: " << name << " -> Taking a bath.\n";
}

void Cat::bath(const std::string & name) const {
    std::cout << "Dog: " << name << " -> Taking a bath.\n";
}
