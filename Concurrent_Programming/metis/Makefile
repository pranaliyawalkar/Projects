#
#
CXX = g++
CXXFLAGS = -g -pthread

TARGET = metis
SRC = metis.cpp

.PHONY: all
all: $(TARGET)


$(TARGET): $(SRC)
	$(CXX) -o $@ $(CXXFLAGS) $^

.PHONY: clean
clean:
	rm -f $(TARGET)
