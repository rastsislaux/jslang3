#include <iostream>
#include <string>

int main() {
    std::string to_print;
    std::getline(std::cin, to_print);
    std::cerr << to_print;
}