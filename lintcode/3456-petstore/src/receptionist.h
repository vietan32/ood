#pragma once
#include <string>
#include "pets.h"

class Receptionist {
public:
    void do_bath(const Pet & pet, const std::string & name);
};
