#include "receptionist.h"

void Receptionist::do_bath(const Pet & pet, const std::string & name) {
    pet.bath(name);
}
