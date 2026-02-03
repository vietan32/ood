#include <iostream>

class Context;

class State {
// protected:
//     Context * context_;
public:
    virtual void consume(Context * context) = 0;
    virtual ~State() = default;
};

class Context {
public:
    State * state_;
    int stock_;

    Context();
    ~Context();
    void consume();
};

//--
class StockAvailable : public State {
public:
    StockAvailable();
    void consume(Context * context) override;
};

class StockUnavailable : public State {
private:
    int cooldown_;
public:
    StockUnavailable();
    void consume(Context * context) override;
};

//--
StockAvailable::StockAvailable() {}

void StockAvailable::consume(Context * context) {
    context->stock_--;
    std::cout << "consume succeed. stock = " << context->stock_ << "\n";
    if (context->stock_ == 0) {
        std::cout << "Entering cooldown state...\n";
        delete context->state_;
        context->state_ = new StockUnavailable();
    }
}

//--
StockUnavailable::StockUnavailable() : cooldown_(3) {}

void StockUnavailable::consume(Context * context) {
    cooldown_--;
    std::cout << "Cooling down: " << cooldown_ << "\n";
    if (cooldown_ == 0) {
        delete context->state_;
        context->stock_ = 3;
        context->state_ = new StockAvailable();
    }
}

//--
Context::Context() : stock_(3), state_(new StockAvailable()) {}

Context::~Context() {
    delete state_;
}

void Context::consume() {
    state_->consume(this);
}

int main() {
    Context * ctx = new Context();
    for (int i = 0; i < 7; i++) {
        ctx->consume();
    }
    return 0;
}
