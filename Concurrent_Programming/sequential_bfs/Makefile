#
#
CXX = g++
CXXFLAGS = -g -pthread

TARGET = sequential_bfs
SRC = sequential_bfs.cpp

.PHONY: all
all: $(TARGET)


$(TARGET): $(SRC)
	$(CXX) -o $@ $(CXXFLAGS) $^

.PHONY: clean
clean:
	rm -f $(TARGET)
