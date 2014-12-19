#
#
CXX = g++
CXXFLAGS = -g -pthread

TARGET = triangle
SRC = triangle.cpp

.PHONY: all
all: $(TARGET)


$(TARGET): $(SRC)
	$(CXX) -o $@ $(CXXFLAGS) $^

.PHONY: clean
clean:
	rm -f $(TARGET)
