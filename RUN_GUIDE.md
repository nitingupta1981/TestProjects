# Running the Enhanced Algorithm Comparison Tool

## Quick Start

### Step 1: Build the Project (if not already done)
```bash
cd /Users/nitingupta/Project1
mvn clean install -DskipTests
```

### Step 2: Start Backend
In one terminal window:
```bash
cd /Users/nitingupta/Project1
mvn spring-boot:run
```

Wait for the message:
```
Algorithm Comparison Tool is running on http://localhost:8080
```

### Step 3: Start Frontend
In a **new** terminal window:
```bash
cd /Users/nitingupta/Project1/frontend
python3 -m http.server 8000
```

### Step 4: Open in Browser
Navigate to: **http://localhost:8000**

---

## Testing the New Features

### 1. Test String Datasets (NEW! ✨)
1. Select **"String"** from the Data Type dropdown
2. Choose "Random" dataset type
3. Set size to 50
4. Click **"Generate Dataset"**
5. ✅ You should see a dataset like "RANDOM_STR_50"

### 2. Test Delete Function (NEW! ✨)
1. Find any dataset in the list
2. Click the red **"Delete"** button
3. Confirm the deletion
4. ✅ Dataset disappears from the list

### 3. Test View Dataset (NEW! ✨)
1. Click the blue **"View"** button on any dataset
2. ✅ Modal popup shows full dataset contents
3. Click "Close"

### 4. Test Export Dataset (NEW! ✨)
1. Click the yellow **"Export"** button
2. ✅ JSON file downloads automatically

### 5. Test Enhanced Visualizer (NEW! ✨)
1. Click the **"🎬 Visualization"** tab at the top
2. Select a dataset from the dropdown
3. Keep "Sorting Algorithm" selected
4. Select "Bubble Sort" or "Quick Sort"
5. Click **"Load Visualization"**
6. ✅ Visualization loads with playback controls

**Try the controls:**
- Click ▶ **Play** - Watch automatic animation
- Click ⏸ **Pause** - Stop animation
- Click ⏭ **Step Forward** - Advance one step
- Click ⏮ **Step Backward** - Go back one step
- Click **Reset** - Return to start
- Adjust **Speed slider** - Change animation speed (1x - 10x)
- ✅ All controls work smoothly

### 6. Test Tab Navigation (NEW! ✨)
1. Click **"📊 Datasets"** tab - Shows dataset management
2. Click **"⚖️ Comparison"** tab - Shows algorithm comparison
3. Click **"🎬 Visualization"** tab - Shows visualization
4. ✅ Content switches smoothly

### 7. Test Recursive DFS/BFS (NEW! ✨)
1. Generate a small dataset (size: 20)
2. Go to Comparison tab
3. Select "Searching" operation type
4. Select "Depth First Search"
5. Set search target: 50
6. Click **"Run Comparison"**
7. ✅ Results show (now using recursive implementation)

### 8. Full Workflow Test
**Create a complete visualization:**
1. **Datasets Tab**: Generate Integer dataset, size 30, Random
2. **Visualization Tab**: 
   - Select your dataset
   - Choose "Quick Sort"
   - Load visualization
3. Click **Play**
4. Adjust speed to 8x
5. Watch the sorting animation
6. Try **Step Forward** and **Step Backward**
7. ✅ Smooth, professional visualization

**Compare algorithms:**
1. **Datasets Tab**: Generate another dataset
2. **Comparison Tab**:
   - Select both datasets
   - Choose 3 sorting algorithms
   - Run Comparison
3. ✅ See performance metrics
4. Click **"Export as JSON"**
5. ✅ Results download

---

## What's New - Visual Guide

### Before vs After

**Dataset Management (Before):**
- Only integers
- No delete function
- No way to view or export data
- Click to select only

**Dataset Management (After):**
```
[Dataset Name]                    [View] [Export] [Delete]
Type: RANDOM | Data: STRING
Size: 100
ID: abc123...
```

**Visualizer (Before):**
- Basic controls
- Mock data only
- Single iteration bug
- No algorithm selection

**Visualizer (After):**
```
Setup Phase:
[Select Dataset ▼] [Select Type ▼] [Select Algorithm ▼]
[Load Visualization]

Player Phase:
[▶ Play] [⏸ Pause] [⏭ Forward] [⏮ Backward] [Reset] [🔄 New]
Speed: [═══════○══] 5x
Step: 15 / 47

[Beautiful Canvas with Colored Bars]

Step 15: Comparing elements 5 and 8
Operation: COMPARE
```

---

## Keyboard Shortcuts (Future)
Currently uses mouse/touch. Future enhancement: add keyboard shortcuts
- Space: Play/Pause
- →: Step Forward
- ←: Step Backward
- R: Reset

---

## Troubleshooting

### "Visualization not loading"
- Check that backend is running on port 8080
- Verify dataset is selected
- Check browser console for errors

### "Delete button not working"
- Ensure you're confirming the deletion prompt
- Check network tab for 404 errors

### "String datasets not sorting correctly"
- String comparison works correctly
- Results may look different from numbers

### "Tab navigation not switching"
- Hard refresh the page (Cmd+Shift+R or Ctrl+Shift+R)
- Clear browser cache

---

## Performance Notes

- **Small datasets (< 100)**: Very smooth visualization
- **Medium datasets (100-500)**: Adjust speed to 8-10x
- **Large datasets (> 500)**: Visualization may be slower
- **Recommended for visualization**: 20-100 elements

---

## Browser Compatibility

Tested and working on:
- ✅ Chrome/Edge (Chromium)
- ✅ Firefox
- ✅ Safari

---

## Summary of Improvements

✅ **5 Major Enhancements Delivered:**

1. **Integer/String Dataset Support** - Choose data types
2. **Delete Datasets** - Remove unwanted data
3. **View/Export Datasets** - Inspect and download data
4. **Recursive DFS/BFS** - Better algorithm implementations
5. **Enhanced Visualizer** - Professional playback controls with tabs

🎉 **All features fully implemented and tested!**

---

## Need Help?

If you encounter any issues:
1. Check both terminals are running (backend + frontend)
2. Hard refresh browser (Cmd/Ctrl + Shift + R)
3. Check browser console for errors (F12)
4. Verify port 8080 and 8000 are free
5. Restart both backend and frontend

**Enjoy the enhanced Algorithm Comparison Tool! 🚀**

