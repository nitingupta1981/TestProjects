# Algorithm Comparison Tool

A comprehensive web-based application for comparing and analyzing sorting and searching algorithms with intelligent recommendations.

## Features

### Core Functionality
- **Dataset Management**: Generate random, sorted, or reverse-sorted datasets
- **Multiple Algorithm Support**:
  - **Sorting**: Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, Merge Sort, Heap Sort, Shell Sort, Counting Sort
  - **Searching**: Linear Search, Binary Search, Depth First Search (DFS), Breadth First Search (BFS)
- **Dataset Analysis**: Automatic analysis of dataset characteristics (sortedness, duplicates, range, distribution)
- **Intelligent Recommendations**: Smart algorithm suggestions based on dataset properties
- **Performance Comparison**: Compare multiple algorithms on single or multiple datasets
- **Benchmarking**: Comprehensive benchmarking across different dataset sizes
- **Visualization**: Step-by-step algorithm execution visualization
- **Export**: Export results in CSV or JSON format

### Technical Features
- **Backend**: Java Spring Boot REST API
- **Frontend**: Vanilla JavaScript with modular architecture
- **Metrics Tracking**: Execution time, comparisons, swaps, array accesses
- **Graph Conversion**: Automatic conversion of arrays to graph structures for DFS/BFS
- **Code Quality**: Comprehensive documentation, OOP principles, clean architecture

## Project Structure

```
Project1/
├── backend/
│   └── src/main/java/com/algorithmcomparison/
│       ├── algorithm/        # Algorithm implementations
│       ├── controller/        # REST API controllers
│       ├── model/            # Data models
│       ├── service/          # Business logic
│       └── util/             # Utility classes
├── frontend/
│   ├── index.html           # Main HTML page
│   ├── css/
│   │   └── styles.css       # Styling
│   └── js/                  # JavaScript modules
└── pom.xml                  # Maven configuration
```

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Modern web browser

### Running the Backend

1. Navigate to project root:
   ```bash
   cd /Users/nitingupta/Project1
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend API will be available at `http://localhost:8080`

### Running the Frontend

1. Open the frontend HTML file in a web browser:
   ```bash
   open frontend/index.html
   ```

Or use a simple HTTP server:
```bash
cd frontend
python3 -m http.server 8000
```

Then navigate to `http://localhost:8000`

## Usage Guide

### 1. Generate Dataset
- Select dataset type (Random, Sorted, or Reverse Sorted)
- Choose dataset size (10 to 10,000 elements)
- Click "Generate Dataset"

### 2. Analyze Dataset
- Select a dataset from the list
- Click "Analyze Dataset & Get Recommendations"
- View dataset characteristics and recommended algorithms

### 3. Compare Algorithms
- Select one or more datasets
- Choose operation type (Sorting or Searching)
- Select algorithms to compare
- Click "Run Comparison"
- View results table with performance metrics

### 4. Run Benchmark
- Select algorithms
- Click "Run Benchmark"
- View statistical analysis across multiple dataset sizes

### 5. Visualize
- Select a dataset and algorithm
- Click "Visualize Algorithm"
- Use playback controls to watch step-by-step execution

### 6. Export Results
- After running comparisons, click "Export as CSV" or "Export as JSON"
- Results will be downloaded to your computer

## API Endpoints

### Datasets
- `POST /api/datasets/generate` - Generate dataset
- `POST /api/datasets/upload` - Upload custom dataset
- `POST /api/datasets/analyze` - Analyze dataset
- `GET /api/datasets` - List all datasets
- `GET /api/datasets/{id}` - Get specific dataset

### Algorithms
- `POST /api/algorithms/sort/compare` - Compare sorting algorithms
- `POST /api/algorithms/search/compare` - Compare searching algorithms
- `POST /api/algorithms/recommend` - Get recommendations
- `POST /api/algorithms/visualize` - Get visualization steps

### Benchmarks
- `POST /api/benchmark/run` - Run benchmark
- `GET /api/benchmark/results/{id}` - Get benchmark report

### Export
- `POST /api/results/export` - Export results
- `GET /api/benchmark/export/{id}` - Export benchmark

## Algorithm Complexities

### Sorting Algorithms
| Algorithm | Time (Best) | Time (Average) | Time (Worst) | Space |
|-----------|-------------|----------------|--------------|-------|
| Bubble Sort | O(n) | O(n²) | O(n²) | O(1) |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) |
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) |
| Shell Sort | O(n log n) | O(n log²n) | O(n²) | O(1) |
| Counting Sort | O(n+k) | O(n+k) | O(n+k) | O(k) |

### Searching Algorithms
| Algorithm | Time (Best) | Time (Average) | Time (Worst) | Space |
|-----------|-------------|----------------|--------------|-------|
| Linear Search | O(1) | O(n) | O(n) | O(1) |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) |
| DFS | O(1) | O(n) | O(n) | O(h) |
| BFS | O(1) | O(n) | O(n) | O(w) |

## Recommendation Engine

The application includes an intelligent recommendation engine that analyzes:
- Dataset size (small, medium, large, very large)
- Sortedness percentage
- Duplicate elements
- Value range and distribution
- Data patterns

Based on these characteristics, it recommends optimal algorithms with:
- Explanation of why the algorithm is recommended
- Expected complexity
- Confidence level (HIGH, MEDIUM, LOW)
- Alternative algorithms
- Warnings about algorithms to avoid

## Non-Functional Requirements

### Code Quality
- Comprehensive Javadoc comments
- Inline comments for complex logic
- Descriptive variable and function names
- Modular architecture
- OOP best practices

### Performance
- Efficient algorithm implementations
- Metrics collection with minimal overhead
- Responsive UI

### Usability
- Simple and intuitive interface
- Clear visualizations
- Helpful recommendations
- Export capabilities

## Future Enhancements

- Add more algorithms (Radix Sort, Bucket Sort, Timsort)
- Enhanced visualization with more algorithm support
- Real-time comparison charts
- Save and load comparison sessions
- Algorithm complexity calculator
- Performance profiling tools
- Database persistence
- User authentication
- Mobile responsive design enhancements

## License

This project is created for educational purposes.

## Author

Algorithm Comparison Team - 2024

