cmake_minimum_required(VERSION 3.3)
project(reversing)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES helloplusplus.cpp)
add_executable(reversing ${SOURCE_FILES})