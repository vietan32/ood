#include <iostream>
#include "receptionist.h"

int main() {
    std::cout << "HELLO WORLD!\n";
    Dog dog;
    Cat cat;

    Receptionist rec;
    rec.do_bath(dog, "Buddy");
    rec.do_bath(cat, "Kitty");

    return 0;
}
