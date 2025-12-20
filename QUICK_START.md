# Quick Start Guide - Algorithm Comparison Tool

## Prerequisites

Before running the application, ensure you have:
- ✅ Java 17 or higher installed
- ✅ Maven 3.6+ installed (optional, can use mvnw wrapper if provided)
- ✅ A modern web browser (Chrome, Firefox, Safari, Edge)

## Step-by-Step Setup

### 1. Verify Java Installation

```bash
java -version
```

You should see Java 17 or higher.

### 2. Start the Backend Server

Open a terminal and navigate to the project directory:

```bash
cd /Users/nitingupta/Project1
```

#### Option A: Using Maven (if installed)

```bash
# Clean and build the project
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

#### Option B: Using Java directly (alternative)

```bash
# Compile
mvn clean package

# Run the JAR
java -jar backend/target/algorithm-comparison-tool-1.0.0.jar
```

**Expected Output:**
```
Algorithm Comparison Tool is running on http://localhost:8080
```

The backend server is now running at `http://localhost:8080`

### 3. Start the Frontend

Open a new terminal window and run:

#### Option A: Using Python HTTP Server

```bash
cd /Users/nitingupta/Project1/frontend
python3 -m http.server 8000
```

Then open your browser to: `http://localhost:8000`

#### Option B: Open HTML Directly

```bash
cd /Users/nitingupta/Project1/frontend
open index.html
```

Or simply double-click the `index.html` file in your file manager.

**Note:** Opening directly may cause CORS issues. Using a local HTTP server is recommended.

## Using the Application

### Quick Test - 5 Minute Tutorial

#### 1. Generate a Dataset (30 seconds)
- Leave "Random" selected
- Set size to 100
- Click "Generate Dataset"
- ✅ You should see the dataset appear in the "Available Datasets" section

#### 2. Analyze the Dataset (30 seconds)
- Select the dataset you just created (checkbox)
- Click "Analyze Dataset & Get Recommendations"
- ✅ You should see:
  - Dataset characteristics (size, sortedness, duplicates, etc.)
  - Algorithm recommendations with explanations

#### 3. Run a Comparison (1 minute)
- Select 2-3 algorithms (e.g., Quick Sort, Merge Sort, Bubble Sort)
- Click "Run Comparison"
- ✅ You should see:
  - Results table with execution time, comparisons, swaps
  - Color-coded performance indicators

#### 4. Run a Benchmark (1 minute)
- Keep algorithms selected
- Click "Run Benchmark"
- ✅ You should see:
  - Statistical analysis (min, max, avg, median)
  - Performance across different dataset sizes
  - Comparison charts

#### 5. Export Results (30 seconds)
- Click "Export as CSV" or "Export as JSON"
- ✅ File should download automatically

#### 6. Visualize Algorithm (1 minute)
- Select a dataset
- Choose one algorithm
- Click "Visualize Algorithm"
- Use Play/Pause controls
- ✅ You should see animated visualization

## Example API Test (Optional)

Test the backend API directly:

### Generate a Dataset
```bash
curl -X POST http://localhost:8080/api/datasets/generate \
  -H "Content-Type: application/json" \
  -d '{
    "type": "RANDOM",
    "size": 100,
    "minValue": 1,
    "maxValue": 1000
  }'
```

### Analyze a Dataset
```bash
curl -X POST http://localhost:8080/api/datasets/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "datasetId": "YOUR_DATASET_ID_HERE",
    "operationType": "SORT"
  }'
```

### Compare Sorting Algorithms
```bash
curl -X POST http://localhost:8080/api/algorithms/sort/compare \
  -H "Content-Type: application/json" \
  -d '{
    "datasetIds": ["YOUR_DATASET_ID_HERE"],
    "algorithmNames": ["Quick Sort", "Merge Sort", "Bubble Sort"]
  }'
```

## Troubleshooting

### Problem: "Port 8080 is already in use"
**Solution:** Another application is using port 8080. Either:
- Stop the other application
- Change the port in `backend/src/main/resources/application.properties`:
  ```
  server.port=8081
  ```
- Update frontend API URL in `frontend/js/app.js`

### Problem: "CORS Error" in browser console
**Solution:** Make sure you're:
1. Running the backend server (http://localhost:8080)
2. Using a local HTTP server for frontend (not opening HTML directly)
3. Or use Chrome with `--disable-web-security` flag (for testing only)

### Problem: "Maven not found" or "mvn command not found"
**Solution:** 
1. Install Maven: https://maven.apache.org/install.html
2. Or download pre-compiled JAR if provided
3. Or use IDE like IntelliJ IDEA or Eclipse to build and run

### Problem: Frontend shows "Failed to fetch"
**Solution:**
1. Verify backend is running: `curl http://localhost:8080/api/datasets`
2. Check browser console for error details
3. Verify API URL in `frontend/js/app.js` matches backend port

### Problem: Visualization not working
**Solution:**
- Visualization uses Canvas API
- Ensure you have a modern browser
- Check browser console for errors
- Try refreshing the page

## Features to Try

### 1. Test Different Dataset Types
- Generate Random dataset (unpredictable performance)
- Generate Sorted dataset (optimal for some algorithms)
- Generate Reverse Sorted dataset (worst case for some)

### 2. Compare Algorithm Performance
- Run Quick Sort vs Merge Sort on random data
- Run Insertion Sort on sorted data (see O(n) performance!)
- Run Bubble Sort on large dataset (see O(n²) slowness)

### 3. Test Recommendation System
- Generate sorted dataset → See Insertion Sort recommended
- Generate large random dataset → See Quick Sort recommended
- Generate dataset with limited range → See Counting Sort recommended

### 4. Test Graph Searches
- Select "Searching" operation type
- Choose DFS or BFS
- See how arrays are converted to graphs

### 5. Test Benchmarking
- Compare all sorting algorithms
- See which scales best with size
- Export benchmark results

## Default Test Data

For quick testing, the application includes:
- Dataset sizes: 10, 50, 100, 500, 1000, 5000, 10000
- All 8 sorting algorithms available
- All 4 searching algorithms available
- Random, Sorted, and Reverse Sorted types

## Performance Tips

- Small datasets (< 100): All algorithms perform similarly
- Medium datasets (100-1000): Differences become noticeable
- Large datasets (> 1000): O(n log n) algorithms clearly outperform O(n²)
- Very large datasets (> 5000): Consider using only efficient algorithms

## Next Steps

1. ✅ Run the application
2. ✅ Complete the 5-minute tutorial above
3. ✅ Experiment with different dataset sizes
4. ✅ Try all algorithms
5. ✅ Export and analyze results
6. ✅ Read README.md for detailed documentation

## Support

If you encounter issues:
1. Check this troubleshooting section
2. Review browser console for errors
3. Check backend logs in terminal
4. Verify all prerequisites are installed
5. Read README.md for more details

## Summary

The Algorithm Comparison Tool is now ready to use! You can:
- ✅ Generate and analyze datasets
- ✅ Get intelligent algorithm recommendations
- ✅ Compare algorithm performance
- ✅ Run comprehensive benchmarks
- ✅ Visualize algorithm execution
- ✅ Export results for analysis

Happy algorithm comparing! 🚀



