#include <iostream>

class ListNodeIterator;

class ListNode {
public:
    int value;
    ListNode * next;
    ListNode(int value);
    ListNodeIterator * get_iterator();
};

class ListNodeIterator {
public:
    ListNode * node;
    ListNodeIterator(ListNode * node);
    bool has_next();
    ListNode * next();
};

// -- ListNode implementation
ListNode::ListNode(int value) {
    this->value = value;
    this->next = nullptr;
}

ListNodeIterator * ListNode::get_iterator() {
    return new ListNodeIterator(this);
}

// -- ListNodeIterator implementation
ListNodeIterator::ListNodeIterator(ListNode * node) {
    this->node = node;
}

bool ListNodeIterator::has_next() {
    return this->node != nullptr;
}

ListNode * ListNodeIterator::next() {
    if (!has_next()) return nullptr;
    ListNode * cur = this->node;
    this->node = cur->next;
    return cur;
} 

int main() {
    ListNode * node0 = new ListNode(0);
    ListNode * node1 = new ListNode(1);    
    ListNode * node2 = new ListNode(2);    

    node0->next = node1;
    node1->next = node2;

    ListNodeIterator it(node0);
    while (it.has_next()) {
        std::cout << it.next()->value << "\n";
    }

    return 0;
}
