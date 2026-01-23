#include <iostream>
#include "receptionist.cpp"

int main() {
    std::string cat_name = "Tom", dog_name = "Teddy";
    Receptionist r;
    r.do_cat_bath(cat_name);
    r.do_dog_bath(dog_name);
    return 0;
}

