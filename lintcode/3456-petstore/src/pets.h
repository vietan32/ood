#include <string>

class Pet {
public:
    virtual void bath(const std::string & name) const = 0;
    virtual ~Pet() = default;
};

class Dog : public Pet {
public:
    void bath(const std::string & name) const override;
};

class Cat : public Pet {
public:
    void bath(const std::string & name) const override;
};
