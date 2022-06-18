#include <iostream>
#include <string>

int main() {
    std::cout << "input:null\n";
    std::string result;
    std::getline(std::cin, result);
    std::cout << "string:" << result;
    return 0;
}