/**
 * Updates algorithm checkboxes based on selected dataset types.
 * Disables algorithms that don't support the selected dataset type.
 * 
 * Add this function to app.js after the toggleDatasetSelection function.
 * Also add: updateAlgorithmAvailability(); at the end of toggleDatasetSelection function.
 */
function updateAlgorithmAvailability() {
    // Get the data type of selected datasets
    let dataType = 'INTEGER'; // Default
    
    if (state.selectedDatasets.length > 0) {
        const firstSelectedDataset = state.datasets.find(d => d.id === state.selectedDatasets[0]);
        if (firstSelectedDataset) {
            dataType = firstSelectedDataset.dataType || 'INTEGER';
        }
    }
    
    // Define which algorithms support which data types
    const algorithmSupport = {
        sorting: {
            'Bubble Sort': ['INTEGER', 'STRING'],
            'Selection Sort': ['INTEGER', 'STRING'],
            'Insertion Sort': ['INTEGER', 'STRING'],
            'Quick Sort': ['INTEGER', 'STRING'],
            'Merge Sort': ['INTEGER', 'STRING'],
            'Heap Sort': ['INTEGER'],
            'Shell Sort': ['INTEGER'],
            'Counting Sort': ['INTEGER']
        },
        searching: {
            'Linear Search': ['INTEGER', 'STRING'],
            'Binary Search': ['INTEGER', 'STRING'],
            'Trie Search': ['STRING'],
            'Depth First Search': ['INTEGER'],
            'Breadth First Search': ['INTEGER']
        }
    };
    
    // Update sorting algorithms
    const sortingContainer = document.getElementById('sorting-algorithms');
    const sortingCheckboxes = sortingContainer.querySelectorAll('input[type="checkbox"]');
    sortingCheckboxes.forEach(checkbox => {
        const algoName = checkbox.value;
        const supportedTypes = algorithmSupport.sorting[algoName] || [];
        
        if (supportedTypes.includes(dataType)) {
            checkbox.disabled = false;
            checkbox.parentElement.style.opacity = '1';
            checkbox.parentElement.style.cursor = 'pointer';
        } else {
            checkbox.disabled = true;
            checkbox.checked = false; // Uncheck if previously checked
            checkbox.parentElement.style.opacity = '0.5';
            checkbox.parentElement.style.cursor = 'not-allowed';
        }
    });
    
    // Update searching algorithms
    const searchingContainer = document.getElementById('searching-algorithms');
    const searchingCheckboxes = searchingContainer.querySelectorAll('input[type="checkbox"]');
    searchingCheckboxes.forEach(checkbox => {
        const algoName = checkbox.value;
        const supportedTypes = algorithmSupport.searching[algoName] || [];
        
        if (supportedTypes.includes(dataType)) {
            checkbox.disabled = false;
            checkbox.parentElement.style.opacity = '1';
            checkbox.parentElement.style.cursor = 'pointer';
        } else {
            checkbox.disabled = true;
            checkbox.checked = false; // Uncheck if previously checked
            checkbox.parentElement.style.opacity = '0.5';
            checkbox.parentElement.style.cursor = 'not-allowed';
        }
    });
}

