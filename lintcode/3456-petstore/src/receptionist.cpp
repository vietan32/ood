#include <iostream>
#include "pets.cpp"

class Receptionist {
private:
    Dog dog_;
    Cat cat_;
public:
    Receptionist();
    void do_dog_bath(std::string & name);
    void do_cat_bath(std::string & name);
};

Receptionist::Receptionist() {
    dog_ = Dog();
    cat_ = Cat();
}

void Receptionist::do_dog_bath(std::string & name) {
    dog_.bath(name);
}

void Receptionist::do_cat_bath(std::string & name) {
    cat_.bath(name);
}
