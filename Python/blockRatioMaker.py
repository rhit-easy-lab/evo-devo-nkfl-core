import pandas as pd
import matplotlib.pyplot as plt
import os
from tkinter import Tk
from tkinter.filedialog import askopenfilename

# Step 1: Use a file explorer dialog to select the file
def select_file():
    Tk().withdraw()  # Hide the root Tkinter window
    file_path = askopenfilename(
        title="Select an Excel or CSV File",
        filetypes=[("CSV Files", "*.csv"),("Excel Files", "*.xlsx")]
    )
    return file_path

file_path = select_file()

# Check if a file was selected
if not file_path:
    print("No file selected. Exiting.")
    exit()

# Step 2: Determine the file type and read the data
if file_path.endswith('.xlsx'):
    data = pd.read_excel(file_path)
elif file_path.endswith('.csv'):
    data = pd.read_csv(file_path)
else:
    print("Unsupported file format. Please provide an Excel (.xlsx) or CSV (.csv) file.")
    exit()

# Step 2: Validate required columns
required_columns = {'Final_Fitness', 'k', 'seed', 'numbertosize'}
if not required_columns.issubset(data.columns):
    print(f"The file must contain the following columns: {', '.join(required_columns)}")
    exit()


# Step 4: Define the custom order for ratios
custom_order = ['1,20,1', '2,10,2', '4,5,4', '5,4,5', '10,2,10', '20,1,20']
#custom_order = ['1,20', '2,10', '4,5', '5,4', '10,2', '20,1']
#custom_order = ['10,2,1', '10,2,2','10,2,4','10,2,6','10,2,8','10,2,10']





# Raise fitness values to the power of 8
# data['Final_Fitness'] = data['Final_Fitness'] ** 8

# Ensure the 'ratio' column is in the custom order
data['numbertosize'] = pd.Categorical(data['numbertosize'], categories=custom_order, ordered=True)

# Group by 'k' and 'ratio', then calculate the mean fitness for each group
grouped_data = data.groupby(['k', 'numbertosize'])['Final_Fitness'].mean().reset_index()

# Step 3: Plot the data
plt.figure(figsize=(10, 6))

# Get the unique 'k' values
k_values = grouped_data['k'].unique()

# Assign colors to each 'k' value
norm = plt.Normalize(min(k_values), max(k_values))
colors = plt.cm.viridis(norm(k_values))

# Plot a line for each 'k' value
for i, k in enumerate(k_values):
    subset = grouped_data[grouped_data['k'] == k]
    plt.plot(
        subset['numbertosize'], 
        subset['Final_Fitness'], 
        label=f'k={k}', 
        color=colors[i], 
        marker='o',  # Circle markers for points
        linestyle='-'  # Solid line
    )
# Add labels, legend, and grid
plt.xlabel('Ratio Values')
plt.ylabel('Fitness')
plt.title('Fitness vs block ratio Values for Different k Values')
plt.legend(title='k Values')
plt.grid(True)
plt.show()
