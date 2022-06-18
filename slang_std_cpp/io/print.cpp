#include <iostream>
#include <string>

int main() {
    std::string to_print;
    std::getline(std::cin, to_print);
    std::cout << "print:" << to_print;
}